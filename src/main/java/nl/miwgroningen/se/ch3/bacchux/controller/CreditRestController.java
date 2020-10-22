package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.CreditPayment;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.CreditPaymentRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import nl.miwgroningen.se.ch3.bacchux.utils.CurrencyFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
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

        User customer = userOpt.get();
        if (!customer.isCreditAllowed()) {
            String message = String.format("User '%s' does not have credit privileges", username);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        userOpt = getCurrentUser();
        if (userOpt.isEmpty()) {
            String message = "No bartender logged in";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        User bartender = userOpt.get();
        CreditPayment payment = new CreditPayment(customer, bartender, amount, order);
        creditPaymentRepository.save(payment);

        return new ResponseEntity<>("Transaction successful", HttpStatus.OK);
    }

    @PostMapping("/clear/{userId}")
    protected ResponseEntity clearCreditPayments(@PathVariable("userId") final Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOpt.get();
        if (!user.isCreditAllowed()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<CreditPayment> payments = creditPaymentRepository.findByCustomer(user);
        CollectionUtils.filter(payments, payment -> !((CreditPayment) payment).isPaid());


        for (CreditPayment payment : payments) {
            payment.setPaid(true);
            creditPaymentRepository.save(payment);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return Optional.empty();
        }
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUsername(username);
    }
}
