package de.rayzs.prof3brand.api.condition;

import de.rayzs.prof3brand.api.condition.operators.exceptions.ConditionException;
import de.rayzs.prof3brand.api.utils.StringUtils;

public abstract class ConditionOperator {

    private final String[] operatorStrs;
    private final ConditionInputType inputType;

    public ConditionOperator(final ConditionInputType inputType, final String... operatorStrs) {
        this.inputType = inputType;
        this.operatorStrs = operatorStrs;
    }


    public abstract boolean evaluate(
            final Object obj
    ) throws ConditionException;

    public abstract boolean evaluate(
            final Object objA,
            final Object objB
    ) throws ConditionException;


    public int[] findOperatorIndex(final String str) {
        for (String operatorStr : this.operatorStrs) {
            final int index = StringUtils.searchIndex(operatorStr, str);
            if (index == -1) continue;

            return new int[] {index, index + operatorStr.length() - 1};
        }

        return new int[] {-1};
    }

    public ConditionInputType getInputType() {
        return inputType;
    }


    public enum ConditionInputType {
        BOOL(Boolean.class),
        NUM(Number.class),
        STR(String.class),

        ALL();



        private final Class<?>[] clazzes;

        ConditionInputType(final Class<?>... clazzes) {
            this.clazzes = clazzes;
        }

        public boolean isValid(final Class<?> clazz) {
            if (clazzes.length == 0) return true;

            for (Class<?> aClass : clazzes) {
                if (clazz.isAssignableFrom(aClass)) {
                    return true;
                }
            }

            return false;
        }
    }
}
