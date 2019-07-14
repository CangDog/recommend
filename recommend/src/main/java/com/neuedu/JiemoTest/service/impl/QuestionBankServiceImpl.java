package com.neuedu.recommend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.neuedu.recommend.dao.BankMapper;
import com.neuedu.recommend.dao.QuestionInBankMapper;
import com.neuedu.recommend.dao.UserInfoMapper;
import com.neuedu.recommend.entity.Bank;
import com.neuedu.recommend.entity.BankExample;
import com.neuedu.recommend.entity.BankExample.Criteria;
import com.neuedu.recommend.entity.QuestionInBankExample;
import com.neuedu.recommend.entity.QuestionInBankKey;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.jms.queue.Produce;
import com.neuedu.recommend.service.QuestionBankService;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

	@Autowired
	BankMapper bankMapper;
	@Autowired
	QuestionInBankMapper questionInBank;
	@Autowired
	Produce produce;
	@Autowired
	UserInfoMapper userMapper;
	//
	@Override
	public List<Bank> selectByUser(UserInfo user ,int state) {
		// TODO Auto-generated method stub
		//到数据库中查询
		System.out.println("in mysql !!!!!!!!!!!!!!!!!!");
		BankExample example = new BankExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(user.getUserid());
		criteria.andSpare1EqualTo(state);
		
		List<Bank> list = bankMapper.selectByExample(example); 
		return list;
	}

	@Override
	public void deletBank(int id) {
		// TODO Auto-generated method stub
		Bank change = new Bank();
		change.setBankid(id);
		change.setSpare1(0);
		//System.out.println("sql");
		bankMapper.updateByPrimaryKeySelective(change);
		//System.out.println("after sql !!!!!!!!!!");
		return;
	}

	@Override
	public void recoverBank(int id) {
		// TODO Auto-generated method stub
		Bank change = new Bank();
		change.setBankid(id);
		change.setSpare1(1);
		//System.out.println("sql");
		bankMapper.updateByPrimaryKeySelective(change);
		//System.out.println("after sql !!!!!!!!!!");
		return;
	}

	@Override
	public void save(Bank bank) {
		// TODO Auto-generated method stub
		System.out.println("start create");
		bankMapper.insert(bank);
		System.out.println("end!");
	}

	@Override
	public void update(Bank bank) {
		// TODO Auto-generated method stub
		bankMapper.updateByPrimaryKeySelective(bank);
	}
	
	@Override
	public Bank selectByPrimaryKey(int bankId) {
		return bankMapper.selectByPrimaryKey(bankId);
	}

	@Override
	public void realDeletBank(int bankId) {
		//题库与题目关系题目
		QuestionInBankExample example = new QuestionInBankExample();
		
		
		
		com.neuedu.recommend.entity.QuestionInBankExample.Criteria criteria = example.createCriteria();
		criteria.andBankidEqualTo(bankId);
		//包含的question，将信息放入activemq中等待处理
		List<QuestionInBankKey> keys = questionInBank.selectByExample(example);
		String str = JSON.toJSONString(keys);		
		questionInBank.deleteByExample(example);
		
		System.out.println("to mq!");
		produce.sendBankQuestion(str);
		
		//删除题库
		bankMapper.deleteByPrimaryKey(bankId);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void copyBank(int sourceId, int userId) {
		// TODO Auto-generated method stub
		Bank sourceBank = bankMapper.selectByPrimaryKey(sourceId);
		Bank newBank = new Bank();
		newBank.setBankintroduction(sourceBank.getBankintroduction());
		newBank.setBankname(sourceBank.getBankname());
		newBank.setSourcebankid(sourceId);
		newBank.setCreatedate((int) (System.currentTimeMillis() / 1000));
		newBank.setSpare1(1);
		newBank.setUserid(userId);
		newBank.setSpare1(sourceBank.getSpare1());
		newBank.setSpare2(sourceBank.getSpare2());
		newBank.setSpare3(sourceBank.getSpare3());
		newBank.setSpare4(sourceBank.getSpare4());
		
		//插入新bank
		bankMapper.insert(newBank);
		int newbankId = bankMapper.selectLastInsert();
		//复制题目
		QuestionInBankExample example = new QuestionInBankExample();
		com.neuedu.recommend.entity.QuestionInBankExample.Criteria criteria = example.createCriteria();
		criteria.andBankidEqualTo(sourceId);
		List<QuestionInBankKey> keys = questionInBank.selectByExample(example);
		
		for (QuestionInBankKey questionInBankKey : keys) {
			QuestionInBankKey key = new QuestionInBankKey();
			key.setBankid(newbankId);
			key.setQuestionid(questionInBankKey.getQuestionid());
			questionInBank.insert(key);
		}
				
	}
	
}
