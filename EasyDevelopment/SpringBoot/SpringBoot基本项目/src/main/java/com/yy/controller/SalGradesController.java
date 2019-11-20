package com.yy.controller;


import com.yy.mapper.RegionsMapper;
import com.yy.mapper.SalGradesMapper;
import com.yy.pojo.Regions;
import com.yy.pojo.SalGrades;
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
@RequestMapping("/salGrades")
public class SalGradesController {
    @Autowired
    private SalGradesMapper salGradesMapper;
    //查出所有的数据到list
    @GetMapping("/sals")
    public String list(Model model){
        Collection<SalGrades> salGrades = salGradesMapper.selectByExample(null);
        System.out.println("----------------------"+salGrades.size());
        System.out.println("进来了");
        model.addAttribute("sals",salGrades);
        return "sal/list";
    }
    //来到添加页面
    @GetMapping("/sal")
    public String toAddPage(){
        return "sal/add";
    }
    //添加
    @PostMapping("/sal")
    public String addReg(SalGrades salGrades,Model model){
        salGradesMapper.insert(salGrades);
        // redirect:表示重定向到一个地址,受视图解析器的控制
        //forward:表示转发到一个地址,不受视图解析器的控制
        //  /代表当前项目
        return "redirect:/salGrades/sals";
    }
    //来到修改页面
    @GetMapping("/sal/{grade}")
    public String toEditPage(@PathVariable("grade") int grade, Model model){
        SalGrades salGrades = salGradesMapper.selectByPrimaryKey((short)grade);
        model.addAttribute("sal",salGrades);
        //add页面是修改与添加二合一
        return "sal/add";
    }
    //修改
    @PutMapping("/sal")
    public String updateReg(SalGrades salGrades){
        salGradesMapper.updateByPrimaryKey(salGrades);
        return "redirect:/salGrades/sals";
    }
    //删除
    @DeleteMapping("/sal/{grade}")
    public String deleteReg(@PathVariable("grade") int grade,Model model){
        salGradesMapper.deleteByPrimaryKey((short)grade);
        return "redirect:/salGrades/sals";
    }
}

