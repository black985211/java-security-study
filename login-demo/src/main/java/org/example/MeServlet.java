package org.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class MeServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        req.setCharacterEncoding("UTF-8");                   // 按 UTF-8 解读收到的请求
        resp.setContentType("text/html;charset=UTF-8");      // 按 UTF-8 输出响应
        HttpSession session=req.getSession(false);
        if(session==null || session.getAttribute("username")==null){
            resp.setStatus(401);
            resp.getWriter().write("请先登录");
        }
        else{
            String username=(String) session.getAttribute("username");
            String role =(String) session.getAttribute("role");
            resp.getWriter().write("当前用户: "+HelloServlet.escapeHtml(username)+", 角色: "+HelloServlet.escapeHtml(role));
        }

    }
}
