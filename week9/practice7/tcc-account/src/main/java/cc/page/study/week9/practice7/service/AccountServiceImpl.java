package cc.page.study.week9.practice7.service;

import cc.page.study.week9.practice7.entity.Account;
import cc.page.study.week9.practice7.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public void exchange(String fromAccount, BigDecimal fromMoney, String toAccount, BigDecimal toMoney) {
        // todo 冻结
    }

    /**
     * 确认冻结
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(Account fromAccount, BigDecimal fromMoney, Account toAccount, BigDecimal toMoney) {
        log.info("============dubbo tcc 执行确认付款接口===============");
        return true;
    }

    /**
     * 冻结还原
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(Account fromAccount, BigDecimal fromMoney, Account toAccount, BigDecimal toMoney) {
        log.info("============ dubbo tcc 执行取消付款接口===============");
        // todo
        return true;
    }
}
