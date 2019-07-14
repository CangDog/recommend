package com.neuedu.recommend.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.neuedu.recommend.entity.Group1;
import com.neuedu.recommend.entity.Group1Example;
import com.neuedu.recommend.entity.GroupAndUser;
import com.neuedu.recommend.entity.UserInfo;

public interface Group1Service {
	public int createGroup(String name,String intro,UserInfo u);

	public int  joinGroup(String groupName,int userId);
	
	public int  outGroup(int userId,int groupId);
	
//  public int  editGroup(Group1 g);
	
//  public List<Group1> queryAll();
	
//	public Group1 selectone(int groupId);
	
	List<GroupAndUser> gauservice(Integer userid,int role);

	int AddMember(Integer userid,Integer groupid);
	
	int DeletMember(Integer userid,Integer groupid);
	
	int deleteGroup(Integer groupid);
	
	int exitGroup(Integer groupid,String name,String intro);


	public List<Group1> selectByExample(Group1Example example);
	
	public Group1 selectedByExample(Group1Example example);


	

}
