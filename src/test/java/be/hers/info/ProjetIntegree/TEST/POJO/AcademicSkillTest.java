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

    @BeforeEach
    void setUp(){
        skill = new AcademicSkill("Mathématiques");
    }

    // Constructor with parameter //

    @Test
    void constructorWithValidDesignation() {
        AcademicSkill a = new AcademicSkill("Informatique");
        assertEquals("Informatique", a.getDesignation());
    }

    @Test
    void constructorWithNullDesignationRaiseException() {
        assertThrows(IllegalArgumentException.class, () -> new AcademicSkill(null));
    }

    // Default Constructor //

    @Test
    void defaultConstructor() {
        AcademicSkill s = new AcademicSkill();
        assertEquals("", s.getDesignation());
    }
}