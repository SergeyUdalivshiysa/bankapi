package model.repository.implementation;

import exception.IncorrectInputDataException;
import model.dto.PaymentDTO;
import model.entities.Payment;
import model.repository.PaymentRepository;
import util.PropertiesManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepositoryImpl implements PaymentRepository {

    private final String insertPaySql = "insert into payment (amount, approved, sender_id, receiver_id) values (?, false, ?, ?)";
    private final String getUnapprovedPayments = "select id, id, amount, approved, sender_id, receiver_id from payment where approved = false";
    private final String approvePaymentSql = "update payment set approved = true where id = ? and approved = false";
    private final String getPaymentSql = "select amount, sender_id, receiver_id from payment where id = ?";
    private final String takeSenderMoneySql = "update account set amount = amount - ? where amount >= ? and id = ?";
    private final String giveMoneySql = "update account set amount = amount + ? where id = ?";

    @Override
    public void addPayment(PaymentDTO paymentDTO) throws SQLException {
        executeQuery(insertPaySql, statement -> {
            statement.setBigDecimal(1, paymentDTO.getAmount());
            statement.setInt(2, paymentDTO.getSenderId());
            statement.setInt(3, paymentDTO.getReceiverId());
            int result = statement.executeUpdate();
            if (result < 1) throw new SQLDataException("Incorrect input data");
            return null;
        });
    }

    @Override
    public List<Payment> getUnapprovedPayments() throws SQLException {
        return executeQuery(getUnapprovedPayments, statement -> {
            List<Payment> payments = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                payments.add(buildPayment(resultSet));
            }
            return payments;
        });
    }

    @Override
    public void approvePayment(String id) throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(PropertiesManager.URL);
            connection.setAutoCommit(false);
            PaymentDTO paymentDTO = createPaymentDTO(connection, id);
            executeTakingMoney(connection, paymentDTO);
            executeGivingMoney(connection, paymentDTO);
            setApproved(connection, id);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                connection.rollback();
            }
            throw new IncorrectInputDataException("Impossible to approve payment");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private PaymentDTO createPaymentDTO(Connection connection, String id) throws SQLException {
        PreparedStatement getPaymentStatement = connection.prepareStatement(getPaymentSql);
        getPaymentStatement.setString(1, id);
        ResultSet resultSet = getPaymentStatement.executeQuery();
        getPaymentStatement.close();
        if (!resultSet.next()) throw new IncorrectInputDataException("Incorrect input data");
        return new PaymentDTO(resultSet.getBigDecimal(1),
                resultSet.getInt(2),
                resultSet.getInt(3));
    }

    private void executeTakingMoney(Connection connection, PaymentDTO paymentDTO) throws SQLException {
        PreparedStatement takeSenderMoneyStatement = connection.prepareStatement(takeSenderMoneySql);
        takeSenderMoneyStatement.setBigDecimal(1, paymentDTO.getAmount());
        takeSenderMoneyStatement.setBigDecimal(2, paymentDTO.getAmount());
        takeSenderMoneyStatement.setInt(3, paymentDTO.getSenderId());
        int result = takeSenderMoneyStatement.executeUpdate();
        takeSenderMoneyStatement.close();
        if (result < 1) throw new IncorrectInputDataException("Incorrect input data");
    }

    private void executeGivingMoney(Connection connection, PaymentDTO paymentDTO) throws SQLException {
        PreparedStatement giveMoneyStatement = connection.prepareStatement(giveMoneySql);
        giveMoneyStatement.setBigDecimal(1, paymentDTO.getAmount());
        giveMoneyStatement.setInt(2, paymentDTO.getReceiverId());
        int result = giveMoneyStatement.executeUpdate();
        giveMoneyStatement.close();
        if (result < 1) throw new IncorrectInputDataException("Incorrect input data");
    }

    private void setApproved(Connection connection, String id) throws SQLException {
        PreparedStatement setApproved = connection.prepareStatement(approvePaymentSql);
        setApproved.setString(1, id);
        int result = setApproved.executeUpdate();
        setApproved.close();
        if (result < 1) throw new IncorrectInputDataException("Incorrect input data");
    }

    private Payment buildPayment(ResultSet resultSet) throws SQLException {
        return new Payment(
                resultSet.getInt(1),
                resultSet.getBigDecimal(2),
                resultSet.getBoolean(3),
                resultSet.getInt(4),
                resultSet.getInt(5));
    }
}

