package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Appointment}.
 * Verifies the correct behaviour of constructors, getters, setters and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
public class AppointmentTest {

    private Appointment appointment;
    private Beneficiary beneficiary;
    private List<Interpreter> interpreters;
    private List<ProfessionalSkill> professionalSkills;
    private TimeSlot timeSlot;

    // Set Up //

    /**
     * Initializes all required dependencies and a default {@link Appointment} with valid arguments before each test.
     */
    @BeforeEach
    void setUp() {
        beneficiary = new Beneficiary();
        interpreters = new ArrayList<>();
        interpreters.add(new Interpreter());
        professionalSkills = new ArrayList<>();
        professionalSkills.add(new ProfessionalSkill("Traduction"));
        timeSlot = new TimeSlotPunctual(LocalTime.of(8, 0), LocalTime.of(1, 0), LocalDate.of(2025, 6, 1));
        appointment = new Appointment(1, beneficiary, null, interpreters, null, professionalSkills, timeSlot);
    }

    // Default Constructor //

    /**
     * Tests that the default constructor sets status to "en attente" and initializes all lists to empty lists.
     * Given : no argument
     * When  : an Appointment is created with the default constructor
     * Then  : getStatus() must return "en attente" and all lists must be empty
     */
    @Test
    void defaultConstructor_StatusIsEnAttenteAndListsAreEmpty() {
        Appointment a = new Appointment();
        assertEquals("en attente", a.getStatus());
        assertTrue(a.getAppointmentLocals().isEmpty());
        assertTrue(a.getInterpreters().isEmpty());
    }

    // Constructor (numAppointment, all fields) //

