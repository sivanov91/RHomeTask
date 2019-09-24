package task.model.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author sivanov on 23.09.2019.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private BigDecimal amount;

    private String firstName;

    private String lastName;

    public Account(){}

}
