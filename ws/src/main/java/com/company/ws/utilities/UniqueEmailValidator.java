package com.company.ws.utilities;

import com.company.ws.entity.User;
import com.company.ws.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            User user = userService.findByEmail(s);
            return false;
        } catch (Exception e) {
            return true;
        }

    }
}
