package com.lion.resource.filter;

import com.lion.resource.configuration.properties.AuthorizationIgnoreProperties;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-07 20:31
 **/
@Deprecated
public class AuthorizationIgnoreRemoveHeaderFilter implements Filter  {

    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    private static final String AUTHORIZATION = "authorization";

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
                    if(StringUtils.hasText(name) && AUTHORIZATION.equals(name.toLowerCase()) ){
                        return null;
                    }
                    return super.getHeader(name);
                }

                @Override
                public Enumeration<String> getHeaders(String name) {
                    if(StringUtils.hasText(name) && AUTHORIZATION.equals(name.toLowerCase()) ){
                        return new Enumeration() {
                            @Override
                            public boolean hasMoreElements() {
                                return false;
                            }

                            @Override
                            public Object nextElement() {
                                return null;
                            }
                        };
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
