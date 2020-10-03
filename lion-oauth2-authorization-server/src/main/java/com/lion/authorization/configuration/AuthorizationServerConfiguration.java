package com.lion.authorization.configuration;

import com.lion.authorization.LionTokenServices;
import com.lion.authorization.handler.LionTokenEnhancer;
import com.lion.authorization.handler.LionWebResponseExceptionTranslator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @description: 授权验证配置
 * @author: Mr.Liu
 * @create: 2020-01-04 10:49
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private LionTokenEnhancer tokenEnhancer;

    @Resource
    private LionWebResponseExceptionTranslator webResponseExceptionTranslator;

    @Resource
    private TokenStore tokenStore;

    @Resource
    private LionTokenServices tokenServices;

    @Resource
    private ClientDetailsService clientDetailsService;


    /**
     * 配置token存储和发放
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)//密码模式
                .tokenStore(tokenStore)//redi存储token
                .tokenServices(tokenServices)
                .authorizationCodeServices(new InMemoryAuthorizationCodeServices())//授权码模式
                .exceptionTranslator(webResponseExceptionTranslator)
                .tokenEnhancer(tokenEnhancer)
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
        clients.withClientDetails(clientDetailsService);
    }

}
