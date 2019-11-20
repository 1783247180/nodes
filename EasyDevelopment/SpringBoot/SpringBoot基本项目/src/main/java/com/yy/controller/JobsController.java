package com.yy.controller;


import com.yy.mapper.JobsMapper;
import com.yy.pojo.Jobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Collection;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yux123
 * @since 2019-06-10
 */
@Controller
@RequestMapping("/jobs")
public class JobsController {
    @Autowired
    private JobsMapper jobsMapper;
    //查出所有的数据到list
    @GetMapping("/jobs")
    public String list(Model model){
        Collection<Jobs> jobs = jobsMapper.selectByExample(null);
        System.out.println("----------------------"+jobs.size());
        System.out.println("进来了");
        model.addAttribute("jobs",jobs);
        return "job/list";
    }
    //来到添加页面
    @GetMapping("/job")
    public String toAddPage(){
        return "job/add";
    }
    //添加
    @PostMapping("/job")
    public String addReg(Jobs jobs,Model model){
        jobsMapper.insert(jobs);
        // redirect:表示重定向到一个地址,受视图解析器的控制
        //forward:表示转发到一个地址,不受视图解析器的控制
        //  /代表当前项目
        return "redirect:/jobs/jobs";
    }
    //来到修改页面
    @GetMapping("/job/{jobId}")
    public String toEditPage(@PathVariable("jobId") String jobId, Model model){
        Jobs jobs = jobsMapper.selectByPrimaryKey(jobId);
        model.addAttribute("job",jobs);
        //add页面是修改与添加二合一
        return "job/add";
    }
    //修改
    @PutMapping("/job")
    public String updateReg(Jobs jobs){
        jobsMapper.updateByPrimaryKey(jobs);
        return "redirect:/jobs/jobs";
    }
    //删除
    @DeleteMapping("/job/{jobId}")
    public String deleteReg(@PathVariable("jobId") String jobId,Model model){
        jobsMapper.deleteByPrimaryKey(jobId);
        return "redirect:/jobs/jobs";
    }
}

