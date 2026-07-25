package de.rayzs.prof3brand.api.condition.operators;

import de.rayzs.prof3brand.api.condition.ConditionOperator;
import de.rayzs.prof3brand.api.condition.operators.exceptions.ConditionException;
import de.rayzs.prof3brand.api.condition.operators.exceptions.InvalidEvaluationException;

import java.util.Objects;

public class EqualsOperator extends ConditionOperator {

    public EqualsOperator() {
        super(ConditionInputType.ALL, "==", "equals", "is", "like");
    }

    @Override
    public boolean evaluate(final Object objA, final Object objB) throws ConditionException {
        return Objects.equals(objA, objB);
    }

    @Override
    public boolean evaluate(final Object obj) throws ConditionException {
        throw new InvalidEvaluationException();
    }
}
