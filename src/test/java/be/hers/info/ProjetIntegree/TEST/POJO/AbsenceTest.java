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
        timeSlot = new TimeSlotPunctual(LocalTime.of(8, 0), LocalTime.of(1, 0), LocalDate.of(2025, 1, 1));
        absence = new Absence(1, "en attente", timeSlot, null, true);
    }

    // Default Constructor //

    /**
     * Tests that the default constructor sets status to "en attente" and timeSlot to null.
     * Given : no argument
     * When  : an Absence is created with the default constructor
     * Then  : getStatus() must return "en attente" and getTimeSlot() must return null
     */
    @Test
    void defaultConstructor_StatusIsEnAttenteAndTimeSlotIsNull() {
        Absence a = new Absence();
        assertEquals("en attente", a.getStatus());
        assertNull(a.getTimeSlot());
    }

    /**
     * Tests that the default constructor initializes reason to an empty string and privateReason to false.
     * Given : no argument
     * When  : an Absence is created with the default constructor
     * Then  : getReason() must return "" and isPrivateReason() must return false
     */
    @Test
    void defaultConstructor_ReasonIsEmptyAndPrivateReasonIsFalse() {
        Absence a = new Absence();
        assertEquals("", a.getReason());
        assertFalse(a.isPrivateReason());
    }

    // Constructor (timeSlot) //

    /**
     * Tests that the constructor with only a TimeSlot sets status to "en attente" and timeSlot correctly.
     * Given : a valid TimeSlotPunctual
     * When  : an Absence is created with only timeSlot
     * Then  : getStatus() must return "en attente" and getTimeSlot() must return the given timeSlot
     */
    @Test
    void constructor_WithTimeSlotOnly_StatusIsEnAttenteAndTimeSlotIsSet() {
        Absence a = new Absence(timeSlot);
        assertEquals("en attente", a.getStatus());
        assertEquals(timeSlot, a.getTimeSlot());
    }

    /**
     * Tests that the constructor with only a TimeSlot initializes reason to "" and privateReason to false.
     * Given : a valid TimeSlotPunctual
     * When  : an Absence is created with only timeSlot
     * Then  : getReason() must return "" and isPrivateReason() must return false
     */
    @Test
    void constructor_WithTimeSlotOnly_ReasonIsEmptyAndPrivateReasonIsFalse() {
        Absence a = new Absence(timeSlot);
        assertEquals("", a.getReason());
        assertFalse(a.isPrivateReason());
    }

    /**
     * Tests that the constructor with only a TimeSlot throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null TimeSlot
     * When  : an Absence is created with null timeSlot
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullTimeSlotOnly_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> new Absence((TimeSlotPunctual) null));
    }

    // Constructor (status, timeSlot) //

    /**
     * Tests that the constructor without numAbsence correctly sets status and timeSlot.
     * Given : status="accepte" and a valid TimeSlotPunctual
     * When  : an Absence is created without numAbsence
     * Then  : getStatus() must return "accepte" and getTimeSlot() must return the timeSlot
     */
    @Test
    void constructor_WithoutNumAbsence_SetsStatusAndTimeSlot() throws BadStatusException {
        Absence a = new Absence("accepte", timeSlot);
        assertEquals("accepte", a.getStatus());
        assertEquals(timeSlot, a.getTimeSlot());
    }

    /**
     * Tests that the constructor without numAbsence initializes reason to "" and privateReason to false.
     * Given : status="accepte" and a valid TimeSlotPunctual
     * When  : an Absence is created without numAbsence
     * Then  : getReason() must return "" and isPrivateReason() must return false
     */
    @Test
    void constructor_WithoutNumAbsence_ReasonIsEmptyAndPrivateReasonIsFalse() throws BadStatusException {
        Absence a = new Absence("accepte", timeSlot);
        assertEquals("", a.getReason());
        assertFalse(a.isPrivateReason());
    }

    /**
     * Tests that the constructor without numAbsence throws a {@link BadStatusException} when an invalid status is provided.
     * Given : an invalid status "mauvais"
     * When  : an Absence is created without numAbsence and with this status
     * Then  : a BadStatusException must be thrown
     */
    @Test
    void constructor_WithoutNumAbsence_WithInvalidStatus_RaisesABadStatusException() {
        assertThrows(BadStatusException.class, () -> new Absence("mauvais", timeSlot));
    }

    /**
     * Tests that the constructor without numAbsence throws an {@link IllegalArgumentException} when timeSlot is null.
     * Given : a null timeSlot
     * When  : an Absence is created without numAbsence and with null timeSlot
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutNumAbsence_WithNullTimeSlot_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> new Absence("en attente", null));
    }

    // Constructor (numAbsence, status, timeSlot, reason, privateReason) //

    /**
     * Tests that the full constructor correctly sets all fields when valid arguments are provided.
     * Given : numAbsence=1, status="en attente", a valid TimeSlot, reason=null and privateReason=true
     * When  : an Absence is created with these arguments
     * Then  : getNumAbsence(), getStatus(), getTimeSlot(), getReason() and isPrivateReason() must return the expected values
     */
    @Test
    void constructor_WithValidArguments_SetsAllFields() {
        assertEquals(1, absence.getNumAbsence());
        assertEquals("en attente", absence.getStatus());
        assertEquals(timeSlot, absence.getTimeSlot());
        assertEquals("", absence.getReason());
        assertTrue(absence.isPrivateReason());
    }

    /**
     * Tests that the full constructor correctly sets reason when a non-null value is provided.
     * Given : reason="Maladie"
     * When  : an Absence is created with this reason
     * Then  : getReason() must return "Maladie"
     */
    @Test
    void constructor_WithValidReason_SetsReason() throws BadStatusException {
        Absence a = new Absence(1, "en attente", timeSlot, "Maladie", false);
        assertEquals("Maladie", a.getReason());
    }

    /**
     * Tests that the full constructor sets reason to empty string when null is passed as reason.
     * Given : reason=null
     * When  : an Absence is created with null reason
     * Then  : getReason() must return ""
     */
    @Test
    void constructor_WithNullReason_SetsReasonToEmpty() throws BadStatusException {
        Absence a = new Absence(1, "en attente", timeSlot, null, false);
        assertEquals("", a.getReason());
    }

    /**
     * Tests that the full constructor throws a {@link BadStatusException} when an invalid status is provided.
     * Given : an invalid status "invalide"
     * When  : an Absence is created with this status
     * Then  : a BadStatusException must be thrown
     */
    @Test
    void constructor_WithInvalidStatus_RaisesABadStatusException() {
        assertThrows(BadStatusException.class,
                () -> new Absence(1, "invalide", timeSlot, null, false));
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when timeSlot is null.
     * Given : a null timeSlot
     * When  : an Absence is created with null timeSlot
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullTimeSlot_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Absence(1, "en attente", null, null, false));
    }

    // setNumAbsence //

    /**
     * Tests that setNumAbsence() correctly updates the id.
     * Given : an Absence initialized with numAbsence=1
     * When  : setNumAbsence(99) is called
     * Then  : getNumAbsence() must return 99
     */
    @Test
    void setNumAbsence_UpdatesTheCorrectValue() {
        absence.setNumAbsence(99);
        assertEquals(99, absence.getNumAbsence());
    }

    /**
     * Tests that setNumAbsence() throws an {@link IllegalArgumentException} when a negative value is passed.
     * Given : a negative value -1
     * When  : setNumAbsence(-1) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setNumAbsence_WithNegativeValue_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> absence.setNumAbsence(-1));
    }

    // setStatus //

    /**
     * Tests that setStatus() correctly updates the status to "accepte".
     * Given : an Absence with status="en attente"
     * When  : setStatus("accepte") is called
     * Then  : getStatus() must return "accepte"
     */
    @Test
    void setStatus_ToAccepte_UpdatesTheValue() throws BadStatusException {
        absence.setStatus("accepte");
        assertEquals("accepte", absence.getStatus());
    }

    /**
     * Tests that setStatus() correctly updates the status to "refuse".
     * Given : an Absence with status="en attente"
     * When  : setStatus("refuse") is called
     * Then  : getStatus() must return "refuse"
     */
    @Test
    void setStatus_ToRefuse_UpdatesTheValue() throws BadStatusException {
        absence.setStatus("refuse");
        assertEquals("refuse", absence.getStatus());
    }

    /**
     * Tests that setStatus() throws a {@link BadStatusException} when an invalid status is passed.
     * Given : an invalid status "invalide"
     * When  : setStatus("invalide") is called
     * Then  : a BadStatusException must be thrown
     */
    @Test
    void setStatus_WithInvalidStatus_RaisesABadStatusException() {
        assertThrows(BadStatusException.class, () -> absence.setStatus("invalide"));
    }

    // setReason //

    /**
     * Tests that setReason() correctly updates the reason.
     * Given : an Absence with reason=""
     * When  : setReason("Maladie") is called
     * Then  : getReason() must return "Maladie"
     */
    @Test
    void setReason_UpdatesTheCorrectValue() {
        absence.setReason("Maladie");
        assertEquals("Maladie", absence.getReason());
    }

    /**
     * Tests that setReason() sets reason to empty string when null is passed.
     * Given : a null value
     * When  : setReason(null) is called
     * Then  : getReason() must return ""
     */
    @Test
    void setReason_WithNull_SetsReasonToEmpty() {
        absence.setReason(null);
        assertEquals("", absence.getReason());
    }

    // setPrivateReason //

    /**
     * Tests that setPrivateReason() correctly updates the privateReason to true.
     * Given : an Absence with privateReason=false
     * When  : setPrivateReason(true) is called
     * Then  : isPrivateReason() must return true
     */
    @Test
    void setPrivateReason_ToTrue_UpdatesTheValue() {
        Absence a = new Absence(timeSlot);
        a.setPrivateReason(true);
        assertTrue(a.isPrivateReason());
    }

    /**
     * Tests that setPrivateReason() correctly updates the privateReason to false.
     * Given : an Absence with privateReason=true
     * When  : setPrivateReason(false) is called
     * Then  : isPrivateReason() must return false
     */
    @Test
    void setPrivateReason_ToFalse_UpdatesTheValue() {
        absence.setPrivateReason(false);
        assertFalse(absence.isPrivateReason());
    }

    // setTimeSlot //

    /**
     * Tests that setTimeSlot() correctly updates the timeSlot.
     * Given : a new valid TimeSlotPunctual
     * When  : setTimeSlot() is called with this new timeSlot
     * Then  : getTimeSlot() must return the new timeSlot
     */
    @Test
    void setTimeSlot_UpdatesTheCorrectValue() {
        TimeSlotPunctual newSlot = new TimeSlotPunctual(LocalTime.of(10, 0), LocalTime.of(2, 0), LocalDate.of(2025, 6, 1));
        absence.setTimeSlot(newSlot);
        assertEquals(newSlot, absence.getTimeSlot());
    }

    /**
     * Tests that setTimeSlot() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setTimeSlot(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setTimeSlot_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> absence.setTimeSlot(null));
    }

    // toString //

    /**
     * Tests that toString() contains the label "Absence".
     * Given : a valid Absence
     * When  : toString() is called
     * Then  : the result must contain "Absence"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(absence.toString().contains("Absence"));
    }

    /**
     * Tests that toString() contains the current status.
     * Given : an Absence with status="en attente"
     * When  : toString() is called
     * Then  : the result must contain "en attente"
     */
    @Test
    void toString_ContainsStatus() {
        assertTrue(absence.toString().contains("en attente"));
    }

    /**
     * Tests that toString() contains "Non renseignée" when reason is empty.
     * Given : an Absence initialized with reason=null (stored as "")
     * When  : toString() is called
     * Then  : the result must contain "Non renseignée"
     */
    @Test
    void toString_WhenReasonIsEmpty_ContainsNonRenseignee() {
        assertTrue(absence.toString().contains("Non renseignée"));
    }
}