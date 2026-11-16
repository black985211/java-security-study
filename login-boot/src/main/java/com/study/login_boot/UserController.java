package com.study.login_boot;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final JdbcTemplate jdbcTemplate;
    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping(value = "/users/count")
    public Integer count(){
        String sql="SELECT COUNT(*) FROM users";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    @GetMapping(value = "/users/search")
    public List<Map<String, @Nullable Object>> search(@RequestParam String username){
        String sql="SELECT username, role, phone FROM users WHERE username=?";

        return jdbcTemplate.queryForList(sql,username);

    }

}
