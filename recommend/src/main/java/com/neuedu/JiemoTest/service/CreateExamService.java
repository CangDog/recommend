package com.neuedu.recommend.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.neuedu.recommend.entity.Bank;
import com.neuedu.recommend.entity.Exam;
import com.neuedu.recommend.entity.Part;
import com.neuedu.recommend.entity.Question;

public interface CreateExamService {
    
	/**
	 * 通过题库ID、起始位置、题目个数，搜索题库中相应位置的试题信息。
	 */
	List<Question> SearchBankQuestion(int bankId,int start,int num);
	/**
	 * 通过用户ID，搜索该用户的所有题库信息。
	 */
	List<Bank> SearchBank(int userId);
	/**
	 * 通过输入Exam对象，向数据库中添加一条记录。
	*/
	String AddExamInfo(Exam exam);
	/**
	 * 通过输入Part对象，向数据库中添加一条记录。
	*/
	String AddPartInfo(Part part);
	/**
	 * 通过输入部分id、分值、试题id，向对应部分中加入一条试题信息。
	*/
	String AddExamPartQuestionInfo(String partid, String grade, String questionid);
	/**
	 * 通过输入用户id，搜索该用户下的所有正常状态的考试
	*/
	List<Exam> SearchExam(int userId);
	String AddExamByPass(int parseInt);
	String AddExamByPass(int examid, int userid);
	String AddPaperByPass(int parseInt, int userid);
	
	
	
}
