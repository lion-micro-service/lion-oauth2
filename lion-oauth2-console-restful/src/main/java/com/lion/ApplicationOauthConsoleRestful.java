package com.lion;

import com.lion.aop.exception.RestulException;
import com.lion.config.EntityAuditorConfiguration;
import com.lion.core.persistence.BaseDaoFactoryBean;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication ()
@ComponentScan(basePackages = "com.lion.**")
@EnableDiscoveryClient
@DubboComponentScan(basePackages = {"com.lion.**"})
@EnableJpaRepositories(basePackages = {"com.lion.oauth.dao.**"}, repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
@EntityScan({"com.lion.oauth.entity.**"})
@EnableJpaAuditing
public class ApplicationOauthConsoleRestful {

    public static void main ( String args[] ) throws Exception {
        /*
         * new SpringApplicationBuilder(Application.class)
         * .web(WebApplicationType.NONE) .run(args);
         */
        SpringApplication.run(ApplicationOauthConsoleRestful.class, args);
    }
}