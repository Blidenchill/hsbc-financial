package com.hsbc.financial.application.controller;

import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.account.service.AccountService;
import com.hsbc.financial.domain.common.exception.AccountNotFoundException;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import com.hsbc.financial.domain.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TransactionController
 *
 * @author zhaoyongping
 * @date 2025/1/12
 * @className TransactionController
 **/
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    /**
     * 用于处理交易相关操作的服务对象。
     */
    private final TransactionService transactionService;

    /**
     * 账户服务对象，用于处理账户相关操作。
     */
    private final AccountService accountService;

    /**
     * 构造函数，用于初始化TransactionController对象。
     * @param transactionService 交易服务对象，用于处理与交易相关的业务逻辑。
     * @param accountService 账户服务对象，用于处理与账户相关的业务逻辑。
     */
    @Autowired
    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    /**
     * 执行交易操作
     * @param command 交易命令对象，包含交易相关信息
     * @return 如果交易成功，返回 HTTP 状态码 200 和消息 "交易已提交"；否则返回 HTTP 状态码 500 和错误消息
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransactionCommand command) {
        try {
            transactionService.processTransaction(command);
            return ResponseEntity.ok("交易已提交");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("交易失败：" + e.getMessage());
        }
    }

    /**
     * 根据账户ID获取账户信息
     * @param accountId 账户ID
     * @return 账户对象，若账户不存在则返回404状态码
     */
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountId) {
        try {
            Account account = accountService.getAccountById(accountId);
            return ResponseEntity.ok(account);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }




}

