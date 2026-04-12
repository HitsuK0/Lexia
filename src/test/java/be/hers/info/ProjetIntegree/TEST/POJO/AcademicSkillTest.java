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


    
}