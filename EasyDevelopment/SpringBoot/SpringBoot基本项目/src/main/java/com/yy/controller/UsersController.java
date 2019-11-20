package com.yy.controller;


import com.yy.mapper.UsersMapper;
import com.yy.mapper.UsersMapper;
import com.yy.pojo.Users;
import com.yy.pojo.Users;
import com.yy.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yux123
 * @since 2019-06-10
 */
@Controller
@RequestMapping(value = "/users")
public class UsersController {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersService usersService;
    @RequestMapping(value = "/login")
    public String login(String username, String password,
                        Map<String,String> map, HttpSession session){
        System.out.println("controller:"+username+password);
        Users users=new Users();
        users.setUserName(username);
        users.setPassword(password);
        if(usersService.login(users)){
            //登录成功,防治表单重复提交
            session.setAttribute("loginUser",username);
            return "redirect:/main.html";
        }else{
            //登录失败
            map.put("msg","用户名密码错误");
            return "login";
        }
    }

    
    //查出所有的数据到list
    @GetMapping("/uses")
    public String list(Model model){
        Collection<Users> users = usersMapper.selectByExample(null);
        System.out.println("----------------------"+users.size());
        System.out.println("进来了");
        model.addAttribute("uses",users);
        return "use/list";
    }
    //来到添加页面
    @GetMapping("/use")
    public String toAddPage(){
        return "use/add";
    }
    //添加
    @PostMapping("/use")
    public String addReg(Users users,Model model){
        System.out.println("---------------------------------------"+users);
        usersMapper.insert(users);
        // redirect:表示重定向到一个地址,受视图解析器的控制
        //forward:表示转发到一个地址,不受视图解析器的控制
        //  /代表当前项目
        return "redirect:/users/uses";
    }
    //来到修改页面
    @GetMapping("/use/{userId}")
    public String toEditPage(@PathVariable("userId") int userId, Model model){
        Users users = usersMapper.selectByPrimaryKey((short)userId);
        model.addAttribute("use",users);
        //add页面是修改与添加二合一
        return "use/add";
    }
    //修改
    @PutMapping("/use")
    public String updateReg(Users users){
        usersMapper.updateByPrimaryKey(users);
        return "redirect:/users/uses";
    }
    //删除
    @DeleteMapping("/use/{userId}")
    public String deleteReg(@PathVariable("userId") int userId,Model model){
        usersMapper.deleteByPrimaryKey((short)userId);
        return "redirect:/users/uses";
    }
}

