package com.kepler.db;

import java.util.Arrays;
import java.util.List;

public class Condition<T> {

    static class Clause{
        List<Clause> clauses;
        List<Condition> conditions;

        UnaryOperator unaryOperator;

        Clause(Clause[] clauses, Condition[] conditions, UnaryOperator operator) {
            this.clauses = Arrays.asList(clauses);
            this.conditions = Arrays.asList(conditions);
            this.unaryOperator = operator;
        }
    }

    enum UnaryOperator{
        AND(" and "), OR(" or "), NULL("null");
        UnaryOperator(String operator){
            this.operator = operator;
        }
        String operator;
    }

    enum Sign{
        EQUAL(" = "), GREATER(" > "), LESS(" < "), LIKE(" ~ "), LIKE_CASE_INSENSITIVE(" ~* ");
        Sign(String signMark){
            this.signMark = signMark;
        }
        String signMark;
    }

    String field;
    T value;
    Sign sign;
    private Condition(String field, T value, Sign sign) {
        this.field = field;
        this.value = value;
        this.sign = sign;
    }

    public static Clause and(Condition... conditions) {
        return new Clause(null, conditions, UnaryOperator.AND);
    }

    public static Clause and(Clause clause, Condition condition) {
        return new Clause(new Clause[]{clause}, new Condition[]{condition}, UnaryOperator.AND);
    }

    public static Clause and(Clause... clauses) {
        return new Clause(clauses, null, UnaryOperator.AND);
    }

    public static Clause or(Condition... conditions) {
        return new Clause(null, conditions, UnaryOperator.OR);
    }

    public static Clause or(Clause clause, Condition condition) {
        return new Clause(new Clause[]{clause}, new Condition[]{condition}, UnaryOperator.OR);
    }

    public static Clause or(Clause... clauses) {
        return new Clause(clauses, null, UnaryOperator.OR);
    }

    public static Condition equals(String column, String value) {
        return new Condition<>(column, value, Sign.EQUAL);
    }

    public static Condition equals(String column, int value) {
        return new Condition<>(column, value, Sign.EQUAL);
    }

    public static Condition greater(String column, int value) {
        return new Condition<>(column, value, Sign.GREATER);
    }

    public static Condition lesser(String column, int value) {
        return new Condition<>(column, value, Sign.LESS);
    }

    public static Condition equals(String column, boolean value) {
        return new Condition<>(column, value, Sign.EQUAL);
    }

    public static Condition like(String column, String regex, boolean caseInsensetive) {
        return new Condition<>(column, regex, caseInsensetive ? Sign.LIKE_CASE_INSENSITIVE : Sign.LIKE);
    }

}
