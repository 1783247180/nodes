package com.yy.mapper;

import com.yy.pojo.Regions;
import com.yy.pojo.RegionsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RegionsMapper  {
    long countByExample(RegionsExample example);

    int deleteByExample(RegionsExample example);

    int deleteByPrimaryKey(Short regionId);

    int insert(Regions record);

    int insertSelective(Regions record);

    List<Regions> selectByExample(RegionsExample example);

    Regions selectByPrimaryKey(Short regionId);

    int updateByExampleSelective(@Param("record") Regions record, @Param("example") RegionsExample example);

    int updateByExample(@Param("record") Regions record, @Param("example") RegionsExample example);

    int updateByPrimaryKeySelective(Regions record);

    int updateByPrimaryKey(Regions record);
}