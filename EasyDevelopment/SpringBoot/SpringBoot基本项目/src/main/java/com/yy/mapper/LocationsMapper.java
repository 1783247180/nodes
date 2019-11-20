package com.yy.mapper;

import com.yy.pojo.Locations;
import com.yy.pojo.LocationsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LocationsMapper  {
    long countByExample(LocationsExample example);

    int deleteByExample(LocationsExample example);

    int deleteByPrimaryKey(Short locationId);

    int insert(Locations record);

    int insertSelective(Locations record);

    List<Locations> selectByExample(LocationsExample example);

    Locations selectByPrimaryKey(Short locationId);

    int updateByExampleSelective(@Param("record") Locations record, @Param("example") LocationsExample example);

    int updateByExample(@Param("record") Locations record, @Param("example") LocationsExample example);

    int updateByPrimaryKeySelective(Locations record);

    int updateByPrimaryKey(Locations record);
}