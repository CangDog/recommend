/**
 * 
 */
package com.neuedu.recommend.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neuedu.recommend.dao.UserInfoMapper;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.service.impl.EditUserServiceImpl;

/**
 * @author lenovo
 *
 */
@Controller
public class EditUserController {

	@Autowired
	UserInfoMapper userInfoMapper;
	
	@Autowired
	EditUserServiceImpl editUserServiceImpl;
		
	UserInfo u = new UserInfo();
	

		
	@RequestMapping("/touserEdit")
	public String showUser(Integer uid) {
		
		return "userEdit";
	}	
	@RequestMapping("/userEdit")
	public @ResponseBody String updateEditUser(@RequestBody UserInfo u,
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		//设置返回编码格式
		response.setCharacterEncoding("utf-8");
		String p1 = editUserServiceImpl.getMD5String(u.getPassword());
		u.setPassword(p1);	
	
//		UserInfo u1 = new UserInfo();
//		u1.setUserid(u.getUserid());
		//Integer userid2 = user2.getUserid();//后台的userID

		int i = editUserServiceImpl.updateEditUser(u);//获得后台的userinfo数据	
		
		if(i == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("UserInfo", u);
			return "信息修改成功";
		}else {
			return "信息修改失败";
		}
		
//		//request.getSession().setAttribute("user", user);//对前台中的数据进行保存、缓存设置		
//		UserInfo p = (UserInfo) request.getSession().getAttribute("userid");//得到前台缓存好的的数据	，相当于前台中的	${session.user.userid}
	}
	

	
}
