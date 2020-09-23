package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.model.UserDTO;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @PutMapping("/topup")
    protected ResponseEntity topUpUser(@RequestParam String username, @RequestParam Integer amount) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else if (amount <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    protected UserDTO getUserByUsername(@PathVariable("username") final String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return null;
        }
        return new UserDTO(user.get());
    }
}
