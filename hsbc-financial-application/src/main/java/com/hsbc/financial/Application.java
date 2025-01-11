package com.hsbc.financial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.hsbc.financial"})
@PropertySource(value = {"classpath:important.properties"}, encoding = "UTF-8")
@EnableRetry
@EnableJpaRepositories
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer {

     /**
       * 允许你将Spring Boot应用程序打包成WAR文件，并在Servlet容器中运行，以便于与传统的Java Web应用程序部署方式兼容。
       * @author zhaoyongping
       * @date 2023/11/8 18:22
       * @param application:
       * @return SpringApplicationBuilder
       */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

     /**
       * main 项目启动入口
       * @author zhaoyongping
       * @date 2023/11/8 18:22
       * @param args:
       */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}