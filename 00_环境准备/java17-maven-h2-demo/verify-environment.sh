#!/usr/bin/env bash

set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
H2_JAR="/Users/fertilizer/.m2/repository/com/h2database/h2/2.3.232/h2-2.3.232.jar"

echo "[1/4] 检查 Java"
java -version

echo "[2/4] 检查 Java 编译器"
javac -version

echo "[3/4] 检查 Maven 并运行测试"
cd "$PROJECT_DIR"
mvn clean test

echo "[4/4] 运行 H2 示例"
java -cp "target/classes:$H2_JAR" com.example.environment.App

echo "环境验收完成。"
