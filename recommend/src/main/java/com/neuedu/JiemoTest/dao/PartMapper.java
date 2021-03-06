package com.neuedu.recommend.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.neuedu.recommend.entity.Part;
import com.neuedu.recommend.entity.PartExample;
@Mapper
public interface PartMapper {
    int countByExample(PartExample example);

    int deleteByExample(PartExample example);

    int deleteByPrimaryKey(Integer partid);

    int insert(Part record);

    int insertSelective(Part record);

    List<Part> selectByExample(PartExample example);

    Part selectByPrimaryKey(Integer partid);

    int updateByExampleSelective(@Param("record") Part record, @Param("example") PartExample example);

    int updateByExample(@Param("record") Part record, @Param("example") PartExample example);

    int updateByPrimaryKeySelective(Part record);

    int updateByPrimaryKey(Part record);
}