package com.lion.common.expose.oauth2;

import org.springframework.security.core.userdetails.UserDetails;

public interface WxUserDetailsService  {

    public UserDetails loadByCode(String code,String encryptedData,String iv) throws Exception;
}
