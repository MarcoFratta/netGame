package rules;

import core.Field;

import java.util.List;

@FunctionalInterface
public interface WinCondition {
    boolean test(Field field);
}
