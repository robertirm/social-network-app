package socialNetwork.domain.validator;

import socialNetwork.domain.Friendship;
import socialNetwork.domain.exception.ValidationException;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errorMessage = "";
        if (entity.getId().getLeft() == null)
            errorMessage += "id first user error ";
        if (entity.getId().getRight() == null)
            errorMessage += "id second user error ";
        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}
