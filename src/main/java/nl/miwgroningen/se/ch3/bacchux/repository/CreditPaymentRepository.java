package nl.miwgroningen.se.ch3.bacchux.repository;

import nl.miwgroningen.se.ch3.bacchux.model.CreditPayment;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditPaymentRepository extends JpaRepository<CreditPayment, Integer> {
    List<CreditPayment> findByCustomer(User customer);
}
