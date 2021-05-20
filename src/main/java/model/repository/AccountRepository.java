package model.repository;

import model.entities.Account;
import model.repository.dto.AccountDTO;
import model.repository.dto.AccountMoneyDTO;

import java.sql.SQLException;
import java.util.List;

public interface AccountRepository extends Repository {

    void update(AccountMoneyDTO dto) throws SQLException;

    List<Account> getAllAccounts() throws SQLException;

    AccountMoneyDTO getBalance(String id) throws SQLException;

    void addAccount(AccountDTO accountDTO) throws SQLException;
}
