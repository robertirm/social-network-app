package socialNetwork.domain.validator;

import socialNetwork.domain.User;
import socialNetwork.domain.exception.ValidationException;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {
        String errorMessage = "";
//        if (entity.getId() == null)
//            errorMessage += "id error ";
        if (entity.getFirstName() == null || "".equals(entity.getFirstName()))
            errorMessage += "first name error ";
        if (entity.getLastName() == null || "".equals(entity.getLastName()))
            errorMessage +="last name error ";
        if (entity.getUsername() == null || "".equals(entity.getUsername()))
            errorMessage +="username error ";
        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}
