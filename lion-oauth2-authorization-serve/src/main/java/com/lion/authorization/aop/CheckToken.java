package com.lion.authorization.aop;

import com.lion.authorization.LionTokenService;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: 自动续期token
 * @author: Mr.Liu
 * @create: 2020-02-03 19:45
 */
//@Component
//@Aspect
public class CheckToken {

    private static final String TOKEN_VALIDITY = "token_validity:";

    @Resource
    private TokenStore tokenStore;

    @Resource
    private LionTokenService tokenServices;

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint.checkToken(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();
        Map<String, Object> response = (Map<String, Object>) proceed;
        if(response.containsKey("active") && (Boolean) response.get("active")){
            Object[] args = pjp.getArgs();
            if( args.length == 1 ){
                String token = String.valueOf(args[0]);
                if(StringUtils.hasText(token)){
                    renewalTokenValidity(token);
                }
            }
        }
        return proceed;
    }

    /**
     * 续期token有效期（也可以在LionRedisTokenStore中重写readAuthentication等方法续期token）
     * @param token
     */
    private void renewalTokenValidity(String token){
        DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken) tokenServices.readAccessToken(token);
        if(Objects.isNull(accessToken) || accessToken.isExpired()){
            return;
        }
        OAuth2Authentication authentication = tokenStore.readAuthentication(token);
        String clientId = authentication.getOAuth2Request().getClientId();
        accessToken.setExpiration(new Date(System.currentTimeMillis() + (getTokenValidity(clientId,authentication.getOAuth2Request())*1000L)));
        tokenStore.storeAccessToken(accessToken,authentication);
    }

    /**
     * 获取token有效时长
     * @param clientId
     * @param oauth2Request
     * @return
     */
    private long getTokenValidity(String clientId, OAuth2Request oauth2Request){
        String validity =redisTemplate.opsForValue().get(TOKEN_VALIDITY+clientId);
        if (!StringUtils.hasText(validity) || !NumberUtils.isDigits(validity)){
            validity = String.valueOf(tokenServices.getTokenValiditySeconds(oauth2Request));
            redisTemplate.opsForValue().set(TOKEN_VALIDITY+clientId,validity,60, TimeUnit.SECONDS);
//            redisTemplate.opsForValue().increment(TOKEN_VALIDITY+clientId,60*1000);
        }
        return Long.valueOf(validity);
    }


}
