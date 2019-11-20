package com.yy.rabbitmq.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HelloSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;


    public void send(){
        Date date=new Date();
        this.rabbitTemplate.convertAndSend("hello","time is"+date.getTime() );
    }

}
