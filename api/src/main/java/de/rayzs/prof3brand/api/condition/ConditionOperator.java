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

            return new int[] {index, index + operatorStr.length()};
        }

        return new int[] {-1};
    }

    public ConditionInputType getInputType() {
        return inputType;
    }


    public enum ConditionInputType {
        BOOL, NUM, STR;

        ConditionInputType() {}

        public <T> T validateIfPossible(final Class<T> type, final String str) {
            Object validatedObj = validate(str);

            if (type.isInstance(validatedObj)) {
                return type.cast(validatedObj);
            }

            return null;
        }

        private Object validate(final String str) {
            switch (this) {
                case STR:
                    return str;

                case NUM:
                    try {
                        return Float.parseFloat(str);
                    } catch (final NumberFormatException exception) {
                        return null;
                    }

                case BOOL:
                    return str.equalsIgnoreCase("true") ? Boolean.TRUE
                            : str.equalsIgnoreCase("false") ? Boolean.FALSE
                            : null;
            }

            return null;
        }
    }
}
