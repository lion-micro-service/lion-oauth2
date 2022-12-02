package com.lion.authorization;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-02-19 22:51
 */
@Component
public class LionRedisTokenStore extends RedisTokenStore {

    public LionRedisTokenStore(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

}