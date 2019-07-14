package com.neuedu.recommend.controller.jsonController;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuedu.recommend.entity.Answer_info;
import com.neuedu.recommend.entity.PaperTest;
import com.neuedu.recommend.entity.Question;
import com.neuedu.recommend.entity.QuestionAnswerVO;
import com.neuedu.recommend.entity.UnAnsweredVO;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.service.AnsweredPapersService;

@RestController
public class ShowDataTableController {

	
	
	@Autowired
	AnsweredPapersService answeredPapersService;
	
	@RequestMapping("/showAnswered")
	public Map test6(@RequestParam(value="examid") String examid) {
		
		List<PaperTest> pt=new ArrayList<PaperTest>();
		pt=answeredPapersService.getExamPapers(Integer.parseInt(examid));
		
		for(PaperTest paperTest : pt) {
			  System.out.println(paperTest.toString());
			}

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", pt);
		resultMap.put("code", "0");
		resultMap.put("msg", "");
		resultMap.put("count", "1");
		//System.out.println(resultMap.toString());
		return resultMap;
	}
	
	@RequestMapping("/showStuUnAnswered")
	public Map test7(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserInfo user=(UserInfo) session.getAttribute("UserInfo");
		//public Map test6(Model m,HttpServletRequest req) {
		List<UnAnsweredVO> pt=new ArrayList<UnAnsweredVO>();//
		pt=answeredPapersService.getUserPapers(user.getUserid(), 0);
       for (UnAnsweredVO unAnsweredVO : pt) {
	       System.out.println(unAnsweredVO.toString());
       }

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", pt);
		resultMap.put("code", "0");
		resultMap.put("msg", "");
		resultMap.put("count", "1");
		//System.out.println(resultMap.toString());
		return resultMap;
	}
	
	@RequestMapping("/showStuAnswered")
	public Map test8(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserInfo user=(UserInfo) session.getAttribute("UserInfo");
		//public Map test6(Model m,HttpServletRequest req) {
		List<UnAnsweredVO> pt=new ArrayList<UnAnsweredVO>();//
		pt=answeredPapersService.getUserPapers(user.getUserid(),3);
       for (UnAnsweredVO unAnsweredVO : pt) {
	       System.out.println(unAnsweredVO.toString());
       }

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("data", pt);
		resultMap.put("code", "0");
		resultMap.put("msg", "");
		resultMap.put("count", "1");
		//System.out.println(resultMap.toString());
		return resultMap;
	}
	
	
}
