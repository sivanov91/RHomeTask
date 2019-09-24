package task.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.micronaut.http.annotation.Body;
import io.micronaut.spring.tx.annotation.Transactional;
import task.model.dto.MoneyTransferDto;
import task.model.entity.Account;
import task.repository.AccountRepository;

/**
 * @author sivanov on 23.09.2019.
 */
@Singleton
public class AccountService {

    @Inject
    private AccountRepository accountRepository;

    //TODO javaDoc
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElse(null);
    }

    public List<Account> getAccountList() {
        List<Account> list = new ArrayList<>();
        accountRepository.findAll().forEach(list::add);
        return list;
    }

    @Transactional
    public void transfer(@Body MoneyTransferDto moneyTransferDto) {

        Long accountIdFrom = moneyTransferDto.getAccountIdFrom();
        Long accountIdTo = moneyTransferDto.getAccountIdTo();
        BigDecimal amount = moneyTransferDto.getAmount();

        // Does not do anything, if accountFrom equal accountTo
        if (accountIdFrom.equals(accountIdTo)) {
            return;
        }

        // Throw exception if isn't enough mount on from account before lock
        if (!accountRepository.isEnoughMoney(accountIdFrom, amount)) {
            throw new RuntimeException("Not enough money.");
        }

        // TODO comment
        // Последовательно блокируем записи аккаунтов, чтобы избежать дедлока.
        Account accountFrom;
        Account accountTo;
        if (accountIdFrom.compareTo(accountIdTo) > 0) {
            accountFrom = accountRepository.findByIdWithLook(accountIdFrom);
            accountTo = accountRepository.findByIdWithLook(accountIdTo);
        } else {
            accountTo = accountRepository.findByIdWithLook(accountIdTo);
            accountFrom = accountRepository.findByIdWithLook(accountIdFrom);
        }

        // Throw exception if isn't enough mount on from account after lock
        if (accountFrom.getAmount().compareTo(amount) < 0) {
            throw new RuntimeException("Not enough money.");
        }

        accountRepository.updateAmount(accountIdFrom, accountFrom.getAmount().subtract(amount));
        accountRepository.updateAmount(accountIdTo, accountTo.getAmount().add(amount));
    }


}
