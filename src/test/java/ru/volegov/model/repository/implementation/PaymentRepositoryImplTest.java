package ru.volegov.model.repository.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.volegov.model.dto.PaymentDTO;
import ru.volegov.model.entities.Payment;
import ru.volegov.model.repository.PaymentRepository;
import ru.volegov.model.repository.util.DataBaseFiller;
import ru.volegov.util.PropertiesManager;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentRepositoryImplTest {
    private final DataBaseFiller dataBaseFiller = new DataBaseFiller();
    private final String getLastCreatedPayment = "select id, amount, approved, sender_id, receiver_id from payment order by id desc limit 1";
    private final String getMoneyAmountSql = "select amount from account where id = ?";
    private final String getApprovedFlag = "select approved from payment where id = ?";
    private final PaymentRepository paymentRepository = new PaymentRepositoryImpl();


    @BeforeEach
    void initializeDatabase() {
        dataBaseFiller.fill();
    }

    @Test
    void addPayment() {
        try {
            PaymentDTO paymentDTO = new PaymentDTO(BigDecimal.valueOf(100), 1, 2);
            paymentRepository.addPayment(paymentDTO);
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            PreparedStatement preparedStatement = connection.prepareStatement(getLastCreatedPayment);
            ResultSet resultSet = preparedStatement.executeQuery();
            Payment actualPayment = null;
            if (resultSet.next()) actualPayment = buildPayment(resultSet);
            preparedStatement.close();
            connection.close();
            Payment expectedPayment = new Payment(3,
                    BigDecimal.valueOf(100).setScale(2),
                    false,
                    1,
                    2);
            assertEquals(expectedPayment, actualPayment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Payment buildPayment(ResultSet resultSet) throws SQLException {
        return new Payment(
                resultSet.getInt(1),
                resultSet.getBigDecimal(2),
                resultSet.getBoolean(3),
                resultSet.getInt(4),
                resultSet.getInt(5)
        );
    }

    @Test
    void getUnapprovedPayments() {
        try {
            List<Payment> expectedPayments = new ArrayList<>();
            Payment payment = new Payment(1, new BigDecimal("1.00"), false, 2, 1);
            expectedPayments.add(payment);
            List<Payment> actualPayments = paymentRepository.getUnapprovedPayments();
            Assertions.assertEquals(expectedPayments, actualPayments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void approvePayment() {
        try {
            int paymentId = 1;
            BigDecimal amount = BigDecimal.valueOf(1.00);
            int senderId = 2;
            int receiver_id = 1;
            paymentRepository.approvePayment(paymentId);
            Connection connection = DriverManager.getConnection(PropertiesManager.URL);
            BigDecimal senderMoneyActual = getActualSenderMoney(connection, senderId);
            BigDecimal receiverMoneyActual = getActualReceiverMoney(connection, receiver_id);
            boolean isApprovedActual = getIsApproved(connection, paymentId);
            Assertions.assertEquals(new BigDecimal("1.00"), senderMoneyActual);
            Assertions.assertEquals(new BigDecimal("201.50"), receiverMoneyActual);
            Assertions.assertTrue(isApprovedActual);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BigDecimal getActualSenderMoney(Connection connection, int id) throws SQLException {
        PreparedStatement senderMoneyStatement = connection.prepareStatement(getMoneyAmountSql);
        senderMoneyStatement.setInt(1, id);
        ResultSet senderResultSet = senderMoneyStatement.executeQuery();
        BigDecimal senderMoney = null;
        if (senderResultSet.next()) {
            senderMoney = senderResultSet.getBigDecimal(1);
        }
        senderMoneyStatement.close();
        return senderMoney;
    }

    private BigDecimal getActualReceiverMoney(Connection connection, int id) throws SQLException {
        PreparedStatement receiverMoneyStatement = connection.prepareStatement(getMoneyAmountSql);
        receiverMoneyStatement.setInt(1, id);
        ResultSet receiverResultSet = receiverMoneyStatement.executeQuery();
        BigDecimal receiverMoney = null;
        if (receiverResultSet.next()) {
            receiverMoney = receiverResultSet.getBigDecimal(1);
        }
        receiverMoneyStatement.close();
        return receiverMoney;
    }

    private boolean getIsApproved(Connection connection, int id) throws SQLException {
        PreparedStatement isApprovedStatement = connection.prepareStatement(getApprovedFlag);
        isApprovedStatement.setInt(1, id);
        ResultSet isApprovedResultSet = isApprovedStatement.executeQuery();
        boolean isApprovedActual = false;
        if (isApprovedResultSet.next()) {
            isApprovedActual = isApprovedResultSet.getBoolean(1);
        }
        isApprovedStatement.close();
        return isApprovedActual;
    }

    @Test
    void approveIncorrectPayment() {
        try {
            int paymentId = 0;
            paymentRepository.approvePayment(paymentId);
            Assertions.fail("Exception expected");
        } catch (Exception e) {
            assertEquals("Impossible to approve payment", e.getMessage());
        }
    }
}