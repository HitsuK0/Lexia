package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.*;
import be.hers.info.ProjetIntegree.POJO.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOAbsence}.
 * Verifies the correct behaviour of find, findAll, create (with and without interpreter),
 * update, delete, findPunctualAbsencesInterpreter and findBaseAbsencesInterpreter.
 * All tests run in a single transaction that is rolled back after all tests, so no data is persisted in the database.
 *
 * Important notes:
 * - Absence requires a TimeSlot (either TimeSlotBase or TimeSlotPunctual) already in the database.
 *   A TimeSlotPunctual and a TimeSlotBase are inserted in setUp() and reused across tests.
 * - create(Absence) inserts without an interpreter (FKnumInterpreter = NULL).
 * - create(Absence, int) inserts with a linked interpreter.
 * - find() loads the linked TimeSlot eagerly (via DAOTimeSlotBase or DAOTimeSlotPunctual).
 * - findPunctualAbsencesInterpreter() and findBaseAbsencesInterpreter() require a linked
 *   Interpreter already inserted in the database — an Interpreter is inserted in setUp().
 * - status must be one of: 'en attente', 'accepte', 'refuse' (DB constraint).
 * - The DB constraint requires either FKTimeSlotBase OR FKTimeSlotPunctual to be set, not both.
 * - The address with ID 1 must already exist in the database.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOAbsenceTest {

    private static DAOAbsence daoAbsence;
    private static DAOInterpreter daoInterpreter;
    private static DAOTimeSlotPunctual daoTimeSlotPunctual;
    private static DAOTimeSlotBase daoTimeSlotBase;
    private static Connection connect;

    private static Absence absenceTest;
    private static TimeSlotPunctual timeSlotPunctualTest;
    private static TimeSlotBase timeSlotBaseTest;
    private static Interpreter interpreterTest;
    private static Address address;

    // Set Up //

    /**
     * Initializes the DAOs, disables auto-commit to allow rollback after all tests,
     * inserts a test TimeSlotPunctual, a test TimeSlotBase, a test Interpreter, and a test Absence linked to the TimeSlotPunctual.
     * The address with ID 1 must already exist in the database.
     */
    @BeforeAll
    public static void setUp() throws SQLException, BadStatusException {
        daoAbsence = new DAOAbsence();
        daoInterpreter = new DAOInterpreter();
        daoTimeSlotPunctual = new DAOTimeSlotPunctual();
        daoTimeSlotBase = new DAOTimeSlotBase();
        connect = daoAbsence.connect;
        connect.setAutoCommit(false);

        DAOAddress daoAddress = new DAOAddress();
        address = daoAddress.find(1);

        timeSlotPunctualTest = new TimeSlotPunctual(
                LocalTime.of(9, 0), LocalTime.of(1, 0),
                LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 12)
        );
        daoTimeSlotPunctual.create(timeSlotPunctualTest);

        timeSlotBaseTest = new TimeSlotBase(LocalTime.of(10, 0), LocalTime.of(1, 0), 1);
        daoTimeSlotBase.create(timeSlotBaseTest);

        interpreterTest = new Interpreter(
                "absLogin", "absPassword", "AbsLastName", "AbsFirstName",
                "0477000099", "abs@mail.be", 38, address
        );
        daoInterpreter.create(interpreterTest);

        absenceTest = new Absence(timeSlotPunctualTest);
        daoAbsence.create(absenceTest);
    }

    /**
     * Rolls back all changes made during the tests and restores auto-commit.
     */
    @AfterAll
    public static void tearDown() throws SQLException {
        if (connect != null && !connect.isClosed()) {
            connect.rollback();
            connect.setAutoCommit(true);
        }
    }

    // create(Absence) — without interpreter //

    /**
     * Tests that create(Absence) returns true when a valid Absence with a TimeSlotPunctual is inserted.
     * Given : a valid Absence linked to an existing TimeSlotPunctual and no interpreter
     * When  : create(Absence) is called
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidAbsenceWithPunctualSlot_ReturnsTrue() throws SQLException {
        Absence absence = new Absence(timeSlotPunctualTest);
        boolean result = daoAbsence.create(absence);
        assertTrue(result);
    }

    /**
     * Tests that create(Absence) sets the generated numAbsence on the object after insertion.
     * Given : a valid Absence linked to an existing TimeSlotPunctual
     * When  : create(Absence) is called
     * Then  : getNumAbsence() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidAbsence_SetsGeneratedIdOnObject() throws SQLException {
        Absence absence = new Absence(timeSlotPunctualTest);
        daoAbsence.create(absence);
        assertTrue(absence.getNumAbsence() > 0);
    }

    /**
     * Tests that create(Absence) correctly inserts an Absence linked to a TimeSlotBase.
     * Given : a valid Absence linked to an existing TimeSlotBase and no interpreter
     * When  : create(Absence) is called
     * Then  : the result must be true
     */
    @Test
    @Order(3)
    public void create_GivenValidAbsenceWithBaseSlot_ReturnsTrue() throws SQLException {
        Absence absence = new Absence(timeSlotBaseTest);
        boolean result = daoAbsence.create(absence);
        assertTrue(result);
    }

    // create(Absence, int) — with interpreter //

    /**
     * Tests that create(Absence, int) returns true when a valid Absence is inserted with an interpreter.
     * Given : a valid Absence linked to a TimeSlotPunctual and a valid interpreter ID
     * When  : create(Absence, int) is called with the interpreter's numInterpreter
     * Then  : the result must be true
     */
    @Test
    @Order(4)
    public void createWithInterpreter_GivenValidAbsenceAndInterpreter_ReturnsTrue() throws SQLException {
        Absence absence = new Absence(timeSlotPunctualTest);
        boolean result = daoAbsence.create(absence, interpreterTest.getNumInterpreter());
        assertTrue(result);
    }

    /**
     * Tests that create(Absence, int) sets the generated numAbsence on the object after insertion.
     * Given : a valid Absence linked to a TimeSlotPunctual and a valid interpreter ID
     * When  : create(Absence, int) is called
     * Then  : getNumAbsence() must return a value greater than 0
     */
    @Test
    @Order(5)
    public void createWithInterpreter_GivenValidAbsenceAndInterpreter_SetsGeneratedIdOnObject() throws SQLException {
        Absence absence = new Absence(timeSlotPunctualTest);
        daoAbsence.create(absence, interpreterTest.getNumInterpreter());
        assertTrue(absence.getNumAbsence() > 0);
    }

    // find //

    /**
     * Tests that find() returns the correct Absence when an existing ID is passed.
     * Given : an Absence inserted in setUp()
     * When  : find() is called with its generated numAbsence
     * Then  : the result must not be null and its numAbsence must match
     */
    @Test
    @Order(6)
    public void find_GivenExistingId_ReturnsMatchingAbsence() throws SQLException {
        int existingId = absenceTest.getNumAbsence();
        Absence result = daoAbsence.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumAbsence());
    }

    /**
     * Tests that find() returns an Absence with the correct status.
     * Given : an Absence inserted in setUp() with status="en attente"
     * When  : find() is called with its numAbsence
     * Then  : getStatus() must return "en attente"
     */
    @Test
    @Order(7)
    public void find_GivenExistingId_ReturnsCorrectStatus() throws SQLException {
        Absence result = daoAbsence.find(absenceTest.getNumAbsence());
        assertNotNull(result);
        assertEquals("en attente", result.getStatus());
    }

    /**
     * Tests that  find() loads the TimeSlot of the Absence eagerly.
     * Given : an Absence inserted in setUp() linked to a TimeSlotPunctual
     * When  : find() is called with its numAbsence
     * Then  : getTimeSlot() must not be null
     */
    @Test
    @Order(8)
    public void find_GivenExistingId_LoadsTimeSlotEagerly() throws SQLException {
        Absence result = daoAbsence.find(absenceTest.getNumAbsence());
        assertNotNull(result);
        assertNotNull(result.getTimeSlot());
    }

    /**
     * Tests that find() loads the correct type of TimeSlot.
     * Given : an Absence inserted in setUp() linked to a TimeSlotPunctual
     * When  : find() is called with its numAbsence
     * Then  : getTimeSlot() must be an instance of TimeSlotPunctual
     */
    @Test
    @Order(9)
    public void find_GivenAbsenceWithPunctualSlot_LoadsTimeSlotPunctual() throws SQLException {
        Absence result = daoAbsence.find(absenceTest.getNumAbsence());
        assertNotNull(result);
        assertInstanceOf(TimeSlotPunctual.class, result.getTimeSlot());
    }

    /**
     * Tests that find() returns null when a non-existing ID is passed.
     * Given : a non-existing numAbsence -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(10)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        Absence result = daoAbsence.find(-1);
        assertNull(result);
    }

    // findAll //

    /**
     * Tests that findAll() never returns null.
     * Given : the Absence table contains at least the absence inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(11)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<Absence> result = daoAbsence.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that findAll() returns a list containing the absence inserted in setUp().
     * Given : an Absence was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted absence
     */
    @Test
    @Order(12)
    public void findAll_GivenAbsenceWasInserted_ListContainsInsertedAbsence() throws SQLException {
        List<Absence> result = daoAbsence.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(a -> a.getNumAbsence() == absenceTest.getNumAbsence());
        assertTrue(found);
    }

    // update //

    /**
     * Tests that update() returns true when an existing Absence is updated.
     * Given : the Absence inserted in setUp() with status updated to "accepte"
     * When  : update() is called with this Absence
     * Then  : the result must be true
     */
    @Test
    @Order(13)
    public void update_GivenExistingAbsence_ReturnsTrue() throws SQLException, BadStatusException {
        absenceTest.setStatus("accepte");
        boolean result = daoAbsence.update(absenceTest);
        assertTrue(result);
    }

    /**
     * Tests that update() persists the status change in the database.
     * Given : the Absence inserted in setUp() with status updated to "refuse"
     * When  : update() is called and then find() is called with the same numAbsence
     * Then  : getStatus() must return "refuse"
     */
    @Test
    @Order(14)
    public void update_GivenExistingAbsence_StatusChangesShouldBePersisted() throws SQLException, BadStatusException {
        absenceTest.setStatus("refuse");
        daoAbsence.update(absenceTest);
        Absence result = daoAbsence.find(absenceTest.getNumAbsence());
        assertNotNull(result);
        assertEquals("refuse", result.getStatus());
    }

    /**
     * Tests that update() persists the reason change in the database.
     * Given : the Absence inserted in setUp() with reason updated to "Maladie"
     * When  : update() is called and then find() is called with the same numAbsence
     * Then  : getReason() must return "Maladie"
     */
    @Test
    @Order(15)
    public void update_GivenExistingAbsence_ReasonChangesShouldBePersisted() throws SQLException {
        absenceTest.setReason("Maladie");
        daoAbsence.update(absenceTest);
        Absence result = daoAbsence.find(absenceTest.getNumAbsence());
        assertNotNull(result);
        assertEquals("Maladie", result.getReason());
    }

    /**
     * Tests that update() returns false when a non-existing Absence is passed.
     * Given : an Absence with a non-existing numAbsence 0
     * When  : update() is called with this Absence
     * Then  : the result must be false
     */
    @Test
    @Order(16)
    public void update_GivenNonExistingAbsence_ReturnsFalse() throws SQLException {
        Absence nonExisting = new Absence(timeSlotPunctualTest);
        nonExisting.setNumAbsence(0);
        boolean result = daoAbsence.update(nonExisting);
        assertFalse(result);
    }

    // delete //

    /**
     * Tests that delete() returns true when an existing Absence is deleted.
     * Given : a valid Absence inserted just before deletion
     * When  : delete() is called with this Absence
     * Then  : the result must be true
     */
    @Test
    @Order(17)
    public void delete_GivenExistingAbsence_ReturnsTrue() throws SQLException {
        Absence absenceToDelete = new Absence(timeSlotPunctualTest);
        daoAbsence.create(absenceToDelete);
        boolean result = daoAbsence.delete(absenceToDelete);
        assertTrue(result);
    }

    /**
     * Tests that delete() removes the Absence from the database.
     * Given : a valid Absence inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted numAbsence
     * Then  : find() must return null
     */
    @Test
    @Order(18)
    public void delete_GivenExistingAbsence_ObjectNoLongerExistsInDatabase() throws SQLException {
        Absence absenceToDelete = new Absence(timeSlotPunctualTest);
        daoAbsence.create(absenceToDelete);
        int deletedId = absenceToDelete.getNumAbsence();
        daoAbsence.delete(absenceToDelete);
        Absence result = daoAbsence.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that delete() returns false when a non-existing Absence is passed.
     * Given : an Absence with a non-existing numAbsence 0
     * When  : delete() is called with this Absence
     * Then  : the result must be false
     */
    @Test
    @Order(19)
    public void delete_GivenNonExistingAbsence_ReturnsFalse() throws SQLException {
        Absence nonExisting = new Absence(timeSlotPunctualTest);
        nonExisting.setNumAbsence(0);
        boolean result = daoAbsence.delete(nonExisting);
        assertFalse(result);
    }

    // findPunctualAbsencesInterpreter //

    /**
     * Tests that findPunctualAbsencesInterpreter() never returns null.
     * Given : a valid interpreter and a date range
     * When  : findPunctualAbsencesInterpreter() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(20)
    public void findPunctualAbsencesInterpreter_GivenValidInterpreterAndDateRange_DoesNotReturnNull() throws SQLException, BadStatusException {
        List<Absence> result = daoAbsence.findPunctualAbsencesInterpreter(interpreterTest, "2026-06-01", "2026-06-30");
        assertNotNull(result);
    }

    /**
     * Tests that findPunctualAbsencesInterpreter() returns a list containing the absence
     * inserted with the interpreter in setUp().
     * Given : an Absence linked to interpreterTest and timeSlotPunctualTest (startDate=2026-06-10)
     *         and a date range covering 2026-06-01 to 2026-06-30
     * When  : findPunctualAbsencesInterpreter() is called
     * Then  : the list must contain the inserted absence
     */
    @Test
    @Order(21)
    public void findPunctualAbsencesInterpreter_GivenAbsenceInRange_ListContainsAbsence() throws SQLException, BadStatusException {
        Absence absenceWithInterpreter = new Absence(timeSlotPunctualTest);
        daoAbsence.create(absenceWithInterpreter, interpreterTest.getNumInterpreter());

        List<Absence> result = daoAbsence.findPunctualAbsencesInterpreter(interpreterTest, "2026-06-01", "2026-06-30");
        boolean found = result.stream()
                .anyMatch(a -> a.getNumAbsence() == absenceWithInterpreter.getNumAbsence());
        assertTrue(found);
    }

    /**
     * Tests that findPunctualAbsencesInterpreter() returns an empty list
     * when the date range does not cover any absence.
     * Given : an interpreter with an absence on 2026-06-10 and a date range of 2020-01-01 to 2020-01-31
     * When  : findPunctualAbsencesInterpreter() is called with this out-of-range date range
     * Then  : the result must be empty
     */
    @Test
    @Order(22)
    public void findPunctualAbsencesInterpreter_GivenOutOfRangeDates_ReturnsEmptyList() throws SQLException, BadStatusException {
        List<Absence> result = daoAbsence.findPunctualAbsencesInterpreter(interpreterTest, "2020-01-01", "2020-01-31");
        assertTrue(result.isEmpty());
    }

    // findBaseAbsencesInterpreter //

    /**
     * Tests that findBaseAbsencesInterpreter() never returns null.
     * Given : a valid interpreter
     * When  : findBaseAbsencesInterpreter() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(23)
    public void findBaseAbsencesInterpreter_GivenValidInterpreter_DoesNotReturnNull() throws SQLException, BadStatusException {
        List<Absence> result = daoAbsence.findBaseAbsencesInterpreter(interpreterTest);
        assertNotNull(result);
    }

    /**
     * Tests that findBaseAbsencesInterpreter() returns a list containing the base absence
     * inserted with the interpreter.
     * Given : an Absence linked to interpreterTest and timeSlotBaseTest
     * When  : findBaseAbsencesInterpreter() is called
     * Then  : the list must contain the inserted absence
     */
    @Test
    @Order(24)
    public void findBaseAbsencesInterpreter_GivenBaseAbsenceLinkedToInterpreter_ListContainsAbsence() throws SQLException, BadStatusException {
        Absence absenceWithBase = new Absence(timeSlotBaseTest);
        daoAbsence.create(absenceWithBase, interpreterTest.getNumInterpreter());

        List<Absence> result = daoAbsence.findBaseAbsencesInterpreter(interpreterTest);
        boolean found = result.stream()
                .anyMatch(a -> a.getNumAbsence() == absenceWithBase.getNumAbsence());
        assertTrue(found);
    }
}