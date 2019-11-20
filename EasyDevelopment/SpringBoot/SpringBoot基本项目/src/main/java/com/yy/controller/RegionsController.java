package com.yy.controller;


import com.yy.mapper.RegionsMapper;
import com.yy.pojo.Regions;
import com.yy.pojo.RegionsExample;
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
@RequestMapping("/regions")
public class RegionsController {
    @Autowired
    private RegionsMapper regionsMapper;
    //查出所有的数据到list
    @GetMapping("/regs")
    public String list(Model model){
        Collection<Regions> regions = regionsMapper.selectByExample(null);
        System.out.println("----------------------"+regions.size());
        System.out.println("进来了");
        model.addAttribute("regs",regions);
        return "reg/list";
    }
    //来到添加页面
    @GetMapping("/reg")
    public String toAddPage(){
        return "reg/add";
    }
    //添加
    @PostMapping("/reg")
    public String addReg(Regions regions,Model model){
        regionsMapper.insert(regions);
        // redirect:表示重定向到一个地址,受视图解析器的控制
        //forward:表示转发到一个地址,不受视图解析器的控制
        //  /代表当前项目
        return "redirect:/regions/regs";
    }
    //来到修改页面
    @GetMapping("/reg/{regionId}")
    public String toEditPage(@PathVariable("regionId") int regionId, Model model){
        Regions regions = regionsMapper.selectByPrimaryKey((short) regionId);
        model.addAttribute("reg",regions);
        //add页面是修改与添加二合一
        return "reg/add";
    }
    //修改
    @PutMapping("/reg")
    public String updateReg(int regionId,String regionName){
        System.out.println("---------------------"+regionId+regionName+"----------------------------------");
        Regions regions = new Regions();
        regions.setRegionId((short)regionId);
        regions.setRegionName(regionName);
        regionsMapper.updateByPrimaryKey(regions);
        return "redirect:/regions/regs";
    }
    //删除
    @DeleteMapping("/reg/{regionId}")
    public String deleteReg(@PathVariable("regionId") int regionId,Model model){
        regionsMapper.deleteByPrimaryKey((short)regionId);
        return "redirect:/regions/regs";
    }
}

