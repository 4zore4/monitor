package org.monitor.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = KafkaProducer.TOPIC_DEMO,groupId = KafkaProducer.TOPIC_GROUP)
    public void topic_demo(ConsumerRecord<?,?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        Optional message = Optional.ofNullable(record.value());

        if (message.isPresent()){
            Object msg = message.get();
            log.info("topic_demo 消费了 topic:{},message:{}",topic,msg);
            ack.acknowledge();
        }
    }
}
