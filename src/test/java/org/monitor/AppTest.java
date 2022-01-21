package org.monitor;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.monitor.service.impl.MacOsSystemCommander;
import org.monitor.util.CommandUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Unit test for simple App.
 */

public class AppTest 
{
    @Resource
    private MacOsSystemCommander macOsSystemCommander;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

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
    public void sendMsg(){

        String result = CommandUtil.execSystemCommand("docker build -t test -f  /Users/user/dockerfile/test_dockerfile .");
        System.out.println(result.indexOf("DONE"));
        System.out.println(result);
    }
}
