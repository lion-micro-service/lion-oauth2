package com.lion.authorization.configuration;

import com.lion.authorization.LionRedisTokenStore;
import com.lion.authorization.LionTokenService;
import com.lion.authorization.handler.LionTokenEnhancer;
import com.lion.authorization.handler.LionWebResponseExceptionTranslator;
import com.lion.authorization.wx.WechatTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @description: 授权验证配置
 * @author: Mr.Liu
 * @create: 2020-01-04 10:49
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LionTokenEnhancer tokenEnhancer;

    @Autowired
    private LionWebResponseExceptionTranslator webResponseExceptionTranslator;

    @Autowired
    private LionRedisTokenStore tokenStore;

    @Autowired
    private LionTokenService tokenServices;

    @Autowired
    private DataSource dataSource;

    /**
     * 配置token存储和发放
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(authenticationManager)
//                .tokenStore(tokenStore)//redis存储token
//                .tokenServices(tokenServices)
//                .authorizationCodeServices(new InMemoryAuthorizationCodeServices())//授权码模式
//                .exceptionTranslator(webResponseExceptionTranslator)
//                .tokenEnhancer(tokenEnhancer)
//                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
        List<TokenGranter> tokenGranters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        tokenGranters.add(new WechatTokenGranter(endpoints.getTokenServices(),endpoints.getClientDetailsService(),endpoints.getOAuth2RequestFactory(),authenticationManager));
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer));
        endpoints.authenticationManager(authenticationManager)
                .tokenEnhancer(tokenEnhancerChain)
                .tokenStore(tokenStore)
                .tokenServices(tokenServices)
                .tokenGranter(new CompositeTokenGranter(tokenGranters))
                .exceptionTranslator(webResponseExceptionTranslator)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 配置端点访问权限
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 配置客户端信息
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure ( ClientDetailsServiceConfigurer clients ) throws Exception {
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

}
