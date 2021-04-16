package com.lion.resource.handler;

import com.lion.core.ResultData;
import com.lion.utils.JsonUtil;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @description: 退出登陆处理类
 * @author: mr.liu
 * @create: 2020-10-05 10:55
 **/
public class LionLogoutHandler implements LogoutHandler {

    private TokenStore tokenStore;

    public LionLogoutHandler(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token)){
            token = token.replace("Bearer","").trim();
        }
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
        if (Objects.nonNull(oAuth2Authentication)) {
            OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(oAuth2Authentication);
            if (Objects.nonNull(existingAccessToken)) {
                tokenStore.removeAccessToken(existingAccessToken);
            }
        }
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        ResultData resultData = ResultData.instance();
        PrintWriter printWriter = response.getWriter();
        resultData.setMessage("退出登陆成功");
        printWriter.write(JsonUtil.convertToJsonString(resultData));
        printWriter.flush();
    }
}
