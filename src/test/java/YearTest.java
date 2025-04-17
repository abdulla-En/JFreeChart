import org.jfree.data.time.Year;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


public class YearTest {
    Year year;

    // (Rule of Test  "Test Method == Test Case")
    // .................I can say here I tested both (DefCons && getYearFunction)...............................//
    private void arrange() {
        year = new Year();
    }

    @Test
    public void testYearDefaultConstructor() {
        arrange();
        assertEquals(2025, year.getYear());
    }


    // ....................................Par_Constructor.......................................................//
    // 1) Normal case
    @Test
    public void testNormalYear() {
        year = new Year(2024);
        assertEquals(2024, year.getYear());

    }

    // 2)Edge cases
    @Test
    public void TestMinValidYear() {
        year = new Year(-9999);
        assertEquals(-9999, year.getYear());

    }

    @Test
    public void TestMaxValidYear() {
        year = new Year(9999);
        assertEquals(9999, year.getYear());

    }

    // 3) Invalid cases
    @Test
    public void TestBelowMinValidYearThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Year(-10000));
    }

    @Test
    public void TestAboveMaxValidYearThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Year(10000));
    }

    // 4)Non_needed(Bad)Test
    @Test
    public void BadTestParConst() {
        assertThrows(NullPointerException.class, () -> new Year(null));
        // int is primitive DataType impossible be null !
        // this is not test of constructor this is result of java conv from int to Integer.
        // compile prevent this from happening .
        // we Do null Test in References Date Types & when method catch an Object Parameter Only.

    }


//................................... test Year(Date time)............................................//

    // Test Normal Date
    @Test
    public void TestConstructor_NormalDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2000,Calendar.MARCH,31);
        Date date = cal.getTime();
         year = new Year(date);
        assertEquals(2000,year.getYear());
    }

    @Test
    public void  TestConst_CurrentDate()
    {
//        Date date = new Date();
//        year = new Year(date);
//        final int fix_Old_way = 1900;
//        assertEquals(date.getYear()+fix_Old_way,year.getYear());

        //OR

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        year = new Year(date);
        assertEquals(cal.get(Calendar.YEAR),year.getYear());
    }

    // Test Edge cases
    @Test
    public void TestConst_FirstDayOfYear()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2000,Calendar.JANUARY,1, 0,0,0);
        cal.set(Calendar.MILLISECOND,0);
        Date date = cal.getTime();
        year = new Year(date);
        assertEquals(2000,year.getYear());

    }
    @Test
    //Calendar.DECEMBER = 11 :: months are 0 based indexing
    public void TestConst_lastDayOfYear(){
        Calendar cal = Calendar.getInstance();
        cal.set(2000,11,31,11,59,59);
        cal.set(Calendar.MILLISECOND,999);
        Date date = cal.getTime();
        year = new Year(date);
        assertEquals(2000,year.getYear());
    }

    // BeforeMilladYears
    @Test
    public void TestConst_BeforeMilladYears(){
        Calendar cal = Calendar.getInstance();
        int YearBeforeMillad= -10;
        cal.set(YearBeforeMillad,Calendar.JANUARY,20);
        Date date = cal.getTime();
        year = new Year(date);
        int expectedYear=Math.abs(YearBeforeMillad)+1;
        assertEquals(expectedYear,year.getYear());
    }

    // Invalid,Null case
    @Test
    public void TestConst_Invalid_NullCase()
    {
        assertThrows(NullPointerException.class,()->new Year((Date) null));
    }

//-------------------------------------------------------------------------------------------------------------//

}


//complete tomorrow inshallah





