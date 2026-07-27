package de.rayzs.prof3brand.api.condition;

import de.rayzs.prof3brand.api.exceptions.ConditionException;

public abstract class ConditionOperator {

    private final String[] operatorStrs;
    private final ConditionInputType inputType;

    protected ConditionOperator(final ConditionInputType inputType, final String... operatorStrs) {
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


    public boolean isOperator(final String str) {
        for (String operatorStr : this.operatorStrs) {
            if (str.equalsIgnoreCase(operatorStr)) {
                return true;
            }
        }

        return false;
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

        private Object validate(String str) {
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
                    final boolean negated = str.charAt(0) == '!';
                    str = negated ? str.substring(1) : str;

                    final boolean isTrue = str.equalsIgnoreCase("true");
                    if (!isTrue && !str.equalsIgnoreCase("false")) {
                        return null;
                    }

                    return negated != isTrue;
            }

            return null;
        }
    }
}
