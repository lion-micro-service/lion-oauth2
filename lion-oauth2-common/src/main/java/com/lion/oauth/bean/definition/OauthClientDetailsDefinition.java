package com.lion.oauth.bean.definition;

import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Mr.Liu
 * @classname OauthClientDetailsDefinition
 * @description
 * @date 2022/04/16 上午10:55
 */
@Data
@Schema(description = "部门")
@MappedSuperclass
public class OauthClientDetailsDefinition extends BaseEntity {
    private static final long serialVersionUID = -90000050L;

    @Schema(description = "客户端id")
//    @Column(name = "client_id",nullable = false,unique = true)
//    @NotBlank(message = "客户端id不能为空", groups = {Validator.Insert.class, Validator.Update.class})
//    @Length(min = 1, max = 256, message = "客户端id为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
//    @Pattern(regexp = "[A-Za-z0-9\\-]{1,256}", message = "客户端id只能是1-256(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String clientId;

    @Schema(description = "资源id")
//    @Column(name = "resource_ids")
//    @Pattern(regexp = "[A-Za-z0-9\\-]{0,256}", message = "客户端只能是0-256(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String resourceIds;

    @Schema(description = "客户端密钥")
    @Column(name = "client_secret",nullable = false)
    private String clientSecret;

    @Schema(description = "客户端密钥明文（用于编辑显示）")
//    @Column(name = "client_secret_plaintext",nullable = false)
//    @NotBlank(message = "客户端密钥不能为空", groups = {Validator.Insert.class, Validator.Update.class})
//    @Length(min = 1, max = 256, message = "客户端密钥为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
//    @Pattern(regexp = "[A-Za-z0-9\\-]{1,256}", message = "客户端密钥只能是1-256个(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String clientSecretPlaintext;

    @Schema(description = "客户端权限范围(数据源参考com.lion.resource.enums.Scope)")
//    @Column(name = "scope",nullable = false)
//    @Length(min = 1, max = 256, message = "客户端权限范围为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
//    @NotBlank(message = "客户端权限范围不能为空", groups = {Validator.Insert.class, Validator.Update.class})
//    @Pattern(regexp = "[A-Za-z0-9\\,]{1,256}", message = "客户端权限范围只能是1-256个(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String scope;

    @Schema(description = "鉴权方式(数据源参考com.lion.resource.enums.GrantTypes)")
//    @Column(name = "authorized_grant_types",nullable = false)
//    @Length(min = 1, max = 256, message = "客户端鉴权方式为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
//    @NotBlank(message = "客户端鉴权方式不能为空", groups = {Validator.Insert.class, Validator.Update.class})
    //正则表达式能力有限无法写出(authorization_code,password,client_credentials,implicit,refresh_token)用逗号隔开且完全匹配的正则，屏蔽以下校验
//    @Pattern(regexp = "[A-Za-z0-9\\-]{1,256}", message = "客户端鉴权方式只能是1-256个(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String authorizedGrantTypes;

    @Schema()
    private String webServerRedirectUri;

    @Schema()
    private String authorities;

    @Schema(description = "token有效期 （不设置默认43200秒）")
//    @Min(value = 1, message = "token有效期必须大于1",groups = {Validator.Insert.class, Validator.Update.class})
    //似乎不能正则数字类型
//    @Pattern(regexp = "^[1-9]+[0-9]*$", message = "token有效期必须为正整数", groups = {Validator.Insert.class, Validator.Update.class})
    private Integer accessTokenValidity;

    @Schema(description = "刷新token有效期")
    private Integer refreshTokenValidity;

    @Schema()
    private String additionalInformation;

    @Schema()
    private String autoapprove;
}
