package model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Payment {
    int id;
    BigDecimal amount;
    boolean isApproved;
    int senderId;
    int receiverId;


}
