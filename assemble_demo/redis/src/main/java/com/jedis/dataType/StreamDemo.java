package com.jedis.dataType;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XAddParams;
import redis.clients.jedis.params.XPendingParams;
import redis.clients.jedis.params.XReadGroupParams;
import redis.clients.jedis.params.XReadParams;
import redis.clients.jedis.resps.StreamEntry;
import redis.clients.jedis.resps.StreamPendingEntry;
import redis.clients.jedis.resps.StreamPendingSummary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 *使用Radix Tree数据结构实现
 * <li>讲数据发送给多个客户端</li>
 * <li>序列存储 ，范围查询</li>
 * <li>多个消费者类似mq</li>
 *
 * <p></p>
 * XADD key [NOMKSTREAM]   [<MAXLEN | MINID> [= | ~] threshold   [LIMIT count]]   <* | id> field value [field value ...]
 *
 * <p></p>
 *  XADD ordrr * num 10 price 100
 * <li> XADD ordrr * num 10 price 100</li>
 * <li>往redis的ordrr流中添加一项数据，没有回自动创建</li>
 * <li>使用* 号标识id由redis自动生成 为时间搓+递增序号，也可以给定</li>
 * <p></p>
 *  XADD ordrr NOMKSREAM * num 10 price 100
 *  <li>给定了NOMKSREAM参数 ordrr流不存在时不回自动创建而是报错</li>
 *
 * <p></p>

 * XADD ordrr MAXLEN 3 * num 10 price 100
 * <li>MAXLEN 3表示最多只能存在3个数据，多余的数据会被剔除</li>
 * <li>MAXLEN ～3表示至少存在3个数据，可能会多几个</li>
 *
 * <p></p>

 * XADD ordrr MinId 1721285935688-0 * num 10 price 103
 * <li>MinId xx表示id小于xx的数据会被剔除</li>
 * <li>MinId ～3x表示id大于等于xx的数据一定要存在，其他数据可能会被剔除</li>
 *
 *
 * <p></p>
 * XADD ordrr MAXLEN ~ 3 limit 2 * num 10 price 107
 * <li>limit用于限制非精确控制时最多移除的数据条数？</li>
 *
 *
 * <p></p>
 * id由时间-递增序号组成，使用的时间的原因时可以给定时间范围查找
 *
 *
 *
 *
 *
 * <p></p>
 *
 * XPENDING key group [[IDLE min-idle-time] start end count [consumer]]
 * <li>XPENDING clustermq1 g1 - + 10  查看给定流给定消费组还未确定的数据 返回10条</li>
 * <li>XPENDING clustermq1 g1  IDLE  1000 - + 10  查看给定流给定消费组超过1s还未确定的数据 返回10条</li>
 * <li>XPENDING clustermq1 g1 - + 10  bob查看给定流给定消费组给定消费者还未确定的数据</li>
 *
 * <p></p>
 *
 * XCLAIM key group consumer min-idle-time id [id ...] [IDLE ms]
 *   [TIME unix-time-milliseconds] [RETRYCOUNT count] [FORCE] [JUSTID]
 *   [LASTID lastid]
 *
 * XCLAIM clustermq1 g1 asdasd 1000 1721295537896-0
 * <li>将给定id且超过1s未确认的消息转移给给定消费者（因为原来的消费者不会再上线了）</li>
 * <li>RETRYCOUNT  count  设置deliveredTimes的值而不是递增</li>
 * <li>FORCE  强制将还未分发的数据分发给某个消费者</li>
 * <li>JUSTID  不回增加 deliveredTimes</li>
 */
public class StreamDemo {
    public static void main(String[] args) {

        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis.select(0);
        StreamPendingSummary xpending = jedis.xpending("clustermq1", "g1");
        System.out.println("一共没确认："+xpending.getTotal()+"条");
        Map<String, Long> consumerMessageCount = xpending.getConsumerMessageCount();
        for (Map.Entry<String, Long> stringLongEntry : consumerMessageCount.entrySet()) {
            System.out.println("消费者："+stringLongEntry.getKey()+",一共没确认："+stringLongEntry.getValue()+"条");
        }
        System.out.println("最小id："+ xpending.getMinId());
        System.out.println("最大id："+ xpending.getMaxId());


        List<StreamPendingEntry> xpending1 = jedis.xpending("clustermq1", "g1", new XPendingParams().count(10)
                .start(StreamEntryID.MINIMUM_ID).end(StreamEntryID.MAXIMUM_ID)
                .consumer("bob")
                .idle(15257465)
        );
        for (StreamPendingEntry streamPendingEntry : xpending1) {
            System.out.println("消息id："+streamPendingEntry.getID());
            System.out.println("消费者名称："+streamPendingEntry.getConsumerName());
            System.out.println("未确认时长："+streamPendingEntry.getIdleTime());
            System.out.println("推送次数："+streamPendingEntry.getDeliveredTimes());
        }


        jedis.close();
    }

