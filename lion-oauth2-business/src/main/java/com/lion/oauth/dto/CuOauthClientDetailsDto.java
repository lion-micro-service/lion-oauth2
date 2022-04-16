package com.lion.oauth.dto;

import com.lion.core.persistence.Validator;
import com.lion.oauth.bean.definition.OauthClientDetailsDefinition;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Mr.Liu
 * @classname CuOauthClientDetailsDto
 * @description
 * @date 2022/04/16 上午11:42
 */
@Data
public class CuOauthClientDetailsDto extends OauthClientDetailsDefinition {

    @Override
    @NotBlank(message = "客户端id不能为空", groups = {Validator.Insert.class, Validator.Update.class})
    @Length(min = 1, max = 256, message = "客户端id为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    @Pattern(regexp = "[A-Za-z0-9\\-]{1,256}", message = "客户端id只能是1-256(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    public String getClientId() {
        return super.getClientId();
    }

    @Override
    @Pattern(regexp = "[A-Za-z0-9\\-]{0,256}", message = "客户端只能是0-256(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    public String getResourceIds() {
        return super.getResourceIds();
    }

    @Override
    @Length(min = 1, max = 256, message = "客户端权限范围为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    @NotBlank(message = "客户端权限范围不能为空", groups = {Validator.Insert.class, Validator.Update.class})
    @Pattern(regexp = "[A-Za-z0-9\\,]{1,256}", message = "客户端权限范围只能是1-256个(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    public String getScope() {
        return super.getScope();
    }

    @Override
    @Length(min = 1, max = 256, message = "客户端鉴权方式为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    @NotBlank(message = "客户端鉴权方式不能为空", groups = {Validator.Insert.class, Validator.Update.class})
    public String getAuthorizedGrantTypes() {
        return super.getAuthorizedGrantTypes();
    }

    @Override
    @NotBlank(message = "客户端密钥不能为空", groups = {Validator.Insert.class, Validator.Update.class})
    @Length(min = 1, max = 256, message = "客户端密钥为{min}-{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    @Pattern(regexp = "[A-Za-z0-9\\-]{1,256}", message = "客户端密钥只能是1-256个(英文/数字)字符", groups = {Validator.Insert.class, Validator.Update.class})
    public String getClientSecretPlaintext() {
        return super.getClientSecretPlaintext();
    }

    @Override
    @Min(value = 1, message = "token有效期必须大于1",groups = {Validator.Insert.class, Validator.Update.class})
    public Integer getAccessTokenValidity() {
        return super.getAccessTokenValidity();
    }

}
