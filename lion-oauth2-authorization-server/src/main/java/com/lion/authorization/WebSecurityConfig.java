package com.lion.authorization;

import com.lion.config.PasswordConfiguration;
import com.lion.constant.DubboConstant;
import com.lion.upm.expose.user.UserSecurityExposeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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

    @DubboReference(cluster= DubboConstant.CLUSTER_FAILOVER,retries = 3)
    private UserSecurityExposeService userSecurityExposdService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Resource
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure ( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService(userSecurityExposdService)
            .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure ( HttpSecurity http ) throws Exception {
        http.requestMatchers()
                .anyRequest()
            .and()
                .authorizeRequests()
                .antMatchers("/oauth/**","/upms/**/login")
                .permitAll()
            .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
            .and()
                .logout()
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
