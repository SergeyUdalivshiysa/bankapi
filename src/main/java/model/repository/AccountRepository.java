package model.repository;

import exception.NoSuchAccountException;
import model.entities.Account;
import model.repository.dto.AccountMoneyDTO;

import java.sql.SQLException;
import java.util.List;

public interface AccountRepository {
    void update(AccountMoneyDTO dto) throws NoSuchAccountException, SQLException;

    List<Account> getAllAccounts() throws SQLException;

    public AccountMoneyDTO getBalance(String id) throws SQLException, NoSuchAccountException;
}
