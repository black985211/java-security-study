package org.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class HelloServlet extends HttpServlet {
    //修复XSS
    static String escapeHtml(String s) {
        // 用 s.replace("旧", "新") 依次替换上表 5 个字符，返回替换后的结果
        s=s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
        s=s.replace("\"","&quot;").replace("'","&#39;");
        return s;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("doGet 被调用了");
        String name=req.getParameter("name");
        System.out.println(name);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("hello, "+escapeHtml(name));
    }
}
