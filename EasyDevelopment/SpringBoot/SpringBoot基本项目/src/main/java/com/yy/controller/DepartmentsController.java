package com.yy.controller;


import com.yy.mapper.LocationsMapper;
import com.yy.mapper.DepartmentsMapper;
import com.yy.pojo.Locations;
import com.yy.pojo.Departments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
@RequestMapping("/departments")
public class DepartmentsController {
    @Autowired
    private DepartmentsMapper departmentsMapper;
    @Autowired
    private LocationsMapper locationsMapper;
    //查出所有的数据到list
    @GetMapping("/deps")
    public String list(Model model){
        Collection<Departments> departments = departmentsMapper.selectByExample(null);
        System.out.println("----------------------"+departments.size());
        System.out.println("进来了");
        model.addAttribute("deps",departments);
        return "dep/list";
    }
    //来到添加页面
    @GetMapping("/dep")
    public String toAddPage(Model model){
        List<Locations> locations = locationsMapper.selectByExample(null);
        model.addAttribute("locs",locations);
        return "dep/add";
    }
    //添加
    @PostMapping("/dep")
    public String adddep(Departments departments,Model model){
        departmentsMapper.insert(departments);
        // redirect:表示重定向到一个地址,受视图解析器的控制
        //forward:表示转发到一个地址,不受视图解析器的控制
        //  /代表当前项目
        return "redirect:/departments/deps";
    }
    //来到修改页面
    @GetMapping("/dep/{departmentId}")
    public String toEditPage(@PathVariable("departmentId") int departmentId, Model model){
        Departments departments = departmentsMapper.selectByPrimaryKey((short)departmentId);
        model.addAttribute("dep",departments);
        List<Locations> locations = locationsMapper.selectByExample(null);
        model.addAttribute("locs",locations);
        //add页面是修改与添加二合一
        return "dep/add";
    }
    //修改
    @PutMapping("/dep")
    public String updatedep(Departments departments ){
        departmentsMapper.updateByPrimaryKey(departments);
        return "redirect:/departments/deps";
    }
    //删除
    @DeleteMapping("/dep/{departmentId}")
    public String deletedep(@PathVariable("departmentId") int departmentId,Model model){
        departmentsMapper.deleteByPrimaryKey((short) departmentId);
        return "redirect:/departments/deps";
    }
}

