package com.codebase.socialnetwork.domain.validator;

import com.codebase.socialnetwork.domain.Event;
import com.codebase.socialnetwork.domain.exception.ValidationException;

import java.util.Objects;

public class EventValidator implements Validator<Event>
{
    @Override
    public void validate(Event entity) throws ValidationException {
        String errors = "";

        if(Objects.equals(entity.getNameEvent(), ""))
            errors += "Name field can't be empty\n";
        if(Objects.equals(entity.getLocation(), ""))
            errors += "Location field can't be empty\n";
        if(Objects.equals(entity.getHost(), ""))
            errors += "Host field can't be empty\n";
        if(entity.getImageStream() == null)
            errors += "You need to upload an image\n";
        if(Objects.equals(entity.getDescription(), ""))
            errors += "You need to give your event a description\n";
        if(entity.getStartingDate() == null || entity.getEndingDate() == null)
            errors += "Pick a valid date\n";
        else if(entity.getStartingDate().compareTo(entity.getEndingDate()) > 0)
            errors += "Pick a valid time interval\n";


        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
