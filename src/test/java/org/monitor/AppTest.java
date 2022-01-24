package org.monitor;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.monitor.kafka.KafkaConsumer;
import org.monitor.kafka.KafkaProducer;
import org.monitor.service.impl.LinuxSystemCommander;
import org.monitor.service.impl.MacOsSystemCommander;
import org.monitor.util.CommandUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Unit test for simple App.
 */

@SpringBootTest
class AppTests
{

    @Test
    void contextLoads() {
    }
    @Resource
    private MacOsSystemCommander macOsSystemCommander;

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    KafkaConsumer kafkaConsumer;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * Rigorous Test :-)
     */


    @Test
    public void macTest() {
        System.out.println(macOsSystemCommander.getAllMonitor());
    }

    @Test
    public void test(){
        String str = " 21.50% user";
        String[] value = str.split(" ");
        System.out.println(value[1].replace("%", ""));
        for (String str1 : value){
            System.out.println(str1);
        }
    }

    @Test
    public void linuxGetMonitor(){
        LinuxSystemCommander commander = new LinuxSystemCommander();
        System.out.println(commander.getAllMonitor());
    }

    @Test
    public void sendMsg(){

        String result = CommandUtil.execSystemCommand("docker build -t test -f  /Users/user/dockerfile/test_dockerfile .");
        System.out.println(result.indexOf("DONE"));
        System.out.println(result);
    }

    @Test
    public void send(){
        kafkaProducer.send("qiao");
    }

}
