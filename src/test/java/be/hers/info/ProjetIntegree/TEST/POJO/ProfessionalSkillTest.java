package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.Appointment;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ProfessionalSkill}.
 * Verifies the correct behavior of constructors, getters, setters and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */

public class ProfessionalSkillTest {

    private ProfessionalSkill skill;

    // Set Up //

    /**
     * Initializes a default {@link ProfessionalSkill} instance with the designation "Traduction" before each test.
     */
    @BeforeEach
    void setUp() {
        skill = new ProfessionalSkill("Traduction");
    }

    // Constructors //

    /**
     * Tests that the constructor with designation correctly sets the value.
     * Given : a valid designation "Interprétariat"
     * When  : a ProfessionalSkill is created
     * Then  : getDesignation() must return "Interprétariat"
     */
    @Test
    void constructor_WithValidDesignation() {
        ProfessionalSkill s = new ProfessionalSkill("Interprétariat");
        assertEquals("Interprétariat", s.getDesignation());
        assertTrue(s.getListInterpreters().isEmpty());
        assertTrue(s.getListAppointment().isEmpty());
    }

    /**
     * Tests that constructor with ID throws an exception when ID is negative.
     * Given : a negative ID
     * When  : creating a ProfessionalSkill
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNegativeId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new ProfessionalSkill(-1, "Test"));
    }

    /**
     * Tests that constructor with ID correctly sets values when valid.
     * Given : ID = 1 and designation "Test"
     * When  : creating a ProfessionalSkill
     * Then  : values must be correctly set
     */
    @Test
    void constructor_WithValidId() {
        ProfessionalSkill s = new ProfessionalSkill(1, "Test");
        assertEquals(1, s.getNumProfessionalSkill());
        assertEquals("Test", s.getDesignation());
        assertTrue(s.getListInterpreters().isEmpty());
        assertTrue(s.getListAppointment().isEmpty());
    }

    /**
     * Tests that default constructor initializes empty values.
     * Given : no argument
     * When  : creating a ProfessionalSkill
     * Then  : designation is "" and lists are empty
     */
    @Test
    void defaultConstructor_InitializeEmptyValues() {
        ProfessionalSkill s = new ProfessionalSkill();
        assertEquals("", s.getDesignation());
        assertTrue(s.getListInterpreters().isEmpty());
        assertTrue(s.getListAppointment().isEmpty());
    }

    // getDesignation //

    /**
     * Tests that getDesignation() returns the designation that was set during initialization.
     * Given : an ProfessionalSkill initialized with "Traduction"
     * When  : getDesignation() is called
     * Then  : the returned value must equal "Traduction"
     */
    @Test
    void getDesignation_ReturnCorrectValue() {
        assertEquals("Traduction", skill.getDesignation());
    }

    // setDesignation //

    /**
     * Tests that setDesignation correctly updates the designation.
     * Given : a ProfessionalSkill initialized with "Traduction"
     * When  : setDesignation("Technique") is called
     * Then  : getDesignation() must return "Technique"
     */
    @Test
    void setDesignation_UpdateValue() {
        skill.setDesignation("Technique");
        assertEquals("Technique", skill.getDesignation());
    }

    // ID //

    /**
     * Tests that setNumProfessionalSkill correctly updates the ID.
     * Given : a default ProfessionalSkill
     * When  : setNumProfessionalSkill(10) is called
     * Then  : getNumProfessionalSkill() must return 10
     */
    @Test
    void setNumProfessionalSkill_ValidValue() {
        skill.setNumProfessionalSkill(10);
        assertEquals(10, skill.getNumProfessionalSkill());
    }

    /**
     * Tests that setNumProfessionalSkill throws an exception when a negative value is passed.
     * Given : a default ProfessionalSkill
     * When  : setNumProfessionalSkill(-5) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setNumProfessionalSkill_Negative_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> skill.setNumProfessionalSkill(-5));
    }

    // Lists //

    /**
     * Tests that setListInterpreters correctly replaces the interpreters list.
     * Given : a default ProfessionalSkill and a non-empty list of interpreters
     * When  : setListInterpreters(list) is called
     * Then  : getListInterpreters() must return the same list instance
     */
    @Test
    void setListInterpreters_UpdateList() {
        List<Interpreter> list = new ArrayList<>();
        skill.setListInterpreters(list);
        assertEquals(list, skill.getListInterpreters());
    }

    /**
     * Tests that setAppointment correctly replaces the appointments list.
     * Given : a default ProfessionalSkill and a non-empty list of appointments
     * When  : setAppointment(list) is called
     * Then  : getListAppointment() must return the same list instance
     */
    @Test
    void setAppointment_UpdateList() {
        List<Appointment> list = new ArrayList<>();
        skill.setAppointment(list);
        assertEquals(list, skill.getListAppointment());
    }

    // toString //

    /**
     * Tests that toString() contains the designation.
     * Given : a ProfessionalSkill initialized with designation="Traduction"
     * When  : toString() is called
     * Then  : the result must contain "Traduction"
     */
    @Test
    void toString_ContainsDesignation() {
        assertTrue(skill.toString().contains("Traduction"));
    }

    /**
     * Tests that toString() contains the label "Compétence Métier".
     * Given : a ProfessionalSkill initialized
     * When  : toString() is called
     * Then  : the result must contain "Compétence Métier"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(skill.toString().contains("Compétence Métier"));
    }

    /**
     * Tests that toString() contains the numProfessionalSkill.
     * Given : a ProfessionalSkill initialized with setNumProfessionalSkill(5)
     * When  : toString() is called
     * Then  : the result must contain "5"
     */
    @Test
    void toString_ContainsId() {
        skill.setNumProfessionalSkill(5);
        assertTrue(skill.toString().contains("5"));
    }
}