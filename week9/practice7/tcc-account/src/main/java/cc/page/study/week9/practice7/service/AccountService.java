package cc.page.study.week9.practice7.service;

import cc.page.study.week9.practice7.entity.Account;

import java.math.BigDecimal;

public interface AccountService {

    void exchange(String fromAccount, BigDecimal fromMoney, String toAccount, BigDecimal toMoney);
}
