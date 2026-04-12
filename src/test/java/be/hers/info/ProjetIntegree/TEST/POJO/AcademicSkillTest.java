package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */

public class AcademicSkillTest {
    private AcademicSkill skill;

    // Set Up //

    @BeforeEach
    void setUp(){
        skill = new AcademicSkill("Mathématiques");
    }

    // Constructor with parameter //

    @Test
    void constructor_WithValidDesignation() {
        AcademicSkill a = new AcademicSkill("Informatique");
        assertEquals("Informatique", a.getDesignation());
    }

    @Test
    void constructor_WithNullDesignationRaiseAnException() {
        assertThrows(IllegalArgumentException.class, () -> new AcademicSkill(null));
    }

    // Default Constructor //

    @Test
    void defaultConstructor() {
        AcademicSkill s = new AcademicSkill();
        assertEquals("", s.getDesignation());
    }

    // getDesignation //

    @Test
    void getDesignation_ReturnTheCorrectValue() {
        assertEquals("Mathématiques", skill.getDesignation());
    }

    // setDesignation //

    @Test
    void setDesignation_ChangeTheCorrectValue() {
        skill.setDesignation("Physique");
        assertEquals("Physique", skill.getDesignation());
    }

    @Test
    void setDesignation_WithNullRaiseAnException() {
        assertThrows(IllegalArgumentException.class, () -> skill.setDesignation(null));
    }

    @Test
    void setDesignation_EmptyStringIsAccept() {
        assertDoesNotThrow(() -> skill.setDesignation(""));
        assertEquals("", skill.getDesignation());
    }

    // toString //

    @Test
    void toString_ContainsDesignation() {
        assertTrue(skill.toString().contains("Mathématiques"));
    }

    @Test
    void toString_ContainsLabel() {
        assertTrue(skill.toString().contains("Compétence académique"));
    }

    @Test
    void toString_ContainsCompleteFormat() {
        String s = "Compétence académique\nDesignation : Mathématiques\n";
        assertEquals(s, skill.toString());
    }

}