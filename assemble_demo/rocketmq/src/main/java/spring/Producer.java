package spring;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("producer")
public class Producer {

    @RequestMapping("hello")
    public String hello() {
        Message<String> msg = MessageBuilder.withPayload("Hello,RocketMQ").build();
//        SendResult sendResult = rocketmqTemplate.send("normal-way", msg);
        return "hello";
    }

    @Autowired
    private RocketMQTemplate rocketmqTemplate;
}
