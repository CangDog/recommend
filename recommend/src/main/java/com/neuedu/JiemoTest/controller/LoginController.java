/**
 * 
 */
package com.neuedu.recommend.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neuedu.recommend.dao.UserInfoMapper;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.service.LoginService;
import com.neuedu.recommend.service.impl.LoginServiceImpl;



/**
 * @author lenovo
 *@PathVariable  只能从地址栏中得到参数，get请求/a标签的跳转
 *@RequestBody 请求对象（体），包含前台包装好的json格式的字符串，转到后台，通过OGNL技术，与实体类互相绑定
 *                           使用方法 例如：public String test1（@RequestBody UserInfo u）{out.println（u.getName）；}
 *@ResponseBody  响应对象，返回给前台的json格式的字符串
 *
 *@RequestParam 只能接受前台get方式请求，单个或多个参数，data也不需要，在25min
 */
@Controller
public class LoginController {
	
	     @Autowired
         UserInfoMapper userInfoMapper;
	     
	     @Autowired
         LoginService loginService;
	     
	     
	     //UserInfo u = new UserInfo();
	     
	     @Autowired
	     LoginServiceImpl loginServiceImpl;
	     
	     @RequestMapping("/tologin")
	     public String login1() {
	    	 return "login";
	     }
         //用户登录
	     //@PostMapping(value = "/login")
         //@RequestMapping(value = "/login", method = RequestMethod.POST)
	     @RequestMapping("/login")
	      public @ResponseBody  String login(@RequestBody UserInfo u,HttpServletRequest request) throws Exception{	    	 
	    	 String pwd = u.getPassword();
	    	 System.out.println("未加密的登录用户名"+u.getUsername());	    	
	    	 System.out.println("未加密的登录密码"+pwd);	    	
	    	 String i = loginServiceImpl.getMD5String(pwd);//md5加密
	    	 System.out.println("加密的登录密码"+i);	    	
	    	 u.setPassword(i);//设置前台传来的数据
	    	 UserInfo user = loginServiceImpl.checkLogin(u);
	    	 if(user != null && user.getUsertype()==1) {	    		 	    		 
	    		 request.getSession().setAttribute("UserInfo",user);
	    		 return "Exam";
	    		 
	    	 }else if(user != null && user.getUsertype()==0) {	    		 	    		 
	    		 request.getSession().setAttribute("UserInfo",user);
	    		 return "toStudentAnsweredPapers";
	    		 
	    	 }
	    	 else{
	    		 return "登录名或密码填写错误! 请重新输入!";
	    	 }
	    	 
	     }
	     
	     @RequestMapping("/toExam111")
	      public String Exam() {
			return "Exam";	    	 
	     }
	     
	     
	     
	     @RequestMapping("/loginout")
	      public String loginout1(HttpServletRequest request) {
	    	HttpSession session = request.getSession();
	    	session.removeAttribute("UserInfo");
			return "login";	    	 
	     }


}
