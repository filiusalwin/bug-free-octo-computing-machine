package nl.miwgroningen.se.ch3.bacchux.repository;


import nl.miwgroningen.se.ch3.bacchux.model.BacchuxUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BacchuxUserRepository extends JpaRepository<BacchuxUser, Integer> {
    Optional<BacchuxUser> findByUsername(String username);
}
