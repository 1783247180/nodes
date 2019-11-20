package com.yy.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class JobHistory extends JobHistoryKey {

    private Date endDate;

    private String jobId;

    private Short departmentId;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId == null ? null : jobId.trim();
    }

    public Short getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Short departmentId) {
        this.departmentId = departmentId;
    }
}