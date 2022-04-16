package com.lion.oauth.entity;

import com.lion.oauth.bean.definition.OauthClientDetailsDefinition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @description: oauth2客户端信息表
 * @author: mr.liu
 * @create: 2020-10-02 20:38
 **/
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "oauth_client_details")
@DynamicInsert
@Data
public class OauthClientDetails extends OauthClientDetailsDefinition {

    private static final long serialVersionUID = -90000050L;

}
