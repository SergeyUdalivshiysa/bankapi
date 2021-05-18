package model.repository;

import exception.NoSuchAccountException;
import model.entities.Account;
import model.repository.dto.AccountMoneyDTO;

import java.util.List;

public interface AccountRepository {
    void update(AccountMoneyDTO dto) throws NoSuchAccountException;

    List<Account> getAllAccounts();
}
