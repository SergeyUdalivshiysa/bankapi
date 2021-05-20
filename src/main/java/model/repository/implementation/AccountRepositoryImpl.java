package model.repository.implementation;

import exception.IncorrectInputDataException;
import model.entities.Account;
import model.repository.AccountRepository;
import model.repository.dto.AccountDTO;
import model.repository.dto.AccountMoneyDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {

    private final String addMoneySql = "update account set amount = amount + ? where id = ?;";
    private final String findAllSql = "select * from account";
    private final String getBalanceSql = "select amount from account where id = ?";
    private final String addAccountSql = "insert into account (user_id) values (?)";


    @Override
    public void update(AccountMoneyDTO dto) throws SQLException {
        executeQuery(addMoneySql, statement -> {
            statement.setBigDecimal(1, dto.getAmount());
            statement.setInt(2, dto.getId());
            int result = statement.executeUpdate();
            if (result < 1) throwNoSuchAccExc(String.valueOf(dto.getId()));
            return null;
        });
    }

    @Override
    public List<Account> getAllAccounts() throws SQLException {
        return executeQuery(findAllSql, statement -> {
            List<Account> accounts = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.add(buildAccount(resultSet));
            }
            return accounts;
        });
    }

    @Override
    public AccountMoneyDTO getBalance(String id) throws SQLException {
        return executeQuery(getBalanceSql, statement -> {
            statement.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) throwNoSuchAccExc(id);
            return new AccountMoneyDTO(Integer.parseInt(id), resultSet.getBigDecimal(1));
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


    private Account buildAccount(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getInt(1),
                resultSet.getBigDecimal(2),
                resultSet.getInt(3)
        );
    }

    private void throwNoSuchAccExc(String id) throws IncorrectInputDataException {
        throw new IncorrectInputDataException("Incorrect input data");
    }
}
