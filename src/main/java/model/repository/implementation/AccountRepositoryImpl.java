package model.repository.implementation;

import exception.NoSuchAccountException;
import lombok.NoArgsConstructor;
import model.entities.Account;
import model.repository.AccountRepository;
import model.repository.dto.AccountMoneyDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {

    private final String URL = "jdbc:h2:/Users/a19189114/IdeaProjects/bankapi/src/main/resources/db/test/testbd";
    private final String ADD_MONEY_SQL = "update account set amount = amount + ? where id = ?;";
    private final String FIND_ALL_SQL = "select * from account";

    @Override
    public void update(AccountMoneyDTO dto) throws NoSuchAccountException {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(ADD_MONEY_SQL);
            ps.setBigDecimal(1, dto.getAmount());
            ps.setInt(2, dto.getId());
            int result = ps.executeUpdate();
            if (result < 1) throw new NoSuchAccountException("There is no account with id = " + dto.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(FIND_ALL_SQL);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(
                        resultSet.getInt(1),
                        resultSet.getBigDecimal(2),
                        resultSet.getInt(3)
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
}
