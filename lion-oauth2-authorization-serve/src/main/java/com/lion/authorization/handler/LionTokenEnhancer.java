package com.lion.authorization.handler;

import com.lion.security.LionUserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @description: 用户信息增强
 * @author: Mr.Liu
 * @create: 2020-01-27 12:55
 */
@Component
public class LionTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        LionUserDetails user = (LionUserDetails) authentication.getPrincipal();
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(user.getExtended());
        return accessToken;
    }
}
