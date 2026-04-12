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
     * Given : a null TimeSlotPunctual"
     * When  : an Absence is created with null timeSlot
     * Then  : a IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WhitNullTimeSlot_RaisesAnException(){
        assertThrows(IllegalArgumentException.class, () -> new Absence(1, "invalide", null));
    }

    // Constructor (status, timeSlotPunctual) //
}
