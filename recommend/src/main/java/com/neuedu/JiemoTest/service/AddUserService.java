package com.neuedu.recommend.service;

import java.util.List;

import com.neuedu.recommend.entity.Bank;

public interface AddUserService {

	String AddOneUser(int examid, String username, String userinfo, int infoType,String title);


	String AddOneUser(int examid, int userId);

}
