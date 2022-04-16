package com.lion;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication ( scanBasePackages = {"com.lion.**"} )
@EnableDiscoveryClient
@EnableDubbo
@DubboComponentScan(basePackages = {"com.lion.**"})
public class ApplicationAuthorizationServe {

    public static void main (String[] args) {
        /*
         * new SpringApplicationBuilder(Application.class)
         * .web(WebApplicationType.NONE) .run(args);
         */
        SpringApplication.run(ApplicationAuthorizationServe.class, args);
    }
}