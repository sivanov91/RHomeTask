package task;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.repository.AccountRepository;

import javax.inject.Singleton;

@Singleton
@TypeHint(typeNames = {"org.h2.Driver", "org.h2.mvstore.db.MVTableEngine"})
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    private final AccountRepository accountRepository;

    Application(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

    @EventListener
    void init(StartupEvent event) {

        /*List<Account> accounts = new ArrayList<>();
        for (long i = 0; i <= 5; i++) {
            accounts.add(Account.builder()
                    .id(i)
                    .amount(BigDecimal.valueOf(100 * i))
                    .firstName("name" + i)
                    .lastName("lname" + i)
                    .build());
        }
        accountRepository.saveAll(accounts);*/

    }

}