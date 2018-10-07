package util;

import exception.IllegalParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;

public class Function {

    final private static Logger logger = LoggerFactory.getLogger(Function.class);

    public static BigDecimal sqrt(BigDecimal num) throws IllegalParamException {
        if (num.compareTo(BigDecimal.ZERO) < 0) {
            logger.warn(String.format("there are no real square root of negative number: %s ", num.stripTrailingZeros().toPlainString()));
            throw new IllegalParamException("there are no real square root of negative number");
        } else if (num.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal x = num.divide(new BigDecimal("2"), MathContext.DECIMAL128);
        while (x.subtract(x = sqrtIteration(x, num)).abs().compareTo(new BigDecimal("0.0000000000000000000001")) > 0) ;
        return x;
    }

    private static BigDecimal sqrtIteration(BigDecimal x, BigDecimal n) {
        return x.add(n.divide(x, MathContext.DECIMAL128)).divide(new BigDecimal("2"), MathContext.DECIMAL128);
    }

}
