package com.yy.sb_redis.service;

import com.yy.sb_redis.bean.Department;
import com.yy.sb_redis.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DeptService {
    @Autowired
    DepartmentMapper departmentMapper;
    /**
     *方法的返回值会存储进cache中
	 cacheManager = "cacheManager",这个cacheManager是MyRedisConfig类里面的方法名
     * @param id
     * @return
     */
    @Cacheable(cacheNames = "department",cacheManager = "cacheManager")
    public Department getDeptById(Integer id){
        System.out.println("查询部门"+id);
        Department department =  departmentMapper.getDeptById(id);
        return department;
    }
}
