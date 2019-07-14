package com.neuedu.recommend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neuedu.recommend.dao.AnnouncementMapper;
import com.neuedu.recommend.dao.Group1Mapper;
import com.neuedu.recommend.dao.GroupUserMapper;
import com.neuedu.recommend.dao.HomeworkMapper;
import com.neuedu.recommend.dao.UserInfoMapper;
import com.neuedu.recommend.entity.AnnouncementExample;
import com.neuedu.recommend.entity.Group1;
import com.neuedu.recommend.entity.Group1Example;
import com.neuedu.recommend.entity.Group1Example.Criteria;
import com.neuedu.recommend.entity.GroupAndUser;
import com.neuedu.recommend.entity.GroupUser;
import com.neuedu.recommend.entity.GroupUserExample;
import com.neuedu.recommend.entity.GroupUserKey;
import com.neuedu.recommend.entity.HomeworkExample;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.service.Group1Service;
@Service
public class Group1ServiceImpl implements Group1Service{
	
	@Autowired
	Group1Mapper group1Mapper;
	
	@Autowired
	GroupUserMapper groupUserMapper;
	
	@Autowired
	UserInfoMapper userInfoMapper;
	
	@Autowired
	AnnouncementMapper announcementMapper;
	
	@Autowired
	HomeworkMapper homeworkMapper;
	
	
	
	//创建群组
	@Override
	public int createGroup(String name, String intro, UserInfo u) {
		//返回i=1插入成功；返回-1表示重名
		Group1Example example = new Group1Example();
		Criteria cc = example.createCriteria();
		cc.andNameEqualTo(name);
		int j = group1Mapper.countByExample(example);
		if(j == 0) {
			Group1 g = new Group1();    
			int timenow = (int) (System.currentTimeMillis() / 1000);
			g.setCreatetime(timenow);
			g.setCreateuserid(u.getUserid());
			g.setName(name);
			int i=group1Mapper.insert(g);
			return i;
		}else {
			return -1;
		}
	}

	//显示群组及成员
		@Override
		public List<GroupAndUser> gauservice(Integer userid, int role) {
			List<GroupAndUser> gau = new ArrayList<GroupAndUser>();
			if(role == 1) {
				Group1Example example = new Group1Example();
				example.or().andCreateuseridEqualTo(userid);
				List<Group1> g1 = group1Mapper.selectByExample(example);
				for(Group1 g : g1) {
					GroupUserExample example2 = new GroupUserExample();
					example2.or().andGroupidEqualTo(g.getGroupid());
					List<GroupUser> gu = groupUserMapper.selectByExample(example2);
					
					List<UserInfo> ul = new ArrayList<UserInfo>();
					for(GroupUser gg : gu) {
						UserInfo u = userInfoMapper.selectByPrimaryKey(gg.getUserid());
						ul.add(u);
					}
					GroupAndUser gau1 = new GroupAndUser();
					gau1.setGroup(g);
					gau1.setUsers(ul);
					gau.add(gau1);
				}
			}else{
				GroupUserExample example3=new GroupUserExample();
				example3.or().andUseridEqualTo(userid);
				List<GroupUser> g2=groupUserMapper.selectByExample(example3);
				
				
				for(GroupUser g:g2) {
				    List<UserInfo> ul=new ArrayList<UserInfo>();
					Group1 g1 = group1Mapper.selectByPrimaryKey(g.getGroupid());
					
					GroupUserExample example2 = new GroupUserExample();
					example2.or().andGroupidEqualTo(g.getGroupid());
					List<GroupUser> gu = groupUserMapper.selectByExample(example2);
					for(GroupUser gg : gu) {
					UserInfo u = userInfoMapper.selectByPrimaryKey(gg.getUserid());
					ul.add(u);
					}
					
					GroupAndUser gau1 = new GroupAndUser();
					gau1.setGroup(g1);
					gau1.setUsers(ul);
					gau.add(gau1);
				}
			}
			System.out.println(gau);
			return gau;
		}
		

	
	      //删除群组
		@Override
	   public int deleteGroup(Integer groupid) {
		GroupUserExample example = new GroupUserExample(); 
		example.or().andGroupidEqualTo(groupid);
		groupUserMapper.deleteByExample(example);
			
		AnnouncementExample example2 = new AnnouncementExample();
		example2.or().andGroupidEqualTo(groupid);
		announcementMapper.deleteByExample(example2);
		
		HomeworkExample example3=new HomeworkExample();
		example3.or().andGroupidEqualTo(groupid);
		homeworkMapper.deleteByExample(example3);
				
		group1Mapper.deleteByPrimaryKey(groupid);//删除群组信息
		
		return 1;
	}
	
