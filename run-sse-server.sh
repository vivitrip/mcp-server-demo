#!/bin/bash

# 设置临时 JDK 路径
JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.15/libexec/openjdk.jdk/Contents/Home
PATH=$JAVA_HOME/bin:$PATH

# 设置 Maven 路径
MAVEN_HOME=/opt/homebrew/Cellar/maven/3.9.4/libexec
PATH=$MAVEN_HOME/bin:$PATH

# 检查 JDK 是否正确配置
$JAVA_HOME/bin/java -version || { echo "Java 未正确配置，请检查 JAVA_HOME 路径"; exit 1; }

# 检查 Maven 是否正确配置
mvn -version || { echo "Maven 未正确配置，请检查 MAVEN_HOME 路径"; exit 1; }

# 检查 2180 端口是否被占用
PORT=2180
if lsof -i:$PORT >/dev/null 2>&1; then
  echo "端口 $PORT 被占用，正在终止相关进程..."
  lsof -i:$PORT | awk 'NR>1 {print $2}' | xargs kill -9 || { echo "无法终止占用端口 $PORT 的进程"; exit 1; }
  echo "端口 $PORT 已释放"
fi

# 构建项目
echo "正在编译项目..."
mvn clean package -DskipTests || { echo "项目编译失败"; exit 1; }

# 启动 SSE 服务器
echo "正在启动 SSE 服务器..."
mvn spring-boot:run || { echo "服务器启动失败"; exit 1; }
