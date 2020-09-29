package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.servlet.headers.HttpStrictTransportSecurityDsl;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/payment/prepaid")
@RestController
public class PrepaidRestController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("")
    protected ResponseEntity<String> makePrepaidPayment(@RequestParam String username, @RequestParam Integer amount) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            String message = String.format("User '%s' not found", username);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        User user = userOpt.get();
        if (!userCanPay(user, amount)) {
            String message = String.format("Max payment: €%.2f", (double) user.getBalance() / 100);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
        return new ResponseEntity<>("Transaction successful", HttpStatus.OK);
    }

    public boolean userCanPay(User user, Integer amount) {
        return amount <= user.getBalance();
    }
}