	//邀请学生
	@Override
	public int AddMember(Integer userid, Integer groupid) {
		//-1代表无此用户,0代表是老师,-2代表已添加此用户
		UserInfo u = userInfoMapper.selectByPrimaryKey(userid);
		if(u == null)
			return -1;
		if(u.getUsertype() == 1)
			return 0;
		GroupUserKey key = new GroupUserKey();
		key.setGroupid(groupid);
		key.setUserid(userid);
		GroupUser groupUser = groupUserMapper.selectByPrimaryKey(key);
		if(groupUser != null) 
			return -2;
		GroupUser gu = new GroupUser();
		gu.setGroupid(groupid);
		gu.setUserid(userid);
		int timenow = (int) (System.currentTimeMillis() / 1000);
		gu.setJointime(timenow);
		int i=groupUserMapper.insert(gu);
		return i;
	}
	//删除学生
	@Override
	public int DeletMember(Integer userid, Integer groupid) {
		GroupUserKey key =new GroupUserKey();
		key.setGroupid(groupid);
		key.setUserid(userid);
		int i=groupUserMapper.deleteByPrimaryKey(key );
		return i;
	}
     //加入群组
	@Override
	public int joinGroup(String groupName, int userId) {
		
		//Group1Example example = new Group1Example();
		//Criteria cc = example.createCriteria();
		//cc.andNameEqualTo(name);
		Group1Example example = new Group1Example();
       Criteria c1 = example.createCriteria();
       c1.andNameEqualTo(groupName);
		List<Group1> list = group1Mapper.selectByExample(example);		
		if(list == null)
			return -1;
		Integer groupid = -1;
		for(Group1 g : list) {
			groupid= g.getGroupid();
		}
		GroupUser gu = new GroupUser();
		gu.setGroupid(groupid);
		gu.setUserid(userId);
		int timenow = (int) (System.currentTimeMillis() / 1000);
		gu.setJointime(timenow);
		int i=groupUserMapper.insert(gu);
		return i;
		
		
	
	}

	//退出群组
	@Override
	public int outGroup(int groupId,int userId) {
		GroupUserKey p1=new GroupUserKey();
		p1.setGroupid(groupId);
		p1.setUserid(userId);
		int i=groupUserMapper.deleteByPrimaryKey(p1);
		return i;
		
	}



	
	
	
	
	
	
//	@Override
//	public List<Group1> queryAll() {
//		List<Group1> list=group1Mapper.selectAll();
//		// TODO Auto-generated method stub
//		return list;
//	}

//	@Override
//	public int editGroup(Group1 g) {
//		int i=group1Mapper.updateByPrimaryKeySelective(g);
//		// TODO Auto-generated method stub
//		return i;
//	}

//	@Override
//	public Group1 selectone(int groupId) {
//		Group1 i=group1Mapper.selectByPrimaryKey(groupId);
//		// TODO Auto-generated method stub
//		return i;
//	}

	@Override
	public List<Group1> selectByExample(Group1Example example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group1 selectedByExample(Group1Example example) {
		// TODO Auto-generated method stub
		return null;
	}




	//编辑群组
	@Override
	public int exitGroup(Integer groupid, String name, String intro) {
		Group1 g = group1Mapper.selectByPrimaryKey(groupid);
		g.setName(name);
		g.setIntroduction(intro);
		group1Mapper.updateByPrimaryKey(g);
		return 1;
			}
}
