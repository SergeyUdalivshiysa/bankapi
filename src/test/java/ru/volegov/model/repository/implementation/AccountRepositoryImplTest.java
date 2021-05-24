package ru.volegov.model.repository.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.volegov.model.dto.AccountDTO;
import ru.volegov.model.dto.AccountMoneyDTO;
import ru.volegov.model.entities.Account;
import ru.volegov.model.repository.AccountRepository;
import ru.volegov.model.repository.util.DataBaseFiller;
import ru.volegov.util.PropertiesManager;

import java.math.BigDecimal;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountRepositoryImplTest {

    private final String getMoneyAmountSql = "select amount from account where id = ?";
    private final String getLastCreatedAccount = "select id, number, amount, user_id from account order by id desc limit 1";
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();
    private final AccountRepository accountRepository = new AccountRepositoryImpl();

    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void update() {
        try {
            int id = 1;
            AccountMoneyDTO accountMoneyDTO = new AccountMoneyDTO(id, new BigDecimal(200));
            accountRepository.update(accountMoneyDTO);
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getMoneyAmountSql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal actual = null;
            if (resultSet.next()) actual = resultSet.getBigDecimal(1);
            preparedStatement.close();
            connection.close();
            Assertions.assertEquals(new BigDecimal("400.50"), actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getBalance() {
        try {
            int accountId = 1;
            AccountMoneyDTO accountMoneyDTO = accountRepository.getBalance(accountId);
            assertEquals(BigDecimal.valueOf(200.5).setScale(2), accountMoneyDTO.getAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addAccount() {
        try {
            AccountDTO accountDTO = new AccountDTO(1);
            accountRepository.addAccount(accountDTO);
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getLastCreatedAccount);
            ResultSet resultSet = preparedStatement.executeQuery();
            Account actualAccount = null;
            if (resultSet.next()) actualAccount = buildAccount(resultSet);
            Account expectedAccount = new Account(3, "1000000000000002", BigDecimal.valueOf(0).setScale(2), 1);
            preparedStatement.close();
            connection.close();
            assertEquals(expectedAccount, actualAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Account buildAccount(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getBigDecimal(3),
                resultSet.getInt(4));
    }
}