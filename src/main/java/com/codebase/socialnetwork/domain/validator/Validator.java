package com.codebase.socialnetwork.domain.validator;

import com.codebase.socialnetwork.domain.exception.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
