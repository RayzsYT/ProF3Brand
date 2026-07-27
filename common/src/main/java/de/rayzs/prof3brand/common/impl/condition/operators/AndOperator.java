package de.rayzs.prof3brand.common.impl.condition.operators;

import de.rayzs.prof3brand.api.condition.ConditionOperator;
import de.rayzs.prof3brand.api.exceptions.ConditionException;
import de.rayzs.prof3brand.api.exceptions.InvalidEvaluationException;
import de.rayzs.prof3brand.api.exceptions.InvalidParametersException;

public class AndOperator extends ConditionOperator {

    public AndOperator() {
        super(ConditionInputType.BOOL,"&");
    }

    @Override
    public boolean evaluate(final Object objA, final Object objB) throws ConditionException {
        if (! (objA instanceof Boolean a)) {
            throw new InvalidParametersException("Left-sided parameter is not a Boolean.");
        }

        if (! (objB instanceof Boolean b)) {
            throw new InvalidParametersException("Right-sided parameter is not a Boolean.");
        }

        return a & b;
    }

    @Override
    public boolean evaluate(final Object obj) throws ConditionException {
        throw new InvalidEvaluationException();
    }
}
