package org.example;

import java.sql.*;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String url="jdbc:mysql://localhost:3306/javastudy";
        try( Connection conn= DriverManager.getConnection(url,"root","");){
            System.out.println("connected");

            String name="admin";
//            String name = "admin' -- ";
//            String password="123456";  //正确密码
//            String password="123457";  //错误密码
            String password="' or 1=1 -- "; //sql 注入
//            String password="' OR '1'='1"; //sql 注入

//            SQL注入修复，改用 PreparedStatement

            String sql="select * from users where username=? and password=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,name);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            System.out.println(sql);
//            String sql="select * from users where username='"+name+"' and password='"+password+"'";
//            System.out.println(sql);
//            Statement stmt = conn.createStatement();
//            ResultSet rs= stmt.executeQuery(sql);


            if(rs.next()){
                System.out.println("login success, role = "+rs.getString("role"));
            }
            else{
                System.out.println("login failed");
            }
//            while (rs.next()){
//                System.out.println(rs.getString("username")+ " - " + rs.getString("role"));
//            }




        } catch (SQLException e) {
            System.out.println("connect failed " + e.getMessage());
        }

    }
}