
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayDeque;

import org.junit.Assert;
import org.junit.Test;

public class CalculatorTest {
    @Test
    public void testCompute() {
        Calculator calculator = new Calculator();
        Class<Calculator> clazz = Calculator.class;
        try {
            Method method = clazz.getDeclaredMethod("compute", new Class[]{String.class});
            method.setAccessible(true);
            method.invoke(calculator, new Object[]{"-22 100 2 3 4 + * - + sqrt"});
            ArrayDeque<BigDecimal> stack = calculator.getStack();
            String result = stack.peekFirst().stripTrailingZeros().toPlainString();
            Assert.assertEquals("8", result);

            method.invoke(calculator, new Object[]{"clear"});
            Assert.assertEquals(0, calculator.getStack().size());

            method.invoke(calculator, new Object[]{"5 4 3 2"});
            method.invoke(calculator, new Object[]{"undo undo *"});
            String result2 = stack.peekFirst().stripTrailingZeros().toPlainString();
            Assert.assertEquals("20", result2);
            method.invoke(calculator, new Object[]{"5 *"});
            String result3 = stack.peekFirst().stripTrailingZeros().toPlainString();
            Assert.assertEquals("100", result3);
            method.invoke(calculator, new Object[]{"undo"});
            String result4 = stack.peekFirst().stripTrailingZeros().toPlainString();
            Assert.assertEquals(2, calculator.getStack().size());
            Assert.assertEquals("5", result4);

            method.invoke(calculator, new Object[]{"clear"});
            Assert.assertEquals(0, calculator.getStack().size());

            method.invoke(calculator, new Object[]{"1 2 3 * 5 + * * 6 5"});
            String result5 = stack.peekFirst().stripTrailingZeros().toPlainString();
            Assert.assertEquals("11", result5);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
