package com.brunosong.exam.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.set("users:300:email", "brunosong@naver.com");
                jedis.set("users:300:name", "brunosong");
                jedis.set("users:300:age", "40");

                List<String> userInfo = jedis.mget("users:300:email", "users:300:name", "users:300:age");
                System.out.println(userInfo);

                long counter = jedis.incr("counter");
                System.out.println(counter);

                counter = jedis.incrBy("counter", 10L);
                System.out.println(counter);

                counter = jedis.decr("counter");
                System.out.println(counter);

                counter = jedis.decrBy("counter", 10L);
                System.out.println(counter);

                Pipeline pipeline = jedis.pipelined();
                pipeline.set("users:700:email", "brunosong@naver.com");
                pipeline.set("users:700:name", "brunosong");
                pipeline.set("users:700:age", "40");
                List<Object> objects = pipeline.syncAndReturnAll();
                objects.forEach(System.out::println);

            }
        }
    }
}