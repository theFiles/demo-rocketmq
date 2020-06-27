package com.lidaye.rocket.rocketproducer.controller;

import com.lidaye.rocket.rocketproducer.comm.PointTransactionListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class RockerController {
    @Resource
    private PointTransactionListener pointTransactionListener;

    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Resource
    private TransactionMQProducer transactionMQProducer;

    @GetMapping("/sendMessage")
    public String sendMessage() throws Exception {

        // 实例化信息类
        Message message = new Message();
        // 配置信息内容
        message.setBody("信息内容".getBytes());
        // 设置标题
        message.setTopic("send-email-topic");
        message.setTags("tag1");

        // 发送信息
        defaultMQProducer.send(message);

        return "success";
    }

    @GetMapping("/transactionSend")
    public Object transactionSend() throws MQClientException {
        // 实例化信息类
        Message message = new Message();
        // 配置信息内容
        message.setBody("事务信息内容".getBytes());
        // 设置标题
        message.setTopic("send-email-topic");
        message.setTags("tag1");

        transactionMQProducer.sendMessageInTransaction(message,null);
        ConcurrentHashMap<String, Integer> localTrans = pointTransactionListener.getLocalTrans();

        localTrans.put(message.getTransactionId(),1);

        return "success";
    }
}
