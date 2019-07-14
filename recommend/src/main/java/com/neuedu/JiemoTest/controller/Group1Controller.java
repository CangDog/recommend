package com.neuedu.recommend.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.neuedu.recommend.dao.UserInfoMapper;
import com.neuedu.recommend.entity.GroupAndUser;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.entity.UserInfoExample;
import com.neuedu.recommend.service.Group1Service;

@Controller
public class Group1Controller {
	
	@Autowired
	Group1Service group1Service;
	
	@Autowired
	UserInfoMapper userInfoMapper;
	
//	@Autowired
//	HomeworkService homeworkService;
//	
//	@Autowired
//	AnnouncementService announcementService;
	
	
	//显示群组和组员
	@RequestMapping("/Group00")
    public ModelAndView showGroup(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
	    //mav.setViewName("student");
		//测试

		HttpSession session = request.getSession();	
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		
		if(userInfo.getUsertype() == 1) {
			mav.setViewName("file_manager");
		}else {
			mav.setViewName("student");
		}
		
		Integer userid = userInfo.getUserid();
		Integer role = userInfo.getUsertype();
		
		List<GroupAndUser> gauservice = group1Service.gauservice(userid, role);
		mav.addObject("gau",gauservice);
		
	    return mav;
    }
	
	@RequestMapping("/student")
    public ModelAndView student(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
//		mav.setViewName("file_manager");
	    mav.setViewName("student");
		//测试

		HttpSession session = request.getSession();	
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		
		Integer userid = userInfo.getUserid();
		Integer role = userInfo.getUsertype();
		
		List<GroupAndUser> gauservice = group1Service.gauservice(userid, role);
		mav.addObject("gau",gauservice);
		
	    return mav;
    }

	
	//创建群组
	@RequestMapping(value = "CreateGroup",method = RequestMethod.POST)
	@ResponseBody
	    public int createGroup(String name,String intro,HttpServletRequest request) {
		
		//user用session里的 这里测试用
//		session.getAttribute("")

		HttpSession session = request.getSession();	
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");

		
		
		int i = group1Service.createGroup(name, intro, userInfo);
		System.out.println(i);
		return i;
	}
	
	//邀请用户
	@RequestMapping(value = "AddMember",method = RequestMethod.POST)
	@ResponseBody
	    public int AddMember(String userName,Integer groupid,HttpSession session) {
		UserInfoExample example = new UserInfoExample();
		example.or().andUsernameEqualTo(userName);
		List<UserInfo> list = userInfoMapper.selectByExample(example);
		if(list == null)
			return -1;
		Integer userid = -1;
		for(UserInfo u : list) {
			userid = u.getUserid();
		}
		int i = group1Service.AddMember(userid, groupid);
		System.out.println(i);
		return i;
	}
	
	//删除用户
	@RequestMapping(value = "DeletMember",method = RequestMethod.POST)
	@ResponseBody
	    public int deleteMember(Integer userid,Integer groupid,HttpSession session) {
		int i=group1Service.DeletMember(userid,groupid);
		System.out.println(i);
			return i;
	}
	
	//解散群组
	@RequestMapping(value="DisGroup",method=RequestMethod.POST)
	@ResponseBody
	   public int deleteGroup(Integer groupid) {
		System.out.println(groupid);
		int group = group1Service.deleteGroup(groupid);
		return group;
	}
	
	
	//加入群组
	@RequestMapping(value="JoinGroup",method=RequestMethod.POST)
	@ResponseBody
	public int JoinGroup(String groupName, int userid,HttpServletRequest request) {
//		UserInfo u = session.getAttribute("user");
//		Integer userid = u.getUserid();

		HttpSession session = request.getSession();	
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
    System.out.println(groupName);
		int i=group1Service.joinGroup(groupName,userInfo.getUserid());
		System.out.println(i);
		
		return i;
	
	}
	
