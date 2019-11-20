package com.yy.service.impl;

import com.yy.mapper.AuthenticationMapper;
import com.yy.pojo.Users;
import com.yy.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yux123
 * @since 2019-06-10
 */
@Service
public class UsersServiceImpl   implements UsersService {
    @Autowired
private AuthenticationMapper authenticationMapper;
    @Override
    public boolean login(Users users) {
        String password = authenticationMapper.login(users.getUserName());
        System.out.println("password:"+password);
        if(users.getPassword().equals(password)){
            System.out.println("-------------------------------------------------true");
return true;
        }
        return false;
    }
}
