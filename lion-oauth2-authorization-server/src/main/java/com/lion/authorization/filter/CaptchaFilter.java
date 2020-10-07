package com.lion.authorization.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.aop.exception.ExceptionData;
import com.lion.core.ICurrentUser;
import com.lion.core.ResultData;
import com.lion.core.common.enums.ResultDataState;
import com.lion.utils.CurrentUserUtil;
import com.lion.utils.SpringContextUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @description: 验证码过滤器
 * @author: mr.liu
 * @create: 2020-10-07 15:16
 **/
public class CaptchaFilter extends OncePerRequestFilter {

    private volatile StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        String vcode = request.getParameter("vcode");
        String verKey = request.getParameter("verKey");
        if (url.equals("/oauth/token") && StringUtils.hasText(vcode) && StringUtils.hasText(verKey)){
            if (!validate(verKey,vcode)){
                ResultData resultData = ResultData.instance();
                resultData.setStatus(ResultDataState.ERROR.getKey());
                resultData.setMessage("验证码错误");
                ObjectMapper objectMapper = new ObjectMapper();
                PrintWriter writer = response.getWriter();
                writer.write(objectMapper.writeValueAsString(resultData));
                writer.flush();
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Boolean  validate(String verKey, String vcode){
        String code = getRedisTemplate().opsForValue().get(verKey);
        if (Objects.equals(vcode,code)){
            return true;
        }
        return false;
    }

    private StringRedisTemplate getRedisTemplate(){
        if(Objects.isNull(redisTemplate)){
            synchronized(CaptchaFilter.class){
                if(Objects.isNull(redisTemplate)){
                    redisTemplate = (StringRedisTemplate) SpringContextUtil.getBean("stringRedisTemplate");
                }
            }
        }
        return redisTemplate;
    }
}