	//退出群组
	@RequestMapping(value="OutGroup",method=RequestMethod.POST)
	@ResponseBody
	public int OutGroup(String groupid,HttpServletRequest request) {
		//userid在session中 这里测试用
		HttpSession session = request.getSession();	
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		int i=group1Service.outGroup(Integer.parseInt(groupid),userInfo.getUserid());
		return i;
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	//创建作业
//	@RequestMapping("/CreateHomework")
//	public String createHomework() {	
//		return "createHomework";
//	}
//	@RequestMapping("/Homework")
//	 public void test8(Homework h,HttpServletResponse resp)throws Exception{
//		int i=homeworkService.createHomework(h);
//		if(i>0) {
//			resp.getWriter().println("sucess");
//		}else {
//			resp.getWriter().println("error");
//		}
//	}
//	
//	//显示作业
//	@RequestMapping("/ShowHomework")
//	public String test9(Model m) {
		//List<Homework> list= homeworkService.queryAll();
//		m.addAttribute("list",list);
//		return "showHomework";
//		
//	}
//	
//	
//	//发布公告
//	@RequestMapping("/CreateAnnouncement")
//	public String createAnnouncement() {
//		return "createAnnouncement";
//	}
//	
//	@RequestMapping("/Announcement")
//	public void test10(Announcement a,HttpServletResponse resp)throws Exception{
//		int i=announcementService.createAnnouncement(a);
//		if(i>0) {
//			resp.getWriter().println("sucess");
//		}else {
//			resp.getWriter().println("error");
//		}	
//	}
//	
//	
//	//显示公告
//	@RequestMapping("/ShowAnnouncement")
//	public String test11(Model m) {
//		List<Announcement> list=announcementService.queryAll();
//		m.addAttribute("list",list);
//		return "showAnnouncement";
//	}
	
	

	


	
	
	

	
	
	
	
	
	
	
	
	
	
//	@RequestMapping("/Group")
//	    public void test1(Group1 g,HttpServletResponse resp)throws Exception{
//		
		//System.out.println(g.getName()+g.getCreateuserid());
		
		//int i=group1Service.createGroup(g);
//		if(i>0) {
//			resp.getWriter().println("sucess");
//		}else {
//			resp.getWriter().println("error");
//		}
//	}
	
	
//	@RequestMapping("/JoinGroup")
//	    public String joinGrouppage() {
//		return "joinGroup";
//	}
//	@RequestMapping("/JGroup")
//	    public void test2(HttpServletRequest request,HttpServletResponse response)throws  Exception{
//		int groupId = Integer.parseInt(request.getParameter("groupId"));
//		int userId = Integer.parseInt(request.getParameter("userId"));
//		
//		System.out.println("-----------------------------------------------------");
//		System.out.println(userId);
//		System.out.println(groupId);
//		int i=group1Service.joinGroup(groupId,userId);
//		
//		if(i>0) {
//			response.getWriter().println("sucess");
//		}else {
//			response.getWriter().println("error");
//		}
//	}
//	
	
//	@RequestMapping("/OutGroup")
//	    public String outGrouppage() {
//		return "outGroup";
//	}
//	@RequestMapping("/OGroup")
//	    public void test3(HttpServletRequest request,HttpServletResponse response)throws Exception{
//		int userId=Integer.parseInt(request.getParameter("userId"));
//		int groupId=Integer.parseInt(request.getParameter("groupId"));
//		System.out.println("-------------------------------------------------------");
//		System.out.println(groupId);
//		int i=group1Service.outGroup(userId,groupId);
//		
//		if(i>0) {
//			response.getWriter().println("sucess");
//		}else {
//			response.getWriter().println("error");
//		}
//	}

//	@RequestMapping("/DisMiss")
//	    public String dismissGroup() {
//		return "disGroup";
//	}
//	@RequestMapping("/DGroup")
//		public void test4(HttpServletRequest request,HttpServletResponse response)throws Exception{
//		int groupId=Integer.parseInt(request.getParameter("groupId"));
//		System.out.println("-------------------------------------------------------");
//		System.out.println(groupId);
//		int i=group1Service.dismissGroup(groupId);
//		if(i>0) {
//			response.getWriter().println("sucess");
//		}else {
//			response.getWriter().println("error");
//		}
//			
//		}
	
//	@RequestMapping("/EditGroup")
//	public String editGroup(String gid ,Model m) {
//		
//		System.out.println(gid+"---------------------------------------------");
//		Group1 group1 = group1Service.selectone(Integer.parseInt(gid));
//		m.addAttribute("kkk",group1 );
//		
//		return "editGroup";
//	}
	
	//编辑群组
//		@RequestMapping(value="EditGroup",method=RequestMethod.POST)
//		@ResponseBody
//		public int EditGroup(String name,String intro,Integer groupid,HttpSession session) {
//			int i=group1Service.exitGroup(groupid,name,intro);
//			return i;
//		}
	
	
	
//	
//	@RequestMapping("/EGroup")
//	public void test5(HttpServletRequest request,HttpServletResponse response,Group1 g)throws Exception{
//		int i=group1Service.editGroup(g);
//		if(i>0) {
//			response.getWriter().println("sucess");
//		}else {
//			response.getWriter().println("error");
//		}
//	}

//	@RequestMapping("/TransferGroup")
//	public String transferGroup(String gid,Model m) {
//		Group1 group1 = group1Service.selectone(Integer.parseInt(gid));
//		m.addAttribute("ttt",group1 );
//		return "transferGroup";
//		
//	}
//	@RequestMapping("/TGroup")
//	public void test6(HttpServletRequest request,HttpServletResponse response,Group1 g)throws Exception{
//		int i=group1Service.editGroup(g);
//		if(i>0) {
//			response.getWriter().println("sucess");
//		}else {
//			response.getWriter().println("error");
//		}
//		
//	}
	
	

	
//	@RequestMapping("/ShowGroup")
//	public String test7(Model m) {
//		List<Group1> list=group1Service.queryAll();
//		m.addAttribute("list",list);
//		return "showGroup";
//	}
	
	
//	@RequestMapping("/CreateHomework")
//	public String createHomework() {	
//		return "createHomework";
//	}
//	@RequestMapping("/Homework")
//	 public void test8(Homework h,HttpServletResponse resp)throws Exception{
//		int i=homeworkService.createHomework(h);
//		if(i>0) {
//			resp.getWriter().println("sucess");
//		}else {
//			resp.getWriter().println("error");
//		}
//	}
	
//	@RequestMapping("/ShowHomework")
//	public String test9(Model m) {
//		List<Homework> list= homeworkService.queryAll();
//		m.addAttribute("list",list);
//		return "showHomework";
//		
//	}
	
	
	
	
//	@RequestMapping("/CreateAnnouncement")
//	public String createAnnouncement() {
//		return "createAnnouncement";
//	}
//	
//	@RequestMapping("/Announcement")
//	public void test10(Announcement a,HttpServletResponse resp)throws Exception{
//		int i=announcementService.createAnnouncement(a);
//		if(i>0) {
//			resp.getWriter().println("sucess");
//		}else {
//			resp.getWriter().println("error");
//		}
//		
//	}
//	
//	@RequestMapping("/ShowAnnouncement")
//	public String test11(Model m) {
//		List<Announcement> list=announcementService.queryAll();
//		m.addAttribute("list",list);
//		return "showAnnouncement";
//	}
	
	
	//创建群组
//	@RequestMapping("/test11")
//    public ModelAndView sdadasd() {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("createGroup");
//	   return mav;
//	}
	
	
	
	

}
