package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link AcademicSkill}.
 * Verifies the correct behavior of constructors, getters, setters and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */

public class AcademicSkillTest {
    private AcademicSkill skill;

    // Set Up //

    /**
     * Initializes a default {@link AcademicSkill} instance with the designation "Mathématiques" before each test.
     */
    @BeforeEach
    void setUp(){
        skill = new AcademicSkill("Mathématiques");
    }

    // Constructor with parameter //

    /**
     * Tests that the parameterized constructor correctly sets the designation when a valid non-null string is provided.
     * Given : a valid designation "Informatique"
     * When  : an AcademicSkill is created with this designation
     * Then  : getDesignation() must return "Informatique"
     */
    @Test
    void constructor_WithValidDesignation() {
        AcademicSkill s = new AcademicSkill("Informatique");
        assertEquals("Informatique", s.getDesignation());
    }

    /**
     * Tests that the parameterized constructor throws an {@link IllegalArgumentException} when null is passed as the designation.
     * Given : a null designation
     * When  : an AcademicSkill is created with null
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullDesignationRaiseAnException() {
        assertThrows(IllegalArgumentException.class, () -> new AcademicSkill(null));
    }

    // Default Constructor //

    /**
     * Tests that the default constructor initializes the designation to an empty string.
     * Given : no argument
     * When  : an AcademicSkill is created with the default constructor
     * Then  : getDesignation() must return ""
     */
    @Test
    void defaultConstructor() {
        AcademicSkill s = new AcademicSkill();
        assertEquals("", s.getDesignation());
    }

    // getDesignation //

    /**
     * Tests that {@code getDesignation()} returns the designation that was set during initialization.
     * Given : an AcademicSkill initialized with "Mathématiques"
     * When  : getDesignation() is called
     * Then  : the returned value must equal "Mathématiques"
     */
    @Test
    void getDesignation_ReturnTheCorrectValue() {
        assertEquals("Mathématiques", skill.getDesignation());
    }

    // setDesignation //

    /**
     * Tests that {@code setDesignation()} correctly updates the designation when a valid non-null string is provided.
     * Given : an AcademicSkill initialized with "Mathématiques"
     * When  : setDesignation("Physique") is called
     * Then  : getDesignation() must return "Physique"
     */
    @Test
    void setDesignation_ChangeTheCorrectValue() {
        skill.setDesignation("Physique");
        assertEquals("Physique", skill.getDesignation());
    }

    /**
     * Tests that {@code setDesignation()} throws an {@link IllegalArgumentException} when null is passed as the new designation.
     * Given : a null value
     * When  : setDesignation(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setDesignation_WithNullRaiseAnException() {
        assertThrows(IllegalArgumentException.class, () -> skill.setDesignation(null));
    }

    /**
     * Tests that {@code setDesignation()} accepts an empty string without throwing any exception, and that the designation is correctly updated to "".
     * Given : an empty string ""
     * When  : setDesignation("") is called
     * Then  : no exception is thrown and getDesignation() must return ""
     */
    @Test
    void setDesignation_EmptyStringIsAccept() {
        assertDoesNotThrow(() -> skill.setDesignation(""));
        assertEquals("", skill.getDesignation());
    }

    // toString //

    /**
     * Tests that {@code toString()} includes the current designation in its output.
     * Given : an AcademicSkill initialized with "Mathématiques"
     * When  : toString() is called
     * Then  : the result must contain "Mathématiques"
     */
    @Test
    void toString_ContainsDesignation() {
        assertTrue(skill.toString().contains("Mathématiques"));
    }

    /**
     * Tests that {@code toString()} includes the expected label "Compétence académique".
     * Given : an AcademicSkill initialized with "Mathématiques"
     * When  : toString() is called
     * Then  : the result must contain "Compétence académique"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(skill.toString().contains("Compétence académique"));
    }

    /**
     * Tests that {@code toString()} returns the exact expected formatted string.
     * Given : an AcademicSkill initialized with "Mathématiques"
     * When  : toString() is called
     * Then  : the result must exactly equal "Compétence académique\nDesignation : Mathématiques\n"
     */
    @Test
    void toString_ContainsCompleteFormat() {
        String s = "Compétence académique\nDesignation : Mathématiques\n";
        assertEquals(s, skill.toString());
    }
}