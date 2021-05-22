package model.repository;

import model.dto.AccountDTO;
import model.dto.AccountMoneyDTO;

import java.sql.SQLException;

public interface AccountRepository extends Repository {

    void update(AccountMoneyDTO dto) throws SQLException;

    AccountMoneyDTO getBalance(int id) throws SQLException;

    void addAccount(AccountDTO accountDTO) throws SQLException;
}
