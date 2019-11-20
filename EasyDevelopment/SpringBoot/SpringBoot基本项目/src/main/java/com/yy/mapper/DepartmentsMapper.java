package com.yy.mapper;

import com.yy.pojo.Departments;
import com.yy.pojo.DepartmentsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentsMapper  {
    long countByExample(DepartmentsExample example);

    int deleteByExample(DepartmentsExample example);

    int deleteByPrimaryKey(Short departmentId);

    int insert(Departments record);

    int insertSelective(Departments record);

    List<Departments> selectByExample(DepartmentsExample example);

    Departments selectByPrimaryKey(Short departmentId);

    int updateByExampleSelective(@Param("record") Departments record, @Param("example") DepartmentsExample example);

    int updateByExample(@Param("record") Departments record, @Param("example") DepartmentsExample example);

    int updateByPrimaryKeySelective(Departments record);

    int updateByPrimaryKey(Departments record);
}