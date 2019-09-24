package task.controller;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import task.model.dto.MoneyTransferDto;
import task.model.entity.Account;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class AccountControllerTest {

    @Inject
    @Client("/account/")
    RxHttpClient client;

    @Test
    void checkManyBeforeAndAfterRandomTransfers() {

        int accountCount = 100;
        int amountFrom = 100;
        int amountTo = 1000;
        int countTransfers = 1000;

        // Create $accountCount accounts
        Flux.range(1, accountCount).parallel()
                .runOn(Schedulers.parallel())
                .map(i -> Account.builder()
                        .amount(BigDecimal.valueOf(getRandomInRange(amountFrom, amountTo)))
                        .firstName("name" + i)
                        .lastName("lname" + i)
                        .build())
                .map(account -> client.toBlocking().exchange(HttpRequest.POST("/create", account)))
                .sequential().blockLast();

        // Calculate amount before transfers.
        BigDecimal amountBefore = client.retrieve(HttpRequest.GET("/list"), Argument.listOf(Account.class)).blockingFirst().stream()
                .map(Account::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Random transfers
        Flux.range(1, countTransfers).parallel()
                .runOn(Schedulers.parallel())
                .map(i -> { randomTransfer(accountCount); return i; })
                .sequential().blockLast();

        // Calculate amount after transfers.
        BigDecimal amountAfter = client.retrieve(HttpRequest.GET("/list"), Argument.listOf(Account.class)).blockingFirst().stream()
                .map(Account::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(amountBefore, amountAfter);
    }

    void randomTransfer(int accountCount) {

        // Get random id from money transfer.
        long accountIdFrom = getRandomInRange(1, accountCount);
        long accountIdTo = getRandomInRange(1, accountCount);

        // Get information about accountFrom.
        Account accountFrom = client.retrieve(HttpRequest.GET("/" + accountIdFrom), Account.class).blockingFirst();

        if (accountFrom.getAmount().compareTo(BigDecimal.ONE) < 1) {
            return;
        }
        BigDecimal amountForTransfer = BigDecimal.valueOf(getRandomInRange(1, accountFrom.getAmount().intValue()));

        MoneyTransferDto moneyTransferDto = new MoneyTransferDto(accountIdFrom, accountIdTo, amountForTransfer);
        try {
            client.toBlocking().exchange(HttpRequest.POST("/transfer", moneyTransferDto));
        } catch (HttpClientResponseException e) {
            //sometime client can throw error "Not enough money" and it is normal =).
        }

    }

    private static int getRandomInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }




}
