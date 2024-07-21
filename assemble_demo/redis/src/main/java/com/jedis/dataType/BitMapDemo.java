package com.jedis.dataType;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.BitCountOption;
import redis.clients.jedis.params.BitPosParams;

import java.util.List;

public class BitMapDemo {
    public static void main(String[] args) {
        // 创建 jedis 客户端对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis.select(0);

//        bitmapBase(jedis);

        BITFIELD(jedis);

        jedis.close();

    }

    /**
     * 按照给定进制 设置给定位置的值
     * @param jedis
     */
    private static void BITFIELD(Jedis jedis) {
        jedis.del("BITFIELD");
        String[]a = new String[1];
        // u1 表示 1bit
        // u8 表示 8bit 范围为0-255   i8表示8bit 范围 -127 ～127

        List<Long> bitfield = jedis.bitfield("BITFIELD", List.of("set","u1","3","1").toArray(a));
        System.out.println(bitfield);

        System.out.println(jedis.getbit("BITFIELD", 3));
    }

    private static void bitmapBase(Jedis jedis) {
        jedis.setbit("bitmark", 100, true);
        jedis.setbit("bitmark", 30, true);

        System.out.println(jedis.bitpos("bitmark", true,new BitPosParams()));
        System.out.println(jedis.bitpos("bitmark", true,new BitPosParams().start(50).end(100).modifier(BitCountOption.BIT)  ));
        System.out.println(jedis.bitpos("bitmark", true,new BitPosParams().start(1).end(20).modifier(BitCountOption.BIT)   ));
        System.out.println(jedis.bitpos("bitmark", true,new BitPosParams().start(1).end(20).modifier(BitCountOption.BYTE)   ));


        System.out.println("");
        System.out.println(jedis.bitcount("bitmark", 1,28,BitCountOption.BIT));
        System.out.println(jedis.bitcount("bitmark", 1,28,BitCountOption.BYTE));
        System.out.println(jedis.bitcount("bitmark", 1,109));
    }
}
