package de.rayzs.prof3brand.api.condition;

import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.ProF3BrandProvider;
import de.rayzs.prof3brand.api.condition.operators.*;
import de.rayzs.prof3brand.api.condition.operators.exceptions.ConditionException;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
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


    private final PlaceholderProvider placeholderProvider;

    private final Map<ConditionOperator, int[]> operatorIndexes = new HashMap<>();
    private final Map<ConditionOperator, String> operatorStrings = new HashMap<>();

    private final ConditionOperator[] operators;
    private final String rawStr;

    public Conditions(final PlaceholderProvider placeholderProvider, final String rawStr) {
        this.placeholderProvider = placeholderProvider;
        this.rawStr = rawStr.replace(" ", "");


        final List<ConditionOperator> foundOperators = new ArrayList<>();
        final String[] split = this.rawStr.split(";");

        for (ConditionOperator operator : OPERATORS) {
            for (final String c : split) {
                final int[] indexes = operator.findOperatorIndex(c);

                if (indexes[0] == -1) continue;


                try {
                    final ConditionOperator conditionOperator = operator.getClass().newInstance();

                    operatorIndexes.put(conditionOperator, indexes);
                    operatorStrings.put(conditionOperator, c);

                    foundOperators.add(conditionOperator);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

        operators = foundOperators.toArray(new ConditionOperator[0]);
    }

    public boolean evaluate(final BrandPlayer player) {
        for (Map.Entry<ConditionOperator, int[]> entry : operatorIndexes.entrySet()) {
            final String str = operatorStrings.get(entry.getKey());
            final ConditionOperator operator = entry.getKey();
            final int[] operatorIndexes = entry.getValue();


            if (str.isEmpty()) continue;


            try {
                if (operatorIndexes[0] == operatorIndexes[1]) {
                    final String rightStr = placeholderProvider.replace(player, str.substring(operatorIndexes[0]));
                    final Object rightObj = operator.getInputType().validateIfPossible(Object.class, rightStr);

                    if (rightObj == null) {
                        ProF3BrandProvider.get().warn("Parameter is not of type " + operator.getInputType().name() + "! (" + rightStr + ", " + str + ")");
                        return false;
                    }

                    if (!operator.evaluate(rightObj)) {
                        return false;
                    }

                } else {

                    final String leftStr = placeholderProvider.replace(player, str.substring(0, operatorIndexes[0]));
                    final Object leftObj = operator.getInputType().validateIfPossible(Object.class, leftStr);

                    final String rightStr = placeholderProvider.replace(player, str.substring(operatorIndexes[1]));
                    final Object rightObj = operator.getInputType().validateIfPossible(Object.class, rightStr);

                    if (leftObj == null) {
                        ProF3BrandProvider.get().warn("Left-sided parameter is not of type " + operator.getInputType().name() + "! (" + leftStr + ", " + str + ")");
                        return false;
                    }

                    if (rightObj == null) {
                        ProF3BrandProvider.get().warn("Right-sided parameter is not of type " + operator.getInputType().name() + "! (" + rightStr + ", " + str + ")");
                        return false;
                    }

                    if (!operator.evaluate(leftObj, rightObj)) {
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
