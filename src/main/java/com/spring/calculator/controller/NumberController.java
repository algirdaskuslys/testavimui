package com.spring.calculator.controller;

import com.spring.calculator.model.Number;
import com.spring.calculator.model.User;
import com.spring.calculator.service.NumberService;
import com.spring.calculator.service.UserService;
import com.spring.calculator.validator.NumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@Controller
public class NumberController {

    @Autowired
    @Qualifier("NumberService")
    public NumberService numberService;

    @Autowired
    @Qualifier("UserService")
    UserService userService;

    @Autowired
    NumberValidator numberValidator;

    @GetMapping("/calculator")
    public String home(Model model) {
        model.addAttribute("number", new Number());

        return "calculator";
    }

    @PostMapping("/calculate")
    public String calculate(@ModelAttribute("number") Number number, BindingResult br,
                            @RequestParam HashMap<String, String> numbers, ModelMap modelMap) {
        numberValidator.validate(number, br);

        if (br.hasErrors()) {
            return "calculator";
        } else {
            int number1 = Integer.parseInt(numbers.get("number1"));
            int number2 = Integer.parseInt(numbers.get("number2"));
            String operation = numbers.get("operation");

            int result = switch (operation) {
                case "+" -> number1 + number2;
                case "-" -> number1 - number2;
                case "*" -> number1 * number2;
                case "/" -> number1 / number2;
                default -> 0;
            };

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();
            User currentUser = userService.findByUsername(currentUserName);

            Number calculation = new Number(number1, number2, operation, result);
            calculation.setUsers(currentUser);

            numberService.save(calculation);

            modelMap.put("number1", number1);
            modelMap.put("number2", number2);
            modelMap.put("operation", operation);
            modelMap.put("result", result);

            return "calculate";
        }
    }

    @GetMapping("/numbers")
    public String getAllNumbers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByUsername(currentUserName);

        boolean isAdmin = currentUser.getRole() == User.UserRole.ADMIN;

        List<Number> numbers;

        if (isAdmin) {
            numbers = numberService.getAll();
        } else {
            numbers = currentUser.getCalculationsList();
        }

        model.addAttribute("numbers", numbers);

        return "numbers";
    }

    @GetMapping("/view{id}")
    public String getById(@RequestParam("id") int id, Model model) {
        Number number = numberService.getById(id);
        model.addAttribute("number", number);

        return "view";
    }

    @GetMapping("/delete{id}")
    public String delete(@RequestParam("id") int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByUsername(currentUserName);

        boolean isAdmin = currentUser.getRole() == User.UserRole.ADMIN;
        if (!isAdmin) {
            return "403";
        }

        numberService.delete(id);

        return "redirect:/numbers";
    }

    @GetMapping("/update{id}")
    public String update(@RequestParam("id") int id, Model model) {
        Number number = numberService.getById(id);
        model.addAttribute("number", number);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByUsername(currentUserName);

        boolean isAdmin = currentUser.getRole() == User.UserRole.ADMIN;
        if (!isAdmin) {

            return "403";
        }

        return "update";
    }

    @PostMapping("/updateNumber")
    public String updateNumber(@ModelAttribute("number") Number updatedNumber, BindingResult result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByUsername(currentUserName);

        if (currentUser.getRole() != User.UserRole.ADMIN) {
            return "403";
        }

        numberValidator.validate(updatedNumber, result);

        if (result.hasErrors()) {
            return "update";
        }

        Number originalNumber = numberService.getById(updatedNumber.getId());

        updatedNumber.setUsers(originalNumber.getUsers());

        originalNumber.setNumber1(updatedNumber.getNumber1());
        originalNumber.setNumber2(updatedNumber.getNumber2());
        originalNumber.setOperation(updatedNumber.getOperation());
        originalNumber.setResult(updatedNumber.getResult());

        numberService.update(originalNumber);

        return "redirect:/view?id=" + originalNumber.getId();
    }
}
