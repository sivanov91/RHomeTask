package task.controller;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import task.model.dto.MoneyTransferDto;
import task.model.entity.Account;
import task.repository.AccountRepository;
import task.service.AccountService;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.util.List;

// TODO javaDoc
@Controller("/account")
public class AccountController {

    @Inject
    private AccountService accountService;
    @Inject
    private AccountRepository accountRepository;

    @Post("/create")
    public void createAccount(@Body Account account) {
        accountService.createAccount(account);
    }

    @Get("/{id}")
    public Account getAccount(@NotBlank Long id) {
        return accountService.getAccount(id);
    }

    @Get("/list")
    public List<Account> getAccountList() {
        return accountService.getAccountList();
    }

    @Post("/transfer")
    public void transfer(@Body MoneyTransferDto moneyTransferDto) {
        accountService.transfer(moneyTransferDto);
    }

}
