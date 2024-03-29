package com.lion.oauth.dao;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.oauth.entity.OauthClientDetails;

import java.util.Optional;

/**
 * @description: OauthClientDetailsDao
 * @author: mr.liu
 * @create: 2020-10-02 22:04
 **/
public interface OauthClientDetailsDao extends BaseDao<OauthClientDetails> {

    /**
     * 根据客户端id查询
     * @param clientId
     * @return
     */
    public Optional<OauthClientDetails> findFirstByClientId(String clientId);

}
