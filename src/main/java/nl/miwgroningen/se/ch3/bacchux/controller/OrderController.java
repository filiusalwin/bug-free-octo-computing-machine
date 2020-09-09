package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.repository.ProductRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/order")
    protected String showCatalog(Model model) {
        model.addAttribute("allProducts", productRepository.findAll());
        return "order";
    }

    @GetMapping("/order/new/prepaid")
    protected String addPrepaidCustomer(Model model) {
        model.addAttribute("allUsers", userRepository.findAll());
        return "order_new_prepaid_customer";
    }
}
