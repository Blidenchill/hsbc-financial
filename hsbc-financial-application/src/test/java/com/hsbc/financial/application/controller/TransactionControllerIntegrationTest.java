package com.hsbc.financial.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import com.hsbc.financial.infrastructure.repository.AccountRepository;
import com.hsbc.financial.infrastructure.repository.TransactionEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TransactionControllerIntegrationTest
 *
 * @author zhaoyongping
 * @date 2025/1/14
 * @className TransactionControllerIntegrationTest
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransactionControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionEventRepository transactionEventRepository;

    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        // 初始化 WebTestClient
        webTestClient = WebTestClient.bindToServer()
                .responseTimeout(Duration.ofSeconds(20))
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void testTransfer() throws Exception {
        // 准备测试数据
        TransactionCommand command = new TransactionCommand();
        command.setSourceAccountId("source123");
        command.setDestAccountId("dest456");
        command.setAmount(new BigDecimal("100.00"));
        command.setTransactionId(UUID.randomUUID().toString());
        //转账前
        Account beforeSourceAccount = accountRepository.findByAccountId("source123").orElseThrow();
        Account beroreDestAccount = accountRepository.findByAccountId("dest456").orElseThrow();

        // 执行并断言
        webTestClient.post()
                .uri("/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("交易已提交");

        Optional<TransactionEvent> transactionEvent = transactionEventRepository.findByTransactionId(command.getTransactionId());
        assertThat(transactionEvent.isPresent()).isTrue();
        assertThat(transactionEvent.get().getSourceAccountId()).isEqualTo(command.getSourceAccountId());
        assertThat(transactionEvent.get().getDestAccountId()).isEqualTo(command.getDestAccountId());
        assertThat(transactionEvent.get().getAmount()).isEqualTo(command.getAmount());
        // 验证账户余额, 因为是异步更新,等待5秒
        Thread.sleep(5000L);
        Account afterSourceAccount = accountRepository.findByAccountId("source123").orElseThrow();
        Account afterDestAccount = accountRepository.findByAccountId("dest456").orElseThrow();

        assertThat(beforeSourceAccount.getBalance().subtract(afterSourceAccount.getBalance())).isEqualTo(new BigDecimal("100.00"));
        assertThat(afterDestAccount.getBalance().subtract(beroreDestAccount.getBalance())).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    void testGetAccount() {
        // 执行并断言
        webTestClient.get()
                .uri("/transactions/accounts/{accountId}", "source123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accountId").isEqualTo("source123");
    }

    @Test
    void testGetAccountNotFound() {
        // 执行并断言
        webTestClient.get()
                .uri("/transactions/accounts/{accountId}", "999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}
