package ru.volegov.model.repository;

import ru.volegov.model.dto.AccountDTO;
import ru.volegov.model.dto.AccountMoneyDTO;

import java.sql.SQLException;

public interface AccountRepository extends Repository {
    /**
     * Handles updating amount of money on an account
     *
     * @param dto
     * @throws SQLException
     */
    void update(AccountMoneyDTO dto) throws SQLException;

    /**
     * Handles returning amount of money on a certain account
     *
     * @param id
     * @return
     * @throws SQLException
     */
    AccountMoneyDTO getBalance(int id) throws SQLException;

    /**
     * Handles adding account
     *
     * @param accountDTO
     * @throws SQLException
     */
    void addAccount(AccountDTO accountDTO) throws SQLException;
}
