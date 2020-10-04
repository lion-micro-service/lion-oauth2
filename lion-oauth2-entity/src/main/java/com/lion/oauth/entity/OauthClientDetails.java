package com.lion.oauth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @description: oauth2客户端信息表
 * @author: mr.liu
 * @create: 2020-10-02 20:38
 **/
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "oauth_client_details")
@DynamicUpdate
@DynamicInsert
@Data
@JsonIgnoreProperties(ignoreUnknown = true,value = {"createDateTime","updateDateTime","createUserId","updateUserId"})
public class OauthClientDetails extends BaseEntity {

    @Column(name = "client_id",nullable = false,unique = true,columnDefinition = " varchar(256) comment '客户端id' ")
    @NotBlank(message = "客户端id不能为空", groups = {Validator.Insert.class, Validator.Update.class})
    @Length(min = 1, max = 256, message = "客户端id为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    @Pattern(regexp = "[A-Za-z0-9\\-]{1,256}", message = "客户端id只能是1-256(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String clientId;

    @Column(name = "resource_ids", columnDefinition = " varchar(256) comment '资源id' ")
    @Pattern(regexp = "[A-Za-z0-9\\-]{0,256}", message = "客户端只能是0-256(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String resourceIds;

    @Column(name = "client_secret",nullable = false, columnDefinition = " varchar(256) comment '客户端密钥' ")
    private String clientSecret;

    @Column(name = "client_secret_plaintext",nullable = false, columnDefinition = " varchar(256) comment '客户端密钥明文（用于编辑显示）' ")
    @NotBlank(message = "客户端密钥不能为空", groups = {Validator.Insert.class, Validator.Update.class})
    @Length(min = 1, max = 256, message = "客户端密钥为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    @Pattern(regexp = "[A-Za-z0-9\\-]{1,256}", message = "客户端密钥只能是1-256个(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String clientSecretPlaintext;

    //数据源参考com.lion.resource.enums.Scope
    @Column(name = "scope",nullable = false, columnDefinition = " varchar(256) comment '客户端权限范围' ")
    @Length(min = 1, max = 256, message = "客户端权限范围为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    @NotBlank(message = "客户端权限范围不能为空", groups = {Validator.Insert.class, Validator.Update.class})
    @Pattern(regexp = "[A-Za-z0-9\\,]{1,256}", message = "客户端权限范围只能是1-256个(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String scope;

    //数据源参考com.lion.resource.enums.GrantTypes
    @Column(name = "authorized_grant_types",nullable = false, columnDefinition = " varchar(256) comment '鉴权方式（authorization_code,password,client_credentials,implicit,refresh_token）多种方式用逗号隔开' ")
    @Length(min = 1, max = 256, message = "客户端鉴权方式为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    @NotBlank(message = "客户端鉴权方式不能为空", groups = {Validator.Insert.class, Validator.Update.class})
    //正则表达式能力有限无法写出(authorization_code,password,client_credentials,implicit,refresh_token)用逗号隔开且完全匹配的正则，屏蔽以下校验
//    @Pattern(regexp = "[A-Za-z0-9\\-]{1,256}", message = "客户端鉴权方式只能是1-256个(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String authorizedGrantTypes;

    @Column(name = "web_server_redirect_uri", columnDefinition = " varchar(256) comment ''")
    private String webServerRedirectUri;

    @Column(name = "authorities", columnDefinition = " varchar(256) comment ''")
    private String authorities;

    @Column(name = "access_token_validity", columnDefinition = " INT(11) comment 'token有效期 （不设置默认43200秒）'")
    @Min(value = 1, message = "token有效期必须大于1",groups = {Validator.Insert.class, Validator.Update.class})
    //似乎不能正则数字类型
//    @Pattern(regexp = "^[1-9]+[0-9]*$", message = "token有效期必须为正整数", groups = {Validator.Insert.class, Validator.Update.class})
    private Integer accessTokenValidity;

    @Column(name = "refresh_token_validity", columnDefinition = " INT(11) comment '刷新token有效期'")
    private Integer refreshTokenValidity;

    @Column(name = "additional_information", columnDefinition = " varchar(4096) comment ''")
    private String additionalInformation;

    @Column(name = "autoapprove", columnDefinition = " varchar(256) comment ''")
    private String autoapprove;

}
