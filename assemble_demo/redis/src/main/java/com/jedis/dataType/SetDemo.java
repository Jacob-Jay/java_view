package com.jedis.dataType;


import redis.clients.jedis.Jedis;

/**
 * 无序  唯一的集合
 * 可以用于 进行并集  差集  交集
 *
 */
public class SetDemo {

    public static void main(String[] args) {
        // 创建 jedis 客户端对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis.select(0);

        jedis.hsetnx("person", "name", "mane");
//        jedis.he
    }
}
