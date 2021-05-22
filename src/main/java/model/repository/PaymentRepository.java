package model.repository;

import model.dto.PaymentDTO;
import model.entities.Payment;

import java.sql.SQLException;
import java.util.List;

public interface PaymentRepository extends Repository {
    /**
     * Handles creating a new payment
     *
     * @param paymentDTO
     * @throws SQLException
     */
    void addPayment(PaymentDTO paymentDTO) throws SQLException;

    /**
     * Handles returning the list of unapprob=ved payments
     *
     * @return
     * @throws SQLException
     */
    List<Payment> getUnapprovedPayments() throws SQLException;

    /**
     * Handles approveing the payment, adding and subtracting money
     *
     * @param id
     * @throws SQLException
     */
    void approvePayment(int id) throws SQLException;
}
