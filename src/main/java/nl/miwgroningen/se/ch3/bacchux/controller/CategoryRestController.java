package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.Category;

import nl.miwgroningen.se.ch3.bacchux.model.CategoryDTO;
import nl.miwgroningen.se.ch3.bacchux.repository.CategoryRepository;
import nl.miwgroningen.se.ch3.bacchux.service.CurrentSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/catalog")
public class CategoryRestController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/byCategoryName/{categoryName}")
    protected CategoryDTO oneCategoryName(@PathVariable String categoryName) {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        if (!categoryOptional.isPresent()) {
            return null;
        }
        return new CategoryDTO(categoryOptional.get());
    }


}
