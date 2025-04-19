import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimePeriodFormatException;
import org.jfree.data.time.Year;
import org.junit.Test;

import java.sql.Time;
import java.util.*;

import static org.junit.Assert.*;


public class YearTest {
    Year year;

    // (Rule of Test  "Test Method == Test Case")


    // constructors Tests ********************************************************************************************
    //****************************************************************************************************************

    // ....................................Def_Constructor.......................................................//


    private void arrange() {
        year = new Year();
    }

    @Test
    public void testYearDefaultConstructor() {
        arrange();
        assertEquals(2025, year.getYear());  // here we tested getYear Method too !
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

    /* 1) Test Normal Case */
    @Test
    public void TestConstructor_NormalDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.MARCH, 31);
        Date date = cal.getTime();
        year = new Year(date);
        assertEquals(2000, year.getYear());
    }

    @Test
    public void TestConst_CurrentDate() {
//        Date date = new Date();
//        year = new Year(date);
//        final int fix_Old_way = 1900;
//        assertEquals(date.getYear()+fix_Old_way,year.getYear());

        //OR

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        year = new Year(date);
        assertEquals(cal.get(Calendar.YEAR), year.getYear());
    }

    /* 2) Test Edge cases */
    @Test
    public void TestConst_FirstDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        year = new Year(date);
        assertEquals(2000, year.getYear());

    }

    @Test
    public void TestConst_lastDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 11, 31, 23, 59, 59);  //Calendar.DECEMBER = 11 :: months are 0 based indexing
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();
        year = new Year(date);
        assertEquals(2000, year.getYear());
    }

    /* 3) special Years non-usual used
     BC years
     LeapYear
     */

    @Test
    public void TestConst_BCYears() {
        Calendar cal = Calendar.getInstance();
        int YearBeforeMillad = -10;
        cal.set(YearBeforeMillad, Calendar.JANUARY, 20);
        Date date = cal.getTime();
        year = new Year(date);
        int expectedYear = Math.abs(YearBeforeMillad) + 1;
        assertEquals(expectedYear, year.getYear());
    }


    @Test
    public void TestConst_LeapYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.FEBRUARY, 29);
        Date date = cal.getTime();
        year = new Year(date);
        assertEquals(2000, year.getYear());
    }


    /* 4) Invalid,Null case */
    @Test
    public void TestConst_Invalid_NullCase() {
        assertThrows(NullPointerException.class, () -> new Year((Date) null));
    }

