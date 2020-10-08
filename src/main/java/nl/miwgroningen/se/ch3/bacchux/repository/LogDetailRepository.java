package nl.miwgroningen.se.ch3.bacchux.repository;

import nl.miwgroningen.se.ch3.bacchux.model.LogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogDetailRepository  extends JpaRepository<LogDetail, Integer> {
}
