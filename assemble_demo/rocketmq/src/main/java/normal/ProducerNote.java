package normal;

import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * 记录生产者的属性作用
 */
public class ProducerNote {
    public static void main(String[] args) throws  Exception{

        DefaultMQProducer producer = new DefaultMQProducer();

        /**
         * 设置注册中心的地址，多个使用；分割
         */
        producer.setNamesrvAddr("localhost:9876");

        /**
         * 设置生产者的组名  broker会按照管理各个生产者实例   事物消息时会选择一个进行回查
         */
        producer.setProducerGroup("groupName");


        producer.setNamespace("");


        /**
         * 设置发送超时事件 毫秒
         */
        producer.setSendMsgTimeout(10);



//        producer.setRetryAnotherBrokerWhenNotStoreOK();

        producer.start();
    }
}
