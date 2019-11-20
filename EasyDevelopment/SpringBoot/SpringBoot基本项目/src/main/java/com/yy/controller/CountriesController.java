package com.yy.controller;


import com.yy.mapper.CountriesMapper;
import com.yy.mapper.RegionsMapper;
import com.yy.pojo.Countries;
import com.yy.pojo.Regions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Collection;
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
@RequestMapping("/countries")
public class CountriesController {
    @Autowired
    private CountriesMapper countriesMapper;
    @Autowired
    private RegionsMapper regionsMapper;
    //查出所有的数据到list
    @GetMapping("/cous")
    public String list(Model model){
        Collection<Countries> countries = countriesMapper.selectByExample(null);
        System.out.println("----------------------"+countries.size());
        System.out.println("进来了");
        model.addAttribute("cous",countries);
        return "cou/list";
    }
    //来到添加页面
    @GetMapping("/cou")
    public String toAddPage(Model model){
        List<Regions> regions = regionsMapper.selectByExample(null);
        model.addAttribute("regs",regions);
        return "cou/add";
    }
    //添加
    @PostMapping("/cou")
    public String addcou(Countries countries,Model model){
        countriesMapper.insert(countries);
        // redirect:表示重定向到一个地址,受视图解析器的控制
        //forward:表示转发到一个地址,不受视图解析器的控制
        //  /代表当前项目
        return "redirect:/countries/cous";
    }
    //来到修改页面
    @GetMapping("/cou/{countryId}")
    public String toEditPage(@PathVariable("countryId") String countryId, Model model){
        Countries countries = countriesMapper.selectByPrimaryKey(countryId);
        model.addAttribute("cou",countries);
        List<Regions> regions = regionsMapper.selectByExample(null);
        model.addAttribute("regs",regions);
        //add页面是修改与添加二合一
        return "cou/add";
    }
    //修改
    @PutMapping("/cou")
    public String updatecou(Countries countries ){
        countriesMapper.updateByPrimaryKey(countries);
        return "redirect:/countries/cous";
    }
    //删除
    @DeleteMapping("/cou/{countryId}")
    public String deletecou(@PathVariable("countryId") String countryId,Model model){
        countriesMapper.deleteByPrimaryKey(countryId);
        return "redirect:/countries/cous";
    }
}

