package task.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransferDto {

    @NotBlank
    private Long accountIdFrom;

    @NotBlank
    private Long accountIdTo;

    @Min(0)
    private BigDecimal amount;

}
