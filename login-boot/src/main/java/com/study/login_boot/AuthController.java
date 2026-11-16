package com.study.login_boot;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AuthController {
    private final JdbcTemplate jdbcTemplate;
    public AuthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @PostMapping(value="/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, HttpServletRequest request) {
        BCryptPasswordEncoder bpe=new BCryptPasswordEncoder();
        String sql="select * from users where username=?";
        List<Map<String, Object>> rows=jdbcTemplate.queryForList(sql,username);
        if (rows.isEmpty()) {
            return "用户名或密码错误";
        } else {
            // 查到了，取第一行
            Map<String, Object> row = rows.get(0);
            // 取出密码
            String hps=(String)row.get("password");
            if(bpe.matches(password,hps)){
                session.invalidate();
                HttpSession newSession = request.getSession(true);
                newSession.setAttribute("role",row.get("role"));
                newSession.setAttribute("username",row.get("username"));
                return "登录成功";
            }
            else{
                return "用户名或密码错误";
            }
        }

    }
    @GetMapping(value = "/me")
    public String me(HttpSession session){
        String username=(String)session.getAttribute("username");
        String role=(String)session.getAttribute("role");
        if(username!=null){
            return "当前用户："+username+", 角色："+role;
        }
        else{
            return "未登录";
        }

    }
    @GetMapping(value = "/profile")
    public List<Map<String, @Nullable Object>> profile(HttpSession session) {
        String username=(String)session.getAttribute("username");
        String sql="SELECT username, role, phone FROM users WHERE username=?";
        return jdbcTemplate.queryForList(sql,username);

    }


}