//--------------------------------------------Year(Date,TimeZone,Locale)----------------------------------------------//


    //  I will make a test discuss How does the Timezone affect  Date and therefore the year.
    // locale doesn't affect just change the format of Date.



    /* 1) Normal TimeZone Effect
        Different time zone + (Boundary) Date     , this is the only case the TimeZone affect the Date
        and
        ensure that locale has no effect*/

    @Test
    public void TestNormalTimeZoneEffectOnLastDayOfYear() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")); // DO IT Bec : default timezone of Eg is (UTC+2 = GMT+2)
        cal.set(2000, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();
        TimeZone timezone = TimeZone.getTimeZone("GMT+2");
        Locale locale = Locale.getDefault();
        year = new Year(date, timezone, locale);

        assertEquals(2001, year.getYear());

    }

    @Test
    public void TestNormalTimeZoneEffectOnFirstDay() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        TimeZone timezone = TimeZone.getTimeZone("Pacific/Honolulu"); // UTC-9
        Locale locale = Locale.CANADA;
        year = new Year(date, timezone, locale);

        assertEquals(1999, year.getYear());

    }

    @Test
    public void Test_EnsureLocaleIsDoesNotMatter() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        TimeZone timezone = TimeZone.getTimeZone("Pacific/Honolulu");
        Locale locale = Locale.CHINESE;
        year = new Year(date, timezone, locale);

        assertEquals(1999, year.getYear());

    }

    /* 2) Test_non_effect_TimeZone
         Same Zone & (Boundary)
         ,
         Different But (NonBoundary)
         Same Zone And (NonBoundary)
         */

    @Test
    public void Test_non_effect_TimeZoneEffect_Different_NonBoundaryDat() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2004, Calendar.APRIL, 19);
        Date date = cal.getTime();
        TimeZone timezone = TimeZone.getTimeZone("Asia,Tokyo");
        Locale locale = Locale.getDefault();
        year = new Year(date, timezone, locale);

        assertEquals(2004, year.getYear());

    }

    @Test
    public void Test_non_effect_TimeZoneEffect_Same_NonBoundaryDat() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Gmt+2"));
        cal.set(2004, Calendar.FEBRUARY, 29);
        Date date = cal.getTime();
        TimeZone timezone = TimeZone.getTimeZone("Africa,Cairo");
        Locale locale = Locale.getDefault();
        year = new Year(date, timezone, locale);

        assertEquals(2004, year.getYear());

    }

    @Test
    public void Test_non_effect_TimeZoneEffect_Same_Boundary() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2000, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();
        TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
        Locale locale = Locale.getDefault();
        year = new Year(date, timezone, locale);

        assertEquals(2000, year.getYear());

    }

    /* 3) invalid Cases (NullCases) */
    @Test
    public void TestConst_NullDate() {
        TimeZone timezone = TimeZone.getDefault();
        Locale locale = Locale.getDefault();
        assertThrows(NullPointerException.class, () -> new Year(null, timezone, locale));

    }

    @Test
    public void TestConst_NullTimeZone() {
        Date date = new Date();
        Locale locale = Locale.getDefault();
        assertThrows(NullPointerException.class, () -> new Year(date, null, locale));

    }

    @Test
    public void TestConst_NullLocale() {
        Date date = new Date();
        TimeZone timezone = TimeZone.getDefault();
        assertThrows(NullPointerException.class, () -> new Year(date, timezone, null));

    }

    @Test
    public void TestConst_allNull() {
        assertThrows(NullPointerException.class, () -> new Year(null, null, null));
    }


// finish all constructors Tests *********************************************************************************
//****************************************************************************************************************


// Start Method Testing *******************************************************************************************
//*****************************************************************************************************************

// .........................................Peg(Calender) Method.....................................................//
//There fore we test in this Section to: getFirstMilliSec ,getFirstMilliSec(Cal) ,getLastMilliSec ,getLastMilliSec(Cal)

