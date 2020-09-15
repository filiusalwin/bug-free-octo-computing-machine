package nl.miwgroningen.se.ch3.bacchux.repository;

import nl.miwgroningen.se.ch3.bacchux.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
