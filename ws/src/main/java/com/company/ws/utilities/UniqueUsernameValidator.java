package com.company.ws.utilities;

import com.company.ws.entity.User;
import com.company.ws.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserService userService;


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            User user = userService.findByUsername(s);
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