// 1) ValidCases


    @Test
    public void Test_PegNormalTimeZoneCase() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        year = new Year(2020);
        year.peg(cal);

        long expectedFirstMilliSec = year.getFirstMillisecond(cal);
        long expectedLastMilliSec = year.getLastMillisecond(cal);

        long actualFirstMilliSec = year.getFirstMillisecond();
        long actualLastMilliSec = year.getLastMillisecond();

        assertEquals(expectedFirstMilliSec,actualFirstMilliSec);
        assertEquals(expectedLastMilliSec,actualLastMilliSec);

    }

    @Test
    public void Test_Peg_DST_TimZoneCase() {   // التوقيت الصيفي
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
        cal.set(2020,Calendar.MARCH,29,0,0,0);
        year = new Year(cal.getTime(),cal.getTimeZone(),Locale.getDefault());
        year.peg(cal);

        long expectedFirstMilliSec = year.getFirstMillisecond(cal);
        long expectedLastMilliSec = year.getLastMillisecond(cal);

        long actualFirstMilliSec = year.getFirstMillisecond();
        long actualLastMilliSec = year.getLastMillisecond();

        assertEquals(expectedFirstMilliSec,actualFirstMilliSec);
        assertEquals(expectedLastMilliSec,actualLastMilliSec);

    }

    @Test
    public void Test_PegVeryLateTimeZoneCase() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Pacific/Honolulu"));
        year = new Year(2025);
        year.peg(cal);

        long expectedFirstMilliSec = year.getFirstMillisecond(cal);
        long expectedLastMilliSec = year.getLastMillisecond(cal);

        long actualFirstMilliSec = year.getFirstMillisecond();
        long actualLastMilliSec = year.getLastMillisecond();

        assertEquals(expectedFirstMilliSec,actualFirstMilliSec);
        assertEquals(expectedLastMilliSec,actualLastMilliSec);

    }

    @Test
    public void Test_PegVeryEarlyTimeZoneCase() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        year = new Year(2025);
        year.peg(cal);

        long expectedFirstMilliSec = year.getFirstMillisecond(cal);
        long expectedLastMilliSec = year.getLastMillisecond(cal);

        long actualFirstMilliSec = year.getFirstMillisecond();
        long actualLastMilliSec = year.getLastMillisecond();

        assertEquals(expectedFirstMilliSec,actualFirstMilliSec);
        assertEquals(expectedLastMilliSec,actualLastMilliSec);

    }

    @Test
    public void Test_PegForMaxBoundaryYear() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        year = new Year(9999);
        year.peg(cal);

        long expectedFirstMilliSec = year.getFirstMillisecond(cal);
        long expectedLastMilliSec = year.getLastMillisecond(cal);

        long actualFirstMilliSec = year.getFirstMillisecond();
        long actualLastMilliSec = year.getLastMillisecond();

        assertEquals(expectedFirstMilliSec,actualFirstMilliSec);
        assertEquals(expectedLastMilliSec,actualLastMilliSec);

    }

    @Test
    public void Test_PegForLowBoundaryYear() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        year = new Year(-9999);
        year.peg(cal);

        long expectedFirstMilliSec = year.getFirstMillisecond(cal);
        long expectedLastMilliSec = year.getLastMillisecond(cal);

        long actualFirstMilliSec = year.getFirstMillisecond();
        long actualLastMilliSec = year.getLastMillisecond();

        assertEquals(expectedFirstMilliSec,actualFirstMilliSec);
        assertEquals(expectedLastMilliSec,actualLastMilliSec);

    }

    @Test
    public void Test_PegForBCYear() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int BC_year = -2;

        cal.set(BC_year,Calendar.MARCH,20);
        year = new Year(cal.getTime(),cal.getTimeZone(),Locale.getDefault());
        year.peg(cal);

        long expectedFirstMilliSec = year.getFirstMillisecond(cal);
        long expectedLastMilliSec = year.getLastMillisecond(cal);

        long actualFirstMilliSec = year.getFirstMillisecond();
        long actualLastMilliSec = year.getLastMillisecond();


        assertEquals(Math.abs(BC_year)+1,year.getYear()); // 3 BC
        assertEquals(expectedFirstMilliSec,actualFirstMilliSec);
        assertEquals(expectedLastMilliSec,actualLastMilliSec);

    }

    @Test
    public void Test_PegForLeapYear() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2020,Calendar.FEBRUARY,29);
        year = new Year(cal.getTime(),cal.getTimeZone(),Locale.getDefault());
        year.peg(cal);

        long expectedFirstMilliSec = year.getFirstMillisecond(cal);
        long expectedLastMilliSec = year.getLastMillisecond(cal);

        long actualFirstMilliSec = year.getFirstMillisecond();
        long actualLastMilliSec = year.getLastMillisecond();

        assertEquals(expectedFirstMilliSec,actualFirstMilliSec);
        assertEquals(expectedLastMilliSec,actualLastMilliSec);

    }

    // invalid Cases
    @Test
    public void Test_PegForNull_calenderParThrowException() {
        assertThrows(NullPointerException.class,()->new Year(2020).peg(null));
    }


// .........................................SerialIndex Method.....................................................//

    @Test  // just a getter without any logic or calculations
    public void TestSerialIndexMethod()
    {
        year = new Year(2020);
        assertEquals(2020L,year.getSerialIndex());

        // if I want to teat all possible cases and un_possible I can add it to last test cases like year.getYear();
    }

