package com.neuedu.recommend.service;

import com.neuedu.recommend.entity.Exam;

public interface AnswerPaperService {
	
	/**
	*输入试卷ID、搜索该试卷对应的考试的总体信息   
	*/
	Exam SearchPaperExam(int paperId);
	/**
	*输入试卷ID、试题id、试题序号、向用户作答表中加入初始信息 
	*/
	String AddStartAnswerInfo(int paperId, int questionId, int questionSerial);
	/**
	*输入试卷ID、试题id、试题序号、试题答案、向用户作答表更新用户答案 
	*/
	String ChangeAnswerInfo(int paperId, int questionId, int questionSerial, String questionAnswer);
	/**
	*输入试卷ID、作答开始时间、作答结束时间、切换窗口次数、更新试卷表中的相应字段 
	*/
	String paperFinish(int paperId, int startTime, int endTime, int windowTime, String nickname);
	
	
}
