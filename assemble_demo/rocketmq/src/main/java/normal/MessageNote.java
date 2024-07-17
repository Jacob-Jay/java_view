package normal;

import org.apache.rocketmq.common.message.Message;

import java.util.List;


/**
 * 记录消息对应的属性作用
 */
public class MessageNote {
    public static void main(String[] args) {
        Message message = new Message();

        /**
         * 消息对应的topic名称，一般对应业务一级分类
         */
        message.setTopic("topicName");

        /**
         * 消息body  为消息传输的数据
         */
        message.setBody(null);

        /**
         * 消息只能有一个tag  用于二次区分   可以用于消费者过滤  对应业务的二级分类
         */
        message.setTags("tageA");


        /**
         * 用于控制broker是否在刷盘后返回，默认为true，设为false可能会出现生产者收到发送成功  但是broker刚好奔溃没有刷盘造成消息丢失
         */
        message.setWaitStoreMsgOK(true);


        /**
         *用于进行标识  多个使用空格分割组成字符串    通常是唯一的业务标识
         */
        message.setKeys(List.of("key1","key2"));

        /**
         * 如果消息是延迟消息通过此方法设置延迟等级
         */
        message.setDelayTimeLevel(1);

/**
 * 生产者内部回自动设置为对应的nameSpace   也可以手动设置  表明所属的应用实例名称
 */
        message.setInstanceId("");


        /**
         * 在事物消息被拉回消费者时自动设置为消息的unique——key   ，不知道作用
         */
        message.setTransactionId("");

    }
}
