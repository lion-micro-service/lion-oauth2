package com.lion.resource.handler;

import com.lion.aop.exception.ExceptionData;
import com.lion.core.ResultData;
import com.lion.utils.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: 资源认证token失败处理（权限不足）
 * @author: Mr.Liu
 * @create: 2020-01-26 22:08
 */
@Component
public class LionAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ResultData resultData = ExceptionData.instance(e);
        PrintWriter printWriter = httpServletResponse.getWriter();
        printWriter.write(JsonUtil.convertToJsonString(resultData));
        printWriter.flush();
    }
}
