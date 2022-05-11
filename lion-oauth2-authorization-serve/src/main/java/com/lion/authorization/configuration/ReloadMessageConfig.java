package com.lion.authorization.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ReloadMessageConfig {

    /**
     * 加载中文的认证提示信息
     *
     * @return
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // .properties 不要加到后面
        messageSource.setBasename("classpath:messages_zh_CN");
        return messageSource;
    }
}
