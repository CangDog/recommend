package com.neuedu.recommend.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.neuedu.recommend.entity.BonusPointsRecord;
import com.neuedu.recommend.entity.Goods;
import com.neuedu.recommend.entity.UserInfo;

public interface ShopSerivce {

	void creatGoods(Goods goods);

	List<BonusPointsRecord> showRecords(int userId);

	@Transactional
	void chargeRecord(UserInfo user, BonusPointsRecord record);

}
