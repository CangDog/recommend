package com.neuedu.recommend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.neuedu.recommend.entity.Bank;
import com.neuedu.recommend.entity.Exam;
import com.neuedu.recommend.entity.Part;
import com.neuedu.recommend.entity.Question;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.service.AddUserService;
import com.neuedu.recommend.service.CreateExamService;
import com.neuedu.recommend.service.QuestionBankService;

@Controller
public class CreateExamController {
	
	@Autowired
	StringRedisTemplate template;
	
	@Autowired
	QuestionBankService questionBankService;
	
	@Autowired
	CreateExamService createExamService;
	
	@Autowired
	AddUserService addUserService;
	
	@RequestMapping("/Exam")
	public ModelAndView toExam(HttpServletRequest request){
		
		HttpSession session = request.getSession();

		
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		if(userInfo==null) {
			ModelAndView modelAndView=new ModelAndView();
			modelAndView.setViewName("login");//只写前缀就行
			//modelAndView.addObject("userid",userInfo.getUserid());//向request范围内添加属性
			return modelAndView;
		}
		int userType = userInfo.getUsertype();
		if(userType == 1) {
			ModelAndView modelAndView=new ModelAndView();
			modelAndView.setViewName("Exam");//只写前缀就行
			//modelAndView.addObject("userid",userInfo.getUserid());//向request范围内添加属性
			return modelAndView;
		}else {
			ModelAndView modelAndView=new ModelAndView();
			modelAndView.setViewName("StudentAnsweredPapers");//只写前缀就行
			modelAndView.addObject("userid",userInfo.getUserid());//向request范围内添加属性
			return modelAndView;
			//return "toStudentAnsweredPapers";
		}
		
	}

	
	@RequestMapping("/HandMovement")
	public String toHandMovement(){
		
		return "HandMovement";
	}
	
	@RequestMapping("/test")
	public String totest(){
		
		return "test";
	}
	
	@RequestMapping("/checkPass")
	public @ResponseBody String getQuestionList(@RequestParam("pass") String pass,HttpServletRequest request){
		
		HttpSession session = request.getSession();
		if(session.getAttribute("UserInfo") == null) {
			return "tologin";
		}
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		
		int userid = userInfo.getUserid();
		Set<String> keys = template.keys("*");
		
		for (String key:keys) {
			String value = template.opsForValue().get(key);
			if(value.equals(pass)) {
				if(Integer.parseInt(key.split("_")[2]) != userInfo.getUsertype()) {
					return "0 没有权限";
				}
				String ret = createExamService.AddExamByPass(Integer.parseInt(key.split("_")[1]),userid);
				return "1 添加考试成功！";
			}
		}

		return "0 没有找到该邀请码";
		
	}
	
	@RequestMapping("/checkStudentPass")
	public @ResponseBody String checkStudentPass(@RequestParam("pass") String pass,HttpServletRequest request){
		HttpSession session = request.getSession();
		if(session.getAttribute("UserInfo") == null) {
			return "tologin";
		}
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		
		Set<String> keys = template.keys("*");
		
		for (String key:keys) {
			String value = template.opsForValue().get(key);
			if(value.equals(pass)) {
				if(Integer.parseInt(key.split("_")[2]) != userInfo.getUsertype()) {
					return "0 没有权限";
				}
				String ret = addUserService.AddOneUser(Integer.parseInt(key.split("_")[1]), userInfo.getUserid());
				return ret;
			}
		}
 
 
		
		return "0 没有找到该邀请码";
		
	}
	
	@RequestMapping("/getUser")
	public @ResponseBody String getUser(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		
		return userInfo.getUserid().toString();
		
	}
	
	@RequestMapping("/getBankList")
	public @ResponseBody String getBankList(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		
		
		List<Bank> banks= createExamService.SearchBank(userInfo.getUserid());
		
		HashMap map = new HashMap();
		
		map.put("BankList", banks);
		
		String str = JSON.toJSONString(map); // 利用fastjson转换字符串
		
		return str; //返回字符串
		
		
	}
	
	@RequestMapping("/getExamList")
	public @ResponseBody String getExamList(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		
		if(userInfo == null) {
			return "请先登录";
		}
		
		List<Exam> ExamList= createExamService.SearchExam(userInfo.getUserid());
		
		HashMap map = new HashMap();
		
		map.put("ExamList", ExamList);
		
		String str = JSON.toJSONString(map); // 利用fastjson转换字符串
		System.out.println(str);
		return str; //返回字符串
		
		
	}
	
	@RequestMapping("/getQuestionList")
	public @ResponseBody String getQuestionList(@RequestParam("bankId") Integer bankId,@RequestParam("start") Integer start,@RequestParam("num") Integer num,HttpServletRequest request){
		System.out.println(bankId);
		System.out.println(start);
		System.out.println(num);
		
		HttpSession session = request.getSession();
		UserInfo userInfo=(UserInfo) session.getAttribute("UserInfo");
		
		List<Question> questions= createExamService.SearchBankQuestion(bankId, start, num);
		
		HashMap map = new HashMap();
		
		map.put("QuestionList", questions);
		
		String str = JSON.toJSONString(map); // 利用fastjson转换字符串
		
		return str; //返回字符串
		
		
	}
	
	@RequestMapping("/addPartQuestion")
	public @ResponseBody String addPartQuestion(@RequestParam("partid") String partid,@RequestParam("grade") String grade,@RequestParam("questionid") String questionid){
		System.out.println(partid);
		System.out.println(grade);
		System.out.println(questionid);
		
		
		String ret = createExamService.AddExamPartQuestionInfo(partid, grade, questionid);

		return ret; //返回字符串
		
		
	}
	
	@RequestMapping("/addExam")
	public @ResponseBody String addExam(@RequestBody Exam exam){

		String ret= createExamService.AddExamInfo(exam);
		
		return ret; //返回字符串
		
	}
	
	
	@RequestMapping("/addPart")
	public @ResponseBody String AddExamPartInfo(@RequestBody Part part){

		String ret= createExamService.AddPartInfo(part);
		
		return ret; //返回字符串
		
	}

	
//	@RequestMapping("/testUserinfo")
//	public ModelAndView test1(HttpServletRequest request) {
//		String paperID=request.getParameter("paperID");
//		System.out.println("paperID:"+paperID);
//		//ModelAndView spring提供的装载数据和视图的对象
//		ModelAndView modelAndView=new ModelAndView();
//		modelAndView.setViewName("paperInfo");//只写前缀就行
//		
//		//List<Question> questions=new ArrayList<Question>();//
//		//questions=answeredPapersService.showPaperQuestion(Integer.parseInt(paperID));
//		
//		//List<Answer_info> answer_infos=new ArrayList<Answer_info>();//
//		//answer_infos=answeredPapersService.showPaperAnswer(Integer.parseInt(paperID));
//		
//		List<QuestionAnswerVO> questionAnswerVOs=new ArrayList<QuestionAnswerVO>();
//		questionAnswerVOs=answeredPapersService.showQuestionAndAnswer(Integer.parseInt(paperID));
//		modelAndView.addObject("questionAnswerVOs", questionAnswerVOs);//向request范围内添加属性
//		
//		//modelAndView.addObject("questions", questions);//向request范围内添加属性
//		//modelAndView.addObject("answerinfos", answer_infos);//向request范围内添加属性
//		//System.out.println("12435431423----------------");
//		return modelAndView;
//	}
	
}
