package org.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");                   // 按 UTF-8 解读收到的请求
        resp.setContentType("text/html;charset=UTF-8");      // 按 UTF-8 输出响应

        String kw=req.getParameter("kw");
        if(kw==null){
            kw="";
        }

        String html="<form method=\"get\" action=\"/admin/users\">\n" +
                "    <input name=\"kw\" value=\""+HelloServlet.escapeHtml(kw)+"\"> <button>搜索</button>\n" +
                "</form>";
        resp.getWriter().write(html);

        String sql = "SELECT id, username, role, phone FROM users WHERE username LIKE ?";
        try(Connection conn= DB.get()){
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1,"%" + kw + "%");
            ResultSet rs= ps.executeQuery();
            String tableHeader="<table border=\"1\">\n" +
                    "  <tr><th>ID</th><th>用户名</th><th>角色</th><th>手机号</th></tr>";
            resp.getWriter().write(tableHeader);
            while(rs.next()){
                String tableRs="<tr><td>"+HelloServlet.escapeHtml(rs.getString("id"))+"</td><td>"+HelloServlet.escapeHtml(rs.getString("username"))+"</td><td>"+HelloServlet.escapeHtml(rs.getString("role"))+"</td><td>"+HelloServlet.escapeHtml(rs.getString("phone"))+"</td></tr>";
                resp.getWriter().write(tableRs);
            }
            String tableEnd="</table>";
            resp.getWriter().write(tableEnd);

        } catch (SQLException e) {
            e.printStackTrace();                  // 详细错误打到服务器日志里，给你自己看
            resp.setStatus(500);                  // 告诉浏览器服务器出错了
            resp.getWriter().write("系统错误");    // 给用户看的，只说出错了，不说细节
        }

    }
}
