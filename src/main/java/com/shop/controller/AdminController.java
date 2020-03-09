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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller 
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @GetMapping("/showForm")
    public String showRegistrationForm(Model m) {
        if (m.getAttribute("user") != null) {
            return "registration-form-admin";
        }
        m.addAttribute("user", new User());
        return "registration-form-admin";
    }

    @ModelAttribute("roloi")
    public List<Role> fereRoles() {
        return roleService.getRoles();
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
                 System.out.println("asdasdasdasd");
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
            
            return "redirect:/admin/showForm";
        }

        try {

            boolean bool = rePassword.equals(user.getPassword());
            if (bool != true) {
                String msg = "password doesn't match";
                redirectAttrs.addFlashAttribute("msgRePassword", msg);
                return "redirect:/admin/showForm";
            }
        } catch (Exception e) {
            String msg = "password doesn't match";
            redirectAttrs.addFlashAttribute("msgRePassword", msg);
            return "redirect:/admin/showForm";
        }

        User existing = userService.findByUsername(user.getUsername());
        if (existing != null) {
            String msg = "Username Already Exists";
            redirectAttrs.addFlashAttribute("msgUserName", msg);
            return "redirect:/admin/showForm";
        }

        User existingMail = userService.findByEmail(user.getEmail());
        if (existingMail != null) {
            String msg = "Email Already Exists";
            redirectAttrs.addFlashAttribute("msgEmail", msg);
            return "redirect:/admin/showForm";
        }
        user.addRole(new Role(1, "ROLE_ADMIN"));
        userService.save(user);
        String msg = "Admin: " + user.getUsername() + " was added succesflully!";
        redirectAttrs.addFlashAttribute("msgSuccess", msg);
        return "redirect:/";
    }
    
}