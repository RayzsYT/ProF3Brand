package de.rayzs.prof3brand.common.impl.condition.operators;

import de.rayzs.prof3brand.api.condition.ConditionOperator;
import de.rayzs.prof3brand.api.exceptions.ConditionException;
import de.rayzs.prof3brand.api.exceptions.InvalidEvaluationException;
import de.rayzs.prof3brand.api.exceptions.InvalidParametersException;

// Not implemented for now, since there's no need for it as it turns out.
public class NotOperator extends ConditionOperator {

    public NotOperator() {
        super(ConditionInputType.BOOL, "!", "not");
    }

    @Override
    public boolean evaluate(final Object obj) throws ConditionException {
        if (! (obj instanceof Boolean b)) {
            throw new InvalidParametersException("Parameter is not a Boolean.");
        }

        return !b;
    }

    @Override
    public boolean evaluate(final Object objA, final Object objB) throws ConditionException {
        throw new InvalidEvaluationException();
    }
}
