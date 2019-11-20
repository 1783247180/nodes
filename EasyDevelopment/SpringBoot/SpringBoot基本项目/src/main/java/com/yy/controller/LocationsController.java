package com.yy.controller;



import com.yy.mapper.CountriesMapper;
import com.yy.mapper.LocationsMapper;
import com.yy.pojo.Countries;
import com.yy.pojo.Locations;
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
@RequestMapping("/locations")
public class LocationsController {
    @Autowired
    private LocationsMapper locationsMapper;
    @Autowired
    private CountriesMapper countriesMapper;
    //查出所有的数据到list
    @GetMapping("/locs")
    public String list(Model model){
        Collection<Locations> locations = locationsMapper.selectByExample(null);
        System.out.println("----------------------"+locations.size());
        System.out.println("进来了");
        model.addAttribute("locs",locations);
        return "loc/list";
    }
    //来到添加页面
    @GetMapping("/loc")
    public String toAddPage(Model model){
        List<Countries> countries = countriesMapper.selectByExample(null);
        model.addAttribute("cous",countries);
        return "loc/add";
    }
    //添加
    @PostMapping("/loc")
    public String addloc(Locations locations,Model model){
        locationsMapper.insert(locations);
        // redirect:表示重定向到一个地址,受视图解析器的控制
        //forward:表示转发到一个地址,不受视图解析器的控制
        //  /代表当前项目
        return "redirect:/locations/locs";
    }
    //来到修改页面
    @GetMapping("/loc/{locationId}")
    public String toEditPage(@PathVariable("locationId") int locationId, Model model){
        Locations locations = locationsMapper.selectByPrimaryKey((short)locationId);
        model.addAttribute("loc",locations);
        List<Countries> countries = countriesMapper.selectByExample(null);
        model.addAttribute("cous",countries);
        //add页面是修改与添加二合一
        return "loc/add";
    }
    //修改
    @PutMapping("/loc")
    public String updateloc(Locations locations ){
        locationsMapper.updateByPrimaryKey(locations);
        return "redirect:/locations/locs";
    }
    //删除
    @DeleteMapping("/loc/{locationId}")
    public String deleteloc(@PathVariable("locationId") int locationId,Model model){
        locationsMapper.deleteByPrimaryKey((short) locationId);
        return "redirect:/locations/locs";
    }
}

