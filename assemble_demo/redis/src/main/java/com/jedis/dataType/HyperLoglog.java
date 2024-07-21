package com.jedis.dataType;

import redis.clients.jedis.Jedis;

/**
 * 用于进行去重计数（会有1%左右的精准度）
 */
public class HyperLoglog {
    public static void main(String[] args) {
        // 创建 jedis 客户端对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis.select(0);
        long pfadd = jedis.pfadd("hyoter", "a", "b", "c");
        long pfadd2 = jedis.pfadd("hyoter2", "a", "b", "c","d");
        System.out.println(pfadd);

        /**
         * 统计给定key的数量  并集 去重
         */
        long pfcount = jedis.pfcount("hyoter", "hyoter2");
        System.out.println(pfcount);

        /**
         *将目标数据合并后保存到新的
         */
        String hyoter = jedis.pfmerge("new", "hyoter", "hyoter2");
        System.out.println(hyoter);
        System.out.println(jedis.pfcount("new"));

        jedis.close();
    }
}
