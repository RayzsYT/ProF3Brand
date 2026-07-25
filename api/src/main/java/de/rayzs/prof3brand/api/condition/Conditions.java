package de.rayzs.prof3brand.api.condition;

import de.rayzs.prof3brand.api.condition.operators.*;
import de.rayzs.prof3brand.api.condition.operators.exceptions.ConditionException;
import de.rayzs.prof3brand.api.player.BrandPlayer;

import java.util.*;

public class Conditions {


    private static final ConditionOperator[] OPERATORS = new ConditionOperator[] {
            new AndAndOperator(),
            new AndOperator(),
            new BiggerEqualsOperator(),
            new BiggerOperator(),
            new EqualsOperator(),
            new LowerEqualsOperator(),
            new LowerOperator(),
            new NotOperator(),
            new OrOperator(),
            new OrOrOperator(),
    };


    private final Map<ConditionOperator, int[]> operatorIndexes = new HashMap<>();
    private final Map<ConditionOperator, String> operatorStrings = new HashMap<>();

    private final ConditionOperator[] operators;
    private final String rawStr;

    public Conditions(final String rawStr) {
        this.rawStr = rawStr.replace(" ", "");


        final List<ConditionOperator> foundOperators = new ArrayList<>();
        final String[] split = this.rawStr.split(";");

        for (ConditionOperator operator : OPERATORS) {
            for (final String c : split) {
                final int[] indexes = operator.findOperatorIndex(c);

                operatorIndexes.put(operator, indexes);
                operatorStrings.put(operator, c);

                foundOperators.add(operator);
            }
        }

        operators = foundOperators.toArray(new ConditionOperator[0]);
    }

    public boolean evaluate(final BrandPlayer player) {
        for (Map.Entry<ConditionOperator, int[]> entry : operatorIndexes.entrySet()) {
            final String str = operatorStrings.get(entry.getKey());

            final ConditionOperator operator = entry.getKey();
            final int[] operatorIndexes = entry.getValue();


            try {
                if (operatorIndexes[0] == operatorIndexes[1]) {
                    final String right = str.substring(operatorIndexes[0]);

                    if (!operator.evaluate(right)) {
                        return false;
                    }

                } else {
                    final String left = str.substring(0, operatorIndexes[0]);
                    final String right = str.substring(operatorIndexes[1]);

                    if (!operator.evaluate(left, right)) {
                        return false;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }
}
