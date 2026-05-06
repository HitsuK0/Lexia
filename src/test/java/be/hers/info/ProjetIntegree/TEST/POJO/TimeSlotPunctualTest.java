package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.TimeSlotBase;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link TimeSlotPunctual}.
 * Verifies the correct behaviour of constructors, getters, setters, overlapsWith and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
public class TimeSlotPunctualTest {

    private TimeSlotPunctual timeSlotPunctual;
    private LocalTime startTime;
    private LocalTime duration;
    private LocalDate startDate;
    private LocalDate endDate;

    // Set Up //

    /**
     * Initializes valid time and date values and a default {@link TimeSlotPunctual} before each test.
     */
    @BeforeEach
    void setUp() {
        startTime = LocalTime.of(9, 0);
        duration = LocalTime.of(1, 0);
        startDate = LocalDate.of(2025, 6, 10);
        endDate = LocalDate.of(2025, 6, 12);
        timeSlotPunctual = new TimeSlotPunctual(startTime, duration, startDate);
    }

    // Default Constructor //

    /**
     * Tests that the default constructor initializes startDate and endDate to null.
     * Given : no argument
     * When  : a TimeSlotPunctual is created with the default constructor
     * Then  : getStartDate() and getEndDate() must both return null
     */
    @Test
    void defaultConstructor_StartDateAndEndDateAreNull() {
        TimeSlotPunctual tsp = new TimeSlotPunctual();
        assertNull(tsp.getStartDate());
        assertNull(tsp.getEndDate());
    }

    /**
     * Tests that the default constructor initializes startTime and duration to null (inherited from TimeSlot).
     * Given : no argument
     * When  : a TimeSlotPunctual is created with the default constructor
     * Then  : getStartTime() and getDuration() must both return null
     */
    @Test
    void defaultConstructor_StartTimeAndDurationAreNull() {
        TimeSlotPunctual tsp = new TimeSlotPunctual();
        assertNull(tsp.getStartTime());
        assertNull(tsp.getDuration());
    }

    // Constructor (startTime, duration, startDate) //

