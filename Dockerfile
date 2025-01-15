# 第一阶段：构建应用程序
FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/maven:3.8.5-openjdk-17 AS builder

# 设置工作目录
WORKDIR /build

# 复制父模块的 pom.xml
COPY pom.xml .

# 复制子模块的 pom.xml（利用 Maven 依赖缓存）
COPY hsbc-financial-application/pom.xml hsbc-financial-application/
COPY hsbc-financial-domain/pom.xml hsbc-financial-domain/
COPY hsbc-financial-infrastructure/pom.xml hsbc-financial-infrastructure/

# 下载依赖，利用缓存
RUN mvn dependency:go-offline

# 复制所有源代码
COPY hsbc-financial-application/src hsbc-financial-application/src
COPY hsbc-financial-domain/src hsbc-financial-domain/src
COPY hsbc-financial-infrastructure/src hsbc-financial-infrastructure/src

# 构建应用程序（在父模块上执行构建）
RUN mvn clean package -DskipTests

# 第二阶段：创建运行镜像
FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/maven:3.8.5-openjdk-17
WORKDIR /app

# 从构建阶段复制生成的 Jar 包到运行阶段
COPY --from=builder /build/hsbc-financial-application/target/*.jar app.jar

# 暴露端口（根据您的应用配置，如有需要可修改）
EXPOSE 8080

# 运行应用程序
ENTRYPOINT ["java", "-jar", "app.jar"]