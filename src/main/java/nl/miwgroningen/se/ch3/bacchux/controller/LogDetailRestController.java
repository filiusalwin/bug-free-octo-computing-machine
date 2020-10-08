package nl.miwgroningen.se.ch3.bacchux.controller;


import nl.miwgroningen.se.ch3.bacchux.model.LogDetail;
import nl.miwgroningen.se.ch3.bacchux.repository.LogDetailRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/log")
public class LogDetailRestController {

    @Autowired
    LogDetailRepository logDetailRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/directPayment")
    protected ResponseEntity logDirectPayment(@RequestParam Integer totalAmount,
                                              @RequestParam String paymentDetails) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String usernameLocal = ((UserDetails)principal).getUsername();
        LogDetail logdetail = new LogDetail();
        logdetail.setTimestamp(LocalDateTime.now());
        logdetail.setServingUser(usernameLocal);
        logdetail.setTotalAmount(totalAmount);
        logdetail.setPaymentDetails(paymentDetails);
        logDetailRepository.save(logdetail);
        return new ResponseEntity(HttpStatus.OK);
    }


}
