package com.donghoon.reflect.controller;

import com.donghoon.reflect.anno.RequestMapping;
import com.donghoon.reflect.controller.dto.JoinDto;
import com.donghoon.reflect.controller.dto.LoginDto;

public class UserController {
	
	@RequestMapping("/user/join")
	public String join(JoinDto dto) { // username, password, email
		System.out.println("join() �Լ� ȣ���");
		System.out.println(dto);
		return "/";
	}
	
	@RequestMapping("/user/login")
	public String login(LoginDto dto) { // username, password
		System.out.println("login() �Լ� ȣ���");
		System.out.println(dto);
		return "/";
	}
	
	@RequestMapping("/user")
	public String user() {
		System.out.println("user() �Լ� ȣ���");
		return "/";
	}
	
	@RequestMapping("/hello")
	public String hello() {
		System.out.println("hello() �Լ� ȣ���");
		return "/";
	}
}
