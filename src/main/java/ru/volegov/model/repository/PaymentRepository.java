package ru.volegov.model.repository;

import ru.volegov.model.dto.PaymentDTO;
import ru.volegov.model.entities.Payment;

import java.sql.SQLException;
import java.util.List;

public interface PaymentRepository extends Repository {
    /**
     * Handles creating a new payment
     *
     * @param paymentDTO
     * @throws SQLException in case of incorrect input data or internal database error
     */
    void addPayment(PaymentDTO paymentDTO) throws SQLException;

    /**
     * Handles returning the list of unapproved payments
     *
     * @return
     * @throws SQLException in case of internal database error
     */
    List<Payment> getUnapprovedPayments() throws SQLException;

    /**
     * Handles approving the payment, adding and subtracting money
     *
     * @param id Id of the certain payment
     * @throws SQLException In case of incorrect input data or internal database error
     */
    void approvePayment(int id) throws SQLException;
}
