package com.lion.oauth.service;

import com.lion.core.service.BaseService;
import com.lion.oauth.entity.OauthClientDetails;

/**
 * @description: OauthClientDetailsService
 * @author: mr.liu
 * @create: 2020-10-02 22:02
 **/
public interface OauthClientDetailsService extends BaseService<OauthClientDetails> {

    /**
     * 检查客户端id是否存在
     * @param clientId
     * @param id
     * @return
     */
    public Boolean checkClientIdIsExist(String clientId, Long id);

    /**
     * 检查客户端id是否存在
     * @param clientId
     * @return
     */
    public Boolean checkClientIdIsExist(String clientId);
}
