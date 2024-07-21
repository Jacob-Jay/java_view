package com.jedis.dataType;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ZParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 具有分值的set，按照score排序
 *
 * 可以按照分值、排序范围获取、删除
 *
 * 用作排行榜
 *
 */
public class SortedSet {
    public static void main(String[] args) {
        // 创建 jedis 客户端对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis.select(0);


     temp(jedis);

        jedis.close();
    }


    private static void zinter(Jedis jedis) {

        /**
         * 求交集  并按照 给定规则计算分值（聚合--取最大最小或者求和     比重  各个分值按照给定比重累加）
         * @param jedis
         */
        jedis.del("zDiff");
        jedis.del("zDiff2");
        jedis.zadd("zDiff", Map.of("a", 1.0, "b", 2.0));
        jedis.zadd("zDiff2", Map.of("a", 1.0, "b", 2.5,"c",3.0));
        System.out.println( jedis.zinterWithScores(new ZParams().weights(1.0,2.0),"zDiff2", "zDiff"));
        System.out.println( jedis.zinterWithScores(new ZParams().aggregate(ZParams.Aggregate.SUM),"zDiff2", "zDiff"));


        /**
         * 返回交集数量
         */

        jedis.del("zDiff");
        jedis.del("zDiff2");
        jedis.zadd("zDiff", Map.of("a", 1.0, "b", 2.0));
        jedis.zadd("zDiff2", Map.of("a", 1.0, "b", 2.5,"c",3.0));
        System.out.println( jedis.zintercard("zDiff2", "zDiff"));
        System.out.println( jedis.zintercard(1,"zDiff2", "zDiff"));
    }

    /**
     * 找出后续没有的 值比较了key  没有比较分值
     * @param jedis
     */
    private static void zdidd(Jedis jedis) {
        jedis.del("zDiff");
        jedis.del("zDiff2");
        jedis.zadd("zDiff", Map.of("a", 1.0, "b", 2.0));
        jedis.zadd("zDiff2", Map.of("a", 1.0, "b", 2.5,"c",3.0));
        System.out.println( jedis.zdiff("zDiff2", "zDiff"));
        System.out.println( jedis.zdiffWithScores("zDiff2", "zDiff"));
    }


    /**
     * 按照首字母获取范围内的数据  需要各项分值一样
     * @param jedis
     */
    private static void temp(Jedis jedis) {
        jedis.del("racer_scores");
        long res13 = jedis.zadd("racer_scores", new HashMap<String,Double>() {{
            put("Norem", 0d);
            put("Sam-Bodden", 0d);
            put("Royce", 0d);
            put("Ford", 0d);
            put("Prickett", 0d);
            put("Castilla", 0d);
        }});
        System.out.println(res13); // >>> 3

        List<String> res14 = jedis.zrange("racer_scores", 0, -1);
        System.out.println(res14); // >>> [Castilla, Ford, Norem, Prickett, Royce, Sam-Bodden]

        List<String> res15 = jedis.zrangeByLex("racer_scores", "[A", "+");
        System.out.println(res15); // >>> [Castilla, Ford]
    }

    private static void base(Jedis jedis) {
        jedis.del("rank");

        jedis.zadd("rank", 8, "jq");
        jedis.zadd("rank", Map.of("ljs",10.0,"sr",11.5));


        /**
         * 获取给定名称的排序
         */
        System.out.println(jedis.zrank("rank","jq"));
        System.out.println(jedis.zrevrank("rank","jq"));

        /**
         * 获取制定排名的数据 只返回名称
         */
        List<String> ranke = jedis.zrange("rank", 0, 2);
        System.out.println(ranke);
        System.out.println(jedis.zrevrange("rank", 0, 2));


/**
 * 获取制定排名的数据  返回名称 分值
 */
        System.out.println(jedis.zrangeWithScores("rank", 0, 2));
        System.out.println(jedis.zrevrangeWithScores("rank", 0, 2));


        /**
         * 按照分数范围获取数据
         */
        System.out.println(  jedis.zrangeByScore("rank", 10, 11));
        System.out.println(  jedis.zrevrangeByScore("rank", 11, 10));
        System.out.println(  jedis.zrangeByScoreWithScores("rank", 10, 11));
        System.out.println(  jedis.zrevrangeByScoreWithScores("rank", 11, 10));


        /**
         * 移除数据
         * 按照分值范围
         * 按照排序范围
         */

        System.out.println(jedis.zrem("rank","jq"));
        System.out.println(jedis.zremrangeByScore("rank",10,11));
        System.out.println(jedis.zremrangeByScore("rank",10,11));

        System.out.println(jedis.zremrangeByRank("rank",0,3));
    }
}
