package com.donghoon.reflect.test;

import org.junit.Test;

public class SetNamingTest {
	
	@Test
	public void 키값을세터로바꾸기() {
		String key = "username";
		
		String firstKey = "set";
		String upperKey = key.substring(0,1).toUpperCase();
		String remainKey = key.substring(1);
		
		String result = firstKey + upperKey + remainKey;
		System.out.println(result);
	}
	
}
