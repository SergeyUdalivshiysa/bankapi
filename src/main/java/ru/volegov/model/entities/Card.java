package ru.volegov.model.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Card {
    int id;
    String number;
    int account_id;
    boolean isActive;
}
