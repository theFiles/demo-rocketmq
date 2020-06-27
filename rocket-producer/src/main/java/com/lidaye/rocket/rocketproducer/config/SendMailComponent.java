package com.lidaye.rocket.rocketproducer.config;

import com.lidaye.rocket.rocketproducer.comm.PointTransactionListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class SendMailComponent {
    @Resource
    private PointTransactionListener pointTransactionListener;


    @Bean
    public TransactionMQProducer transactionMQProducer() throws MQClientException {
        // 创建事务生产者，制定其生产者组
        TransactionMQProducer producer = new TransactionMQProducer("TsendMailProducer");

        //设置服务器地址
        producer.setNamesrvAddr("localhost:9876");

        // 设置事务决断处理类
        producer.setTransactionListener(pointTransactionListener);

        // 启动
        producer.start();

        return producer;
    }

    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        // 创建生产者，制定其生产者组
        DefaultMQProducer producer = new DefaultMQProducer("sendMailProducer");

        //设置服务器地址
        producer.setNamesrvAddr("localhost:9876");

        // 启动
        producer.start();

        return producer;
    }
}
