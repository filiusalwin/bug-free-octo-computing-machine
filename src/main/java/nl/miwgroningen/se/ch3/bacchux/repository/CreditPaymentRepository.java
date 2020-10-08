package nl.miwgroningen.se.ch3.bacchux.repository;

import nl.miwgroningen.se.ch3.bacchux.model.CreditPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditPaymentRepository extends JpaRepository<CreditPayment, Integer> {
}
