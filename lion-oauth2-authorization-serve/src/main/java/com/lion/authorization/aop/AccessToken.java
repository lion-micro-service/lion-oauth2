package com.lion.authorization.aop;

import com.lion.aop.exception.ExceptionData;
import com.lion.core.ResultData;
import com.lion.core.common.enums.ResultDataState;
import com.lion.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @description: 修改登陆返回的数据结果集
 * @author: Mr.Liu
 * @create: 2020-02-03 19:45
 */

@Component
@Aspect
public class AccessToken {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))" +
            "|| execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.getAccessToken(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) {
        ResultData resultData = new ResultData();
        Object proceed = null;
        try {
            validateCaptcha();
            proceed = pjp.proceed();
        }catch (Throwable e){
            resultData.setMessage(e.getMessage());
            resultData.setStatus(ResultDataState.ERROR.getKey());
        }
        if (Objects.nonNull(proceed) && proceed instanceof ResponseEntity) {
            ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>)proceed;
            OAuth2AccessToken accessToken = responseEntity.getBody();
            resultData.setMessage("登陆成功");
            resultData.setData(accessToken);
        }
        return ResponseEntity.status(200).body(resultData);
    }

    private void validateCaptcha(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        String vcode = request.getParameter("vcode");
        String verKey = request.getParameter("verKey");
        if (!(StringUtils.hasText(vcode)&&StringUtils.hasText(verKey))){
            return;
        }
        String code = redisTemplate.opsForValue().get(verKey);
        if (!Objects.equals(vcode.toLowerCase().trim(),code)){
            new BusinessException("验证码错误");
        }
    }


}
