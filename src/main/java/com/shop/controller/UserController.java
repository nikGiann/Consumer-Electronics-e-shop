package com.shop.controller;

import com.shop.entities.Role;
import com.shop.entities.User;
import com.shop.service.RoleService;
import com.shop.service.UserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @GetMapping("/showForm")
    public String showRegistrationForm(Model m) {
        if (m.getAttribute("user") != null) {
            return "registration-form-user";
        }
        m.addAttribute("user", new User());
        return "registration-form-user";
    }

    @PostMapping("/processRegistration")
    public String processRegistration(@Valid @ModelAttribute("user") User user,
            BindingResult result, Model m,
            @RequestParam("rePassword") String rePassword,
            RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("user", user); // ksanavazw ton user sto modelo gia na to parei to redirect
        System.out.println("User = " + user);

        if (result.hasErrors()) {

            if (user.getUsername().length() == 0) {
                String msg = "User name must not be null";
                redirectAttrs.addFlashAttribute("msgUserName", msg);
            }
            if (user.getPassword().length() == 0) {
                String msg = "password must not be null";
                redirectAttrs.addFlashAttribute("msgPassword", msg);
            }
            if (user.getPassword().length() < 4) {
                String msg = "password must be at least 4 characters";
                redirectAttrs.addFlashAttribute("msgPassword", msg);
            }
            if (rePassword == null) {
                String msg = "You must retype the password";
                redirectAttrs.addFlashAttribute("msgRePassword", msg);
            }

            if (user.getFname().length() == 0) {
                String msg = "First name must not be null";
                redirectAttrs.addFlashAttribute("msgFname", msg);
            }
            if (user.getLname().length() == 0) {
                String msg = "Last name must not be null";
                redirectAttrs.addFlashAttribute("msgLname", msg);
            }
            if (user.getEmail().length() == 0) {
                String msg = "email must not be null";
                redirectAttrs.addFlashAttribute("msgEmail", msg);
            }
            if (user.getEmail().length() == 0) {
                String msg = "email must not be null";
                redirectAttrs.addFlashAttribute("msgEmail", msg);
            }
            if (user.getTelephone().length() == 0) {
                String msg = "telephone must not be null";
                redirectAttrs.addFlashAttribute("msgTel", msg);
            }
            if (user.getCountry().length() == 0) {
                String msg = "Country must not be null";
                redirectAttrs.addFlashAttribute("msgCountry", msg);
            }
            if (user.getCity().length() == 0) {
                String msg = "City must not be null";
                redirectAttrs.addFlashAttribute("msgCity", msg);
            }
            if (user.getAddress().length() == 0) {
                String msg = "Address must not be null";
                redirectAttrs.addFlashAttribute("msgAddress", msg);
            }
            if (user.getPostal_code().length() == 0) {
                String msg = "Postal Code must not be null";
                redirectAttrs.addFlashAttribute("msgPostal", msg);
            }

            return "redirect:/user/showForm";
        }

        try {

            boolean bool = rePassword.equals(user.getPassword());
            if (bool != true) {
                String msg = "password doesn't match";
                redirectAttrs.addFlashAttribute("msgRePassword", msg);
                return "redirect:/user/showForm";
            }
        } catch (Exception e) {
            String msg = "password doesn't match";
            redirectAttrs.addFlashAttribute("msgRePassword", msg);
            return "redirect:/user/showForm";
        }

        User existing = userService.findByUsername(user.getUsername());
        if (existing != null) {
            String msg = "Username Already Exists";
            redirectAttrs.addFlashAttribute("msgUserName", msg);
            return "redirect:/user/showForm";
        }

        User existingMail = userService.findByEmail(user.getEmail());
        if (existingMail != null) {
            String msg = "Email Already Exists";
            redirectAttrs.addFlashAttribute("msgEmail", msg);
            return "redirect:/user/showForm";
        }
        user.addRole(new Role(2, "ROLE_USER"));
        userService.save(user);
        String msg = "Welcome to Geek.com, "+ user.getUsername()+"!";
        redirectAttrs.addFlashAttribute("msgSuccess", msg);
        return "redirect:/";
    }

}
