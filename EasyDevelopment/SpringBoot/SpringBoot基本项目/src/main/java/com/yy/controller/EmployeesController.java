package com.yy.controller;


import com.yy.config.DateUtil;
import com.yy.mapper.DepartmentsMapper;
import com.yy.mapper.EmployeesMapper;
import com.yy.mapper.JobsMapper;
import com.yy.pojo.Departments;
import com.yy.pojo.Employees;
import com.yy.pojo.Jobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/employees")
public class EmployeesController {

    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private DepartmentsMapper departmentsMapper;
    @Autowired
    private JobsMapper jobsMapper;
    
    //查出所有的数据到list
    @GetMapping("/emps")
    public String list(Model model){
        Collection<Employees> employees = employeesMapper.selectByExample(null);
        System.out.println("----------------------"+employees.size());
        System.out.println("进来了");
        model.addAttribute("emps",employees);
        return "emp/list";
    }
    //来到添加页面
    @GetMapping("/emp")
    public String toAddPage(Model model){
        List<Departments> departments = departmentsMapper.selectByExample(null);
        List<Jobs> jobs = jobsMapper.selectByExample(null);
        model.addAttribute("deps",departments);
        model.addAttribute("jobs",jobs);
        return "emp/add";
    }
    //添加
    @PostMapping("/emp")
    public String addemp(Employees employees,Model model){
        System.out.println("--------------------emp"+employees);
        employeesMapper.insert(employees);
        // redirect:表示重定向到一个地址,受视图解析器的控制
        //forward:表示转发到一个地址,不受视图解析器的控制
        //  /代表当前项目
        return "redirect:/employees/emps";
    }
    //来到修改页面
    @GetMapping("/emp/{employeeId}")
    public String toEditPage(@PathVariable("employeeId") int employeeId,  Model model){
        Employees employees = employeesMapper.selectByPrimaryKey(employeeId);
        model.addAttribute("emp",employees);
        List<Departments> departments = departmentsMapper.selectByExample(null);
        List<Jobs> jobs = jobsMapper.selectByExample(null);
        model.addAttribute("deps",departments);
        model.addAttribute("jobs",jobs);
        //add页面是修改与添加二合一
        return "emp/add";
    }
    //修改
    @PutMapping("/emp")
    public String updateemp(Employees employees ){
        employeesMapper.updateByPrimaryKey(employees);
        return "redirect:/employees/emps";
    }
    //删除
    @DeleteMapping("/emp/{employeeId}")
    public String deleteemp(@PathVariable("employeeId") int employeeId,Model model){
        employeesMapper.deleteByPrimaryKey(employeeId);
        return "redirect:/employees/emps";
    }
}

