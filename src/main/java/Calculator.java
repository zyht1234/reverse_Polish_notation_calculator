import java.math.BigDecimal;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.Function;
import util.Operator;
import util.Operator.OperatorsEnum;
import exception.IllegalParamException;


public class Calculator {

    final private static Logger logger = LoggerFactory.getLogger(Calculator.class);

    private static final int PLACES_OF_STORE_PRECISION = Integer.parseInt(Optional.ofNullable(System.getenv("PLACES_OF_STORE_PRECISION")).orElse("16"));
    private static final int PLACES_OF_DISPLAY_PRECISION = Integer.parseInt(Optional.ofNullable(System.getenv("PLACES_OF_STORE_PRECISION")).orElse("10"));
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    private ArrayDeque<BigDecimal> stack = new ArrayDeque<BigDecimal>();
    private ArrayDeque<BigDecimal> externalStack = new ArrayDeque<BigDecimal>();
    private String lastLabel;

    public ArrayDeque<BigDecimal> getStack() {
        return stack;
    }

    /**
     * 读取外设的输入表达式
     *
     * @return String 输入的字符串信息
     */
    private String readEquation() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    /**
     * 判断操作符是否有足够的操作数在stack中
     *
     * @param operator String 操作符
     * @return boolean 是否有足够的操作数
     */
    private boolean isSufficientParameters(String operator) {
        boolean isSufficient = true;
        if ("+".equals(operator) || "-".equals(operator) || "*".equals(operator) || "/".equals(operator)) {
            if (this.stack.size() < 2) {
                isSufficient = false;
            }
        } else if ("sqrt".equals(operator)) {
            if (this.stack.size() < 1) {
                isSufficient = false;
            }
        }
        return isSufficient;
    }

    /**
     * 对输入的表达式进行遍历、执行相应的计算或操作并存储结算结果
     *
     * @param equation String 输入的表达式
     */
    private void compute(String equation) {
        int position = 1;
        String[] postfix = equation.split(" ");
        OperatorsEnum operatorsEnum;
        for (String str : postfix) {
            operatorsEnum = Operator.getOperatorsEnum(str);
            if (operatorsEnum == null) {
                if (str.matches("-?[0-9]+.*[0-9]*")) {
                    BigDecimal number = new BigDecimal(str).setScale(PLACES_OF_STORE_PRECISION, BigDecimal.ROUND_HALF_UP);
                    stack.push(number);
                    lastLabel = "number";
                } else {
                    logger.warn(String.format("character: %s is neither a number or a supported operator, please check and retype it", str));
                }
            } else {
                if (this.isSufficientParameters(str)) {
                    try {
                        executeCompute(operatorsEnum);
                    } catch (IllegalParamException e) {
                        break;
                    }

                } else {
                    logger.warn(String.format("operator %s (position:%s): insufficient parameters", str, String.valueOf(position)));
                    break;
                }
            }
            position++;
        }
    }

    /**
     * 执行相应的计算或操作，存储计算结果
     *
     * @param operatorsEnum 操作符枚举
     */
    private void executeCompute(OperatorsEnum operatorsEnum) throws IllegalParamException {
        BigDecimal popFirst, popSecond;
        switch (operatorsEnum) {
            case PLUS: {
                popFirst = stack.pop();
                popSecond = stack.pop();
                stack.push(popSecond.add(popFirst).setScale(PLACES_OF_STORE_PRECISION, ROUNDING_MODE));
                externalStack.push(popFirst);
                externalStack.push(popSecond);
                break;
            }
            case MINUS: {
                popFirst = stack.pop();
                popSecond = stack.pop();
                stack.push(popSecond.subtract(popFirst).setScale(PLACES_OF_STORE_PRECISION, ROUNDING_MODE));
                externalStack.push(popFirst);
                externalStack.push(popSecond);
                break;
            }
            case MULTIPLY: {
                popFirst = stack.pop();
                popSecond = stack.pop();
                stack.push(popSecond.multiply(popFirst).setScale(PLACES_OF_STORE_PRECISION, ROUNDING_MODE));
                externalStack.push(popFirst);
                externalStack.push(popSecond);
                break;
            }
            case DIVIDE: {
                popFirst = stack.pop();
                popSecond = stack.pop();
                if (popFirst.compareTo(BigDecimal.ZERO) != 0) {
                    stack.push(popSecond.divide(popFirst, PLACES_OF_STORE_PRECISION, ROUNDING_MODE));
                    externalStack.push(popFirst);
                    externalStack.push(popSecond);
                } else {
                    stack.push(popSecond);
                    stack.push(popFirst);
                    logger.warn("the divisor can not be 0");
                    throw new IllegalParamException("the divisor can not be 0");
                }

                break;
            }
            case SQRT: {
                popFirst = stack.pop();
                try {
                    stack.push(Function.sqrt(popFirst).setScale(PLACES_OF_STORE_PRECISION, ROUNDING_MODE));
                    externalStack.push(popFirst);
                } catch (IllegalParamException e) {
                    stack.push(popFirst);
                    throw e;
                }
                break;
            }
            case UNDO: {
                if ("number".equals(lastLabel) || "undo".equals(lastLabel)) {
                    stack.pop();
                } else if ("sqrt".equals(lastLabel)) {
                    stack.pop();
                    stack.push(externalStack.pop());
                } else if ("clear".equals(lastLabel)) {
                    for (BigDecimal number : externalStack) {
                        stack.add(number);
                    }
                } else {
                    stack.pop();
                    stack.push(externalStack.pop());
                    stack.push(externalStack.pop());
                }
                break;
            }
            case CLEAR: {
                externalStack.clear();
                for (BigDecimal number : stack) {
                    externalStack.add(number);
                }
                stack.clear();
                break;
            }
        }
        lastLabel = operatorsEnum.operator;
    }

    /**
     * 展示当前栈中的所有元素，从栈底到栈顶
     */
    private void displayStack() {
        String output = "stack: ";
        List<String> values = new LinkedList<String>();
        for (BigDecimal number : this.stack) {
            values.add(0, number.setScale(PLACES_OF_DISPLAY_PRECISION, ROUNDING_MODE).stripTrailingZeros().toPlainString());
        }
        for (String value : values) {
            output = output + value + " ";
        }
        System.out.println(output.substring(0, output.length() - 1));
    }

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal("0");
        Calculator calculator = new Calculator();
        logger.info("RPN calculator have been started");
        while (true) {
            System.out.println("Please input your expression, then press Enter to submit!");
            String equation = calculator.readEquation();
            if (equation.equals("end")) {
                break;
            }
            calculator.compute(equation);
            calculator.displayStack();
        }
        logger.info("RPN calculator have been closed");
        System.exit(0);
    }
}
