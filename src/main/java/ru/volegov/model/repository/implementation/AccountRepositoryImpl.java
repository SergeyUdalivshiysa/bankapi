package ru.volegov.model.repository.implementation;

import ru.volegov.exception.IncorrectInputDataException;
import ru.volegov.exception.NotFoundException;
import ru.volegov.model.dto.AccountDTO;
import ru.volegov.model.dto.AccountMoneyDTO;
import ru.volegov.model.repository.AccountRepository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRepositoryImpl implements AccountRepository {

    private final String addMoneySql = "update account set amount = amount + ? where id = ?;";
    private final String getBalanceSql = "select amount from account where id = ?";
    private final String addAccountSql = "insert into account (user_id) values (?)";


    @Override
    public void update(AccountMoneyDTO dto) throws SQLException {
        executeQuery(addMoneySql, statement -> {
            statement.setBigDecimal(1, dto.getAmount());
            statement.setInt(2, dto.getId());
            if (dto.getAmount().compareTo(BigDecimal.ZERO) < 1)
                throw new IncorrectInputDataException("Incorrect input data");
            int result = statement.executeUpdate();
            if (result < 1) throw new IncorrectInputDataException("Incorrect input data");
            return null;
        });
    }

    @Override
    public AccountMoneyDTO getBalance(int id) throws SQLException {
        return executeQuery(getBalanceSql, statement -> {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) throw new NotFoundException("No such account");
            return new AccountMoneyDTO(id, resultSet.getBigDecimal(1));
        });
    }

    @Override
    public void addAccount(AccountDTO accountDTO) throws SQLException {
        executeQuery(addAccountSql, statement -> {
            statement.setInt(1, accountDTO.getUserId());
            statement.executeUpdate();
            return null;
        });
    }
}
