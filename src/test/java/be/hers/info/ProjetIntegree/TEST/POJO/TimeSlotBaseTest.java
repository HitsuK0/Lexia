package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.TimeSlotBase;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link TimeSlotBase}.
 * Verifies the correct behaviour of constructors, getters, setters, overlapsWith and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
public class TimeSlotBaseTest {

    private TimeSlotBase timeSlotBase;
    private LocalTime startTime;
    private LocalTime duration;

    // Set Up //

    /**
     * Initializes valid time values and a default {@link TimeSlotBase} on Monday before each test.
     */
    @BeforeEach
    void setUp() {
        startTime = LocalTime.of(9, 0);
        duration = LocalTime.of(1, 0);
        timeSlotBase = new TimeSlotBase(startTime, duration, 1);
    }

    // Default Constructor //

    /**
     * Tests that the default constructor initializes startTime and duration to null (inherited from TimeSlot).
     * Given : no argument
     * When  : a TimeSlotBase is created with the default constructor
     * Then  : getStartTime() and getDuration() must both return null
     */
    @Test
    void defaultConstructor_StartTimeAndDurationAreNull() {
        TimeSlotBase tsb = new TimeSlotBase();
        assertNull(tsb.getStartTime());
        assertNull(tsb.getDuration());
    }

    /**
     * Tests that the default constructor initializes dayNumber to 0.
     * Given : no argument
     * When  : a TimeSlotBase is created with the default constructor
     * Then  : getDayNumber() must return 0
     */
    @Test
    void defaultConstructor_DayNumberIsZero() {
        TimeSlotBase tsb = new TimeSlotBase();
        assertEquals(0, tsb.getDayNumber());
    }

    // Constructor (startTime, duration, dayNumber) //

