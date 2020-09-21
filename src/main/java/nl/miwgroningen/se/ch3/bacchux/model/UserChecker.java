package nl.miwgroningen.se.ch3.bacchux.model;

import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UserChecker {

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
            userRepository.save(newUser);
        }
    }
}
