package com.lion.authorization.handler;

import com.lion.aop.exception.ExceptionData;
import com.lion.core.ResultData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

/**
 * @description: 自定义错误信息
 * @author: Mr.Liu
 * @create: 2020-01-27 15:39
 */
@Component
public class LionWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity<Object> translate(Exception e) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        if (e instanceof InvalidTokenException){
            InvalidTokenException exception = (InvalidTokenException) e;
            if (Objects.equals(exception.getMessage(),"Token was not recognised") || Objects.equals(exception.getMessage(),"Token has expired")){
                return new ResponseEntity<Object>(Collections.EMPTY_MAP, headers, HttpStatus.OK);
            }
        }
        ResultData resultData = ExceptionData.instance(e);
        ResponseEntity<Object> response = new ResponseEntity<Object>(resultData, headers, HttpStatus.OK);
        return response;
    }
}
