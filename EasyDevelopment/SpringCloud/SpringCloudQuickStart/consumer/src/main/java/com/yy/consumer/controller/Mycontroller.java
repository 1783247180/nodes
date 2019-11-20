package com.yy.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Mycontroller {
    @Autowired
    private RestTemplate restTemplate;
    @RequestMapping("/buy")
    public String buy(){
        return "我买了"+restTemplate.getForObject("http://PROVIDER-01/ticket",String.class);
    }
}
