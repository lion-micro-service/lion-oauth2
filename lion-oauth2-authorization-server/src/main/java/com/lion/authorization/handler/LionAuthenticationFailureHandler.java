package com.lion.authorization.handler;

import com.lion.aop.exception.ExceptionData;
import com.lion.core.ResultData;
import com.lion.utils.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: 登陆失败自定义错误信息处理
 * @author: Mr.Liu
 * @create: 2020-01-23 09:44
 */
@Component
public class LionAuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ResultData resultData = ExceptionData.instance(e);
        PrintWriter printWriter = httpServletResponse.getWriter();
        printWriter.write(JsonUtil.convertToJsonString(resultData));
        printWriter.flush();
//        printWriter.close();
    }
}
