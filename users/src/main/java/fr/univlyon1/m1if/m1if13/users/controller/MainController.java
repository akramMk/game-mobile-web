package fr.univlyon1.m1if.m1if13.users.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String welcomePage() {
        return "index";
    }
}