    /**
     * Tests that the full constructor correctly sets all fields when valid arguments are provided.
     * Given : valid numAppointment=1, beneficiary, interpreters, professionalSkills and timeSlot
     * When  : an Appointment is created with these arguments
     * Then  : all getters must return the expected values and status must be "en attente"
     */
    @Test
    void constructor_WithValidArguments_SetsAllFields() {
        assertEquals(1, appointment.getNumAppointment());
        assertEquals(beneficiary, appointment.getBeneficiary());
        assertEquals(interpreters, appointment.getInterpreters());
        assertEquals(professionalSkills, appointment.getProfessionalSkillsNeeded());
        assertEquals(timeSlot, appointment.getTimeSlot());
        assertEquals("en attente", appointment.getStatus());
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when beneficiary is null.
     * Given : a null beneficiary
     * When  : an Appointment is created with null beneficiary
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullBeneficiary_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Appointment(1, null, null, interpreters, null, professionalSkills, timeSlot));
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when interpreters is null.
     * Given : a null interpreters list
     * When  : an Appointment is created with null interpreters
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullInterpreters_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Appointment(1, beneficiary, null, null, null, professionalSkills, timeSlot));
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when interpreters is empty.
     * Given : an empty interpreters list
     * When  : an Appointment is created with empty interpreters
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithEmptyInterpreters_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Appointment(1, beneficiary, null, new ArrayList<>(), null, professionalSkills, timeSlot));
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when professionalSkills is null.
     * Given : a null professionalSkills list
     * When  : an Appointment is created with null professionalSkills
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullProfessionalSkills_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Appointment(1, beneficiary, null, interpreters, null, null, timeSlot));
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when professionalSkills is empty.
     * Given : an empty professionalSkills list
     * When  : an Appointment is created with empty professionalSkills
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithEmptyProfessionalSkills_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Appointment(1, beneficiary, null, interpreters, null, new ArrayList<>(), timeSlot));
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when timeSlot is null.
     * Given : a null timeSlot
     * When  : an Appointment is created with null timeSlot
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullTimeSlot_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Appointment(1, beneficiary, null, interpreters, null, professionalSkills, null));
    }

    // setNumAppointment //

    /**
     * Tests that {@code setNumAppointment()} correctly updates the id.
     * Given : an Appointment initialized with numAppointment=1
     * When  : setNumAppointment(99) is called
     * Then  : getNumAppointment() must return 99
     */
    @Test
    void setNumAppointment_UpdatesTheCorrectValue() {
        appointment.setNumAppointment(99);
        assertEquals(99, appointment.getNumAppointment());
    }

    /**
     * Tests that {@code setNumAppointment()} throws an {@link IllegalArgumentException} when a negative value is passed.
     * Given : a negative value -1
     * When  : setNumAppointment(-1) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setNumAppointment_WithNegativeValue_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> appointment.setNumAppointment(-1));
    }

    // setAppointmentLocals //

    /**
     * Tests that {@code setAppointmentLocals()} correctly updates the list.
     * Given : a non-empty list of locals
     * When  : setAppointmentLocals() is called with this list
     * Then  : getAppointmentLocals() must return the new list
     */
    @Test
    void setAppointmentLocals_UpdatesTheCorrectValue() {
        List<String> locals = new ArrayList<>();
        locals.add("Salle A");
        appointment.setAppointmentLocals(locals);
        assertEquals(locals, appointment.getAppointmentLocals());
    }

    /**
     * Tests that {@code setAppointmentLocals()} throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null list
     * When  : setAppointmentLocals(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAppointmentLocals_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> appointment.setAppointmentLocals(null));
    }

    /**
     * Tests that {@code setAppointmentLocals()} throws an {@link IllegalArgumentException} when an empty list is passed.
     * Given : an empty list
     * When  : setAppointmentLocals() is called with an empty list
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAppointmentLocals_WithEmptyList_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> appointment.setAppointmentLocals(new ArrayList<>()));
    }

    // setBeneficiary //

    /**
     * Tests that {@code setBeneficiary()} throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setBeneficiary(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setBeneficiary_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> appointment.setBeneficiary(null));
    }

    // setInterpreters //

    /**
     * Tests that {@code setInterpreters()} throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null list
     * When  : setInterpreters(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setInterpreters_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> appointment.setInterpreters(null));
    }

    /**
     * Tests that {@code setInterpreters()} throws an {@link IllegalArgumentException} when an empty list is passed.
     * Given : an empty list
     * When  : setInterpreters() is called with an empty list
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setInterpreters_WithEmptyList_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> appointment.setInterpreters(new ArrayList<>()));
    }

    // setTimeSlot //

    /**
     * Tests that {@code setTimeSlot()} throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setTimeSlot(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setTimeSlot_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> appointment.setTimeSlot(null));
    }

    // setStatus //

    /**
     * Tests that {@code setStatus()} correctly updates the status to "accepte".
     * Given : an Appointment with status="en attente"
     * When  : setStatus("accepte") is called
     * Then  : getStatus() must return "accepte"
     */
    @Test
    void setStatus_ToAccepte_UpdatesTheValue() throws BadStatusException {
        appointment.setStatus("accepte");
        assertEquals("accepte", appointment.getStatus());
    }

    /**
     * Tests that {@code setStatus()} correctly updates the status to "refuse".
     * Given : an Appointment with status="en attente"
     * When  : setStatus("refuse") is called
     * Then  : getStatus() must return "refuse"
     */
    @Test
    void setStatus_ToRefuse_UpdatesTheValue() throws BadStatusException {
        appointment.setStatus("refuse");
        assertEquals("refuse", appointment.getStatus());
    }

    /**
     * Tests that {@code setStatus()} throws a {@link BadStatusException} when an invalid status is passed.
     * Given : an invalid status "invalide"
     * When  : setStatus("invalide") is called
     * Then  : a BadStatusException must be thrown
     */
    @Test
    void setStatus_WithInvalidStatus_RaisesABadStatusException() {
        assertThrows(BadStatusException.class, () -> appointment.setStatus("invalide"));
    }

    /**
     * Tests that {@code setStatus()} throws a {@link BadStatusException} when the same status as the current one is passed.
     * Given : an Appointment with status="en attente"
     * When  : setStatus("en attente") is called again
     * Then  : a BadStatusException must be thrown
     */
    @Test
    void setStatus_WithSameStatus_RaisesABadStatusException() {
        assertThrows(BadStatusException.class, () -> appointment.setStatus("en attente"));
    }

    /**
     * Tests that {@code setStatus()} throws a {@link BadStatusException} when trying to change a status that is no longer "en attente".
     * Given : an Appointment whose status has already been set to "accepte"
     * When  : setStatus("refuse") is called
     * Then  : a BadStatusException must be thrown
     */
    @Test
    void setStatus_WhenStatusAlreadyFinal_RaisesABadStatusException() throws BadStatusException {
        appointment.setStatus("accepte");
        assertThrows(BadStatusException.class, () -> appointment.setStatus("refuse"));
    }

    /**
     * Tests that {@code setStatus()} throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setStatus(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setStatus_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> appointment.setStatus(null));
    }

    // toString //

    /**
     * Tests that {@code toString()} contains the label "Rendez-vous".
     * Given : a valid Appointment
     * When  : toString() is called
     * Then  : the result must contain "Rendez-vous"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(appointment.toString().contains("Rendez-vous"));
    }

    /**
     * Tests that {@code toString()} contains the current status.
     * Given : an Appointment with status="en attente"
     * When  : toString() is called
     * Then  : the result must contain "en attente"
     */
    @Test
    void toString_ContainsStatus() {
        assertTrue(appointment.toString().contains("en attente"));
    }
}