package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.CreditPayment;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.CreditPaymentRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/payment/credit")
@RestController
public class CreditRestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CreditPaymentRepository creditPaymentRepository;

    @PostMapping("")
    protected ResponseEntity<String> makeCreditPayment(@RequestParam String username, @RequestParam Integer amount, @RequestParam String order) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            String message = String.format("User '%s' not found", username);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        User user = userOpt.get();
        if (!user.isCreditAllowed()) {
            String message = String.format("User '%s' does not have credit privileges", username);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        CreditPayment payment = new CreditPayment(user, amount, false, order);
        creditPaymentRepository.save(payment);

        return new ResponseEntity<>("Transaction successful", HttpStatus.OK);
    }
}
