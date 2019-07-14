package com.neuedu.recommend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neuedu.recommend.dao.BankMapper;
import com.neuedu.recommend.dao.ExamMapper;
import com.neuedu.recommend.dao.PartMapper;
import com.neuedu.recommend.dao.QuestionInBankMapper;
import com.neuedu.recommend.dao.QuestionInPartMapper;
import com.neuedu.recommend.dao.QuestionMapper;
import com.neuedu.recommend.entity.Bank;
import com.neuedu.recommend.entity.BankExample;
import com.neuedu.recommend.entity.BankExample.Criteria;
import com.neuedu.recommend.entity.Exam;
import com.neuedu.recommend.entity.ExamExample;
import com.neuedu.recommend.entity.Part;
import com.neuedu.recommend.entity.PartExample;
import com.neuedu.recommend.entity.Question;
import com.neuedu.recommend.entity.QuestionInPart;
import com.neuedu.recommend.entity.QuestionInPartExample;
import com.neuedu.recommend.service.CreateExamService;
@Service
public class CreateExamServiceImpl implements CreateExamService {
	@Autowired
	QuestionInBankMapper questionInBankMapper;
	
	@Autowired
	QuestionMapper questionMapper;
	
	@Autowired
	BankMapper bankMapper;
	
	@Autowired
	ExamMapper examMapper;
	
	@Autowired
	PartMapper partMapper;
	
	@Autowired
	QuestionInPartMapper questionInPartMapper;
	
	@Override
	public List<Question> SearchBankQuestion(int bankId,int start,int num){
		List<Question> QuestionList = questionInBankMapper.selectQuestionByQuestionBank(bankId, start, num);
		return QuestionList;
	}
	
	@Override
	public List<Bank> SearchBank(int userId){
		BankExample example = new BankExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userId);
		criteria.andSpare1EqualTo(1);
		List<Bank> BankList = bankMapper.selectByExample(example);
		return BankList;
	}
	
	@Override
	public List<Exam> SearchExam(int userId){
		
		ExamExample example = new ExamExample();
		com.neuedu.recommend.entity.ExamExample.Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userId);
		List<Integer> State = new ArrayList<Integer>();
		State.add(0);
		State.add(1);
		criteria.andExamstateIn(State);
		List<Exam> ExamList = examMapper.selectByExample(example);
		return ExamList;
	}
	
	@Override
	public String AddExamInfo(Exam exam) {
		

		int nowtime = (int) (System.currentTimeMillis()/1000);
		exam.setEdittime(nowtime);
		exam.setSourceexamid(-1);
		exam.setSpare1(0);
		int ret = examMapper.insert(exam);
		int examId = exam.getExamid();
		
		
		if(ret==1) {
			return "1"+" "+examId;
		}else {
			return "0 添加失败";
		}
		
	}
	
	@Override
	public String AddPartInfo(Part part) {
		
		
		int ret = partMapper.insert(part);
		int partId = part.getPartid();
		if(ret==1) {
			return "1"+" "+partId;
		}else {
			return "0 添加失败";
		}
	}
	
	@Override
	public String AddExamPartQuestionInfo(String partid,String grade,String questionid) {
		
		
		String[] gradeList = grade.split(" ");
		String[] questionidList = questionid.split(" ");
		int allInsertNum = 0;
		for(int i = 0 ; i < gradeList.length ; i++) {
			QuestionInPart q = new QuestionInPart();
			q.setPartid(Integer.parseInt(partid));
			q.setQuestionid(Integer.parseInt(questionidList[i]));
			q.setGrade(Integer.parseInt(gradeList[i]));
			q.setSerialnum(i+1);
			int ret = questionInPartMapper.insert(q);
			allInsertNum = allInsertNum +ret;
		}
		

		if(allInsertNum>0) {
			return "1"+" "+allInsertNum;
		}else {
			return "0 添加失败";
		}
	}
	@Override
	public String AddExamByPass(int examid,int userid) {
		
		
		Exam exam = examMapper.selectByPrimaryKey(examid);
		
		exam.setExamid(null);
		exam.setUserid(userid);
		examMapper.insert(exam);
		System.out.println(exam.getExamid());
		Integer newID = exam.getExamid();
		
		PartExample partExample = new PartExample();
		com.neuedu.recommend.entity.PartExample.Criteria criteria = partExample.createCriteria();
		criteria.andExamidEqualTo(examid);
		
		List<Part> partList = partMapper.selectByExample(partExample);
		Integer lastPartId = -1;
		Integer newPartId = -1;
		for(Part p : partList) {
			lastPartId = p.getPartid();
			p.setPartid(null);
			p.setExamid(newID);
			p.setSpare1(0);
			int ret = partMapper.insert(p);
			newPartId = p.getPartid();
			
			QuestionInPartExample questionInPartExample = new QuestionInPartExample();
			com.neuedu.recommend.entity.QuestionInPartExample.Criteria questionInPartCriteria = questionInPartExample.createCriteria();
			questionInPartCriteria.andPartidEqualTo(lastPartId);
			List<QuestionInPart> QuestionInPartList = questionInPartMapper.selectByExample(questionInPartExample);
			for(QuestionInPart questionInPart : QuestionInPartList) {
				QuestionInPart newQuestionInPart = new QuestionInPart();
				newQuestionInPart.setGrade(questionInPart.getGrade());
				newQuestionInPart.setQuestionid(questionInPart.getQuestionid());
				newQuestionInPart.setPartid(newPartId);
				newQuestionInPart.setSerialnum(questionInPart.getSerialnum());
				questionInPartMapper.insert(newQuestionInPart);
			}

		}
		
		return "1";
		
		
	}

	@Override
	public String AddExamByPass(int parseInt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String AddPaperByPass(int parseInt, int userid) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
