package com.yy.mapper;

import com.yy.pojo.SalGrades;
import com.yy.pojo.SalGradesExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalGradesMapper  {
    long countByExample(SalGradesExample example);

    int deleteByExample(SalGradesExample example);

    int deleteByPrimaryKey(Short grade);

    int insert(SalGrades record);

    int insertSelective(SalGrades record);

    List<SalGrades> selectByExample(SalGradesExample example);

    SalGrades selectByPrimaryKey(Short grade);

    int updateByExampleSelective(@Param("record") SalGrades record, @Param("example") SalGradesExample example);

    int updateByExample(@Param("record") SalGrades record, @Param("example") SalGradesExample example);

    int updateByPrimaryKeySelective(SalGrades record);

    int updateByPrimaryKey(SalGrades record);
}