package org.monitor.controller;

import com.alibaba.fastjson.JSONObject;
import org.monitor.kafka.KafkaProducer;
import org.monitor.service.impl.MacOsSystemCommander;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class test {

    @Resource
    private MacOsSystemCommander macOsSystemCommander;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    KafkaProducer kafkaProducer;

    @RequestMapping("/test")
    public JSONObject memory(){
        return macOsSystemCommander.getAllMonitor();
    }

    @RequestMapping("/send")
    public void send(){
        rabbitTemplate.convertAndSend("test","test");
    }
    @RequestMapping("/kafka")
    public void send_test(){
        kafkaProducer.send("test_controller");
    }


}
