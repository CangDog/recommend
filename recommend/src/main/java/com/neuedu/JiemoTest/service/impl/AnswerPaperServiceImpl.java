package com.neuedu.recommend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neuedu.recommend.dao.Answer_infoMapper;
import com.neuedu.recommend.dao.ExamMapper;
import com.neuedu.recommend.dao.PaperTestMapper;
import com.neuedu.recommend.dao.QuestionMapper;
import com.neuedu.recommend.entity.Answer_info;
import com.neuedu.recommend.entity.Answer_infoExample;
import com.neuedu.recommend.entity.Answer_infoExample.Criteria;
import com.neuedu.recommend.entity.Exam;
import com.neuedu.recommend.entity.PaperTest;
import com.neuedu.recommend.entity.Part;
import com.neuedu.recommend.entity.Question;
import com.neuedu.recommend.entity.QuestionInPartExample;
import com.neuedu.recommend.service.AnswerPaperService;
import com.neuedu.recommend.service.AnsweredPapersService;
@Service
public class AnswerPaperServiceImpl implements AnswerPaperService {
	
	@Autowired
	PaperTestMapper paperTestMapper;
	
	@Autowired
	ExamMapper examMapper;
	
	@Autowired
	Answer_infoMapper answer_infoMapper;
	
	@Autowired
	QuestionMapper questionMapper;
	
	@Autowired
	AnsweredPapersService answeredPapersService;
	
	@Override
	public Exam SearchPaperExam(int paperId) {
		
		PaperTest paperTest = paperTestMapper.selectByPrimaryKey(paperId);
		Exam exam = examMapper.selectByPrimaryKey(paperTest.getExamid());
		return exam;
	}
	
	@Override
	public String AddStartAnswerInfo(int paperId,int questionId,int questionSerial) {
		Answer_info AnswerInfo = new Answer_info();
		AnswerInfo.setPaperid(paperId);
		AnswerInfo.setQuestionid(questionId);
		AnswerInfo.setQuestionserialnum(questionSerial);
		AnswerInfo.setAnswercontent("");
		AnswerInfo.setScore(0);
		AnswerInfo.setCorrectstate(0);
		int ret = answer_infoMapper.insert(AnswerInfo);
		if(ret==1) {
			return "1 添加成功";
		}else {
			return "0 添加失败";
		}
	}
	
	@Override
	public String ChangeAnswerInfo(int paperId,int questionId,int questionSerial,String questionAnswer) {
		
		Answer_info AnswerInfo = new Answer_info();
		AnswerInfo.setPaperid(paperId);
		AnswerInfo.setQuestionid(questionId);
		AnswerInfo.setQuestionserialnum(questionSerial);
		AnswerInfo.setAnswercontent(questionAnswer);
		AnswerInfo.setScore(0);
		AnswerInfo.setCorrectstate(0);
		Answer_infoExample answer_infoExample = new Answer_infoExample();
		Criteria criteria = answer_infoExample.createCriteria();
		criteria.andPaperidEqualTo(paperId);
		criteria.andQuestionidEqualTo(questionId);
		int ret = answer_infoMapper.updateByExampleSelective(AnswerInfo, answer_infoExample);
		
		
		if(ret==1) {
			return "1 修改成功";
		}else {
			return "0 修改失败";
		}
	}	
	
	@Override
	public String paperFinish(int paperId,int startTime,int endTime,int windowTime,String nickname) {
		PaperTest p = new PaperTest();
		p.setPaperid(paperId);
		p.setPaperstate(1);
		p.setParticipatetime(startTime);
		p.setSubmittime(endTime);
		p.setSpare3(nickname);
		p.setChangewindowsnum(windowTime);
		int ret = paperTestMapper.updateByPrimaryKeySelective(p);
		
		List<Part> parts=new ArrayList<Part>();
		parts=answeredPapersService.showParts(paperId);
		
		for (Part part : parts) {
			answeredPapersService.autoScore(part.getPartid(), paperId);
		}
		
		if(ret==1) {
			return "1 修改成功";
		}else {
			return "0 修改失败";
		}
	}
	
	
}
