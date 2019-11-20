package com.yy.mapper;

import com.yy.pojo.Countries;
import com.yy.pojo.CountriesExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CountriesMapper  {
    long countByExample(CountriesExample example);

    int deleteByExample(CountriesExample example);

    int deleteByPrimaryKey(String countryId);

    int insert(Countries record);

    int insertSelective(Countries record);

    List<Countries> selectByExample(CountriesExample example);

    Countries selectByPrimaryKey(String countryId);

    int updateByExampleSelective(@Param("record") Countries record, @Param("example") CountriesExample example);

    int updateByExample(@Param("record") Countries record, @Param("example") CountriesExample example);

    int updateByPrimaryKeySelective(Countries record);

    int updateByPrimaryKey(Countries record);
}