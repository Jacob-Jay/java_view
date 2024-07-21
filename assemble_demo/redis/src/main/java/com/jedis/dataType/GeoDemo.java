package com.jedis.dataType;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;


/**
 * 坐标类型
 *
 * 可以给定坐标获取给定半径类的点
 *
 *
 * 可以获取两点之间的距离
 *
 *
 * 通过geohash将坐标转换为int 底层使用的sortedSet
 */
public class GeoDemo {
    public static void main(String[] args) {
        // 创建 jedis 客户端对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis.select(0);
        add(jedis);
        Double geodist = jedis.geodist("location", "a", "b");
        System.out.println(geodist);
        jedis.close();


        BigDecimal l = new BigDecimal(180);
        BigDecimal n = new BigDecimal(90);
        for (int i = 0; i < 25; i++) {
            l = l.divide(new BigDecimal(2), 8, RoundingMode.HALF_UP);
            n = n.divide(new BigDecimal(2), 8, RoundingMode.HALF_UP);
        }
        System.out.println(l);
        System.out.println(n);
    }

    /**
     * 获取给定坐标 给定半径内的点
     * @param jedis
     */
    private static void serach(Jedis jedis) {
        List<GeoRadiusResponse> location = jedis.geosearch("location",
                new GeoSearchParam().asc().withDist().fromLonLat(103.99, 30.64).byRadius(10, GeoUnit.KM));
        for (GeoRadiusResponse geoRadiusResponse : location) {
            System.out.println(geoRadiusResponse.getMemberByString()+"  距离："+geoRadiusResponse.getDistance());
        }
    }

    /**
     * 添加坐标点
     * @param jedis
     */
    private static void add(Jedis jedis) {
        jedis.geoadd("location", Map.of("午后立交", new GeoCoordinate(104.00, 30.63)));
        jedis.geoadd("location", Map.of("接待寺立交", new GeoCoordinate(103.95,30.61)));
        jedis.geoadd("location", Map.of("大悦城", new GeoCoordinate(104.01,30.63)));
        jedis.geoadd("location", Map.of("a", new GeoCoordinate(103.98,30.65)));
        jedis.geoadd("location", Map.of("b", new GeoCoordinate(103.98,30.66)));
    }
}
