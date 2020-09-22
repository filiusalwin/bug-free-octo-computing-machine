package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class LandingController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    protected String landingPage() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails)) {
            return "redirect:/login";
        }
        String username = ((UserDetails)principal).getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        if (user.get().getPasswordNeedsChange()) {
            return "redirect:/profile/password";
        }
        return "redirect:/order";
    }
}