    /**
     * 集群消费  每个消费者消费一部分数据
     */
    private static void clusterCOnsumer() {
        // 创建 jedis 客户端对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis.select(0);
        // 创建 jedis 客户端对象
        Jedis jedis2 = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis2.select(0);
        /**
         * 创建消费者组  对应的stream必须先存在（或者给定参数自动创建），不能重复创建
         *
         */
//        String res19 = jedis.xgroupCreate("clustermq1","g1",StreamEntryID.LAST_ENTRY,true);
//        System.out.println(res19); // >>> OK

        new Thread(()->ClusterCOnsumer(jedis,"Alice")).start();
        new Thread(()->ClusterCOnsumer(jedis2,"bob")).start();
        try {
            TimeUnit.SECONDS.sleep(111111);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        jedis.close();
    }

    /**
     * 使用> id表示接收新消息
     * 使用具体的id表示接收分给自己但是还没有ack的消息  就算制定了阻塞也不会被阻塞
     * @param jedis
     * @param consumer
     */
    private static void ClusterCOnsumer(Jedis jedis,String consumer) {
        StreamEntryID streamEntryID = new StreamEntryID();
        StreamEntryID used = streamEntryID;
        while (true) {
            List<Map.Entry<String, List<StreamEntry>>> res21 = jedis.xreadGroup("g1", consumer,
                    XReadGroupParams.xReadGroupParams().count(1).block(0),
//                    new HashMap<String,StreamEntryID>(){{put("clustermq1",StreamEntryID.UNRECEIVED_ENTRY);}}
                    Map.of("clustermq1",used)
            );
            /**
             * 第一次先获取未确认的数据  没有就切换为新数据
             */
            if (res21.get(0).getValue().isEmpty()&&used==streamEntryID) {
                System.out.println("switch");
                used = StreamEntryID.UNRECEIVED_ENTRY;
            }else {
                System.out.println("ok");
                for (Map.Entry<String, List<StreamEntry>> stringListEntry : res21) {
                    String key = stringListEntry.getKey();
                    List<StreamEntry> value = stringListEntry.getValue();
                    for (StreamEntry streamEntry : value) {
                        System.out.println(consumer+"      "+key+" "+ streamEntry.getID()+"  "+streamEntry.getFields());
//                        jedis.xack(key,"g1",streamEntry.getID());
                    }

                }
            }


        }
    }


    /**
     * 多个客户端同时接收stream的数据(全量接收)
     */
    private static void borderCast() {
        // 创建 jedis 客户端对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis.select(0);

        // 创建 jedis 客户端对象
        Jedis jedis2 = new Jedis("127.0.0.1", 6379);
// 选择使用一个库，默认 0 号库
        jedis2.select(0);
        new Thread(()->tailMessag(jedis,"a")).start();
        new Thread(()->tailMessag(jedis2,"b")).start();
        try {
            TimeUnit.SECONDS.sleep(111111111);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        jedis.close();
        jedis2.close();
    }

    private static void tailMessag(Jedis jedis, String b) {
        List<Map.Entry<String, List<StreamEntry>>> res18= jedis.xread(
                XReadParams.xReadParams().count(2).block(Integer.MAX_VALUE),
                new HashMap<String,StreamEntryID>(){{put("mq1",new StreamEntryID());}}
        );
        while (res18.size()>0){
            String id ="";
            for (Map.Entry<String, List<StreamEntry>> stringListEntry : res18) {
                System.out.println(b+"线程："+stringListEntry.getKey()+"  ");
                List<StreamEntry> value = stringListEntry.getValue();
                for (StreamEntry streamEntry : value) {
                    System.out.println(b+"线程："+streamEntry.getID()+"  "+streamEntry.getFields());
                }
                id = value.get(value.size() - 1).getID().toString();
            }
            StreamEntryID streamEntryID = new StreamEntryID(id);
            res18= jedis.xread(
                    XReadParams.xReadParams().count(2).block(Integer.MAX_VALUE),
                    new HashMap<String,StreamEntryID>(){{put("mq1",streamEntryID);}}
            );
        }
    }

    /**
     * 范围查找
     * @param jedis
     */
    private static void rangeQuery(Jedis jedis) {
        //        List<StreamEntry> ordrr = jedis.xrange("ordrr", String.valueOf(System.currentTimeMillis()-1000000000), "+");
        //通过限制条数 可以做到类似翻页查询，且在数据量大时不回阻塞服务器
        List<StreamEntry> ordrr = jedis.xrange("ordrr", String.valueOf(System.currentTimeMillis()-1000000000), "+",2);
        for (StreamEntry streamEntry : ordrr) {
            System.out.println(streamEntry.getID().toString()+"  "+streamEntry.getFields());
        }
    }

    /**
     * 添加数据
     * @param jedis
     */
    private static void addData(Jedis jedis) {
        /**
         * 自动生成id
         */
        StreamEntryID res1 = jedis.xadd("race:france",new HashMap<String,String>(){{put("rider","Castilla");put("speed","30.2");put("position","1");put("location_id","1");}} , XAddParams.xAddParams());
        System.out.println(res1); // >>> 1701760582225-0
        StreamEntryID res2 = jedis.xadd("race:france",new HashMap<String,String>(){{put("rider","Norem");put("speed","28.8");put("position","3");put("location_id","1");}} , XAddParams.xAddParams());
        System.out.println(res2); // >>> 1701760582225-1
        StreamEntryID res3 = jedis.xadd("race:france",new HashMap<String,String>(){{put("rider","Prickett");put("speed","29.7");put("position","2");put("location_id","1");}} , XAddParams.xAddParams());
        System.out.println(res3); // >>> 1701760582226-0


        /**
         * 给定id
         */
        StreamEntryID res8 = jedis.xadd("race:usa", new HashMap<String,String>(){{put("racer","Castilla");}},XAddParams.xAddParams().id("0-11"));
        System.out.println(res8); // >>> 0-1


        /**
         * 给定一部分后面的递增
         */
        StreamEntryID res11 = jedis.xadd("race:usa", new HashMap<String,String>(){{put("racer","Norem");}},XAddParams.xAddParams().id("0-*"));
        System.out.println(res11);

        /**
         * 不能比之前的小
         */
        StreamEntryID res12 = jedis.xadd("race:usa", new HashMap<String,String>(){{put("racer","Norem");}},XAddParams.xAddParams().id("0-10"));
        System.out.println(res11);
    }
}



