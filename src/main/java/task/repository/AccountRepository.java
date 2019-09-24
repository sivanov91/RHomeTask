package task.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import task.model.entity.Account;

import java.math.BigDecimal;

@JdbcRepository(dialect = Dialect.H2)
public interface AccountRepository extends CrudRepository<Account, Long> {

    //language=SQL
    @Query(value = "select * from account where id = :accountId for update", nativeQuery = true)
    Account findByIdWithLook(Long accountId);

    //language=SQL
    @Query(value = "update account set amount = :amount where id = :accountId", nativeQuery = true)
    void updateAmount(Long accountId, BigDecimal amount);

    int countByIdAndAmountAfter(Long accountId, BigDecimal amount);

    default boolean isEnoughMoney(Long accountId, BigDecimal amount) {
        return countByIdAndAmountAfter(accountId, amount) > 0;
    }

}
