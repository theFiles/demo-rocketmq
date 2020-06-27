package com.lidaye.rocket.rocketconsumer.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConsumerCompoment {
    @Bean
    public DefaultMQPushConsumer defaultMQPushConsumer() throws MQClientException {
        // 创建消费者，制定其消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("sendMailConsumer");
        // 配置服务器地址
        consumer.setNamesrvAddr("localhost:9876");
        // 设置标题
        consumer.subscribe("send-email-topic", "tag1");
        // 设置消费者的信息偏移量
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 订阅
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            // 收信息时的逻辑
            msgs.forEach(mt -> {
                System.out.println(new String(mt.getBody()));
            });
            // 返回状态
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        // 启动
        consumer.start();

        return consumer;
    }
}
