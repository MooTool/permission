package com.mmall.common;

import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * http请求前后监听工具
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {
    private static final String START_TIME = "requestStartTime";
    //请求准备实现时 处理之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       String url = request.getRequestURL().toString();
       Map map = request.getParameterMap();

        long start = System.currentTimeMillis();
        request.setAttribute(START_TIME,start);
        log.info("request start. url:{},params:{}",url, JsonMapper.obj2String(map));
        return true;
    }
    //正常请求，请求结束后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String url = request.getRequestURL().toString();
        Map map = request.getParameterMap();
        long start = (Long)request.getAttribute(START_TIME);
        long end = System.currentTimeMillis();
        log.info("request finished. time:{}",end-start);
        removeThreadLocalInfo();
    }
    //任何情况下都会调用
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        Map map = request.getParameterMap();
        long start = (Long)request.getAttribute(START_TIME);
        long end = System.currentTimeMillis();
        log.info("request completed. time:{}",end-start);
        removeThreadLocalInfo();
    }

    public void removeThreadLocalInfo(){
        RequestHolder.remove();
    }
}
