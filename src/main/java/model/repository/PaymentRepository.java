package model.repository;

import model.entities.Payment;
import model.repository.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentRepository extends Repository {

    void addPayment(PaymentDTO paymentDTO) throws SQLException;

    List<Payment> getUnapprovedPayments() throws SQLException;

    void approvePayment(String id) throws SQLException;
}
