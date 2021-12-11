package com.lion.resource.authentication;

import com.lion.resource.configuration.properties.AuthorizationIgnoreProperties;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

public class LionBearerTokenExtractor extends BearerTokenExtractor implements TokenExtractor {

    private AuthorizationIgnoreProperties authorizationIgnoreProperties;

    public LionBearerTokenExtractor(AuthorizationIgnoreProperties authorizationIgnoreProperties) {
        this.authorizationIgnoreProperties = authorizationIgnoreProperties;
    }

    @Override
    protected String extractHeaderToken(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (authorizationIgnoreProperties.getIgnoreUrl().contains("/**") || Objects.equals(uri,"/error") || authorizationIgnoreProperties.getIgnoreUrl().contains(uri)){
            return null;
        }
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                // Add this here for the auth details later. Would be better to change the signature of this method.
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE,
                        value.substring(0, OAuth2AccessToken.BEARER_TYPE.length()).trim());
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }

        return null;
    }
}
