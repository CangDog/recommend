/**
 * 
 */
package com.neuedu.recommend.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neuedu.recommend.dao.UserInfoMapper;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.service.RegisterService;
import com.neuedu.recommend.service.UserService;
import com.neuedu.recommend.service.impl.LoginServiceImpl;
import com.neuedu.recommend.service.impl.RegisterServiceImpl;

/**
 * @author lenovo
 *用ajax提交方式
 *传的是useinfo对象体
 *form表单提交不需要@RequestBody
 */
@Controller
public class RegisterController {
	@Autowired
	UserInfoMapper userInfoMapper;

	@Autowired
	RegisterService registerService;
	
	@Autowired
	RegisterServiceImpl registerServiceImpl;
	
	@Autowired
	UserService userService;
	
	@RequestMapping("/toregister")
	public String Register1() {
		
		return "register";
	}
	
	@RequestMapping("/register")
	public @ResponseBody String Register(@RequestBody UserInfo u,HttpServletRequest request,HttpServletResponse response) throws Exception{
//		UserInfo u = (UserInfo)request.getSession().getAttribute("user");

		//PrintWriter out = response.getWriter();
		//List<UserInfo> userList = registerServiceImpl.checkRegister(u);
		//String i = registerServiceImpl.convertMD5(u.getPassword());
		String i = registerServiceImpl.getMD5String(u.getPassword());
		System.out.println("输出未加密的密码试试"+u.getPassword());
		u.setPassword(i);
		int user = registerService.checkRegister(u);
		if(user != 1) {	
			return "登录名重复! 请重新输入!";
		}else{
			int r1 = registerService.addRegister(u);
			
	   		u.setUserid(userService.getIdByName(u.getUsername()));
	   		request.getSession().setAttribute("UserInfo",u);
	   		
	   		if(r1 == 1) {
				return "Exam";
			}else {
				return "注册失败";
			}
			
   	 }
		
		}
	@RequestMapping("/toExam1111")
    public String Exam() {
		return "Exam";	    	 
   }
	
}
