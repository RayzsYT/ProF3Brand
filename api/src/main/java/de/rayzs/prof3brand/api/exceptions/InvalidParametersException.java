package de.rayzs.prof3brand.api.exceptions;

public class InvalidParametersException extends ConditionException {

    public InvalidParametersException() {
        super("Invalid parameters!");
    }

    public InvalidParametersException(final String message) {
        super("Invalid parameters! " + message);
    }
}
