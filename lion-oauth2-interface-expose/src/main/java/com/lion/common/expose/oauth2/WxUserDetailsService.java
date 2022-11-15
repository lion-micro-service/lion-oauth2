package com.lion.common.expose.oauth2;

import org.springframework.security.core.userdetails.UserDetails;

public interface WxUserDetailsService  {

    public UserDetails loadByCode(String code,String encryptedData,String iv,String nickName,String avatarUrl,String phone) throws Exception;
}
