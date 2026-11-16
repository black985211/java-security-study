package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    static Connection get() throws SQLException {
        String url="jdbc:mysql://localhost:3306/javastudy";
        return DriverManager.getConnection(url,"root","");
    }
}
