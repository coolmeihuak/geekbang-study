package cc.page.study.week9.practice7;

import cc.page.study.week9.practice7.entity.Account;
import cc.page.study.week9.practice7.entity.User;
import cc.page.study.week9.practice7.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    final private AccountService accountService;

    @Autowired(required = false)
    public TransactionServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public void transaction() {
        accountService.exchange("62226111", BigDecimal.valueOf(-1), "62226222", BigDecimal.valueOf(7));
        accountService.exchange("62226333", BigDecimal.valueOf(7), "62226444", BigDecimal.valueOf(-1));
    }

    public void confirm() {
        log.info("=========confirm操作完成================");
    }

    public void cancel() {
        log.info("=========cancel操作完成================");
    }
}
