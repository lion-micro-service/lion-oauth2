package com.lion.authorization.wx;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Objects;

public class WechatAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    @Getter
    private String encryptedData;
    @Getter
    private String iv;

    /**
     * 昵称
     */
    @Getter
    private String nickName;

    /**
     * 头像
     */
    @Getter
    private String avatarUrl;

    /**
     * 手机号
     */
    @Getter
    private String phone;
    /**
     * 账号校验之前的token构建
     *
     * @param principal
     */
    public WechatAuthenticationToken(Object principal, String encryptedData,String iv,String nickName,String avatarUrl,String phone) {
        super(null);
        this.principal = principal;
        this.encryptedData = encryptedData;
        this.iv=iv;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.phone = phone;
        setAuthenticated(false);
    }

    /**
     * 账号校验成功之后的token构建
     *
     * @param principal
     * @param authorities
     */
    public WechatAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        if (Objects.isNull(principal)){
            super.setAuthenticated(false);
        }else {
            super.setAuthenticated(true);
        }
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
