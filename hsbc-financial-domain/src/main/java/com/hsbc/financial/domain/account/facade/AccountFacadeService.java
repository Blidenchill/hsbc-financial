package com.hsbc.financial.domain.account.facade;

import com.hsbc.financial.domain.account.entity.Account;

/**
 * AccountFacadeService
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountFacadeService
 **/
public interface AccountFacadeService {
    Account findByIdForUpdate(String accountId);
    void save(Account account);
}
