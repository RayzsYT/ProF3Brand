package de.rayzs.prof3brand.api.condition.operators.exceptions;

public class InvalidEvaluationException extends ConditionException {

    public InvalidEvaluationException() {
        super("Invalid evaluation!");
    }

    public InvalidEvaluationException(final String message) {
        super("Invalid evaluation! " + message);
    }
}
