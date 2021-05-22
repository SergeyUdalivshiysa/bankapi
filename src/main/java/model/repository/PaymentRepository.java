package model.repository;

import model.dto.PaymentDTO;
import model.entities.Payment;

import java.sql.SQLException;
import java.util.List;

public interface PaymentRepository extends Repository {

    void addPayment(PaymentDTO paymentDTO) throws SQLException;

    List<Payment> getUnapprovedPayments() throws SQLException;

    void approvePayment(int id) throws SQLException;
}
