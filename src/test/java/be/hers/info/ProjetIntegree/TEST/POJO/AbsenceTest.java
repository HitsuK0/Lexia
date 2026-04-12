package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.LocalTime;

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



}