    /**
     * Tests that the constructor without ID correctly sets startTime, duration and dayNumber.
     * Given : valid startTime=09:00, duration=01:00 and dayNumber=1
     * When  : a TimeSlotBase is created with these arguments
     * Then  : getStartTime() must return 09:00, getDuration() must return 01:00
     *         and getDayNumber() must return 1
     */
    @Test
    void constructor_WithoutId_SetsAllFields() {
        assertEquals(startTime, timeSlotBase.getStartTime());
        assertEquals(duration, timeSlotBase.getDuration());
        assertEquals(1, timeSlotBase.getDayNumber());
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when dayNumber is less than 1.
     * Given : dayNumber=0
     * When  : a TimeSlotBase is created with this dayNumber
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithDayNumberTooLow_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotBase(startTime, duration, 0));
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when dayNumber is greater than 7.
     * Given : dayNumber=8
     * When  : a TimeSlotBase is created with this dayNumber
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithDayNumberTooHigh_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotBase(startTime, duration, 8));
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when startTime is null.
     * Given : a null startTime
     * When  : a TimeSlotBase is created with null startTime
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithNullStartTime_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotBase(null, duration, 1));
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when duration is null.
     * Given : a null duration
     * When  : a TimeSlotBase is created with null duration
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithNullDuration_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotBase(startTime, null, 1));
    }

    /**
     * Tests that the constructor without ID accepts all valid day numbers from 1 to 7 without throwing.
     * Given : dayNumber=7 (Sunday)
     * When  : a TimeSlotBase is created with dayNumber=7
     * Then  : no exception must be thrown and getDayNumber() must return 7
     */
    @Test
    void constructor_WithoutId_WithMaxDayNumber_DoesNotRaiseAnException() {
        assertDoesNotThrow(() -> new TimeSlotBase(startTime, duration, 7));
    }

    // Constructor (numTimeSlotBase, startTime, duration, dayNumber) //

    /**
     * Tests that the constructor with ID correctly sets numTimeSlot, startTime, duration and dayNumber.
     * Given : numTimeSlotBase=3, valid startTime, duration and dayNumber=2
     * When  : a TimeSlotBase is created with these arguments
     * Then  : getNumTimeSlot() must return 3, getStartTime(), getDuration()
     *         and getDayNumber() must return the expected values
     */
    @Test
    void constructor_WithId_SetsAllFields() {
        TimeSlotBase tsb = new TimeSlotBase(3, startTime, duration, 2);
        assertEquals(3, tsb.getNumTimeSlot());
        assertEquals(startTime, tsb.getStartTime());
        assertEquals(duration, tsb.getDuration());
        assertEquals(2, tsb.getDayNumber());
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when dayNumber is less than 1.
     * Given : dayNumber=0
     * When  : a TimeSlotBase is created with ID and this dayNumber
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithDayNumberTooLow_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotBase(1, startTime, duration, 0));
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when dayNumber is greater than 7.
     * Given : dayNumber=8
     * When  : a TimeSlotBase is created with ID and this dayNumber
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithDayNumberTooHigh_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotBase(1, startTime, duration, 8));
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when startTime is null.
     * Given : a null startTime
     * When  : a TimeSlotBase is created with ID and null startTime
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithNullStartTime_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotBase(1, null, duration, 1));
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when duration is null.
     * Given : a null duration
     * When  : a TimeSlotBase is created with ID and null duration
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithNullDuration_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotBase(1, startTime, null, 1));
    }

    // setNumTimeSlot (inherited from TimeSlot) //

    /**
     * Tests that setNumTimeSlot() correctly updates the ID.
     * Given : a TimeSlotBase initialized on Monday
     * When  : setNumTimeSlot(5) is called
     * Then  : getNumTimeSlot() must return 5
     */
    @Test
    void setNumTimeSlot_UpdatesTheCorrectValue() {
        timeSlotBase.setNumTimeSlot(5);
        assertEquals(5, timeSlotBase.getNumTimeSlot());
    }

    // setStartTime (inherited from TimeSlot) //

    /**
     * Tests that setStartTime() correctly updates the start time.
     * Given : a TimeSlotBase initialized with startTime=09:00
     * When  : setStartTime(10:00) is called
     * Then  : getStartTime() must return 10:00
     */
    @Test
    void setStartTime_UpdatesTheCorrectValue() {
        LocalTime newStartTime = LocalTime.of(10, 0);
        timeSlotBase.setStartTime(newStartTime);
        assertEquals(newStartTime, timeSlotBase.getStartTime());
    }

    /**
     * Tests that setStartTime() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setStartTime(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setStartTime_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> timeSlotBase.setStartTime(null));
    }

    // setDuration (inherited from TimeSlot) //

    /**
     * Tests that setDuration() correctly updates the duration.
     * Given : a TimeSlotBase initialized with duration=01:00
     * When  : setDuration(02:00) is called
     * Then  : getDuration() must return 02:00
     */
    @Test
    void setDuration_UpdatesTheCorrectValue() {
        LocalTime newDuration = LocalTime.of(2, 0);
        timeSlotBase.setDuration(newDuration);
        assertEquals(newDuration, timeSlotBase.getDuration());
    }

    /**
     * Tests that setDuration() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setDuration(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setDuration_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> timeSlotBase.setDuration(null));
    }

    // setDayNumber //

    /**
     * Tests that setDayNumber() correctly updates the day number.
     * Given : a TimeSlotBase initialized with dayNumber=1
     * When  : setDayNumber(5) is called
     * Then  : getDayNumber() must return 5
     */
    @Test
    void setDayNumber_UpdatesTheCorrectValue() {
        timeSlotBase.setDayNumber(5);
        assertEquals(5, timeSlotBase.getDayNumber());
    }

    /**
     * Tests that setDayNumber() throws an {@link IllegalArgumentException} when dayNumber is less than 1.
     * Given : a value 0
     * When  : setDayNumber(0) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setDayNumber_WithValueTooLow_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> timeSlotBase.setDayNumber(0));
    }

    /**
     * Tests that setDayNumber() throws an {@link IllegalArgumentException} when dayNumber is greater than 7.
     * Given : a value 8
     * When  : setDayNumber(8) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setDayNumber_WithValueTooHigh_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> timeSlotBase.setDayNumber(8));
    }

    /**
     * Tests that setDayNumber() accepts 1 as a valid value.
     * Given : value 1 (Monday)
     * When  : setDayNumber(1) is called
     * Then  : getDayNumber() must return 1 and no exception must be thrown
     */
    @Test
    void setDayNumber_WithMinValue_DoesNotRaiseAnException() {
        assertDoesNotThrow(() -> timeSlotBase.setDayNumber(1));
        assertEquals(1, timeSlotBase.getDayNumber());
    }

    /**
     * Tests that setDayNumber() accepts 7 as a valid value.
     * Given : value 7 (Sunday)
     * When  : setDayNumber(7) is called
     * Then  : getDayNumber() must return 7 and no exception must be thrown
     */
    @Test
    void setDayNumber_WithMaxValue_DoesNotRaiseAnException() {
        assertDoesNotThrow(() -> timeSlotBase.setDayNumber(7));
        assertEquals(7, timeSlotBase.getDayNumber());
    }

    // overlapsWith(TimeSlotBase) //

    /**
     * Tests that two TimeSlotBase on the same day with overlapping times return true.
     * Given : two TimeSlotBase on Monday (dayNumber=1), one at 09:00 for 1h and one at 09:30 for 1h
     * When  : overlapsWith() is called
     * Then  : the result must be true
     */
    @Test
    void overlapsWith_Base_SameDayAndOverlappingTimes_ReturnsTrue() {
        TimeSlotBase other = new TimeSlotBase(LocalTime.of(9, 30), LocalTime.of(1, 0), 1);
        assertTrue(timeSlotBase.overlapsWith(other));
    }

    /**
     * Tests that two TimeSlotBase on different days return false even if times overlap.
     * Given : two TimeSlotBase at the same time, one on Monday (dayNumber=1) and one on Tuesday (dayNumber=2)
     * When  : overlapsWith() is called
     * Then  : the result must be false
     */
    @Test
    void overlapsWith_Base_DifferentDays_ReturnsFalse() {
        TimeSlotBase other = new TimeSlotBase(startTime, duration, 2);
        assertFalse(timeSlotBase.overlapsWith(other));
    }

    /**
     * Tests that two TimeSlotBase on the same day with non-overlapping times return false.
     * Given : two TimeSlotBase on Monday (dayNumber=1), one at 09:00 for 1h and one at 11:00 for 1h
     *         (the first ends at 10:40 including travel time, the second starts at 11:00)
     * When  : overlapsWith() is called
     * Then  : the result must be false
     */
    @Test
    void overlapsWith_Base_SameDayNonOverlappingTimes_ReturnsFalse() {
        TimeSlotBase other = new TimeSlotBase(LocalTime.of(11, 0), LocalTime.of(1, 0), 1);
        assertFalse(timeSlotBase.overlapsWith(other));
    }

    /**
     * Tests that overlapsWith(TimeSlotBase) throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null TimeSlotBase
     * When  : overlapsWith(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void overlapsWith_Base_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> timeSlotBase.overlapsWith((TimeSlotBase) null));
    }

    // overlapsWith(TimeSlotPunctual) //

    /**
     * Tests that a TimeSlotBase overlaps with a TimeSlotPunctual when they share the same day of week and overlapping times.
     * Given : a TimeSlotBase on Monday (dayNumber=1) at 09:00 for 1h
     *         and a TimeSlotPunctual on a Monday (2025-06-09) at 09:30 for 1h
     * When  : overlapsWith() is called
     * Then  : the result must be true
     */
    @Test
    void overlapsWith_Punctual_SameDayAndOverlappingTimes_ReturnsTrue() {
        // 2025-06-09 is a Monday (DayOfWeek = 1)
        LocalDate monday = LocalDate.of(2025, 6, 9);
        TimeSlotPunctual punctual = new TimeSlotPunctual(LocalTime.of(9, 30), LocalTime.of(1, 0), monday);
        assertTrue(timeSlotBase.overlapsWith(punctual));
    }

    /**
     * Tests that a TimeSlotBase does not overlap with a TimeSlotPunctual on a different day of week.
     * Given : a TimeSlotBase on Monday (dayNumber=1) at 09:00 for 1h
     *         and a TimeSlotPunctual on a Tuesday (2025-06-10) at 09:30 for 1h
     * When  : overlapsWith() is called
     * Then  : the result must be false
     */
    @Test
    void overlapsWith_Punctual_DifferentDay_ReturnsFalse() {
        // 2025-06-10 is a Tuesday (DayOfWeek = 2)
        LocalDate tuesday = LocalDate.of(2025, 6, 10);
        TimeSlotPunctual punctual = new TimeSlotPunctual(LocalTime.of(9, 30), LocalTime.of(1, 0), tuesday);
        assertFalse(timeSlotBase.overlapsWith(punctual));
    }

    /**
     * Tests that overlapsWith(TimeSlotPunctual) throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null TimeSlotPunctual
     * When  : overlapsWith(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void overlapsWith_Punctual_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> timeSlotBase.overlapsWith((TimeSlotPunctual) null));
    }

    // overlapsWith(TimeSlot) //

    /**
     * Tests that overlapsWith(TimeSlot) throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null TimeSlot
     * When  : overlapsWith(null) is called with a TimeSlot argument
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void overlapsWith_TimeSlot_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> timeSlotBase.overlapsWith((TimeSlotBase) null));
    }

    // toString //

    /**
     * Tests that toString() contains the label "Tranche horaire répétitive".
     * Given : a valid TimeSlotBase
     * When  : toString() is called
     * Then  : the result must contain "Tranche horaire répétitive"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(timeSlotBase.toString().contains("Tranche horaire répétitive"));
    }

    /**
     * Tests that toString() contains the start time.
     * Given : a TimeSlotBase initialized with startTime=09:00
     * When  : toString() is called
     * Then  : the result must contain "09:00"
     */
    @Test
    void toString_ContainsStartTime() {
        assertTrue(timeSlotBase.toString().contains("09:00"));
    }

    /**
     * Tests that toString() contains the day number.
     * Given : a TimeSlotBase initialized with dayNumber=1
     * When  : toString() is called
     * Then  : the result must contain "1"
     */
    @Test
    void toString_ContainsDayNumber() {
        assertTrue(timeSlotBase.toString().contains("1"));
    }
}