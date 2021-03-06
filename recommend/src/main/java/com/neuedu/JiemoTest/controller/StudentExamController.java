package com.neuedu.recommend.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.neuedu.recommend.entity.Exam;
import com.neuedu.recommend.entity.Part;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.service.AddUserService;
import com.neuedu.recommend.service.AnswerPaperService;
import com.neuedu.recommend.service.ManageExamService;

@Controller
public class StudentExamController {
	
	@Autowired
	AddUserService addUserService;
	
	@Autowired
	AnswerPaperService answerPaperService;
	
	@Autowired
	ManageExamService manageExamService;
	
	@RequestMapping("/addOneStudent")
	public @ResponseBody String addOneStudent(@RequestParam("examid") int examid,
											  @RequestParam("username") String username,
											  @RequestParam("userphoneOrEmail") String userphoneOrEmail,
											  @RequestParam("infoType") Integer infoType,
											  @RequestParam("examTitle") String examTitle,
											  HttpServletRequest request){
		
		HttpSession session = request.getSession();
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		System.out.println("------------------------------");
		System.out.println(username+userphoneOrEmail+infoType);
		if(userInfo==null) {
			return "0 请先登录";
		}
		
		String ret = addUserService.AddOneUser(examid,username, userphoneOrEmail, infoType,examTitle);

		return ret; //返回字符串
		
		
	}
	
	@RequestMapping("/toMyExam")
	public String EditExam(HttpServletRequest request){

		return "MyExam"; //返回字符串
		
		
	}
	
	@RequestMapping("/toAnswerPaper")
	public String toAnswerPaper(HttpServletRequest request){
		int PaperId = Integer.parseInt(request.getParameter("PaperId"));
		HttpSession session = request.getSession();
		session.setAttribute("PaperId", PaperId);
		
		
		return "AnswerPaper"; //返回字符串
		
		
	}
	
	@RequestMapping("/searchPaperExam")
    public @ResponseBody String searchPaperExam(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		Integer PaperId =  (Integer) session.getAttribute("PaperId"); 
		
		if(PaperId == null) {
			return "0 请先选择要编辑的考试";
		}
		System.out.println("----------------------");
		System.out.println(PaperId);
		Exam exam = answerPaperService.SearchPaperExam(PaperId);
		//可以加redis
		//可以加redis
		//可以加redis
		//可以加redis
		//可以加redis
		//可以加redis
		session.setAttribute("PaperExamId", exam.getExamid());
			
		String str = JSON.toJSONString(exam); // 利用fastjson转换字符串
		return str; //返回字符串
		
		
	}
	
	@RequestMapping("/searchPaperExamPart")
	public @ResponseBody String searchPaperExamPart(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		Integer PaperExamId =  (Integer) session.getAttribute("PaperExamId"); 
		
		List<Part> PartList= manageExamService.SearchExamPart(PaperExamId);
		HashMap map = new HashMap();
		map.put("PartList", PartList);	
		String str = JSON.toJSONString(map); // 利用fastjson转换字符串
		return str; //返回字符串	
		
	}
	
	@RequestMapping("/AddStartAnswerInfo")
	public @ResponseBody String AddStartAnswerInfo(@RequestParam("questionId") Integer questionId,
											  @RequestParam("questionSerial") Integer questionSerial,
											  HttpServletRequest request){
		
		HttpSession session = request.getSession();
		Integer PaperId =  (Integer) session.getAttribute("PaperId"); 
		
		String ret = answerPaperService.AddStartAnswerInfo(PaperId, questionId, questionSerial);

		return ret; //返回字符串
		
		
	}
	
	@RequestMapping("/ChangeAnswerInfo")
	public @ResponseBody String ChangeAnswerInfo(@RequestParam("questionId") Integer questionId,
											  @RequestParam("questionSerial") Integer questionSerial,
											  @RequestParam("questionAnswer") String questionAnswer,
											  HttpServletRequest request){
		
		HttpSession session = request.getSession();
		Integer PaperId =  (Integer) session.getAttribute("PaperId"); 
		
		String ret = answerPaperService.ChangeAnswerInfo(PaperId, questionId, questionSerial,questionAnswer);

		return ret; //返回字符串
		
		
	}

	@RequestMapping("/paperFinish")
	public @ResponseBody String paperFinish(@RequestParam("startTime") Integer startTime,
											  @RequestParam("endTime") Integer endTime,
											  @RequestParam("windowTime") Integer windowTime,
											  HttpServletRequest request){

		HttpSession session = request.getSession();
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		
		Integer PaperId =  (Integer) session.getAttribute("PaperId"); 
		
		String ret = answerPaperService.paperFinish(PaperId, startTime, endTime,windowTime,userInfo.getNickname());

		return ret; //返回字符串
		
		
	}
	
}
