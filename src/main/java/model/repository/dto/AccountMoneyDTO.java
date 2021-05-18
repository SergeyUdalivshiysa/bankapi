package model.repository.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AccountMoneyDTO {
    int id;
    BigDecimal amount;
}



