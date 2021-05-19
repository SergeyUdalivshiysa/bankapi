package model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    int id;
    String name;
    boolean isCounterparty;
    boolean isLegalEntity;
    int partnerId;

    public User(String name, boolean isCounterparty, boolean isLegalEntity, int partnerId) {
        this.name = name;
        this.isCounterparty = isCounterparty;
        this.isLegalEntity = isLegalEntity;
        this.partnerId = partnerId;
    }
}
