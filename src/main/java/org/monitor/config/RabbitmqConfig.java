package org.monitor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitmqConfig {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate(){

        cachingConnectionFactory.setPublisherConfirms(false);
        cachingConnectionFactory.setPublisherReturns(false);

        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);

        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack){
                    log.debug("消息发送成功：correlationData({}),ack({}),cause({})",correlationData, true, cause);
                }else {
                    log.debug("消息发送失败：correlationData({}),ack({}),cause({})",correlationData, false, cause);

                }
            }
        });


        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.debug("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
            }
        });

        return rabbitTemplate;
    }
}
