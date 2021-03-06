package com.neuedu.recommend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.neuedu.recommend.entity.Question;
import com.neuedu.recommend.entity.QuestionInBankExample;
import com.neuedu.recommend.entity.QuestionInBankKey;
@Mapper
public interface QuestionInBankMapper {
    int countByExample(QuestionInBankExample example);

    int deleteByExample(QuestionInBankExample example);

    int deleteByPrimaryKey(QuestionInBankKey key);

    int insert(QuestionInBankKey record);

    int insertSelective(QuestionInBankKey record);

    List<QuestionInBankKey> selectByExample(QuestionInBankExample example);

    int updateByExampleSelective(@Param("record") QuestionInBankKey record, @Param("example") QuestionInBankExample example);

    int updateByExample(@Param("record") QuestionInBankKey record, @Param("example") QuestionInBankExample example);
    
    List<Question> selectQuestionByQuestionBank(@Param("bankid") Integer bankid, @Param("start") int start, @Param("count") int count);
}