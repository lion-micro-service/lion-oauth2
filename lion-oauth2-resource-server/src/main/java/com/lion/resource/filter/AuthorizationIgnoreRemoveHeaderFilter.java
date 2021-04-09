package com.lion.resource.filter;

import com.lion.resource.config.properties.AuthorizationIgnoreProperties;
import org.springframework.beans.factory.InitializingBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-07 20:31
 **/
public class AuthorizationIgnoreRemoveHeaderFilter implements Filter  {

    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    public AuthorizationIgnoreRemoveHeaderFilter(AuthorizationIgnoreProperties authorizationIgnoreProperties) {
        this.authorizationIgnoreProperties = authorizationIgnoreProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String uri = httpServletRequest.getRequestURI();
        if (authorizationIgnoreProperties.getIgnoreUrl().contains("/**") || authorizationIgnoreProperties.getIgnoreUrl().contains(uri)){
            //假装从header删除Authorization
            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {
                @Override
                public String getHeader(String name) {
                    if("Authorization".equals(name) ){
                        return "";
                    }
                    return super.getHeader(name);
                }

                @Override
                public Enumeration<String> getHeaders(String name) {
                    Enumeration<String> headers = super.getHeaders(name);
                    if("Authorization".equals(name) ){
                        while (headers.hasMoreElements()) {
                            String value = headers.nextElement();
                        }
                        return headers;
                    }
                    return super.getHeaders(name);
                }
            };
            filterChain.doFilter(requestWrapper,servletResponse );
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }


}
