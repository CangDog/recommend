package com.neuedu.recommend.entity;

import java.util.List;

public class GroupAndUser {
	
//可以加一个老师
	Group1 group;
	List<UserInfo> users;

	public List<UserInfo> getUsers() {
		return users;
	}
	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}
	public Group1 getGroup() {
		return group;
	}
	public void setGroup(Group1 group) {
		this.group = group;
	}
}
