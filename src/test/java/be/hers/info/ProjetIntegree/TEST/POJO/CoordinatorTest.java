package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Coordinator}.
 * Verifies the correct behaviour of constructors, getters, setters and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
public class CoordinatorTest {

    private Coordinator coordinator;
    private Address address;

    // Set Up //

    /**
     * Initializes a valid {@link Address} and a default {@link Coordinator} before each test.
     */
    @BeforeEach
    void setUp() {
        address = new Address();
        coordinator = new Coordinator();
    }

    // Default Constructor //

    /**
     * Tests that the default constructor initializes isAdmin to false.
     * Given : no argument
     * When  : a Coordinator is created with the default constructor
     * Then  : isAdmin() must return false
     */
    @Test
    void defaultConstructor_IsAdminIsFalse() {
        assertFalse(coordinator.isAdmin());
    }

    // Constructor (login, password, lastName, firstName, email, phoneNumber, weeklyWorkHours, address) //

    /**
     * Tests that the constructor without numCoordinator sets all inherited fields correctly.
     * Given : valid login, password, lastName, firstName, email, phoneNumber, weeklyWorkHours and address
     * When  : a Coordinator is created with these arguments
     * Then  : all getters must return the expected values and isAdmin must be false
     */
    @Test
    void constructor_WithoutNumCoordinator_SetsAllFields() {
        Coordinator c = new Coordinator("e0001", "secret", "Nicolas", "JF",
                "JF@mail.be", "0477000000", 38, address);
        assertEquals("e0001", c.getLogin());
        assertEquals("Nicolas", c.getLastName());
        assertEquals("JF", c.getFirstName());
        assertEquals("JF@mail.be", c.getEmail());
        assertEquals("0477000000", c.getPhoneNumber());
        assertEquals(38, c.getWeeklyWorkHours());
        assertSame(address, c.getAddress());
        assertFalse(c.isAdmin());
    }

    // Constructor (numInterpreter, login, password, lastName, firstName, email, phoneNumber, weeklyWorkHours, address) //

    /**
     * Tests that the constructor with numInterpreter sets the interpreter ID correctly.
     * Given : numInterpreter=3 and valid arguments
     * When  : a Coordinator is created with these arguments
     * Then  : getNumInterpreter() must return 3 and isAdmin must be false
     */
    @Test
    void constructor_WithNumInterpreter_SetsNumInterpreterAndIsAdminFalse() {
        Coordinator c = new Coordinator(3, "e0001", "secret", "Nicolas", "JF",
                "JF@mail.be", "0477000000", 38, address);
        assertEquals(3, c.getNumInterpreter());
        assertFalse(c.isAdmin());
    }

    // Constructor (numInterpreter, login, password, ..., lists) without numCoordinator //

    /**
     * Tests that the constructor with lists but without numCoordinator sets all fields correctly.
     * Given : valid numInterpreter, login, password, personal info, address and empty lists
     * When  : a Coordinator is created with these arguments
     * Then  : all list getters must return the provided lists and isAdmin must be false
     */
    @Test
    void constructor_WithListsWithoutNumCoordinator_SetsAllFields() {
        List<Absence> absences = new ArrayList<>();
        List<Appointment> appointments = new ArrayList<>();
        List<ProfessionalSkill> profSkills = new ArrayList<>();
        List<AcademicSkill> acaSkills = new ArrayList<>();
        List<Beneficiary> beneficiaries = new ArrayList<>();

        Coordinator c = new Coordinator(1, "e0001", "secret", "Nicolas", "JF",
                "JF@mail.be", "0477000000", 38, address,
                absences, appointments, profSkills, acaSkills, beneficiaries);

        assertSame(absences, c.getAbsences());
        assertSame(appointments, c.getAppointmentsList());
        assertSame(profSkills, c.getProfessionalSkillsList());
        assertSame(acaSkills, c.getAcademicSkillsList());
        assertSame(beneficiaries, c.getBeneficiariesList());
        assertFalse(c.isAdmin());
    }

    // Constructor complet (numInterpreter, login, password, ..., numCoordinator, isAdmin) //

    /**
     * Tests that the full constructor sets numCoordinator and isAdmin correctly.
     * Given : numCoordinator=10 and isAdmin=true along with all other valid arguments
     * When  : a Coordinator is created with these arguments
     * Then  : getNumCoordinator() must return 10 and isAdmin() must return true
     */
    @Test
    void fullConstructor_SetsNumCoordinatorAndIsAdmin() {
        List<Absence> absences = new ArrayList<>();
        List<Appointment> appointments = new ArrayList<>();
        List<ProfessionalSkill> profSkills = new ArrayList<>();
        List<AcademicSkill> acaSkills = new ArrayList<>();
        List<Beneficiary> beneficiaries = new ArrayList<>();

        Coordinator c = new Coordinator(1, "e0001", "secret", "Nicolas", "JF",
                "JF@mail.be", "0477000000", 38, address,
                absences, appointments, profSkills, acaSkills, beneficiaries, 10, true);

        assertEquals(10, c.getNumCoordinator());
        assertTrue(c.isAdmin());
    }

