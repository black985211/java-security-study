package org.example;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;     // 强制转换，才能用 getSession 这类 HTTP 专用的方法
        HttpServletResponse resp = (HttpServletResponse) response;

        String curUrl=req.getRequestURI();
        if(curUrl.equals("/login")){
            chain.doFilter(req,resp);
        }
        else{
            HttpSession curSession=req.getSession(false);
            if(curSession==null||curSession.getAttribute("username")==null){
                req.setCharacterEncoding("UTF-8");                   // 按 UTF-8 解读收到的请求
                resp.setContentType("text/html;charset=UTF-8");      // 按 UTF-8 输出响应
                resp.setStatus(401);
                resp.getWriter().write("请先登录!");
                return;
            }
            else{
                chain.doFilter(req,resp);
            }
        }

    }
}
