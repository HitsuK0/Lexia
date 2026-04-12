package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Absence}
 * Verifies the correct behavior of constructors, getters, setters and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
public class AbsenceTest {
    private Absence absence;
    private TimeSlotPunctual timeSlot;

    // Set Up //

    /**
     * Initializes a default {@link TimeSlotPunctual} and a default {@link Absence} with status "en attente" before each test.
     */
    @BeforeEach
    void setUp() throws BadStatusException {
        timeSlot = new TimeSlotPunctual(LocalTime.of(8,0), LocalTime.of(1,0), LocalDate.of(2025,1,1));
        absence = new Absence(1, "en attente", timeSlot);
    }

    // Constructor (numAbsence, status, timeSlotPunctual) //

    /**
     * Tests that full constructor correctly sets all fields when valid arguments are provided.
     * Given : numAbsence=1, status="en attente", a valid TimeSlotPunctual
     * When  : an Absence is created with these arguments.
     * Then  : getNumAbsence(), getStatus(), and getTimeSlotPunctual() must return the expected values.
     */
    @Test
    void constructor_WithValidArguments_SetsAllFields() throws BadStatusException{
        assertEquals(1, absence.getNumAbsence());
        assertEquals("en attente", absence.getStatus());
        assertEquals(timeSlot, absence.getTimeSlotPonctual());
    }

    /**
     * Tests that the full constructor throws a {@link BadStatusException} when an invalid status is provided.
     * Given : an invalid status "invalide"
     * When  : an Absence is created with this status
     * Then  : a BadStatusException must be thrown
     */
    @Test
    void constructor_WithInvalidStatus_RaisesABadStatusException(){
        assertThrows(BadStatusException.class, () -> new Absence(1, "invalide", timeSlot));
    }

    /**
     * Tests that the full constructor throws a {@link IllegalArgumentException} when the TimeSlotPunctual is null.
     * Given : a null TimeSlotPunctual
     * When  : an Absence is created with null timeSlot
     * Then  : a IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WhitNullTimeSlot_RaisesAnException(){
        assertThrows(IllegalArgumentException.class, () -> new Absence(1, "en attente", null));
    }

    // Constructor (status, timeSlotPunctual) //

    /**
     * Tests that the constructor without numAbsence correctly sets status and timeSlot.
     * Given : status="accepte" and a valid TimeSlotPunctual
     * When  : an Absence is created without numAbsence
     * Then  : getStatus() must return "accepte" and getTimeSlotPunctual() must return the timeSlot
     */
    @Test
    void constructor_WithoutNumAbsence_SetsStatusAndTimeSlot() throws BadStatusException {
        Absence a = new Absence("accepte", timeSlot);
        assertEquals("accepte", a.getStatus());
        assertEquals(timeSlot, a.getTimeSlotPonctual());
    }

    /**
     * Tests that the constructor without numAbsence throws a {@link BadStatusException} when an invalid status is provided.
     * Given : an invalid status "mauvais"
     * When  : an Absence is created without numAbsence and with this status
     * Then  : a BadStatusException must be thrown
     */
    @Test
    void constructor_WithoutNumAbsence_InvalidStatus_RaisesABadStatusException(){
        assertThrows(BadStatusException.class, () -> new Absence("mauvais", timeSlot));
    }

    /**
     * Tests that the constructor without numAbsence throws a {@link IllegalArgumentException} when the TimeSlotPunctual is null.
     * Given : a null TimeSlotPunctual
     * When  : an Absence is created without numAbsence and with null timeSlot
     * Then  : a IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutNumAbsence_WhitNullTimeSlot_RaisesAnException(){
        assertThrows(IllegalArgumentException.class, () -> new Absence("en attente", null));
    }

    // Constructor (timeSlotPunctual only) //

    /**
     * Tests that constructor with only a TimeSlotPunctual sets the status to "en attente".
     * Given : a valid TimeSlotPunctual
     * When  : an Absence is created with only timeSlot
     * Then  : getStatus() must return "en attente"
     */
    @Test
    void constructor_WithTimeSlotOnly_StatusIsEnAttente(){
        Absence a = new Absence(timeSlot);
        assertEquals("en attente", a.getStatus());
        assertEquals(timeSlot, a.getTimeSlotPonctual());
    }

    /**
     * Tests that the constructor with only a TimeSlotPunctual throws an {@link IllegalArgumentException} when the timeSlot is null.
     * Given : a null TimeSlotPunctual
     * When  : an Absence is created with null
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WhitNullTimeSlotOnly_RaisesAnException(){
        assertThrows(IllegalArgumentException.class, () -> new Absence((TimeSlotPunctual) null));
    }

    // Default Constructor //

    /**
     * Tests that the default constructor sets status to "en attente" and timeSlotPunctual to null.
     * Given : no argument
     * When  : an absence is created with the default constructor
     * Then  : getStatus() must return "en attente" and timeSlotPunctual must return null
     */
    @Test
    void defaultConstructor_StatusIsEnAttenteAndTimeSlotIsNull() {
        Absence a = new Absence();
        assertEquals("en attente", a.getStatus());
        assertNull(a.getTimeSlotPonctual());
    }
    
}
