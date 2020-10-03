package com.lion.oauth.service.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.oauth.dao.OauthClientDetailsDao;
import com.lion.oauth.entity.OauthClientDetails;
import com.lion.oauth.service.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @description: OauthClientDetailsServiceImpl
 * @author: mr.liu
 * @create: 2020-10-02 22:03
 **/
@Service
public class OauthClientDetailsServiceImpl extends BaseServiceImpl<OauthClientDetails> implements OauthClientDetailsService {

    @Autowired
    private OauthClientDetailsDao oauthClientDetailsDao;


    @Override
    public Boolean checkClientIdIsExist(String clientId, Long id) {
        OauthClientDetails oauthClientDetails = oauthClientDetailsDao.findFirstByClientId(clientId);
        if (Objects.isNull(oauthClientDetails)){
            return false;
        }
        if (Objects.nonNull(id) && id.equals(oauthClientDetails.getId())){
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkClientIdIsExist(String clientId) {
        return checkClientIdIsExist(clientId, null);
    }
}
