package nl.miwgroningen.se.ch3.bacchux.controller;


import nl.miwgroningen.se.ch3.bacchux.model.Category;
import nl.miwgroningen.se.ch3.bacchux.model.CategoryDTO;
import nl.miwgroningen.se.ch3.bacchux.model.Product;
import nl.miwgroningen.se.ch3.bacchux.model.ProductDTO;
import nl.miwgroningen.se.ch3.bacchux.repository.CategoryRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/catalog")
public class ProductRestController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/productName/{productName}")
    protected ProductDTO oneProductName(@PathVariable String productName) {
        Optional<Product> productOptional = productRepository.findByName(productName);
        if (!productOptional.isPresent()) {
            return null;
        }
        return new ProductDTO(productOptional.get());
    }



}
