package com.brunosong.exam.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // list
                // 1. stack
                jedis.rpush("stack2", "a");
                jedis.rpush("stack2", "b");
                jedis.rpush("stack2", "c");

                System.out.println(jedis.rpop("stack2"));
                System.out.println(jedis.rpop("stack2"));
                System.out.println(jedis.rpop("stack2"));

                // 2. queue
                jedis.rpush("queue2", "1");
                jedis.rpush("queue2", "2");
                jedis.rpush("queue2", "3");

                System.out.println(jedis.lpop("queue2"));
                System.out.println(jedis.lpop("queue2"));
                System.out.println(jedis.lpop("queue2"));

                // 3. block brpop, blpop
//                List<String> blpop = jedis.blpop(10, "queue:blocking");
//                if (blpop != null) {
//                    blpop.forEach(System.out::println);
//                }

                jedis.sadd("users:500:follow", "100","200","300","400");
                jedis.srem("users:500:follow","100");

                Set<String> smembers = jedis.smembers("users:500:follow");
                smembers.forEach(System.out::println);

                boolean sismember1 = jedis.sismember("users:500:follow", "200");
                System.out.println(sismember1);
                boolean sismember2 = jedis.sismember("users:500:follow", "900");
                System.out.println(sismember2);

                Set<String> sinter = jedis.sinter("users:500:follow", "users:100:follow");

            }
        }
    }
}