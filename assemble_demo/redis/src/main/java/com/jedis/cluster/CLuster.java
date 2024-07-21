package com.jedis.cluster;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
public class CLuster {


    /**
     * Redis Cluster 配置
     *
     * @author wangbo
     * @date 2021/6/15
     */

        private CLuster() {
        }

//        private static final JedisCluster JEDIS_CLUSTER;

        static {
            Properties props = new Properties();


            String redisHost1 = props.getProperty("redis.host1");
            String redisHost2 = props.getProperty("redis.host2");
            String redisHost3 = props.getProperty("redis.host3");
            int masterPort = Integer.parseInt(props.getProperty("redis.master.port"));
            int slavePort = Integer.parseInt(props.getProperty("redis.slave.port"));

            Set<HostAndPort> nodes = new HashSet<>();
            nodes.add(new HostAndPort(redisHost1, masterPort));
            nodes.add(new HostAndPort(redisHost2, masterPort));
            nodes.add(new HostAndPort(redisHost3, masterPort));
            nodes.add(new HostAndPort(redisHost1, slavePort));
            nodes.add(new HostAndPort(redisHost2, slavePort));
            nodes.add(new HostAndPort(redisHost3, slavePort));

            String password = props.getProperty("redis.password");
            int connectionTimeout = Integer.parseInt(props.getProperty("redis.connection-timeout"));
            int soTimeout = Integer.parseInt(props.getProperty("redis.so-timeout"));
            int maxAttempts = Integer.parseInt(props.getProperty("redis.max-attempts"));

            boolean testOnBorrow = Boolean.parseBoolean(props.getProperty("redis.testOnBorrow"));

            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setTestOnBorrow(testOnBorrow);

//            JEDIS_CLUSTER = new JedisCluster(nodes, connectionTimeout, soTimeout, maxAttempts, password, jedisPoolConfig);
//            JEDIS_CLUSTER.set("name", "asd");
        }

        /**
         * 获取JedisCluster对象
         */
        public static JedisCluster getJedis() {
//            return JEDIS_CLUSTER;
            return null;
        }


}
