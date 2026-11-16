# Java 17 + Maven + H2 环境示例

这是“块 0：开发环境”的可运行验收项目。它使用 JDK 17、Maven 和 H2 内嵌数据库，不需要先安装 MySQL。

## 命令行验收

```bash
java -version
javac -version
mvn -version
cd java17-maven-h2-demo
mvn clean test
```

运行主程序：

```bash
mvn package
java -cp "target/classes:$HOME/.m2/repository/com/h2database/h2/2.3.232/h2-2.3.232.jar" com.example.environment.App
```

看到下面这样的输出即表示 H2 可用：

```text
H2 连接成功：id=1, name=Java 17 环境验收
```

## IntelliJ IDEA 打开方式

1. 启动 IntelliJ IDEA。
2. 选择 **Open**。
3. 选择本目录中的 `pom.xml`，并点击 **Open as Project**。
4. Project SDK 选择已安装的 **JDK 17**。
5. 等待 Maven 依赖加载完成后，运行 `App.java` 或 `AppTest.java`。

`.idea/` 和 `*.iml` 已加入 `.gitignore`，避免把个人 IDE 设置混入项目。
