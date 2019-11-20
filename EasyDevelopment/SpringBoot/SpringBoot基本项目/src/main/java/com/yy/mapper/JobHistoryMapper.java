package com.yy.mapper;

import com.yy.pojo.JobHistory;
import com.yy.pojo.JobHistoryExample;
import com.yy.pojo.JobHistoryKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobHistoryMapper  {
    long countByExample(JobHistoryExample example);

    int deleteByExample(JobHistoryExample example);

    int deleteByPrimaryKey(JobHistoryKey key);

    int insert(JobHistory record);

    int insertSelective(JobHistory record);

    List<JobHistory> selectByExample(JobHistoryExample example);

    JobHistory selectByPrimaryKey(JobHistoryKey key);

    int updateByExampleSelective(@Param("record") JobHistory record, @Param("example") JobHistoryExample example);

    int updateByExample(@Param("record") JobHistory record, @Param("example") JobHistoryExample example);

    int updateByPrimaryKeySelective(JobHistory record);

    int updateByPrimaryKey(JobHistory record);
}