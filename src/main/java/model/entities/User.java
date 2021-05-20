package model.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    int id;
    String name;
    boolean isLegalEntity;
}
