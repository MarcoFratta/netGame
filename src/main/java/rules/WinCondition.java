package rules;

import core.Field;
@FunctionalInterface
public interface WinCondition {
    boolean test(Field field);
}
