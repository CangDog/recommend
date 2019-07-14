package com.neuedu.recommend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neuedu.recommend.dao.UserInfoMapper;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.entity.UserInfoExample;
import com.neuedu.recommend.entity.UserInfoExample.Criteria;
import com.neuedu.recommend.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserInfoMapper userInfoMapper;

	@Override
	public int getIdByName(String name) {
		// TODO Auto-generated method stub
		UserInfoExample example = new UserInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(name);
		List<UserInfo> userInfo = userInfoMapper.selectByExample(example);
		return userInfo.get(0).getUserid();
	}

}
