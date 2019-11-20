package com.yy.mapper;

import com.yy.pojo.Jobs;
import com.yy.pojo.JobsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobsMapper  {
    long countByExample(JobsExample example);

    int deleteByExample(JobsExample example);

    int deleteByPrimaryKey(String jobId);

    int insert(Jobs record);

    int insertSelective(Jobs record);

    List<Jobs> selectByExample(JobsExample example);

    Jobs selectByPrimaryKey(String jobId);

    int updateByExampleSelective(@Param("record") Jobs record, @Param("example") JobsExample example);

    int updateByExample(@Param("record") Jobs record, @Param("example") JobsExample example);

    int updateByPrimaryKeySelective(Jobs record);

    int updateByPrimaryKey(Jobs record);
}