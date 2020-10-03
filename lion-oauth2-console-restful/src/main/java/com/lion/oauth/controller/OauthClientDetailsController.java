package com.lion.oauth.controller;

import com.lion.constant.SearchConstant;
import com.lion.core.IResultData;
import com.lion.core.LionPage;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.JpqlParameter;
import com.lion.oauth.service.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: oauth客户端控制层
 * @author: mr.liu
 * @create: 2020-10-02 22:08
 **/
@RestController
@RequestMapping("/oauth/client/console")
public class OauthClientDetailsController extends BaseControllerImpl implements BaseController {

    @Autowired
    private OauthClientDetailsService oauthClientDetailsService;

    /**
     * 列表
     * @param lionPage
     * @param clientId
     * @return
     */
    @GetMapping("/list")
    public IResultData list(LionPage lionPage,String clientId){
        JpqlParameter jpqlParameter = new JpqlParameter();
        if (StringUtils.hasText(clientId)){
            jpqlParameter.setSearchParameter(SearchConstant.LIKE+"_clientId",clientId);
        }
        jpqlParameter.setSortParameter("createDateTime", Sort.Direction.DESC);
        lionPage.setJpqlParameter(jpqlParameter);
        return (IResultData) oauthClientDetailsService.findNavigator(lionPage);
    }


}
