package com.lion.resource.configuration;

import com.lion.config.RestTemplateConfiguration;
import com.lion.resource.authentication.LionBearerTokenExtractor;
import com.lion.resource.configuration.properties.AuthorizationIgnoreProperties;
import com.lion.resource.configuration.properties.OauthClientScopeProperties;
import com.lion.resource.enums.Scope;
import com.lion.resource.filter.AuthorizationIgnoreRemoveHeaderFilter;
import com.lion.resource.handler.LionAccessDeniedHandler;
import com.lion.resource.handler.LionAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @description: 资源服务器配置
 * @author: Mr.Liu
 * @create: 2020-01-26 11:40
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity( prePostEnabled = true, securedEnabled = true, jsr250Enabled = true )
@AutoConfigureAfter(AuthorizationIgnoreConfiguration.class)
@ConditionalOnClass( {OauthClientScopeProperties.class} )
@EnableConfigurationProperties(OauthClientScopeProperties.class)
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

    @Resource
    private OauthClientScopeProperties oauthClientScopeProperties;

//    @Value("${security.oauth2.resource.scope}")
//    private String scope;



    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceServerProperties.getResourceId())
                .tokenServices(getResourceServerTokenServices())
                .stateless(true)
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .tokenExtractor(new LionBearerTokenExtractor(authorizationIgnoreProperties));

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        authorizationIgnoreProperties.getIgnoreUrl().add("/actuator/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/webjars/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/resources/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/swagger-resources/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/v2/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/v3/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/swagger-ui/**");
        authorizationIgnoreProperties.getIgnoreUrl().add("/favicon.ico");
        authorizationIgnoreProperties.getIgnoreUrl().add("/oauth/**");

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry _http = http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers(authorizationIgnoreProperties.getIgnoreUrl().toArray(new String[]{}))
                .permitAll();
        //动态从配置文件读取配置配置权限(必须优先与以下配置设置否则会被以下规则优先拦截会导致该配置无效)
        if (Objects.nonNull(oauthClientScopeProperties.getScopes()) && oauthClientScopeProperties.getScopes().size()>0){
            oauthClientScopeProperties.getScopes().forEach(scopes -> {
                _http.antMatchers(scopes.getUrl().toArray(new String[]{})).access("#oauth2.hasScope('"+scopes.getScope()+"')");
            });
        }
        _http.and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET).access( "#oauth2.hasScope('"+Scope.READ.getName().toLowerCase()+"')")
                .antMatchers(HttpMethod.POST).access( "#oauth2.hasScope('"+Scope.WRITE.getName().toLowerCase()+"')")
                .antMatchers(HttpMethod.PUT).access( "#oauth2.hasScope('"+Scope.UPDATE.getName().toLowerCase()+"')")
                .antMatchers(HttpMethod.DELETE).access( "#oauth2.hasScope('"+Scope.DELETE.getName().toLowerCase()+"')")
                .antMatchers(HttpMethod.PATCH).access( "#oauth2.hasScope('"+Scope.UPDATE.getName().toLowerCase()+"')")
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
        resourceServerTokenServices.setClientId("console");//此设置不知道为何没卵用，既然可以随便写-有待研究
        resourceServerTokenServices.setClientSecret("console");//此设置不知道为何没卵用，既然可以随便写-有待研究
        resourceServerTokenServices.setCheckTokenEndpointUrl(resourceServerProperties.getTokenInfoUri());
        resourceServerTokenServices.setRestTemplate(restTemplate);
        return resourceServerTokenServices;
    }

}
