package com.lion.authorization.handler;

import com.lion.core.ResultData;
import com.lion.core.ResultDataState;
import com.lion.utils.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: 登陆成功自定义信息处理
 * @author: Mr.Liu
 * @create: 2020-01-23 09:46
 */
@Component
public class LionAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ResultData resultData = new ResultData();
        resultData.setStatus(ResultDataState.SUCCESS.getKey());
        resultData.setMessage("登陆成功");
        PrintWriter printWriter = httpServletResponse.getWriter();
        printWriter.write(JsonUtil.convertToJsonString(resultData));
        printWriter.flush();
    }
}
