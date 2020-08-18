package com.lion.resource;

import com.lion.config.RestTemplateConfiguration;
import com.lion.resource.config.AuthorizationIgnoreConfiguration;
import com.lion.resource.config.properties.AuthorizationIgnoreProperties;
import com.lion.resource.handler.LionAuthenticationEntryPoint;
import com.lion.resource.handler.LionAccessDeniedHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @description: 资源服务器配置
 * @author: Mr.Liu
 * @create: 2020-01-26 11:40
 */
@Data
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity( prePostEnabled = true, securedEnabled = true, jsr250Enabled = true )
@AutoConfigureAfter(AuthorizationIgnoreConfiguration.class)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    @Qualifier(RestTemplateConfiguration.REST_TAMPLATE_LOAD_BALANCED_BEAN_NAME)
    private RestTemplate restTemplate;

    @Resource
    private ResourceServerProperties resourceServerProperties;

    @Resource
    private LionAccessDeniedHandler accessDeniedHandler;

    @Resource
    private LionAuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    @Value("${security.oauth2.resource.scope}")
    private String scope;



    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceServerProperties.getResourceId())
                .tokenServices(getResourceServerTokenServices())
                .stateless(true)
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers(authorizationIgnoreProperties.getIgnoreUrl().toArray(new String[]{}))
                .permitAll()
            .and()
                .authorizeRequests()
                .antMatchers("/**")
                .access( "#oauth2.hasScope('"+scope+"')")
            .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();


    }

    /**
     * token校验服务
     * @return
     */
    protected ResourceServerTokenServices getResourceServerTokenServices(){
        RemoteTokenServices resourceServerTokenServices = new RemoteTokenServices();
        resourceServerTokenServices.setClientId(resourceServerProperties.getClientId());
        resourceServerTokenServices.setClientSecret(resourceServerProperties.getClientSecret());
        resourceServerTokenServices.setCheckTokenEndpointUrl(resourceServerProperties.getTokenInfoUri());
        resourceServerTokenServices.setRestTemplate(restTemplate);
        return resourceServerTokenServices;
    }

}
