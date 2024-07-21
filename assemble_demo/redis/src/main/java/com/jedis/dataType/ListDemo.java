package com.jedis.dataType;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.ListDirection;
import redis.clients.jedis.util.KeyValue;

import java.util.List;


/**
 * list采用链表实现   增加移除元素很快 但是下标访问很慢
 *
 * 用于实现队列 或者栈
 */
public class ListDemo {
    public static void main(String[] args) {
        // 创建 jedis 客户端对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis.select(0);



        List<String> queue = jedis.blpop(1000, "queue");
        System.out.println(queue);
        jedis.close();
    }

    /**
     * 多个弹出  但一次只操作一个list
     * @param jedis
     */
    private static void lmPopo(Jedis jedis) {
        jedis.del("queue");
        jedis.del("queue2");
        System.out.println("queue length:"+ jedis.llen("queue"));
        System.out.println("queue2 length:"+ jedis.llen("queue2"));
        for (int i = 0; i < 10; i++) {
            jedis.lpush("queue", String.valueOf(i));
            jedis.lpush("queue2", String.valueOf(i));
        }

        System.out.println("queue length:"+ jedis.llen("queue"));
        System.out.println("queue2 length:"+ jedis.llen("queue2"));
        KeyValue<String, java.util.List<String>> lmpop = jedis.lmpop(ListDirection.LEFT, 11, "queue", "queue2");
        lmpop.getValue().forEach(System.out::println);
        System.out.println("queue length:"+ jedis.llen("queue"));
        System.out.println("queue2 length:"+ jedis.llen("queue2"));

        lmpop = jedis.lmpop(ListDirection.LEFT, 11, "queue", "queue2");
        lmpop.getValue().forEach(System.out::println);
        System.out.println("queue length:"+ jedis.llen("queue"));
        System.out.println("queue2 length:"+ jedis.llen("queue2"));
    }

    /**
     * 按照制定方向从一个队列移动一个数据到另一个队列
     * @param jedis
     */
    private static void lMove(Jedis jedis) {
        jedis.del("queue");
        jedis.del("queue2");
        System.out.println("queue length:"+ jedis.llen("queue"));
        System.out.println("queue2 length:"+ jedis.llen("queue2"));
        for (int i = 0; i < 10; i++) {
            jedis.lpush("queue", String.valueOf(i));
        }
        jedis.lmove("queue", "queue2", ListDirection.LEFT, ListDirection.LEFT);
        System.out.println("queue length:"+ jedis.llen("queue"));
        System.out.println("queue2 length:"+ jedis.llen("queue2"));
    }

    /**
     * 只保留给定范围的数据
     * @param jedis
     */
    private static void lTrim(Jedis jedis) {
        jedis.del("queue");
        System.out.println("length:"+ jedis.llen("queue"));
        for (int i = 0; i < 10; i++) {
            jedis.lpush("queue", String.valueOf(i));
        }
        System.out.println("");
        System.out.println("length:"+ jedis.llen("queue"));
        String queue = jedis.ltrim("queue", 5, 7);
        System.out.println(queue);
        System.out.println("");
        System.out.println("length:"+ jedis.llen("queue"));
    }

    /**
     * 获取制定返回的元素
     * @param jedis
     */
    private static void lRange(Jedis jedis) {
        System.out.println("length:"+ jedis.llen("queue"));
        for (int i = 0; i < 10; i++) {
            jedis.lpush("queue", String.valueOf(i));
        }
        System.out.println("");
        System.out.println("length:"+ jedis.llen("queue"));
        java.util.List<String> queue = jedis.lrange("queue", 5, 7);
        System.out.println(queue);
        System.out.println("");
        System.out.println("length:"+ jedis.llen("queue"));
    }

    private static void pushPop(Jedis jedis) {
        System.out.println("length:"+ jedis.llen("queue"));
        for (int i = 0; i < 10; i++) {
            jedis.lpush("queue", String.valueOf(i));
        }
        System.out.println("");
        System.out.println("length:"+ jedis.llen("queue"));
        for (int i = 0; i < 10; i++) {
            System.out.println(jedis.rpop("queue"));
        }
        System.out.println("");
        System.out.println("length:"+ jedis.llen("queue"));
    }
}
