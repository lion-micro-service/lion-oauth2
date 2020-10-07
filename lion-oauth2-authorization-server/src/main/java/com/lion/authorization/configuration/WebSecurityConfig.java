package com.lion.authorization.configuration;

import com.lion.authorization.filter.CaptchaFilter;
import com.lion.authorization.handler.LionLogoutHandler;
import com.lion.config.PasswordConfiguration;
import com.lion.constant.DubboConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.annotation.Resource;

/**
 * @description: 安全配置
 * @author: Mr.Liu
 * @create: 2020-01-04 10:49
 */
@Configuration
@AutoConfigureAfter(PasswordConfiguration.class)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @DubboReference(cluster= DubboConstant.CLUSTER_FAILOVER,retries = 3)
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure ( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure ( HttpSecurity http ) throws Exception {
        CaptchaFilter captchaFilter = new CaptchaFilter();

        http.addFilter(captchaFilter)
                .requestMatchers()
                .anyRequest()
            .and()
                .authorizeRequests()
                .antMatchers("/oauth/**")
                .permitAll()
            .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
            .and()
                .logout()
                .addLogoutHandler(new LionLogoutHandler(tokenStore))
                .invalidateHttpSession(true)
                .permitAll()
            .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
            .and()
                .cors()
            .and()
                .csrf().disable();
    }

}
