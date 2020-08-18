package com.lion.resource.handler;

import com.lion.aop.exception.ExceptionData;
import com.lion.core.ResultData;
import com.lion.utils.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: 鉴权失败自定义返回信息（token无效）
 * @author: Mr.Liu
 * @create: 2020-01-27 11:47
 */
@Component
public class LionAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ResultData resultData = ExceptionData.instance(e);
        PrintWriter printWriter = httpServletResponse.getWriter();
        printWriter.write(JsonUtil.convertToJsonString(resultData));
        printWriter.flush();
    }
}
