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
    protected ResponseEntity logPayment(@RequestParam Integer totalAmount,
                                        @RequestParam String paymentDetails,
                                        @RequestParam String customer,
                                        @RequestParam String paymentType) {
        // to fetch to local user/ bartender
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String usernameLocal = ((UserDetails)principal).getUsername();

        // save all details to LogDetail
        LogDetail logdetail = new LogDetail();
        logdetail.setTimestamp(LocalDateTime.now());
        logdetail.setCustomer(customer);
        logdetail.setBartender(usernameLocal);
        logdetail.setTotalAmount(totalAmount);
        logdetail.setPaymentType(paymentType);
        logdetail.setPaymentDetails(paymentDetails);
        logDetailRepository.save(logdetail);
        return new ResponseEntity(HttpStatus.OK);
    }


}
