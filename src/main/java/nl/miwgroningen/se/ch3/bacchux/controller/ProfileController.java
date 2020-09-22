package nl.miwgroningen.se.ch3.bacchux.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile/password")
    protected String changePassword() {
        return "changePassword";
    }
}