    /**
     * Tests that the constructor without ID correctly sets startTime, duration and startDate,
     * and leaves endDate as null.
     * Given : valid startTime=09:00, duration=01:00 and startDate=2025-06-10
     * When  : a TimeSlotPunctual is created with these arguments
     * Then  : getStartTime(), getDuration() and getStartDate() must return the expected values
     *         and getEndDate() must return null
     */
    @Test
    void constructor_WithoutIdAndStartDate_SetsFieldsAndEndDateIsNull() {
        assertEquals(startTime, timeSlotPunctual.getStartTime());
        assertEquals(duration, timeSlotPunctual.getDuration());
        assertEquals(startDate, timeSlotPunctual.getStartDate());
        assertNull(timeSlotPunctual.getEndDate());
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when startDate is null.
     * Given : a null startDate
     * When  : a TimeSlotPunctual is created with null startDate
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithNullStartDate_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotPunctual(startTime, duration, null));
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when startTime is null.
     * Given : a null startTime
     * When  : a TimeSlotPunctual is created with null startTime
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithNullStartTime_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotPunctual(null, duration, startDate));
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when duration is null.
     * Given : a null duration
     * When  : a TimeSlotPunctual is created with null duration
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithNullDuration_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotPunctual(startTime, null, startDate));
    }

    // Constructor (numTimeSlotPunctual, startTime, duration, startDate) //

    /**
     * Tests that the constructor with ID correctly sets numTimeSlot, startTime, duration and startDate,
     * and leaves endDate as null.
     * Given : numTimeSlotPunctual=1, valid startTime, duration and startDate
     * When  : a TimeSlotPunctual is created with these arguments
     * Then  : getNumTimeSlot() must return 1, getStartDate() must return startDate and getEndDate() must return null
     */
    @Test
    void constructor_WithId_SetsAllFieldsAndEndDateIsNull() {
        TimeSlotPunctual tsp = new TimeSlotPunctual(1, startTime, duration, startDate);
        assertEquals(1, tsp.getNumTimeSlot());
        assertEquals(startTime, tsp.getStartTime());
        assertEquals(duration, tsp.getDuration());
        assertEquals(startDate, tsp.getStartDate());
        assertNull(tsp.getEndDate());
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when startDate is null.
     * Given : a null startDate
     * When  : a TimeSlotPunctual is created with ID and null startDate
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithNullStartDate_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotPunctual(1, startTime, duration, null));
    }

    // Constructor (startTime, duration, startDate, endDate) //

    /**
     * Tests that the constructor without ID and with both dates correctly sets all fields.
     * Given : valid startTime, duration, startDate=2025-06-10 and endDate=2025-06-12
     * When  : a TimeSlotPunctual is created with these arguments
     * Then  : getStartDate() must return 2025-06-10 and getEndDate() must return 2025-06-12
     */
    @Test
    void constructor_WithoutIdAndBothDates_SetsBothDates() {
        TimeSlotPunctual tsp = new TimeSlotPunctual(startTime, duration, startDate, endDate);
        assertEquals(startDate, tsp.getStartDate());
        assertEquals(endDate, tsp.getEndDate());
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when startDate is null.
     * Given : a null startDate
     * When  : a TimeSlotPunctual is created with null startDate and a valid endDate
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutIdAndBothDates_WithNullStartDate_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotPunctual(startTime, duration, null, endDate));
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when endDate is null.
     * Given : a null endDate
     * When  : a TimeSlotPunctual is created with a valid startDate and null endDate
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutIdAndBothDates_WithNullEndDate_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotPunctual(startTime, duration, startDate, null));
    }

    // Constructor (numTimeSlotPunctual, startTime, duration, startDate, endDate) //

    /**
     * Tests that the full constructor with ID correctly sets all fields.
     * Given : numTimeSlotPunctual=2, valid startTime, duration, startDate and endDate
     * When  : a TimeSlotPunctual is created with these arguments
     * Then  : all getters must return the expected values
     */
    @Test
    void constructor_WithIdAndBothDates_SetsAllFields() {
        TimeSlotPunctual tsp = new TimeSlotPunctual(2, startTime, duration, startDate, endDate);
        assertEquals(2, tsp.getNumTimeSlot());
        assertEquals(startTime, tsp.getStartTime());
        assertEquals(duration, tsp.getDuration());
        assertEquals(startDate, tsp.getStartDate());
        assertEquals(endDate, tsp.getEndDate());
    }

    /**
     * Tests that the full constructor with ID throws an {@link IllegalArgumentException} when startDate is null.
     * Given : a null startDate
     * When  : a TimeSlotPunctual is created with ID, null startDate and a valid endDate
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithIdAndBothDates_WithNullStartDate_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotPunctual(1, startTime, duration, null, endDate));
    }

    /**
     * Tests that the full constructor with ID throws an {@link IllegalArgumentException} when endDate is null.
     * Given : a null endDate
     * When  : a TimeSlotPunctual is created with ID, a valid startDate and null endDate
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithIdAndBothDates_WithNullEndDate_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlotPunctual(1, startTime, duration, startDate, null));
    }

    // setNumTimeSlot (inherited from TimeSlot) //

    /**
     * Tests that setNumTimeSlot() correctly updates the ID.
     * Given : a TimeSlotPunctual initialized with startDate
     * When  : setNumTimeSlot(5) is called
     * Then  : getNumTimeSlot() must return 5
     */
    @Test
    void setNumTimeSlot_UpdatesTheCorrectValue() {
        timeSlotPunctual.setNumTimeSlot(5);
        assertEquals(5, timeSlotPunctual.getNumTimeSlot());
    }

    // setStartTime (inherited from TimeSlot) //

    /**
     * Tests that setStartTime() correctly updates the start time.
     * Given : a TimeSlotPunctual initialized with startTime=09:00
     * When  : setStartTime(10:00) is called
     * Then  : getStartTime() must return 10:00
     */
    @Test
    void setStartTime_UpdatesTheCorrectValue() {
        LocalTime newStartTime = LocalTime.of(10, 0);
        timeSlotPunctual.setStartTime(newStartTime);
        assertEquals(newStartTime, timeSlotPunctual.getStartTime());
    }

    /**
     * Tests that setStartTime() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setStartTime(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setStartTime_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> timeSlotPunctual.setStartTime(null));
    }

    // setDuration (inherited from TimeSlot) //

    /**
     * Tests that setDuration() correctly updates the duration.
     * Given : a TimeSlotPunctual initialized with duration=01:00
     * When  : setDuration(02:00) is called
     * Then  : getDuration() must return 02:00
     */
    @Test
    void setDuration_UpdatesTheCorrectValue() {
        LocalTime newDuration = LocalTime.of(2, 0);
        timeSlotPunctual.setDuration(newDuration);
        assertEquals(newDuration, timeSlotPunctual.getDuration());
    }

    /**
     * Tests that setDuration() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setDuration(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setDuration_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> timeSlotPunctual.setDuration(null));
    }

    // setStartDate //

    /**
     * Tests that setStartDate() correctly updates the start date.
     * Given : a TimeSlotPunctual initialized with startDate=2025-06-10
     * When  : setStartDate(2025-06-11) is called
     * Then  : getStartDate() must return 2025-06-11
     */
    @Test
    void setStartDate_UpdatesTheCorrectValue() {
        LocalDate newStartDate = LocalDate.of(2025, 6, 11);
        timeSlotPunctual.setStartDate(newStartDate);
        assertEquals(newStartDate, timeSlotPunctual.getStartDate());
    }

    /**
     * Tests that setStartDate() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setStartDate(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setStartDate_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> timeSlotPunctual.setStartDate(null));
    }

    /**
     * Tests that setStartDate() throws an {@link IllegalArgumentException} when startDate is after endDate.
     * Given : a TimeSlotPunctual with startDate=2025-06-10 and endDate=2025-06-12
     * When  : setStartDate(2025-06-13) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setStartDate_AfterEndDate_RaisesAnException() {
        TimeSlotPunctual tsp = new TimeSlotPunctual(startTime, duration, startDate, endDate);
        assertThrows(IllegalArgumentException.class,
                () -> tsp.setStartDate(LocalDate.of(2025, 6, 13)));
    }

    // setEndDate //

    /**
     * Tests that setEndDate() correctly updates the end date.
     * Given : a TimeSlotPunctual initialized with startDate=2025-06-10
     * When  : setEndDate(2025-06-15) is called
     * Then  : getEndDate() must return 2025-06-15
     */
    @Test
    void setEndDate_UpdatesTheCorrectValue() {
        timeSlotPunctual.setEndDate(LocalDate.of(2025, 6, 15));
        assertEquals(LocalDate.of(2025, 6, 15), timeSlotPunctual.getEndDate());
    }

    /**
     * Tests that setEndDate() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setEndDate(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setEndDate_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> timeSlotPunctual.setEndDate(null));
    }

    /**
     * Tests that setEndDate() throws an {@link IllegalArgumentException} when endDate is before startDate.
     * Given : a TimeSlotPunctual with startDate=2025-06-10
     * When  : setEndDate(2025-06-09) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setEndDate_BeforeStartDate_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> timeSlotPunctual.setEndDate(LocalDate.of(2025, 6, 9)));
    }

    // getTravelTimeMinutes (inherited from TimeSlot) //

    /**
     * Tests that getTravelTimeMinutes() returns 40.
     * Given : any TimeSlotPunctual
     * When  : getTravelTimeMinutes() is called
     * Then  : the result must equal 40
     */
    @Test
    void getTravelTimeMinutes_Returns40() {
        assertEquals(40, timeSlotPunctual.getTravelTimeMinutes());
    }

    // overlapsWith(TimeSlotPunctual) //

    /**
     * Tests that two TimeSlotPunctual on the same date with overlapping times return true.
     * Given : two TimeSlotPunctual on 2025-06-10, one at 09:00 for 1h and one at 09:30 for 1h
     * When  : overlapsWith() is called
     * Then  : the result must be true
     */
    @Test
    void overlapsWith_Punctual_SameDateOverlappingTimes_ReturnsTrue() {
        TimeSlotPunctual other = new TimeSlotPunctual(LocalTime.of(9, 30), LocalTime.of(1, 0), startDate);
        assertTrue(timeSlotPunctual.overlapsWith(other));
    }

    /**
     * Tests that two TimeSlotPunctual on different dates return false even if times overlap.
     * Given : two TimeSlotPunctual at the same time but on different dates (2025-06-10 and 2025-06-11)
     * When  : overlapsWith() is called
     * Then  : the result must be false
     */
    @Test
    void overlapsWith_Punctual_DifferentDates_ReturnsFalse() {
        TimeSlotPunctual other = new TimeSlotPunctual(startTime, duration, LocalDate.of(2025, 6, 11));
        assertFalse(timeSlotPunctual.overlapsWith(other));
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
                () -> timeSlotPunctual.overlapsWith((TimeSlotPunctual) null));
    }

    // overlapsWith(TimeSlotBase) //

    /**
     * Tests that a TimeSlotPunctual overlaps with a TimeSlotBase when they share the same day of week and overlapping times.
     * Given : a TimeSlotPunctual on a Monday (2025-06-09) at 09:00 for 1h
     *         and a TimeSlotBase on Monday (dayNumber=1) at 09:30 for 1h
     * When  : overlapsWith() is called
     * Then  : the result must be true
     */
    @Test
    void overlapsWith_Base_SameDayAndOverlappingTimes_ReturnsTrue() {
        // 2025-06-09 is a Monday (DayOfWeek = 1)
        LocalDate monday = LocalDate.of(2025, 6, 9);
        TimeSlotPunctual tsp = new TimeSlotPunctual(LocalTime.of(9, 0), LocalTime.of(1, 0), monday);
        TimeSlotBase base = new TimeSlotBase(LocalTime.of(9, 30), LocalTime.of(1, 0), 1);
        assertTrue(tsp.overlapsWith(base));
    }

    /**
     * Tests that a TimeSlotPunctual does not overlap with a TimeSlotBase on a different day of week.
     * Given : a TimeSlotPunctual on a Monday (2025-06-09) at 09:00 for 1h
     *         and a TimeSlotBase on Tuesday (dayNumber=2) at 09:30 for 1h
     * When  : overlapsWith() is called
     * Then  : the result must be false
     */
    @Test
    void overlapsWith_Base_DifferentDay_ReturnsFalse() {
        // 2025-06-09 is a Monday (DayOfWeek = 1)
        LocalDate monday = LocalDate.of(2025, 6, 9);
        TimeSlotPunctual tsp = new TimeSlotPunctual(LocalTime.of(9, 0), LocalTime.of(1, 0), monday);
        TimeSlotBase base = new TimeSlotBase(LocalTime.of(9, 30), LocalTime.of(1, 0), 2);
        assertFalse(tsp.overlapsWith(base));
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
                () -> timeSlotPunctual.overlapsWith((TimeSlotBase) null));
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
                () -> timeSlotPunctual.overlapsWith((TimeSlotPunctual) null));
    }

    // toString //

    /**
     * Tests that toString() contains the label "Tranche horaire ponctuelle".
     * Given : a valid TimeSlotPunctual
     * When  : toString() is called
     * Then  : the result must contain "Tranche horaire ponctuelle"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(timeSlotPunctual.toString().contains("Tranche horaire ponctuelle"));
    }

    /**
     * Tests that toString() contains the start date.
     * Given : a TimeSlotPunctual initialized with startDate=2025-06-10
     * When  : toString() is called
     * Then  : the result must contain "2025-06-10"
     */
    @Test
    void toString_ContainsStartDate() {
        assertTrue(timeSlotPunctual.toString().contains("2025-06-10"));
    }

    /**
     * Tests that toString() contains "Aucune date de fin" when endDate is null.
     * Given : a TimeSlotPunctual with no endDate
     * When  : toString() is called
     * Then  : the result must contain "Aucune date de fin"
     */
    @Test
    void toString_WhenNoEndDate_ContainsAucuneDateDeFin() {
        assertTrue(timeSlotPunctual.toString().contains("Aucune date de fin"));
    }

    /**
     * Tests that toString() contains the end date when it is set.
     * Given : a TimeSlotPunctual with startDate=2025-06-10 and endDate=2025-06-12
     * When  : toString() is called
     * Then  : the result must contain "2025-06-12"
     */
    @Test
    void toString_WhenEndDateIsSet_ContainsEndDate() {
        TimeSlotPunctual tsp = new TimeSlotPunctual(startTime, duration, startDate, endDate);
        assertTrue(tsp.toString().contains("2025-06-12"));
    }
}