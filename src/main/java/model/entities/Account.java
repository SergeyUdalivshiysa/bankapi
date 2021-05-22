package model.entities;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Account {
    int id;
    String number;
    BigDecimal amount;
    int userId;
}
