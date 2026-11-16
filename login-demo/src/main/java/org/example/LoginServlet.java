package org.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String html="""
        <form method="post" action="/login">
            用户名 <input name="username"><br>
            密码 <input name="password" type="password"><br>
            <button type="submit">登录</button>
        </form>
        """;
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(html);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        req.setCharacterEncoding("UTF-8");                   // 按 UTF-8 解读收到的请求
        resp.setContentType("text/html;charset=UTF-8");      // 按 UTF-8 输出响应

        try(Connection conn= DB.get()){
            System.out.println("connected");
            String name=req.getParameter("username");
            String password=req.getParameter("password");

            String sql="select * from users where username=? and password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,name);
            ps.setString(2,password);
            ResultSet rs= ps.executeQuery();

            if (rs.next()){
                String role=rs.getString("role");
                resp.getWriter().write("登录成功，角色: "+HelloServlet.escapeHtml(role));

                //记录session
                HttpSession session = req.getSession();
                session.setAttribute("username",name);
                session.setAttribute("role",role);


            }
            else{
                resp.getWriter().write("用户名或密码错误！");
            }

        } catch (SQLException e) {
            e.printStackTrace();                  // 详细错误打到服务器日志里，给你自己看
            resp.setStatus(500);                  // 告诉浏览器服务器出错了
            resp.getWriter().write("系统错误");    // 给用户看的，只说出错了，不说细节
        }

    }
}
