package com.oth.sw.hoffmannairways.web.queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class QueueConfiguration {
    @Value("${active-mq-broker-url}")
    private String brokerUrl;

    public QueueConfiguration() {

    }

    @Bean
    public ActiveMQQueue airlineQueue() {
        return new ActiveMQQueue("sw_matteo_hoffmann_queue_Airline");
    }

    @Bean
    public ActiveMQQueue testQueue() {
        return new ActiveMQQueue("sw_matteo_hoffmann_queue_Customer");
    }

    @Bean
    public ActiveMQQueue repairQueueInquiry() {
        return new ActiveMQQueue("sw_simon_haberl_queue_RepairOrderInquiry");
    }

    @Bean
    public ActiveMQQueue repairQueueReply() {
        return new ActiveMQQueue("sw_simon_haberl_queue_RepairOrderReply");
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter;
        converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate(connectionFactory());
        template.setMessageConverter(jacksonJmsMessageConverter());
        return template;
    }
}
