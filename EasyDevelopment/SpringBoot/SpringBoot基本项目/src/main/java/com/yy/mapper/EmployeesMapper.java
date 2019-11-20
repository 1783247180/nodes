package com.yy.mapper;

import com.yy.pojo.Employees;
import com.yy.pojo.EmployeesExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeesMapper  {
    long countByExample(EmployeesExample example);

    int deleteByExample(EmployeesExample example);

    int deleteByPrimaryKey(Integer employeeId);

    int insert(Employees record);

    int insertSelective(Employees record);

    List<Employees> selectByExample(EmployeesExample example);

    Employees selectByPrimaryKey(Integer employeeId);

    int updateByExampleSelective(@Param("record") Employees record, @Param("example") EmployeesExample example);

    int updateByExample(@Param("record") Employees record, @Param("example") EmployeesExample example);

    int updateByPrimaryKeySelective(Employees record);

    int updateByPrimaryKey(Employees record);
}