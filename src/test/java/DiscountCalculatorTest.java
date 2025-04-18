import static org.junit.Assert.*;
import com.Jfree.DiscountCalculator;
import org.jfree.data.time.Week;
import org.junit.Test;
import java.util.Calendar;

public class DiscountCalculatorTest {

    // 1-Test Case for Special Week
    // --- testing (isTheSpecialWeek())
    @Test
    public void testTrueSpecialWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 23);
        Week week = new Week(calendar.getTime());
        DiscountCalculator calculator = new DiscountCalculator(week);
        // week 26
        assertTrue(calculator.isTheSpecialWeek());
    }
    // 2-not special week
    @Test
    public void testFalseSpecialWeek() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 3); // Week 25
        Week week = new Week(calendar.getTime());
        DiscountCalculator calculator = new DiscountCalculator(week);
        // week 25
        assertFalse(calculator.isTheSpecialWeek());
    }

    // 3-Test Cases for Percentage ---- (even=7 , odd=5)
    // ---- testing (getDiscountPercentage())
    @Test
    public void testEvenWeeks() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 23);
        Week week = new Week(calendar.getTime());
        DiscountCalculator calculator = new DiscountCalculator(week);
        // week 26
        assertEquals(7, calculator.getDiscountPercentage());
    }

    @Test
    public void testOddWeek() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JUNE, 17);
        Week week = new Week(calendar.getTime());
        DiscountCalculator calculator = new DiscountCalculator(week);
        // week 25
        assertEquals(5, calculator.getDiscountPercentage());
    }

    // 5-Edge Cases test for first and last day in the year
    // ---- testing (getDiscountPercentage())
    @Test
    public void testFirstDay_OfTheYear() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JANUARY, 1); // Week 1
        Week week = new Week(calendar.getTime());
        DiscountCalculator calculator = new DiscountCalculator(week);

        assertEquals(5, calculator.getDiscountPercentage());
    }

    @Test
    public void testLastDay_OfTheYear() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.DECEMBER, 31);
        Week week = new Week(calendar.getTime());
        DiscountCalculator calculator = new DiscountCalculator(week);

        assertEquals(5, calculator.getDiscountPercentage());
    }
    //7-Test leap year
    @Test
    public void testLeapYearWeek() {
        // 2024 is a leap year
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.FEBRUARY, 29);
        Week week = new Week(cal.getTime());
        DiscountCalculator calc = new DiscountCalculator(week);
        assertEquals(5, calc.getDiscountPercentage());
    }

}
