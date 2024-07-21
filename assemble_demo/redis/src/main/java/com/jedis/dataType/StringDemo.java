package com.jedis.dataType;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.LCSParams;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.resps.LCSMatchResult;

import java.util.List;

public class StringDemo {
    public static void main(String[] args) {
        // 创建 jedis 客户端对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis.select(0);


        jedis.mset("key1", "asdfgh", "key2", "qweasdgh");
        LCSMatchResult lcs = jedis.lcs("key1", "key2", LCSParams.LCSParams());
        System.out.println(lcs.getLen());
        System.out.println(lcs.getMatchString());
        List<LCSMatchResult.MatchedPosition> matches = lcs.getMatches();

        for (LCSMatchResult.MatchedPosition match : matches) {
            System.out.println("");
            System.out.println(match.getMatchLen());
            System.out.println(match.getA().getStart()+"  "+match.getA().getEnd());
            System.out.println(match.getB().getStart()+"  "+match.getB().getEnd());
        }

        jedis.close();
    }


    /**
     * 当作数字递增 增加给定数值
     * @param jedis
     */
    private static void incre(Jedis jedis) {
        jedis.set("count", "0");
        jedis.incr("count");
        System.out.println(jedis.get("count"));
        jedis.incrBy("count", 10);
        System.out.println(jedis.get("count"));
    }

    /**
     * 批量设置获取
     * @param jedis
     */
    private static void mgetSet(Jedis jedis) {
        String mset = jedis.mset("key1", "value1", "key2", "value2");
        System.out.println(mset);
        List<String> mget = jedis.mget("key1", "key2");
        System.out.println(mget);
    }

    private static void setXX(Jedis jedis) {
        /**
         * 存在对应的key才修改
         */
        System.out.println(jedis.set("jq:name","stw", SetParams.setParams().xx()));
    }

    private static void setNx(Jedis jedis) {
        /**
         * 不存在对应的key才设置
         */
        System.out.println(jedis.setnx("jq:name","stw"));
    }

    private static void set(Jedis jedis) {
        /**
         * 设置数据  直接覆盖原来的key 不管类型
         */
        String jq = jedis.set("jq:name", "jq");
        System.out.println(jq);
    }
}
