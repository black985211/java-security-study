# Java 安全学习

11 周 Java 安全 / 内网学习计划的代码仓库。路线是先学会写，再学会打，最后学会审。

## 目录

| 目录 | 内容 |
|---|---|
| `00_环境准备/` | 环境验收：JDK 17 + Maven + H2 的最小可运行项目 |
| `java-study/` | Java 语法练习 |
| `login-demo/` | **第一版**：纯 Servlet + 内嵌 Tomcat + JDBC 的登录网站 |
| `login-boot/` | **第二版**：用 Spring Boot 重写同一个网站 |

两个版本功能相同，可以对照着看：同样的需求，手写和用框架分别是什么样子。

## 环境

- JDK 17
- Maven
- MySQL 8（本地，库名 `javastudy`）
- IntelliJ IDEA

## 初始化数据库（参考）

```sql
CREATE DATABASE IF NOT EXISTS javastudy DEFAULT CHARSET utf8mb4;
USE javastudy;

CREATE TABLE users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,   -- BCrypt 哈希固定 60 位
    role     VARCHAR(20)  NOT NULL,
    phone    VARCHAR(20)
);

-- 本地演示账号：admin / 123456，alice / alice123
INSERT INTO users (username, password, role, phone) VALUES
('admin', '$2a$10$fsCIvahauRCpEbzE5QKETO1iZuygAexPTFSdllDXn0QjJimjLx7Li', 'admin', '13800000001'),
('alice', '$2a$10$3X8IqZGAb6MV.f8cLOCZe.R.MhocCGbdeKBK77A7m8eBKw2g.VnM2', 'user',  '13900000002');
```

> 第一版还在用明文比对密码，换成上面的哈希之后，第一版无法登录。这是第一版的已知问题，见文末。

## 第一版：login-demo（Servlet）

运行 `org.example.App` 的 `main`，访问 `http://localhost:8080`。

| 地址 | 说明 |
|---|---|
| `GET /login` | 登录表单 |
| `POST /login` | 登录，成功后写入 session |
| `GET /me` | 当前用户 |
| `GET /profile?username=` | 查看资料：只能看自己，admin 可以看所有人 |
| `GET /admin/users?kw=` | 按用户名模糊搜索，仅 admin |
| `GET /hello?name=` | XSS 练习 |

- 启动、注册 Servlet 和 Filter：`App.java` 里手写
- 登录检查：`LoginFilter`（`/*`，白名单 `/login`）
- 管理员检查：`AdminFilter`（`/admin/*`）
- `Main.java`：JDBC 入门和 SQL 注入演示

## 第二版：login-boot（Spring Boot 4 / Tomcat 11）

```bash
cd login-boot
./mvnw spring-boot:run
```

或者在 IDEA 里运行 `LoginBootApplication`。登录页：`http://localhost:8080/login.html`

| 地址 | 权限 | 说明 |
|---|---|---|
| `GET /login.html` | 公开 | 登录表单（静态页） |
| `POST /login` | 公开 | BCrypt 校验密码，登录成功后更换 session |
| `GET /me` | 登录 | 当前用户 |
| `GET /profile` | 登录 | 只返回自己的资料，身份从 session 取 |
| `GET /users/count` | admin | 用户总数 |
| `GET /users/search?username=` | admin | 按用户名查询 |
| `GET /hello`、`GET /greet?name=` | 登录 | Controller 入门练习 |

- `LoginInterceptor`：拦截 `/**`，白名单 `/login`、`/login.html`、`/error`，未登录返回 401
- `AdminInterceptor`：拦截 `/users/**`，非 admin 返回 403
- 注册位置：`WebConfig`

### 两版对照

| 需求 | 第一版 | 第二版 |
|---|---|---|
| 启动服务器 | 手写 `new Tomcat()`、注册 Servlet | `SpringApplication.run` 自动配置 |
| 处理请求 | 继承 `HttpServlet`，重写 `doGet`/`doPost` | `@RestController` + `@GetMapping`/`@PostMapping` |
| 取参数 | `req.getParameter()` | `@RequestParam` |
| 数据库 | 手写 `DB` 工具类 + `PreparedStatement` | 连接池 + `JdbcTemplate`（依赖注入） |
| 登录检查 | `Filter`（Servlet 层） | `HandlerInterceptor`（Spring MVC 层） |
| 密码存储 | 明文 | BCrypt |

## 安全练习记录

| 主题 | 在哪里练的 | 攻击方式 | 修复 |
|---|---|---|---|
| 字符型 SQL 注入 | v1 `Main.java`、v2 `/users/search` | `' OR '1'='1`、`admin' -- ` | `PreparedStatement` / `JdbcTemplate` 的 `?` 占位 |
| 数字型 SQL 注入 | v2 `/users/search`（练习版本） | `1 OR 1=1` | 同上 |
| UNION 注入 | v2 `/users/search`（练习版本） | `' UNION SELECT username, password, role FROM users #` | 同上；密码改为 BCrypt |
| 反射型 XSS | v1 `/hello`、v2 `/greet` | `<script>alert(1)</script>` | 输出转义（`escapeHtml` / `HtmlUtils.htmlEscape`）或 `produces = "text/plain"` |
| 存储型 XSS | v1 输出数据库内容的页面 | 用户名里存脚本 | 输出前转义 |
| 水平越权（IDOR） | v1 `/profile`、v2 `/profile` | 修改 `username` 参数查看别人的资料 | v1：只允许本人或 admin；v2：去掉参数，身份只从 session 取 |
| 垂直越权 | v2 `/users/search` | 普通用户访问管理接口 | `AdminFilter` / `AdminInterceptor` |
| 会话劫持 | v1 | 复制别人的 `JSESSIONID` | 演示 |
| 会话固定 | v2 `/login` | 登录前把自己的 `JSESSIONID` 塞给受害者 | 登录成功后 `invalidate()`，再新建 session |
| 明文密码 | v1、v2 | 拖库后密码直接泄露 | v2 改为 BCrypt（随机盐） |
| 用户名枚举 | `/login` | 根据提示区分"用户不存在"和"密码错误" | 两种情况返回同样的提示 |

## 已知问题（第 2 周审计时处理）

- v1 仍然用明文比对密码；数据库换成 BCrypt 后，v1 无法登录
- v1 登录后没有更换 session ID，存在会话固定
- v2 `/login` 有时序侧信道：用户名不存在时不执行 BCrypt，响应明显更快，可以用来枚举用户名
- v2 登录查询用的是 `select *`，取出了不需要的列
- v2 `/greet` 原样回显参数，只靠 `text/plain` 防护，没有设置 `X-Content-Type-Options: nosniff`
- 默认 Whitelabel 错误页会暴露框架指纹
- 所有 POST 接口都没有 CSRF 防护
- `/users/**` 规则把 `/users/count` 也限制成了只有 admin 能访问，路径规则粒度太粗
- 数据库用的是 root 空密码，连接配置直接写在代码和配置文件里（仅限本地演示；生产环境应使用最小权限账号，并把配置外部化）
