package com.neuedu.recommend.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckIfLoginController {
	
	@RequestMapping("/check")
	public String test2(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		String userinfo = (String) session.getAttribute("UserInfo"); 
		if(userinfo==null || userinfo.equals("")) {
			return "0";
		}
		return userinfo;
		
	}

}
