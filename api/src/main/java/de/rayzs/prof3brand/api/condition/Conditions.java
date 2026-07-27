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
            new NotEqualsOperator(),
            new LowerEqualsOperator(),
            new LowerOperator(),
            //new NotOperator(),
            new OrOperator(),
            new OrOrOperator(),
    };


    private final PlaceholderProvider placeholderProvider;

    private final Map<ConditionOperator, String[]> operatorVariables = new HashMap<>();

    private final ConditionOperator[] operators;
    private final String rawStr;

    public Conditions(final PlaceholderProvider placeholderProvider, final String rawStr) {
        this.placeholderProvider = placeholderProvider;
        this.rawStr = rawStr;


        final List<ConditionOperator> foundOperators = new ArrayList<>();
        final String[] split = this.rawStr.split(",");

        for (final String c : split) {
            for (ConditionOperator operator : OPERATORS) {

                final String[] variables = c.trim().split(" ");
                if (c.isBlank() || variables.length >= 2 && !operator.isOperator(variables[1])) {
                    continue;
                }


                try {
                    final ConditionOperator conditionOperator = operator.getClass().newInstance();

                    operatorVariables.put(conditionOperator, variables.length == 1
                            ? new String[] { variables[0] } : variables.length == 3
                            ? new String[] { variables[0], variables[2] } : variables
                    );

                    foundOperators.add(conditionOperator);

                    // No need for a one-variable condition to be on mapped with every condition-operator.
                    // So we only do it once and switch to the next variable.
                    if (variables.length == 1) {
                        break;
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

        operators = foundOperators.toArray(new ConditionOperator[0]);
    }

    public boolean evaluate(final BrandPlayer player) {
        for (Map.Entry<ConditionOperator, String[]> entry : operatorVariables.entrySet()) {
            final String[] variables = entry.getValue();
            final ConditionOperator operator = entry.getKey();

            if (variables.length == 0) continue;


            try {
                if (variables.length == 1) {
                    final String rightStr = replacePlaceholdersIfPossible(player, variables[0]);

                    if (rightStr.equalsIgnoreCase("false") || rightStr.equalsIgnoreCase("!true")) {
                        return false;
                    }

                    if (rightStr.equalsIgnoreCase("true") || rightStr.equalsIgnoreCase("!false")) {
                        continue;
                    }

                    final Object rightObj = operator.getInputType().validateIfPossible(Object.class, rightStr);

                    if (rightObj == null) {
                        ProF3BrandProvider.get().warn("Parameter is not of type " + operator.getInputType().name() + "! (" + rightStr + ")");
                        return false;
                    }

                    if (!operator.evaluate(rightObj)) {
                        return false;
                    }

                } else {

                    final String leftStr = replacePlaceholdersIfPossible(player, variables[0]);
                    final Object leftObj = operator.getInputType().validateIfPossible(Object.class, leftStr);

                    final String rightStr = replacePlaceholdersIfPossible(player, variables[1]);
                    final Object rightObj = operator.getInputType().validateIfPossible(Object.class, rightStr);

                    if (leftObj == null) {
                        ProF3BrandProvider.get().warn("Left-sided parameter is not of type " + operator.getInputType().name() + "! (" + leftStr + ", " + variables[0] + ")");
                        return false;
                    }

                    if (rightObj == null) {
                        ProF3BrandProvider.get().warn("Right-sided parameter is not of type " + operator.getInputType().name() + "! (" + rightStr + ", " + variables[1] + ")");
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

    private String replacePlaceholdersIfPossible(final BrandPlayer player, final String str) {
        if (str.isEmpty()) return str;

        return str.charAt(0) == '%' || str.charAt(1) == '%' ? this.placeholderProvider.replace(player, str) : str;
    }
}
