package com.lion.authorization.configuration;

import com.lion.authorization.handler.LionLogoutHandler;
import com.lion.authorization.wx.WechatAuthenticationProvider;
import com.lion.common.expose.oauth2.WxUserDetailsService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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

    @DubboReference()
    private UserDetailsService userDetailsService;

    @DubboReference()
    private WxUserDetailsService wxUserDetailsService;

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
        auth.authenticationProvider(wechatAuthenticationProvider())
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure ( HttpSecurity http ) throws Exception {
        http.requestMatchers()
                .anyRequest()
            .and()
                .authorizeRequests()
                .antMatchers("/oauth/**","/actuator/**")
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

    @Bean
    public WechatAuthenticationProvider wechatAuthenticationProvider() {
        WechatAuthenticationProvider provider = new WechatAuthenticationProvider();
        provider.setUserDetailsService(wxUserDetailsService);
        return provider;
    }

}
