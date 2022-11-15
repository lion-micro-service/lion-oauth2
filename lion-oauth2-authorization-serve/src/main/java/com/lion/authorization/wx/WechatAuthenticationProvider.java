package com.lion.authorization.wx;

import com.lion.common.expose.oauth2.WxUserDetailsService;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;

@Data
public class WechatAuthenticationProvider implements AuthenticationProvider {

    private WxUserDetailsService userDetailsService;

    /**
     * 微信认证
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;
        String code = (String) authenticationToken.getPrincipal();
        String encryptedData = authenticationToken.getEncryptedData();
        String iv = authenticationToken.getIv();
        String nickName = authenticationToken.getNickName();
        String avatarUrl = authenticationToken.getAvatarUrl();
        String phone = authenticationToken.getPhone();
        UserDetails userDetails = userDetailsService.loadByCode(code,encryptedData ,iv ,nickName,avatarUrl,phone);
        WechatAuthenticationToken result = new WechatAuthenticationToken(userDetails, new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
