package com.hsbc.financial.application.controller;

import com.hsbc.financial.application.pressure.DataGenerator;
import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.common.utils.JacksonUtil;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import com.hsbc.financial.domain.transaction.service.TransactionService;
import com.hsbc.financial.infrastructure.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PressureTestController
 *
 * @author zhaoyongping
 * @date 2025/1/12
 * @className PressureTestController
 **/
@RestController
@RequestMapping("/pressure/test")
public class PressureTestController {
    /**
     * 自动注入的交易服务，用于处理交易请求。
     */
    @Autowired
    private TransactionService transactionService;

    /**
     * 自动注入的账户存储库，用于管理账户信息。
     */
    @Autowired
    private AccountRepository accountRepository;

    /**
     * 测试交易服务
     * @return 如果交易成功，返回"交易已提交"；如果发生IllegalArgumentException，返回错误消息；否则，返回"交易失败："加上异常消息。
     */
    @GetMapping("transaction")
    public ResponseEntity<String> transactionTest() {
        try {
            TransactionCommand transactionCommand = DataGenerator.generateTransactionCommand();
            transactionService.processTransaction(transactionCommand);
            String result = JacksonUtil.toJson(transactionCommand);
            return ResponseEntity.ok("交易已提交:" + result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("交易失败：" + e.getMessage());
        }
    }

    /**
     * 创建50个新账户并保存到数据库中。
     * @return 创建结果的响应实体，包含状态码和消息。
     */
    @GetMapping("account/add")
    public ResponseEntity<String> createAccount() {
        try {
            // 创建账户的逻辑
            List<Account> accounts = DataGenerator.generateAccounts(50);
            List<String> collect = accounts.stream()
                    .map(Account::getAccountId)
                    .collect(Collectors.toList());
            System.out.println(JacksonUtil.toJson(collect));
            accountRepository.saveAll(accounts);
            return ResponseEntity.ok("账户已创建");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("创建账户失败：" + e.getMessage());
        }
    }
}
