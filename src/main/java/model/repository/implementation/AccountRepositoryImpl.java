package model.repository.implementation;

import exception.NoSuchAccountException;
import lombok.NoArgsConstructor;
import model.entities.Account;
import model.repository.AccountRepository;
import model.repository.dto.AccountMoneyDTO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {

    private final String URL = "jdbc:h2:/Users/a19189114/IdeaProjects/bankapi/src/main/resources/db/test/testbd";
    private final String ADD_MONEY_SQL = "update account set amount = amount + ? where id = ?;";
    private final String FIND_ALL_SQL = "select * from account";
    private final String GET_BALANCE_SQL = "select amount from account where id = ?";

    @Override
    public void update(AccountMoneyDTO dto) throws NoSuchAccountException, SQLException {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(ADD_MONEY_SQL);
            ps.setBigDecimal(1, dto.getAmount());
            ps.setInt(2, dto.getId());
            int result = ps.executeUpdate();
            if (result < 1) throwNoSuchAccExc(String.valueOf(dto.getId()));
        }
    }

    @Override
    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(FIND_ALL_SQL);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                accounts.add(buildAccount(resultSet));
            }
        }
        return accounts;
    }

    private Account buildAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account(
                resultSet.getInt(1),
                resultSet.getBigDecimal(2),
                resultSet.getInt(3)
        );
        return account;
    }

    @Override
    public AccountMoneyDTO getBalance(String id) throws SQLException, NoSuchAccountException {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(GET_BALANCE_SQL);
            ps.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) throwNoSuchAccExc(id);
            return new AccountMoneyDTO(Integer.parseInt(id), resultSet.getBigDecimal(1));
        }
    }

    private void throwNoSuchAccExc(String id) throws NoSuchAccountException {
        throw new NoSuchAccountException("There is no account with id = " + id);
    }
}
