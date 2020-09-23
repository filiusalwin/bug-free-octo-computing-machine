package nl.miwgroningen.se.ch3.bacchux.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    protected String changeProfile() {
        return "profileOverview";
    }

    @GetMapping("/profile/password")
    protected String changePassword() {
        return "changePassword";
    }

    @GetMapping("/profile/pin")
    protected String changePin() {
        return "changePin";
    }

}
