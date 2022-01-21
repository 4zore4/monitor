package org.monitor.controller;

import com.alibaba.fastjson.JSONObject;
import org.monitor.service.impl.MacOsSystemCommander;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class test {

    @Resource
    private MacOsSystemCommander macOsSystemCommander;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/test")
    public JSONObject memory(){
        return macOsSystemCommander.getAllMonitor();
    }

    @RequestMapping("/send")
    public void send(){
        rabbitTemplate.convertAndSend("test","test");
    }


}
