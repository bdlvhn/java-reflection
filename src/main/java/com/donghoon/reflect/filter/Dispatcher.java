package com.donghoon.reflect.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.donghoon.reflect.anno.RequestMapping;
import com.donghoon.reflect.controller.UserController;

// �б� ��Ű��
public class Dispatcher implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
//		System.out.println("context path : " + request.getContextPath()); // ������Ʈ ���� �ּ�
//		System.out.println("�ĺ��� �ּ� : " + request.getRequestURI()); // �� �ּ�
//		System.out.println("��ü �ּ� : " + request.getRequestURL()); // ��ü �ּ�
		
		// /user Parsing
		String endPoint = request.getRequestURI().replaceAll(request.getContextPath(), "");
		System.out.println("endPoint : " + endPoint);
		
		UserController userController = new UserController();
		
//		if (endPoint.equals("/join")) {
//			userController.join();
//		} else if (endPoint.equals("/login")) {
//			userController.login();
//		} else if (endPoint.equals("/user")) {
//			userController.user();
//		}
		
		// ���÷��� -> �޼��带 ��Ÿ�� �������� ã�Ƴ��� ����
		Method[] methods = userController.getClass().getDeclaredMethods(); // �� ���Ͽ� �޼��常 (getMethods()�� ��ӵ� �ͱ��� ���)

		//		for (Method method : methods) {
////			System.out.println(method.getName());
//			if (endPoint.equals("/" + method.getName())) {
//				try {
//					method.invoke(userController);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
		boolean isMatching = false;
		for (Method method : methods) { // 4���� (join, login, user, hello)
			Annotation annotation = method.getDeclaredAnnotation(RequestMapping.class);
			RequestMapping requestMapping = (RequestMapping) annotation;
//			System.out.println(requestMapping.value());
			
			
			if (requestMapping.value().equals(endPoint)) {
				isMatching = true;
				try {
					Parameter[] params = method.getParameters();
					String path = null;
					if (params.length != 0) {
						// �ش� dtoInstance�� ���÷����ؼ� set�Լ� ȣ��(username, password)
						Object dtoInstance = params[0].getType().newInstance();
						setData(dtoInstance, request);
						path = (String) method.invoke(userController, dtoInstance);
					} else {
						path = (String) method.invoke(userController);
					}
					RequestDispatcher dis = request.getRequestDispatcher(path); // ���͸� �ٽ� �� Ž.
					dis.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		
		if (!isMatching) {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("�߸��� �ּ� ��û�Դϴ�. 404 error");
			out.flush();
		}
	}
	
	private <T> void setData(T instance, HttpServletRequest request) {
		Enumeration<String> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String methodKey = keyToMethodKey(key);
			
			Method[] methods = instance.getClass().getDeclaredMethods();
			
			for (Method method : methods) {
				if(method.getName().equals(methodKey)) {
					try {
						method.invoke(instance, request.getParameter(key));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private String keyToMethodKey(String key) {
		String firstKey = "set";
		String upperKey = key.substring(0,1).toUpperCase();
		String remainKey = key.substring(1);
		return firstKey + upperKey + remainKey;
	}
}
