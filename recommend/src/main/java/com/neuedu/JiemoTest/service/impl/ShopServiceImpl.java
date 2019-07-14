package com.neuedu.recommend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neuedu.recommend.dao.BonusPointsRecordMapper;
import com.neuedu.recommend.dao.GoodsMapper;
import com.neuedu.recommend.dao.UserInfoMapper;
import com.neuedu.recommend.entity.BonusPointsRecord;
import com.neuedu.recommend.entity.BonusPointsRecordExample;
import com.neuedu.recommend.entity.BonusPointsRecordExample.Criteria;
import com.neuedu.recommend.entity.Goods;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.service.ShopSerivce;

@Service
public class ShopServiceImpl implements ShopSerivce {

	@Autowired
	GoodsMapper goodsMapper;
	
	@Autowired
	BonusPointsRecordMapper recordMapper;
	
	@Autowired
	UserInfoMapper userMapper;
	
	@Override
	public void creatGoods(Goods goods) {
		// TODO Auto-generated method stub
		goodsMapper.insert(goods);
	}

	@Override
	public List<BonusPointsRecord> showRecords(int userId) {
		// TODO Auto-generated method stub
		BonusPointsRecordExample example = new BonusPointsRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userId);
		return recordMapper.selectByExample(example);
	}

	@Override
	public void chargeRecord(UserInfo user, BonusPointsRecord record) {
		// TODO Auto-generated method stub
		userMapper.updateByPrimaryKey(user);
		recordMapper.insert(record);
	}

}
