package de.rayzs.prof3brand.common.impl.condition;

import de.rayzs.prof3brand.api.ProF3BrandProvider;
import de.rayzs.prof3brand.api.condition.ConditionOperator;
import de.rayzs.prof3brand.api.condition.Conditions;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.utils.TripleMap;
import de.rayzs.prof3brand.common.impl.condition.operators.*;

public class ImplConditions implements Conditions {


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


    private final TripleMap<ConditionOperator, String[], Object[]> operatorVariables = new TripleMap<>();
    private final PlaceholderProvider placeholderProvider;

    public ImplConditions(final PlaceholderProvider placeholderProvider, final String rawStr) {
        this.placeholderProvider = placeholderProvider;


        final String[] split = rawStr.split(";");

        for (final String c : split) {
            for (ConditionOperator operator : OPERATORS) {

                final String[] variables = c.trim().split(" ", 3);
                if (c.isBlank() || variables.length >= 2 && !operator.isOperator(variables[1])) {
                    continue;
                }


                try {
                    final ConditionOperator conditionOperator = operator.getClass().newInstance();
                    Object fstObj = null, sndObj = null;


                    final String fstStr = variables[0];
                    final String sndStr = variables.length == 3 ? variables[2] : null;

                    if (!fstStr.contains("%")) {
                        fstObj = operator.getInputType().validateIfPossible(Object.class, fstStr);
                    }

                    if (sndStr != null && !sndStr.contains("%")) {
                        sndObj = operator.getInputType().validateIfPossible(Object.class, sndStr);
                    }

                    operatorVariables.putFst(conditionOperator,
                            sndStr == null
                                    ? new String[] { variables[0] }
                                    : new String[] { variables[0], variables[2] }
                    );

                    operatorVariables.putSnd(conditionOperator,
                            sndStr == null
                                    ? new Object[] { fstObj }
                                    : new Object[] { fstObj, sndObj }
                    );

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
    }

    @Override
    public boolean evaluate(final BrandPlayer player) {
        for (ConditionOperator operator : operatorVariables.keySet()) {
            final String[] variables = operatorVariables.getFst(operator);
            final Object[] objects = operatorVariables.getSnd(operator);

            if (variables.length == 0) continue;


            try {
                if (variables.length == 1) {
                    Object rightObj = objects[0];
                    String rightStr = variables[0];

                    if (rightObj == null) {
                        rightStr = replacePlaceholdersIfPossible(player, rightStr);
                        rightObj = operator.getInputType().validateIfPossible(Object.class, rightStr);
                    }


                    if (rightObj == null) {
                        ProF3BrandProvider.get().warn("Parameter is not of type " + operator.getInputType().name() + "! (" + rightStr + ")");
                        return false;
                    }

                    if (rightObj instanceof Boolean b) {
                        if (b) continue;
                        else return false;
                    }

                    if (!operator.evaluate(rightObj)) {
                        return false;
                    }

                } else {
                    Object leftObj = objects[0];
                    String leftStr = variables[0];

                    if (leftObj == null) {
                        leftStr = replacePlaceholdersIfPossible(player, leftStr);
                        leftObj = operator.getInputType().validateIfPossible(Object.class, leftStr);
                    }


                    Object rightObj = objects[1];
                    String rightStr = variables[1];

                    if (rightObj == null) {
                        rightStr = replacePlaceholdersIfPossible(player, rightStr);
                        rightObj = operator.getInputType().validateIfPossible(Object.class, rightStr);
                    }


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
