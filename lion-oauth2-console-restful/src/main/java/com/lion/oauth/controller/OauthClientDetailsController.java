package com.lion.oauth.controller;

import com.lion.constant.SearchConstant;
import com.lion.core.IResultData;
import com.lion.core.LionPage;
import com.lion.core.ResultData;
import com.lion.core.controller.BaseController;
import com.lion.core.controller.impl.BaseControllerImpl;
import com.lion.core.persistence.JpqlParameter;
import com.lion.core.persistence.Validator;
import com.lion.exception.BusinessException;
import com.lion.oauth.entity.OauthClientDetails;
import com.lion.oauth.service.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    /**
     * 添加客户端
     * @param oauthClientDetails
     * @return
     */
    @PostMapping("/add")
    public IResultData add(@RequestBody @Validated({Validator.Insert.class}) OauthClientDetails oauthClientDetails){
        if (oauthClientDetailsService.checkClientIdIsExist(oauthClientDetails.getClientId())){
            new BusinessException("该客户端id已存在");
        }
        oauthClientDetails.setClientSecret(passwordEncoder.encode(oauthClientDetails.getClientSecretPlaintext()));
        oauthClientDetailsService.save(oauthClientDetails);
        return ResultData.instance();
    }

    /**
     * 修改客户端
     * @param oauthClientDetails
     * @return
     */
    @PutMapping("/update")
    public IResultData update(@RequestBody @Validated({Validator.Update.class}) OauthClientDetails oauthClientDetails){
        if (oauthClientDetailsService.checkClientIdIsExist(oauthClientDetails.getClientId(),oauthClientDetails.getId())){
            new BusinessException("该客户端id已存在");
        }
        oauthClientDetails.setClientSecret(passwordEncoder.encode(oauthClientDetails.getClientSecretPlaintext()));
        oauthClientDetailsService.update(oauthClientDetails);
        return ResultData.instance();
    }

    /**
     * 检查客户端id是否存在
     * @param clientId
     * @param id
     * @return
     */
    @GetMapping("/check/clientId/exist")
    public IResultData checkClientIdExist(@NotBlank(message = "客户端id不能为空")String clientId, Long id){
        return ResultData.instance().setData("isExist",oauthClientDetailsService.checkClientIdIsExist(clientId, id));
    }

    /**
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/details")
    public IResultData details(@NotNull(message = "id不能为空") Long id){
        OauthClientDetails oauthClientDetails = oauthClientDetailsService.findById(id);
        return ResultData.instance().setData("oauthClientDetails",oauthClientDetails);
    }

    /**
     * 删除客户端
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public IResultData delete(@NotNull(message = "id不能为空") @RequestParam(value = "id",required = false) List<Long> id){
        id.forEach(i->{
            oauthClientDetailsService.deleteById(i);
        });
        ResultData resultData = ResultData.instance();
        return resultData;
    }
}
