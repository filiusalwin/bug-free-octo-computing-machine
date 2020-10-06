package nl.miwgroningen.se.ch3.bacchux.controller;


import nl.miwgroningen.se.ch3.bacchux.model.LogDetail;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.LogDetailRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/log")
public class LogDetailRestController {

    @Autowired
    LogDetailRepository logDetailRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/directPayment")
    protected ResponseEntity logDirectPayment(@RequestParam String username, @RequestParam String paymentDetails) {
        LogDetail logdetail = new LogDetail();
        logdetail.setLocalDateTime(LocalDateTime.now());
        logdetail.setServingUser(username);
        logdetail.setDirectPaymentDetails(paymentDetails);
        logDetailRepository.save(logdetail);
        return new ResponseEntity(HttpStatus.OK);
    }


}
