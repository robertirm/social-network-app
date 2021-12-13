package com.codebase.socialnetwork.domain.validator;

import com.codebase.socialnetwork.domain.User;
import com.codebase.socialnetwork.domain.exception.ValidationException;

import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {
        String errorMessage = "";
        if (entity.getFirstName() == null || "".equals(entity.getFirstName()))
            errorMessage += "First name can't be empty. \n";
        if (entity.getLastName() == null || "".equals(entity.getLastName()))
            errorMessage +="Last name can't be empty. \n";
        if (entity.getUsername() == null || "".equals(entity.getUsername()))
            errorMessage +="Username can't be empty. \n";
        if(!Pattern.matches("[a-zA-Z]+",entity.getFirstName()))
            errorMessage += "First name can't contain numbers. \n";
        if(!Pattern.matches("[a-zA-Z]+",entity.getLastName()))
            errorMessage += "Last name can't contain numbers. \n";
        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}
