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
        //第一个参数是mybootfanoutExchange交换机(生产者没有将消息直接发送到队列，而是发送到了交换机,每个队列都要绑定到交换机)
        this.rabbitTemplate.convertAndSend("mybootfanoutExchange","","time is"+date.getTime() );
    }

}
