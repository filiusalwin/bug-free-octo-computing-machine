package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.IbanValidation;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.model.UserDTO;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// is used to add a new customer from the order page, for security reasons the user url has been disbaled
// in order for a bartender to acces this particular method this new controller was created with the url
// order/newCustomer

@RestController
@RequestMapping("/order")
public class CustomerRestController {

    @Autowired
    UserRepository userRepository;


    @PutMapping ("/newCustomer")
    protected ResponseEntity saveNewCustomer(@RequestParam String username,
                                     @RequestParam String name,
                                     @RequestParam boolean prepaidOn) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setRoles("ROLE_CUSTOMER");
        user.setPrepaidAllowed(prepaidOn);
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }


}
