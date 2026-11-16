package com.study.login_boot;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // ① 取 session，没有的话不要新建
        HttpSession curSession=request.getSession(false);
        // ② 判断：session 是 null，或者 session 里取不到 username
        if (curSession==null||curSession.getAttribute("username")==null){
            response.setStatus(401);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("请先登录");
            return false;
        }
        else{
            return true;
        }
    }
}