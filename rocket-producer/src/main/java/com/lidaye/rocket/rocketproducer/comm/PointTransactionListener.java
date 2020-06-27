package com.lidaye.rocket.rocketproducer.comm;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class PointTransactionListener implements TransactionListener {
    /**
     * 事务状态集合：事务id => 事务状态
     */
    private ConcurrentHashMap<String,Integer> localTrans = new ConcurrentHashMap<>();

    /**
     * 第一次交互回调
     * 工作原理：接受第一次发送消息给 mq 且 mq 收到后的回调
     *
     * @param msg           消息对象（给 mq 发送消息时的那个对象）
     * @param arg           额外的参数（给 mq 发送消息时附带的参数对象）
     * @return              该方法返回值决定是否把消息发送给客户端
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        /**
         * LocalTransactionState：
         * UNKNOW 未知的
         * COMMIT_MESSAGE 确认/提交
         * ROLLBACK_MESSAGE 回滚/取消
         */
        return LocalTransactionState.UNKNOW;
    }

    /**
     * 第一次之后的交互回调
     * 只有返回 UNKNOW 或没有返回值，会重复调用该方法，重复确认结果
     * @param msg           消息对象（给 mq 发送消息时的那个对象）
     * @return              该方法返回值决定是否把消息发送给客户端
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        return localTrans.get(msg.getTransactionId()).equals(1)
                ? LocalTransactionState.COMMIT_MESSAGE
                : LocalTransactionState.ROLLBACK_MESSAGE;
    }

    /**
     * 普通的 get 方法，不用管他
     * @return              拿到事务状态集合
     */
    public ConcurrentHashMap<String, Integer> getLocalTrans() {
        return localTrans;
    }
}
