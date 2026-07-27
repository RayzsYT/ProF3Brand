package de.rayzs.prof3brand.common.impl.condition.operators;

import de.rayzs.prof3brand.api.condition.ConditionOperator;
import de.rayzs.prof3brand.api.exceptions.ConditionException;
import de.rayzs.prof3brand.api.exceptions.InvalidEvaluationException;

import java.util.Objects;

public class EqualsOperator extends ConditionOperator {

    public EqualsOperator() {
        super(ConditionInputType.STR, "==", "equals", "is", "like");
    }

    @Override
    public boolean evaluate(final Object objA, final Object objB) throws ConditionException {
        if (objA instanceof String a && objB instanceof String b) {
            return a.equalsIgnoreCase(b);
        }

        return Objects.equals(objA, objB);
    }

    @Override
    public boolean evaluate(final Object obj) throws ConditionException {
        throw new InvalidEvaluationException();
    }
}
