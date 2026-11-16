package com.example.environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public record DatabaseConfig(String url, String username, String password) {

    public static DatabaseConfig load() {
        Properties properties = new Properties();

        try (InputStream input = DatabaseConfig.class.getResourceAsStream("/application.properties")) {
            if (input == null) {
                throw new IllegalStateException("找不到 application.properties");
            }
            properties.load(input);
        } catch (IOException exception) {
            throw new IllegalStateException("读取数据库配置失败", exception);
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        return new DatabaseConfig(url, username, password);
    }
}
