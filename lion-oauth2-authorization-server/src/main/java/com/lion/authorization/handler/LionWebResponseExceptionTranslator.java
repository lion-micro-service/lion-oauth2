package com.lion.authorization.handler;

import com.lion.aop.exception.ExceptionData;
import com.lion.core.ResultData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @description: 自定义错误信息
 * @author: Mr.Liu
 * @create: 2020-01-27 15:39
 */
@Component
public class LionWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity<ResultData> translate(Exception e) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        ResultData resultData = ExceptionData.instance(e);
        ResponseEntity<ResultData> response = new ResponseEntity<ResultData>(resultData, headers, HttpStatus.OK);
        return response;
    }
}
