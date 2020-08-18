package com.lion.authorization.aop;

import com.lion.aop.exception.ExceptionData;
import com.lion.core.ResultData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @description: 修改登陆返回的数据结果集
 * @author: Mr.Liu
 * @create: 2020-02-03 19:45
 */

@Component
@Aspect
public class AccessToken {
    /// @Around是可以改变controller返回值的
    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))" +
            "|| execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.getAccessToken(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) {
        ResultData resultData = new ResultData();
        Object proceed = null;
        try {
            proceed = pjp.proceed();
        }catch (Throwable e){
            resultData = ExceptionData.instance(e);
            if (!StringUtils.hasText(resultData.getMessage())){
                resultData.setMessage("用户名/密码错误");
            }
        }
        if (Objects.nonNull(proceed) && proceed instanceof ResponseEntity) {
            ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>)proceed;
            OAuth2AccessToken accessToken = responseEntity.getBody();
            resultData.setMessage("登陆成功");
            resultData.setData(accessToken);
        }
        return ResponseEntity.status(200).body(resultData);
    }
}
