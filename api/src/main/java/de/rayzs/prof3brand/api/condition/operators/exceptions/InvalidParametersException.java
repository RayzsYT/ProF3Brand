package de.rayzs.prof3brand.api.condition.operators.exceptions;

import java.security.InvalidParameterException;

public class InvalidParametersException extends ConditionException {

    public InvalidParametersException() {
        super("Invalid parameters!");
    }

    public InvalidParametersException(final String message) {
        super("Invalid parameters! " + message);
    }
}
