## 引言
本工程交易更新账户余额的操作,设计了双重重试机制.

因为重试有两个维度, 第一个维度是MQ维度. 因为交易更新账户余额的操作是通过mq异步实现的, 所以当抛出异常后, MQ会重试发送消息, 直到达到最大重试次数. 第二个维度是方法维度. 因为更新账户余额的操作是更新数据库, 所以当抛出异常后, 更新余额的方法可以重试, 直到达到最大重试次数.

针对第一个维度的重试比较简单,只需要注意对抛出异常类型管理到位就好了. 

以下重点介绍第二个维度的实现, 针对第二个维度,要在`updateAccountBalances`方法中为失败的交易实现重试机制，我使用了**Spring Retry**框架。Spring Retry提供了简单的注解和配置，允许你在出现特定异常时自动重试方法调用。

以下是实现步骤：

1. **引入Spring Retry依赖**
2. **启用Spring Retry功能**
3. **在`updateAccountBalances`方法上添加`@Retryable`注解**
4. **配置重试策略**
5. **处理可能的并发和事务问题**

下面我将详细说明每个步骤。

---

## 一、引入Spring Retry依赖

首先，需要在项目的`pom.xml`中添加Spring Retry的依赖。

**使用Maven：**

```xml
<dependency>
    <groupId>org.springframework.retry</groupId>
    <artifactId>spring-retry</artifactId>
    <version>1.3.1</version>
</dependency>
```

---

## 二、启用Spring Retry功能

在Spring Boot应用的主类或配置类上添加`@EnableRetry`注解，以启用Spring Retry功能。

```java
@SpringBootApplication
@EnableRetry
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(RealTimeBalanceApplication.class, args);
    }
}
```

---

## 三、在`updateAccountBalances`方法上添加`@Retryable`注解

在需要重试的方法上，添加`@Retryable`注解，指定需要重试的异常类型等参数。

```java
@Service
public class AccountServiceImpl implements AccountService {

    // 省略其他代码...

    @Transactional
    @Retryable(
        value = { CannotAcquireLockException.class, DeadlockLoserDataAccessException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000)
    )
    @Override
    public void updateAccountBalances(TransactionCommand command) {
        // 方法实现...
    }

    // 省略其他代码...
}
```

**说明：**

- `value`：指定需要触发重试的异常类型，可以根据实际情况调整。
    - 例如，`CannotAcquireLockException`和`DeadlockLoserDataAccessException`常用于表示数据库死锁或无法获取锁的情况。
- `maxAttempts`：最大重试次数，包括初始调用。
- `backoff`：重试的等待策略，这里设置了固定延迟`delay = 2000`毫秒。

---



## 四、处理可能的并发和事务问题

当涉及到事务和重试时，需要注意以下几点：

1. **事务传播属性**

   默认情况下，`@Transactional`的方法在同一个事务中执行。如果发生回滚，事务将结束。为了使重试生效，需要确保每次重试都是在新的事务中进行。

   可以通过设置`@Transactional`的`propagation`属性为`REQUIRES_NEW`来实现。

2. **捕获异常**

   确保方法中没有捕获并吞掉需要触发重试的异常，异常需要抛出才能被`@Retryable`捕获。

3. **不可重试的异常**

   对于业务异常（如余额不足）或者业务账号不存在，不应触发重试。

---
