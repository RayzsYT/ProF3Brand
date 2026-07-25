package de.rayzs.prof3brand.api.condition.operators;

import de.rayzs.prof3brand.api.condition.ConditionOperator;
import de.rayzs.prof3brand.api.condition.operators.exceptions.ConditionException;
import de.rayzs.prof3brand.api.condition.operators.exceptions.InvalidEvaluationException;
import de.rayzs.prof3brand.api.condition.operators.exceptions.InvalidParametersException;

public class AndAndOperator extends ConditionOperator {

    public AndAndOperator() {
        super(ConditionInputType.BOOL, "&&", "and");
    }

    @Override
    public boolean evaluate(final Object objA, final Object objB) throws ConditionException {
        if (! (objA instanceof Boolean a)) {
            throw new InvalidParametersException("Left-sided parameter is not a Boolean.");
        }

        if (!a) return false;

        if (! (objB instanceof Boolean b)) {
            throw new InvalidParametersException("Right-sided parameter is not a Boolean.");
        }

        return b;
    }

    @Override
    public boolean evaluate(final Object obj) throws ConditionException {
        throw new InvalidEvaluationException();
    }
}
