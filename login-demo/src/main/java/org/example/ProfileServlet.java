package org.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");                   // 按 UTF-8 解读收到的请求
        resp.setContentType("text/html;charset=UTF-8");      // 按 UTF-8 输出响应

        String username=req.getParameter("username");
        String sql="SELECT username, role, phone FROM users WHERE username=?";

        //判断当前用户权限,权限符合才进行sql查询
        HttpSession curSession=req.getSession(false);
        String curRole=(String)curSession.getAttribute("role");
        String curUsername=(String)curSession.getAttribute("username");
        if ("admin".equals(curRole)||curUsername.equals(username)){
            try(Connection conn= DB.get()){
                PreparedStatement ps=conn.prepareStatement(sql);
                ps.setString(1,username);
                ResultSet rs= ps.executeQuery();

                if(rs.next()){
                    String stout="用户名: "+HelloServlet.escapeHtml(rs.getString("username"))+"; role: "+HelloServlet.escapeHtml(rs.getString("role"))+"; 手机号: "+HelloServlet.escapeHtml(rs.getString("phone"));
                    resp.getWriter().write(stout);
                }
                else{
                    resp.getWriter().write("用户不存在！");
                }
            } catch (SQLException e) {
                e.printStackTrace();                  // 详细错误打到服务器日志里，给你自己看
                resp.setStatus(500);                  // 告诉浏览器服务器出错了
                resp.getWriter().write("系统错误");    // 给用户看的，只说出错了，不说细节
            }
        }
        else{
            resp.setStatus(403);
            resp.getWriter().write("无权查看");
            return;
        }




    }
}