// ..............................................next() Method.....................................................//

    // valid :
    @Test
    public void TestNormalYearNext()
    {
       Year nextYear = (Year) new Year(2025).next(); // Down casting bec next is RegularTimePeriod Type
        assertEquals(2026,nextYear.getYear());
    }

    @Test
    public void TestBelowTheBoundaryYearNext()
    {
        Year nextYear = (Year) new Year(9998).next();
        assertEquals(9999,nextYear.getYear());
    }

    // invalid but handled with programmer in the source code
    @Test
    public void TestBoundaryYearNext()
    {
        Year nextYear = (Year) new Year(9999).next();
        assertNull(nextYear);
    }

    @Test
    public void TestBCYearNext()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(-2,Calendar.FEBRUARY,20); // -2 i 3 BC
        year = new Year(cal.getTime(),TimeZone.getDefault(),Locale.getDefault());
        Year nextyear = (Year) year.next();
        assertEquals(4,nextyear.getYear()); // it must be 2BC not 4 BC


    }

    // ..............................................Prev() Method.....................................................//

    // valid :
    @Test
    public void TestNormalYearPrev()
    {
        Year PrevYear = (Year) new Year(2025).previous(); // Down casting bec next is RegularTimePeriod Type
        assertEquals(2024,PrevYear.getYear());
    }

    @Test
    public void TestBelowTheBoundaryYearPrev()
    {
        Year PrevYear = (Year) new Year(-9998).previous();
        assertEquals(-9999,PrevYear.getYear());
    }

    // invalid but handled with programmer in the source code
    @Test
    public void TestBoundaryYearPrev()
    {
        Year PrevYear = (Year) new Year(-9999).previous();
        assertNull(PrevYear);
    }

    @Test
    public void TestBCYearPrev()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(-2,Calendar.FEBRUARY,20); // -2 i 3 BC
        year = new Year(cal.getTime(),TimeZone.getDefault(),Locale.getDefault());
        Year PrevYear = (Year) year.previous();
        assertEquals(2,PrevYear.getYear()); // it must be 4 BC not 2 BC


    }

    // conclusion in BC years the next is previous and vice versa

    // ............................................equals(Object) Method.............................................//


    // Equivalent Test
    @Test
    public void TestEqualsSameYear()
    {
        year = new Year(2025);
        Year year2 = new Year(2025);
        boolean check = year.equals(year2);
        assertTrue(check);
    }

    // non_Equivalent Test
    @Test
    public void TestEqualsNotSameYear()
    {
        year = new Year(2025);
        Year year2 = new Year(2026);
        boolean check = year.equals(year2);
        assertFalse(check);
    }
    // instance of Test
    @Test
    public void TestEqualObjectInstanceOfYear()
    {
        year = new Year(2025);
        RegularTimePeriod year2 = new Year(2025);
        boolean check = year.equals(year2);
        assertTrue(check);
    }

    // not instance of Test
    @Test
    public void TestEqualObjectNotInstanceOfYear()
    {
        year = new Year(2025);
        String FakeYear = "2025";
        boolean check = year.equals(FakeYear);
        assertFalse(check);
    }
    // null object
    @Test
    public void TestEqualNullObject()
    {
        year = new Year(2025);
        boolean check = year.equals(null);
        assertFalse(check);
    }
    // null object
    @Test
    public void TestEqualGenericObject()
    {
        year = new Year(2025);
        Object obj = new Object();
        boolean check = year.equals(obj);
        assertFalse(check);
    }


    // ............................................hashCode() Method.............................................//

    @Test
    public void HashCode()
    {
        year = new Year(2025);
        int expectedHashCode = 37 * 17 + 2025;
        int actualHashCode = year.hashCode();
        assertEquals(expectedHashCode,actualHashCode);

    }

