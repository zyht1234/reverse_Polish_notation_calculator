package util;

public class Operator {
    public enum OperatorsEnum {
        PLUS("+"), MINUS("-"), MULTIPLY("*"), DIVIDE("/"), SQRT("sqrt"), UNDO("undo"), CLEAR("clear");

        public String operator; //操作符

        private OperatorsEnum(String operator) {
            this.operator = operator;
        }

        public String toString() {
            return String.valueOf(operator);
        }
    }

    public static OperatorsEnum getOperatorsEnum(String str) {
        for (OperatorsEnum op : OperatorsEnum.values()) {
            if (str.equals(op.operator)) {
                return op;
            }
        }
        return null;
    }
}
