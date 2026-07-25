package de.rayzs.prof3brand.api.condition.operators;

import de.rayzs.prof3brand.api.condition.ConditionOperator;
import de.rayzs.prof3brand.api.condition.operators.exceptions.ConditionException;
import de.rayzs.prof3brand.api.condition.operators.exceptions.InvalidEvaluationException;
import de.rayzs.prof3brand.api.condition.operators.exceptions.InvalidParametersException;

public class LowerOperator extends ConditionOperator {

    public LowerOperator() {
        super(ConditionInputType.NUM, ">", "lower than");
    }

    @Override
    public boolean evaluate(final Object objA, final Object objB) throws ConditionException {
        if (! (objA instanceof Number a)) {
            throw new InvalidParametersException("Left-sided parameter is not a number.");
        }

        if (! (objB instanceof Number b)) {
            throw new InvalidParametersException("Right-sided parameter is not a number.");
        }

        return a.doubleValue() < b.doubleValue();
    }

    @Override
    public boolean evaluate(final Object obj) throws ConditionException {
        throw new InvalidEvaluationException();
    }
}
