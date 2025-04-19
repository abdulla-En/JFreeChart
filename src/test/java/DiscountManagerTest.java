import com.Jfree.DiscountManager;
import com.Jfree.IDiscountCalculator;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountManagerTest {

    @Test
    public void testCalculatePriceWhenDiscountsSeasonIsFalse() throws Exception {
        // Arrange
        boolean isDiscountsSeason = false;
        double originalPrice = 100.0;
        double expectedPrice = 100.0;

        Mockery mockingContext = new Mockery();
        IDiscountCalculator mockedDependency = mockingContext.mock(IDiscountCalculator.class);
        mockingContext.checking(new Expectations(){
            {
                // make sure that none of the functions are called
                never(mockedDependency).getDiscountPercentage();
                never(mockedDependency).isTheSpecialWeek();
            }
        });
        DiscountManager discountManager = new DiscountManager(isDiscountsSeason, mockedDependency);
        // Act
        double randomPrice = ThreadLocalRandom.current().nextDouble(-Double.MAX_VALUE , Double.MAX_VALUE);
        double randomPriceAfterCalculation = discountManager.calculatePriceAfterDiscount(randomPrice);


        assertEquals(randomPrice , randomPriceAfterCalculation);
        // Assert
        // make sure that mocking Expectations Is Satisfied
        // make sure that the actual value exactly equals the expected value
    }

    @Test
    public void testCalculatePriceWhenDiscountsSeasonIsTrueAndSpecialWeekIsTrue() throws Exception {
        // Arrange
        boolean isDiscountsSeason = true;
        double originalPrice = 200.0;
        double expectedPrice = originalPrice * 0.8; // 20% discount

        Mockery mockingContext = new Mockery();
        IDiscountCalculator mockedDependency = mockingContext.mock(IDiscountCalculator.class);

        mockingContext.checking(new Expectations() {
            {
                oneOf(mockedDependency).isTheSpecialWeek();
                will(returnValue(true));
                never(mockedDependency).getDiscountPercentage();
            }
        });

        DiscountManager discountManager = new DiscountManager(isDiscountsSeason, mockedDependency);

        // Act
        double actualPrice = discountManager.calculatePriceAfterDiscount(originalPrice);

        // Assert
        assertEquals(expectedPrice, actualPrice);
        mockingContext.assertIsSatisfied();
    }

    @Test
    public void testCalculatePriceWhenDiscountsSeasonIsTrueAndSpecialWeekIsFalse_EvenWeek() throws Exception {
        // Arrange
        boolean isDiscountsSeason = true;
        double originalPrice = 150.0;
        double expectedPrice = originalPrice * 7; // ازاي خصم بيضرب السعر في سبعه؟

        Mockery mockingContext = new Mockery();
        IDiscountCalculator mockedDependency = mockingContext.mock(IDiscountCalculator.class);

        mockingContext.checking(new Expectations() {
            {
                oneOf(mockedDependency).isTheSpecialWeek();
                will(returnValue(false));

                oneOf(mockedDependency).getDiscountPercentage();
                will(returnValue(7));
            }
        });

        DiscountManager discountManager = new DiscountManager(isDiscountsSeason, mockedDependency);

        // Act
        double actualPrice = discountManager.calculatePriceAfterDiscount(originalPrice);

        // Assert
        assertEquals(expectedPrice, actualPrice);
        mockingContext.assertIsSatisfied();
    }


    @Test
    public void testCalculatePriceWhenDiscountsSeasonIsTrueAndSpecialWeekIsFalse_OddWeek() throws Exception {
        // Arrange
        boolean isDiscountsSeason = true;
        double originalPrice = 120.0;
        double expectedPrice = originalPrice * 5; // ؟؟

        Mockery mockingContext = new Mockery();
        IDiscountCalculator mockedDependency = mockingContext.mock(IDiscountCalculator.class);

        mockingContext.checking(new Expectations() {
            {
                oneOf(mockedDependency).isTheSpecialWeek();
                will(returnValue(false));

                oneOf(mockedDependency).getDiscountPercentage();
                will(returnValue(5));
            }
        });

        DiscountManager discountManager = new DiscountManager(isDiscountsSeason, mockedDependency);

        // Act
        double actualPrice = discountManager.calculatePriceAfterDiscount(originalPrice);

        // Assert
        assertEquals(expectedPrice, actualPrice);
        mockingContext.assertIsSatisfied();
    }

    // test missing cases
}
