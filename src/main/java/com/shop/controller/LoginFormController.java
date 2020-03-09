package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class LoginFormController {
    
    @GetMapping
    public String showLoginForm(){
        
        return "loginForm";
    }
}
