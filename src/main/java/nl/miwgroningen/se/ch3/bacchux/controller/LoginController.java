package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void checkForFirstUser() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.size() == 0) {
            User newUser = new User();
            newUser.setUsername("admin");
            newUser.setPassword(passwordEncoder.encode("admin"));
            newUser.setRoles("ROLE_BARMANAGER");
            newUser.setPasswordNeedsChange(true);
            userRepository.save(newUser);
        }
    }

    @GetMapping("/login")
    public String login(){
        checkForFirstUser();
        return "login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/403";
    }
}
