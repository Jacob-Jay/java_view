package normal.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 自己创建producer 发送事物消息
 */
public class TransMsg {
    public static void main(String[] args) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer("sync");


        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });


        Message message = new Message();
        message.setTopic("normal-way");
        message.setBody("hello  message".getBytes());

        TransactionSendResult send = producer.sendMessageInTransaction(message, "string");
        String msgId = send.getMsgId();
        String offsetMsgId = send.getOffsetMsgId();
        SendStatus sendStatus = send.getSendStatus();
        System.out.println(send.getMessageQueue().getBrokerName()+" "+send.getMessageQueue().getQueueId());
        System.out.println("msgId:   "+msgId+"     offsetMsgId    "+offsetMsgId);
    }
}
