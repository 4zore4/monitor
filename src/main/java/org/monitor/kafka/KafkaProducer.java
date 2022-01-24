package org.monitor.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component

public class KafkaProducer {

    public static final String TOPIC_DEMO = "demo";

    public static final String TOPIC_GROUP = "topic.group1";

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);


    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    public void send(String str){
        log.info("send msg:{}",str);
        ListenableFuture<SendResult<String, Object>> future = (ListenableFuture<SendResult<String, Object>>) kafkaTemplate.send(TOPIC_DEMO,str);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("{}-{}-{}",TOPIC_DEMO,"send failure",ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("sucess：{}-{}-{}",TOPIC_DEMO,result.toString(),"+ok");
            }
        });
    }
}
