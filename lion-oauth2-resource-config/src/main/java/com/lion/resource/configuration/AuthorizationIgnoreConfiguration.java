package com.lion.resource.configuration;

import cn.hutool.core.util.ReUtil;
import com.lion.annotation.AuthorizationIgnore;
import com.lion.resource.configuration.properties.AuthorizationIgnoreProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @description: security 排除url鉴权
 * @author: Mr.Liu
 * @create: 2020-02-07 13:31
 */
@Configuration
@ConditionalOnClass( {ResourceServerConfigurerAdapter.class} )
@EnableConfigurationProperties(AuthorizationIgnoreProperties.class)
public class AuthorizationIgnoreConfiguration implements InitializingBean {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)}");
    private static final String ASTERISK = "*";

    @Resource
    private WebApplicationContext applicationContext;

    @Resource
    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.keySet().forEach(mappingInfo -> {
            HandlerMethod handlerMethod = map.get(mappingInfo);
            AuthorizationIgnore method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), AuthorizationIgnore.class);
            Optional.ofNullable(method).ifPresent(authIgnore -> mappingInfo.getPatternsCondition().getPatterns().forEach(url -> authorizationIgnoreProperties.getIgnoreUrl().add(ReUtil.replaceAll(url, PATTERN, ASTERISK))));
            AuthorizationIgnore controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), AuthorizationIgnore.class);
            Optional.ofNullable(controller).ifPresent(authIgnore -> mappingInfo.getPatternsCondition().getPatterns().forEach(url -> authorizationIgnoreProperties.getIgnoreUrl().add(ReUtil.replaceAll(url, PATTERN, ASTERISK))));
        });
    }
}
