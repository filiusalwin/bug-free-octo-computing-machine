package nl.miwgroningen.se.ch3.bacchux.repository;

import nl.miwgroningen.se.ch3.bacchux.model.Category;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
