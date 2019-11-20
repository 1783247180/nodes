package com.yy.controller;


import com.yy.config.DateUtil;
import com.yy.mapper.DepartmentsMapper;
import com.yy.mapper.JobHistoryMapper;
import com.yy.mapper.JobsMapper;
import com.yy.pojo.*;
import oracle.sql.TIMESTAMP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yux123
 * @since 2019-06-10
 */
@Controller
@RequestMapping("/jobHistory")
public class JobHistoryController {
    @Autowired
    private JobHistoryMapper jobHistoryMapper;
    @Autowired
    private DepartmentsMapper departmentsMapper;
    @Autowired
    private JobsMapper jobsMapper;
    @Autowired
    private JobHistoryKey jobHistoryKey;
    //查出所有的数据到list
    @GetMapping("/jobhs")
    public String list(Model model){
        Collection<JobHistory> jobHistory = jobHistoryMapper.selectByExample(null);
        System.out.println("----------------------"+jobHistory.size());
        System.out.println("进来了");
        model.addAttribute("jobhs",jobHistory);
        return "jobh/list";
    }
    //来到添加页面
    @GetMapping("/jobh")
    public String toAddPage(Model model){
        List<Departments> departments = departmentsMapper.selectByExample(null);
        List<Jobs> jobs = jobsMapper.selectByExample(null);
        model.addAttribute("deps",departments);
        model.addAttribute("jobs",jobs);
        return "jobh/add";
    }
    //添加
    @PostMapping("/jobh")
    public String addjobh(JobHistory jobHistory,Model model){
        System.out.println("-----------------jobH"+jobHistory.getEndDate());
        jobHistoryMapper.insert(jobHistory);
        // redirect:表示重定向到一个地址,受视图解析器的控制
        //forward:表示转发到一个地址,不受视图解析器的控制
        //  /代表当前项目
        return "redirect:/jobHistory/jobhs";
    }
    //来到修改页面
    @GetMapping("/jobh/{employeeId}/{startDate}")
    public String toEditPage(@PathVariable("employeeId") int employeeId, @PathVariable("startDate") String sDate, Model model){
        Date date = DateUtil.getDate(sDate);
        JobHistoryKey jobHistoryKey=new JobHistoryKey();
            jobHistoryKey.setEmployeeId(employeeId);
            jobHistoryKey.setStartDate(date);
        JobHistory jobHistory = jobHistoryMapper.selectByPrimaryKey(jobHistoryKey);
        model.addAttribute("jobh",jobHistory);
        List<Departments> departments = departmentsMapper.selectByExample(null);
        List<Jobs> jobs = jobsMapper.selectByExample(null);
        model.addAttribute("deps",departments);
        model.addAttribute("jobs",jobs);
        //add页面是修改与添加二合一
        return "jobh/add";
    }
    //修改
    @PutMapping("/jobh")
    public String updatejobh(JobHistory jobHistory ){
        jobHistoryMapper.updateByPrimaryKey(jobHistory);
        return "redirect:/jobHistory/jobhs";
    }
    //删除
    @DeleteMapping("/jobh/{employeeId}/{startDate}")
    public String deletejobh(@PathVariable("employeeId") int employeeId, @PathVariable("startDate")String sDate,Model model){
        Date date = DateUtil.getDate(sDate);
        JobHistoryKey jobHistoryKey=new JobHistoryKey();
        jobHistoryKey.setEmployeeId(employeeId);
        jobHistoryKey.setStartDate(date);
        jobHistoryMapper.deleteByPrimaryKey(jobHistoryKey);
        return "redirect:/jobHistory/jobhs";
    }
}

