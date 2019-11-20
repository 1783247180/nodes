package com.yy.provider.controller;


import com.yy.provider.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @Autowired
    private MyService myService;
@RequestMapping("/ticket")
    public String getT(){
    System.out.println("8001");
    String ticket = myService.getTicket();
    return ticket;
}
}
