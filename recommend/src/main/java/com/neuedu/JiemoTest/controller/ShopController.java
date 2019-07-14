package com.neuedu.recommend.controller;

import java.util.List;

import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.neuedu.recommend.dao.UserInfoMapper;
import com.neuedu.recommend.entity.Bank;
import com.neuedu.recommend.entity.BonusPointsRecord;
import com.neuedu.recommend.entity.Goods;
import com.neuedu.recommend.entity.Order1;
import com.neuedu.recommend.entity.UserInfo;
import com.neuedu.recommend.service.OrderService;
import com.neuedu.recommend.service.QuestionBankService;
import com.neuedu.recommend.service.ShopSerivce;
import com.neuedu.recommend.service.UserService;

@Controller
public class ShopController {

	@Autowired
	ShopSerivce shopSerivce;
	
	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	QuestionBankService bankService;
	
	@RequestMapping("/creatGoods")
	@ResponseBody
	public String creatGoods(HttpServletRequest request , @RequestBody JSONObject data) {
		System.out.println(JSON.toJSONString(data));
		//var Jsdata = '{"bankid":'+bankid+',"price":"'+price+'","skill":"'+skill+'",type:0,"sp3":"'+name+'"}';
		UserInfo user = (UserInfo) request.getSession().getAttribute("UserInfo");
		
		Goods goods = new Goods();
		goods.setFromid(data.getIntValue("bankid"));
		goods.setGoodsprice(data.getFloat("price"));
		goods.setGoodstag(data.getString("skill"));
		//商品名字
		goods.setSpare3(data.getString("sp3"));
		//购买商品数量
		goods.setSpare1(0);
		goods.setGoodstype(data.getIntValue("type"));
		goods.setUserid(user.getUserid());
		
		int seconds = (int) (System.currentTimeMillis() / 1000);
		goods.setCreatetime(seconds);
		
		shopSerivce.creatGoods(goods);
		//使用ajax调用，返回值可以不用
		return "success";
	}
	
	@RequestMapping("/shareBanks")
	@ResponseBody
	public String shareBanks(HttpServletRequest request , @RequestBody JSONObject data) {
		System.out.println(JSON.toJSONString(data));

		UserInfo user = (UserInfo) request.getSession().getAttribute("UserInfo");
		//var Jsdata = '{"bankid":'+bankid+',"name":"'+name+'"}';
		String name = data.getString("name");
		
		int userid = userService.getIdByName(name);
		Order1 order1 = new Order1();
		order1.setUserid(userid);
		order1.setBonuspointsnum((float)0);
		//完成
		order1.setPaystate(1);
		//订单
		order1.setOrdertype(0);
		//分享则商品存bankid
		int bankid = data.getIntValue("bankid");
		order1.setGoodsid(bankid);
		//spar1分享者id
		order1.setSpare1(user.getUserid());
		
		int seconds = (int) (System.currentTimeMillis() / 1000);
		order1.setCreatetime(seconds);
		
		orderService.creatOrder(order1);
		//使用ajax调用，返回值可以不用
		return "success";
	}
	
	@RequestMapping("/showOrder")
	public String showOrder(HttpServletRequest request , Model model) {
		int type = Integer.parseInt(request.getParameter("type"));
		UserInfo user = (UserInfo) request.getSession().getAttribute("UserInfo");
		int userid = user.getUserid();
		List<Order1> order1s = orderService.selectOrder(userid,type);
		model.addAttribute("orders", order1s);
		model.addAttribute("ordersJSON",JSON.toJSONString(order1s));
		if (type == 0) {
			return "bank/share";
		}
 		else {
 			return "trad/orders";
		}
	}
	
	@RequestMapping("/copyBank")
	public String copyBank(HttpServletRequest request, @RequestBody JSONObject data) throws Exception {
		int sourceId = data.getIntValue("bankid");
		UserInfo user = (UserInfo) request.getSession().getAttribute("UserInfo");
		int userId = user.getUserid();
		
		List<Bank> banks = bankService.selectByUser(user, 1);
		for (Bank bank : banks) {
			if (bank.getSourcebankid()==sourceId) {
				//已经有复制的bank
				throw new Exception();
			}
		}
		bankService.copyBank(sourceId,userId);
		return "success";
	}
	
	@RequestMapping("/showPoints")
	public String showPoints(HttpServletRequest request , Model model) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("UserInfo");
		int userId = user.getUserid();
		List<BonusPointsRecord> records = shopSerivce.showRecords(userId);
		model.addAttribute("points",user.getBonuspoints());
		model.addAttribute("record",records);
		model.addAttribute("recordJSON",JSON.toJSONString(records));
		return "trad/points";
	}
	
	@RequestMapping("/creatRecord")
	@ResponseBody
	public String creatRecord(HttpServletRequest request , @RequestBody JSONObject data , Model model) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("UserInfo");
		int userId = user.getUserid();
		int userPoints =user.getBonuspoints();
		int changePoints = data.getIntValue("points");
		//var Jsdata = '{"points":'+points+'}';"notEnough"
		if (userPoints-changePoints<0) {
			return "notEnough";
		}
		//更新用户余额
		user.setBonuspoints(userPoints-changePoints);
		//新建账单
		BonusPointsRecord record = new BonusPointsRecord();
		record.setUserid(userId);
		if (changePoints < 0) {
			record.setOperatetype(1);
			changePoints = - changePoints;
		}else {
			record.setOperatetype(0);
		}
		record.setBonuspointsnum(changePoints);
		int seconds = (int) (System.currentTimeMillis() / 1000);
		record.setOperatetime(seconds);
		record.setAccount("success");
		//积分操作
		shopSerivce.chargeRecord(user , record);
		
		//更新session和model
		request.getSession().setAttribute("UserInfo", user);
		List<BonusPointsRecord> records = shopSerivce.showRecords(userId);
		model.addAttribute("points",user.getBonuspoints());
		model.addAttribute("record",records);
		model.addAttribute("recordJSON",JSON.toJSONString(records));
		return "success";
	}
}
