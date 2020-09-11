package nl.miwgroningen.se.ch3.bacchux.repository;


import nl.miwgroningen.se.ch3.bacchux.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
