package model.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Card {
    int id;
    String number;
    int account_id;
    boolean isActive;
}
