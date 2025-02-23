package com.spring.calculator.controller;

import com.spring.calculator.model.User;
import com.spring.calculator.service.UserService;
import com.spring.calculator.validator.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserValidator userValidator;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    @Qualifier("UserService")
    UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/loginUser")
    public String loginUser(@ModelAttribute("user") User user, BindingResult result, HttpSession session) {

        userValidator.validate(user, result);

        if (result.hasErrors()) {
            return "login";
        }

        User userFromDB = userService.findByUsername(user.getUsername());

        bCryptPasswordEncoder.encode(user.getPassword());

        if (bCryptPasswordEncoder.matches(user.getPassword(), userFromDB.getPassword())) {
            session.setAttribute("username", userFromDB.getUsername());
        }
        return "redirect:/calculator";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("user") User user, BindingResult result) {
        userValidator.validate(user, result);

        if (result.hasErrors()) {
            return "register";
        }

        userService.saveUser(user);

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByUsername(currentUserName);
        boolean isAdmin = currentUser.getRole() == User.UserRole.ADMIN;
        if (!isAdmin) {

            return "403";
        }

        return "users";
    }
}
