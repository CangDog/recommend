package com.neuedu.recommend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neuedu.recommend.dao.BankMapper;
import com.neuedu.recommend.dao.GoodsMapper;
import com.neuedu.recommend.dao.Order1Mapper;
import com.neuedu.recommend.dao.QuestionInBankMapper;
import com.neuedu.recommend.dao.QuestionMapper;
import com.neuedu.recommend.dao.UserInfoMapper;
import com.neuedu.recommend.entity.Bank;
import com.neuedu.recommend.entity.BankExample;
import com.neuedu.recommend.entity.Goods;
import com.neuedu.recommend.entity.Order1;
import com.neuedu.recommend.entity.Question;
import com.neuedu.recommend.entity.QuestionExample;
import com.neuedu.recommend.entity.QuestionExample.Criteria;
import com.neuedu.recommend.entity.QuestionInBankExample;
import com.neuedu.recommend.entity.QuestionInBankKey;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.service.BankService;
import com.neuedu.recommend.service.QuestionBankService;

@Service
public class BankServiceImpl implements BankService{
	
	@Autowired
    QuestionMapper questionMapper;
	@Autowired
	QuestionInBankMapper questionInBankMapper;
	@Autowired
	GoodsMapper goodsmapper;
	@Autowired
	BankMapper bankMapper;
	@Autowired
	UserInfoMapper userInfoMapper;
	@Autowired
	Order1Mapper order1Mapper;
	@Autowired
	QuestionBankService questionBankService;
	
	@Override
	public List<Question> getQuestionByBankID(int bankid) {
		// TODO Auto-generated method stub
		List<Question> questions=questionInBankMapper.selectQuestionByQuestionBank(bankid, 0, 5);
		
		return questions;
	}

	@Override
	public int getQuestionNum(int bankid, int questionType) {
		// TODO Auto-generated method stub
		QuestionInBankExample example = new QuestionInBankExample();
		com.neuedu.recommend.entity.QuestionInBankExample.Criteria criteria = example.createCriteria();
		criteria.andBankidEqualTo(bankid);
		List<QuestionInBankKey> list = questionInBankMapper.selectByExample(example);
		List<Integer> questionIdIntegers = new ArrayList<Integer>();
		for(QuestionInBankKey q:list) {
			questionIdIntegers.add(q.getQuestionid());
		}
		
		QuestionExample Questionexample = new QuestionExample();
		Criteria Questioncriteria = Questionexample.createCriteria();
		Questioncriteria.andQuestionidIn(questionIdIntegers);
		Questioncriteria.andQuestiontypeEqualTo(questionType);
		int ret = questionMapper.countByExample(Questionexample);
		return ret;
	}

	@Override
	public Goods getGoods(int goodsid) {
		// TODO Auto-generated method stub
		Goods goods=goodsmapper.selectByPrimaryKey(goodsid);
		return goods;
	}

	@Transactional
	@Override
	public int getUserBank(int userid, int bankid) {
		System.out.println("userid:"+userid+"    bankid:"+bankid);
		// TODO Auto-generated method stub
		BankExample example=new BankExample();//第一种情况 题库是自己创建的 bankid和输入值符合
		com.neuedu.recommend.entity.BankExample.Criteria criteria=example.createCriteria();
		criteria.andUseridEqualTo(userid);
		criteria.andBankidEqualTo(bankid);
		int ret=bankMapper.countByExample(example);
		
		BankExample example2=new BankExample();//第一种情况 题库是买过的sourceid和输入值符合
		com.neuedu.recommend.entity.BankExample.Criteria criteria2=example2.createCriteria();
		criteria2.andUseridEqualTo(userid);
		criteria2.andSourcebankidEqualTo(bankid);
		int ret2=bankMapper.countByExample(example2);	
		//两种情况只要有一种为1 说明拥有此题库 返回较大的即可
		if(ret>ret2) 
			return ret;
		else 
			return ret2;
		
	
	}

	@Transactional
	@Override
	public void buyGoods(int userid,int  goodsid, int  goodsprice,int  bankid) {
		// TODO Auto-generated method stub
		//本人题库增加一个题库 并将商品购买数加1，生成订单记录，卖家积分增加，本人积分减少
		questionBankService.copyBank(bankid, userid);
		
		Goods record2=new Goods();
		record2.setGoodsid(goodsid);
		record2.setSpare1(goodsmapper.selectByPrimaryKey(goodsid).getSpare1()+1);
		goodsmapper.updateByPrimaryKeySelective(record2);
		
		Order1 record3=new Order1();
		record3.setUserid(userid);
		record3.setGoodsid(goodsid);
		record3.setCreatetime((int)System.currentTimeMillis()/1000);
		record3.setBonuspointsnum((float) goodsprice);
		record3.setPaystate(1);//1已支付
		record3.setOrdertype(1);//1购买
		order1Mapper.insert(record3);
		
		UserInfo record4=new UserInfo();
		record4.setUserid(userid);
		record4.setBonuspoints(userInfoMapper.selectByPrimaryKey(userid).getBonuspoints()-goodsprice);
		userInfoMapper.updateByPrimaryKeySelective(record4);
		//System.out.println(" -------"+userid+" "+userInfoMapper.selectByPrimaryKey(userid).getBonuspoints());
		
		Goods goods=goodsmapper.selectByPrimaryKey(goodsid);
		int fromid=goods.getUserid();
		UserInfo record5=new UserInfo();
		record5.setUserid(fromid);
		record5.setBonuspoints(userInfoMapper.selectByPrimaryKey(fromid).getBonuspoints()+goodsprice);
		userInfoMapper.updateByPrimaryKeySelective(record5);
		//System.out.println(" -------"+fromid+" "+userInfoMapper.selectByPrimaryKey(fromid).getBonuspoints());
	}

}
