package com.lion.authorization;

import com.lion.authorization.handler.LionTokenEnhancer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * @description: 自定义token缓存（登陆成功后删除之前所有用户缓存数据）,主要作用（单设备登陆）
 * @author: Mr.Liu
 * @create: 2020-02-19 15:36
 */
@Component
@EnableAspectJAutoProxy
public class LionTokenServices extends DefaultTokenServices  {

    @Resource
    private TokenStore tokenStore;

    @Resource
    private LionTokenEnhancer tokenEnhancer;

    @Resource
    private ClientDetailsService clientDetailsService;

    @PostConstruct
    public void init(){
        super.setTokenStore(tokenStore);
        super.setTokenEnhancer(tokenEnhancer);
        super.setClientDetailsService(clientDetailsService);
    }

    @Transactional
    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
        if (Objects.nonNull(existingAccessToken)){
            tokenStore.removeAccessToken(existingAccessToken);
        }
        return super.createAccessToken(authentication);
    }

    /**
     * 获取token有效期
     * @param clientAuth
     * @return
     */
    public int getTokenValiditySeconds(OAuth2Request clientAuth){
        return super.getAccessTokenValiditySeconds(clientAuth);
    }
}