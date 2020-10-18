package com.lion.oauth.controller;

import com.lion.constant.SearchConstant;
import com.lion.core.IResultData;
import com.lion.core.LionPage;
import com.lion.core.PageResultData;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_OAUTH2_CLIENT_LIST')")
    public PageResultData<List<OauthClientDetails>> list(LionPage lionPage, String clientId){
        JpqlParameter jpqlParameter = new JpqlParameter();
        if (StringUtils.hasText(clientId)){
            jpqlParameter.setSearchParameter(SearchConstant.LIKE+"_clientId",clientId);
        }
        jpqlParameter.setSortParameter("createDateTime", Sort.Direction.DESC);
        lionPage.setJpqlParameter(jpqlParameter);
        return (PageResultData) oauthClientDetailsService.findNavigator(lionPage);
    }

    /**
     * 添加客户端
     * @param oauthClientDetails
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_OAUTH2_CLIENT_ADD')")
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
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_OAUTH2_CLIENT_UPDATE')")
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
    public IResultData<Boolean> checkClientIdExist(@NotBlank(message = "客户端id不能为空")String clientId, Long id){
        return ResultData.instance().setData(oauthClientDetailsService.checkClientIdIsExist(clientId, id));
    }

    /**
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/details")
    public IResultData<OauthClientDetails> details(@NotNull(message = "id不能为空") Long id){
        OauthClientDetails oauthClientDetails = oauthClientDetailsService.findById(id);
        return ResultData.instance().setData(oauthClientDetails);
    }

    /**
     * 删除客户端
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SYSTEM_SETTINGS_OAUTH2_CLIENT_DELETE')")
    public IResultData delete(@NotNull(message = "id不能为空") @RequestParam(value = "id",required = false) List<Long> id){
        id.forEach(i->{
            oauthClientDetailsService.deleteById(i);
        });
        ResultData resultData = ResultData.instance();
        return resultData;
    }

    /**
     * 测试动态配置权限（nacos/resources下的配置文件配置）
     * @return
     */
    @GetMapping("/test")
    @PreAuthorize("#oauth2.hasScope('test')")
    public IResultData test(){
        return ResultData.instance();
    }

    /**
     * 测试动态配置权限（nacos/resources下的配置文件配置）
     * @return
     */
    @GetMapping("/test1")
    @PreAuthorize("#oauth2.hasScope('test')")
//    @PreAuthorize(value = "#oauth2.hasAnyScope('servera','serverb','serverc')")
//    @PreAuthorize("hasAuthority('user_console_list1')")
//    @PreAuthorize("hasAnyAuthority('add,update')")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public IResultData test1(){
        return ResultData.instance();
    }

    /**
     * 测试动态配置权限（nacos/resources下的配置文件配置）
     * @return
     */
    @GetMapping("/test2")
    @PreAuthorize("#oauth2.hasScope('test1')")
    public IResultData test2(){
        return ResultData.instance();
    }

    /**
     * 测试动态配置权限（nacos/resources下的配置文件配置）
     * @return
     */
    @GetMapping("/test3")
    @PreAuthorize("#oauth2.hasScope('test1')")
    public IResultData test3(){
        return ResultData.instance();
    }

}
