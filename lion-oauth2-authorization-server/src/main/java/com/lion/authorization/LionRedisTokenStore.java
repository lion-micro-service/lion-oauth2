package com.lion.authorization;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-02-19 22:51
 */
public class LionRedisTokenStore extends RedisTokenStore {

    public LionRedisTokenStore(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }


}