package socialNetwork.domain.validator;

import socialNetwork.domain.exception.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