// ............................................compareTo(Object o1)..............................................//

    @Test
    // Test instance of year
    public void TestCompareToWithEqualYear()
    {
        year = new Year(2024);
        RegularTimePeriod SameYear = new Year(2024);
        int diff = year.compareTo(SameYear);
        assertEquals(0,diff);
    }
    @Test
    public void TestCompareToWithBiggerYear()
    {
        year = new Year(2024);
        RegularTimePeriod SameYear = new Year(2025);
        int diff = year.compareTo(SameYear);
        assertEquals(-1,diff);
    }
    @Test
    public void TestCompareToWithSmallerYear()
    {
        year = new Year(2024);
        RegularTimePeriod SameYear = new Year(2023);
        int diff = year.compareTo(SameYear);
        assertEquals(1,diff);
    }

    // Test instance of RegularTimePeriod

    @Test
    public void TestCompareToWithRegularTimeObj()
    {
        year = new Year(2024);
        RegularTimePeriod RegularTimeObj =new RegularTimePeriod() {
            @Override public RegularTimePeriod previous() {return null;}
            @Override public RegularTimePeriod next() {return null;}
            @Override public long getSerialIndex() {return 0;}
            @Override public void peg(Calendar calendar) {}
            @Override public long getFirstMillisecond() {return 0;}
            @Override public long getFirstMillisecond(Calendar calendar) {return 0;}
            @Override public long getLastMillisecond() {return 0;}
            @Override public long getLastMillisecond(Calendar calendar) {return 0;}
            @Override public int compareTo(Object o) {return 0;}
        };
        int comparison = year.compareTo(RegularTimeObj);
        assertEquals(0,comparison);
    }

    // Test instance of Some Object
    @Test
    public void TestCompareToWithNullObj()
    {
        year = new Year(2024);
        int comparison = year.compareTo(null);
        assertEquals(1,comparison);
    }

    @Test
    public void TestCompareToWithSomeObj()
    {
        year = new Year(2024);
        String test = "2025";
        int comparison = year.compareTo(test);
        assertEquals(1,comparison);
    }

    @Test
    public void TestCompareToWithGenericObj()
    {
        year = new Year(2024);
        Object obj = new Object();
        int comparison = year.compareTo(obj);
        assertEquals(1,comparison);
    }


// ..............................................toString()................................................//

    // test to String Method
    @Test
    public void TestToStringNormal()
    {
        assertEquals("2025",new Year(2025).toString());
    }

    @Test
    public void TestToStringBcYear()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(0,Calendar.FEBRUARY,20); // 0 i 1 BC
        year = new Year(cal.getTime(),TimeZone.getDefault(),Locale.getDefault());
        assertEquals("1",year.toString());
    }

    // ..............................................ParseYear()................................................//

    // Test valid Cases

    @Test
    public void TestParseYearNormalYear()
    {
        assertEquals(2025,Year.parseYear("2025").getYear());
    }
    @Test
    public void TestParseYearMaxBoundaryYear()
    {
        assertEquals(9999,Year.parseYear("9999").getYear());
    }
    @Test
    public void TestParseYearMinBoundaryYear()
    {
        assertEquals(-9999,Year.parseYear("-9999").getYear());
    }

    // Test Invalid Cases

    @Test
    public void TestParseYearAboveMaxBoundaryYear()
    {
        TimePeriodFormatException Ex = assertThrows(TimePeriodFormatException.class,()->Year.parseYear("10000"));
        assertEquals("Year outside valid range.",Ex.getMessage());
    }

    @Test
    public void TestParseYearBelowMinBoundaryYear()
    {
        TimePeriodFormatException Ex = assertThrows(TimePeriodFormatException.class,()->Year.parseYear("-10000"));
        assertEquals("Year outside valid range.",Ex.getMessage());

    }

    @Test
    public void TestParseYearNotaNumber()
    {
        TimePeriodFormatException Ex = assertThrows(TimePeriodFormatException.class,()->Year.parseYear("HelloWorld").getYear());
        assertEquals("Cannot parse string.",Ex.getMessage());
    }

    // Bad Test
    @Test
    public void TestParseBadTest()
    {
        assertThrows(NullPointerException.class,()->Year.parseYear(null));
    }

    //***********************************************((Finish))*********************************************************
    //******************************************************************************************************************






}

