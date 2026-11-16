package com.example.environment;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void createsTableAndInsertsStudent() throws Exception {
        String url = "jdbc:h2:mem:test_database;DB_CLOSE_DELAY=-1";

        try (Connection connection = DriverManager.getConnection(url, "sa", "")) {
            App.createTable(connection);
            App.insertStudent(connection, "测试学生");

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM student")) {
                resultSet.next();
                assertEquals(1, resultSet.getInt(1));
            }
        }
    }
}
