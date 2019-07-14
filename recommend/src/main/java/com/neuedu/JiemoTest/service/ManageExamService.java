package com.neuedu.recommend.service;

import java.util.List;

import com.neuedu.recommend.entity.Exam;
import com.neuedu.recommend.entity.Part;
import com.neuedu.recommend.entity.Question;

public interface ManageExamService {
	/**
	*输入考试ID、考试开始时间考试结束时间、最大切换窗口次数信息，更改考试数据库表   
	*中相应信息。
	*/
	String PublishExam(int examid, int startTime, int endTime, int maxTimes);
	/**
	 * 通过考试ID，搜索相应考试信息。
	 */
	Exam SearchExam(int examid);
	/**
	 * 通过考试ID，搜索相应考试部分信息。
	 */
	List<Part> SearchExamPart(int examid);
	/**
	 * 通过部分ID，搜索相应考试部分信息。
	 */
	List<Question> SearchPartExam(int partId);
	/**
	 * 通过考试ID，将考试的所有相关信息全部删除。
	 */
	String deleteExamInfo(int examId);
	/**
	 * 通过考试ID，更改该考试的状态，将其删除放入归档考试中。
	 */
	String PlaceExamOnFile(int examId);
	/**
	 * 通过考试ID，更改该考试的状态，将其删除放入回收站。
	 */
	String DeleteExam(int examId);
	/**
	*输入考试ID、考试开始时间、考试结束时间信息，更改考试数据库表   
	*中相应信息。
	*/
	String ChangePublishExam(int examid, int startTime, int endTime);
	/**
	*输入用户ID、搜索该用户ID下所有的已归档考试
	*/
	List<Exam> SearchFileExam(int userId);
	/**
	 * 通过考试ID，更改该考试的状态，将其还原至正常状态。
	 */
	String RenewExam(int examId);
	/**
	*输入用户ID、搜索该用户ID下所有回收站中的考试
	*/
	List<Exam> SearchDeleteExam(int userId);
	/**
	*输入用户ID、考试ID，检测该用户是否已被录入考试
	*/
	String CheckExamUser(int userId, int examId);
	
	String ShareExam(int examId,int ShareType);
}
