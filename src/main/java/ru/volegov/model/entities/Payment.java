package ru.volegov.model.entities;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Payment {
    int id;
    BigDecimal amount;
    boolean isApproved;
    int senderId;
    int receiverId;


}
