package com.spring.calculator.validator;

import com.spring.calculator.model.User;
import com.spring.calculator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    private static final String EMAIL_REGEX_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$";
    private static final String PASSWORD_REGEX_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";


    @Autowired
    @Qualifier("UserService")
    private UserService userService;

    @Autowired
    @Qualifier("BCryptPasswordEncoder")
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() < 3 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        if (!isValidEmail(user.getEmail())) {
            errors.rejectValue("email", "Bad.userForm.email");

        }
        if (userService.findByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate.userForm.email");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (!isValidPassword(user.getPassword())) {
            errors.rejectValue("password", "Size.userForm.password");
        }
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