    /**
     * Tests that the full constructor sets numInterpreter correctly.
     * Given : numInterpreter=5 and numCoordinator=10 along with all other valid arguments
     * When  : a Coordinator is created with these arguments
     * Then  : getNumInterpreter() must return 5
     */
    @Test
    void fullConstructor_SetsNumInterpreter() {
        List<Absence> absences = new ArrayList<>();
        List<Appointment> appointments = new ArrayList<>();
        List<ProfessionalSkill> profSkills = new ArrayList<>();
        List<AcademicSkill> acaSkills = new ArrayList<>();
        List<Beneficiary> beneficiaries = new ArrayList<>();

        Coordinator c = new Coordinator(5, "e0001", "secret", "Nicolas", "JF",
                "JF@mail.be", "0477000000", 38, address,
                absences, appointments, profSkills, acaSkills, beneficiaries, 10, true);

        assertEquals(5, c.getNumInterpreter());
    }

    // setNumCoordinator //

    /**
     * Tests that setNumCoordinator() correctly updates the coordinator number.
     * Given : a default Coordinator with numCoordinator=0
     * When  : setNumCoordinator(5) is called
     * Then  : getNumCoordinator() must return 5
     */
    @Test
    void setNumCoordinator_UpdatesTheCorrectValue() {
        coordinator.setNumCoordinator(5);
        assertEquals(5, coordinator.getNumCoordinator());
    }

    /**
     * Tests that setNumCoordinator() throws an {@link IllegalArgumentException} when a negative value is passed.
     * Given : a negative value -1
     * When  : setNumCoordinator(-1) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setNumCoordinator_WithNegativeValue_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> coordinator.setNumCoordinator(-1));
    }

    /**
     * Tests that setNumCoordinator() accepts 0 as a valid value.
     * Given : a default Coordinator
     * When  : setNumCoordinator(0) is called
     * Then  : getNumCoordinator() must return 0 and no exception must be thrown
     */
    @Test
    void setNumCoordinator_WithZero_DoesNotRaiseAnException() {
        assertDoesNotThrow(() -> coordinator.setNumCoordinator(0));
        assertEquals(0, coordinator.getNumCoordinator());
    }

    // setAdmin //

    /**
     * Tests that setAdmin() correctly updates isAdmin to true.
     * Given : a default Coordinator with isAdmin=false
     * When  : setAdmin(true) is called
     * Then  : isAdmin() must return true
     */
    @Test
    void setAdmin_ToTrue_UpdatesTheValue() {
        coordinator.setAdmin(true);
        assertTrue(coordinator.isAdmin());
    }

    /**
     * Tests that setAdmin() correctly updates isAdmin to false.
     * Given : a Coordinator with isAdmin set to true
     * When  : setAdmin(false) is called
     * Then  : isAdmin() must return false
     */
    @Test
    void setAdmin_ToFalse_UpdatesTheValue() {
        coordinator.setAdmin(true);
        coordinator.setAdmin(false);
        assertFalse(coordinator.isAdmin());
    }

    // toString //

    /**
     * Tests that toString() contains the label "Coordinateur/Coordinatrice".
     * Given : a default Coordinator
     * When  : toString() is called
     * Then  : the result must contain "Coordinateur/Coordinatrice"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(coordinator.toString().contains("Coordinateur/Coordinatrice"));
    }

    /**
     * Tests that toString() contains "Resa" when isAdmin is false.
     * Given : a default Coordinator with isAdmin=false
     * When  : toString() is called
     * Then  : the result must contain "Resa"
     */
    @Test
    void toString_WhenNotAdmin_ContainsResa() {
        assertTrue(coordinator.toString().contains("Resa"));
    }

    /**
     * Tests that toString() contains "principale" when isAdmin is true.
     * Given : a Coordinator with isAdmin=true
     * When  : toString() is called
     * Then  : the result must contain "principale"
     */
    @Test
    void toString_WhenAdmin_ContainsPrincipale() {
        coordinator.setAdmin(true);
        assertTrue(coordinator.toString().contains("principale"));
    }

    /**
     * Tests that toString() contains the numCoordinator.
     * Given : a Coordinator with numCoordinator=42
     * When  : toString() is called
     * Then  : the result must contain "42"
     */
    @Test
    void toString_ContainsNumCoordinator() {
        coordinator.setNumCoordinator(42);
        assertTrue(coordinator.toString().contains("42"));
    }
}