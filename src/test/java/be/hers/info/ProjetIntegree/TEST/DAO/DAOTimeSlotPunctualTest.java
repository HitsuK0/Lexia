package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.DAOTimeSlotPunctual;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOTimeSlotPunctual}.
 * Verifies the correct behaviour of find, findAll, create, update and delete operations.
 * All tests run in a single transaction that is rolled back after all tests, so no data is persisted in the database.
 *
 * Important notes:
 * - The DAO requires both startDate and endDate to be non-null on insertion
 *   (the constructor with only startDate stores endDate as null, but the DB column
 *   is NOT NULL — always use the constructor with both dates for DAO tests).
 * - startTime and duration are stored as TIMESTAMP in Oracle using the startDate
 *   as the date part. Only the time part is relevant on retrieval.
 * - startDate and endDate are stored as DATE in Oracle (time part is zeroed out).
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOTimeSlotPunctualTest {

    private static DAOTimeSlotPunctual daoTimeSlotPunctual;
    private static Connection connect;
    private static TimeSlotPunctual timeSlotPunctualTest;

    private static final LocalTime START_TIME   = LocalTime.of(9, 0);
    private static final LocalTime DURATION     = LocalTime.of(1, 0);
    private static final LocalDate START_DATE   = LocalDate.of(2026, 6, 10);
    private static final LocalDate END_DATE     = LocalDate.of(2026, 6, 12);

    // Set Up //

    /**
     * Initializes the DAO, disables auto-commit to allow rollback after all tests, and inserts a test TimeSlotPunctual.
     */
    @BeforeAll
    public static void setUp() throws SQLException {
        daoTimeSlotPunctual = new DAOTimeSlotPunctual();
        connect = daoTimeSlotPunctual.connect;
        connect.setAutoCommit(false);

        timeSlotPunctualTest = new TimeSlotPunctual(START_TIME, DURATION, START_DATE, END_DATE);
        daoTimeSlotPunctual.create(timeSlotPunctualTest);
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

    // create //

    /**
     * Tests that create() returns true when a valid TimeSlotPunctual is inserted.
     * Given : a valid TimeSlotPunctual with startTime=10:00, duration=02:00,
     *         startDate=2026-07-01 and endDate=2026-07-03
     * When  : create() is called with this TimeSlotPunctual
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidTimeSlotPunctual_ReturnsTrue() throws SQLException {
        TimeSlotPunctual tsp = new TimeSlotPunctual(
                LocalTime.of(10, 0), LocalTime.of(2, 0),
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3)
        );
        boolean result = daoTimeSlotPunctual.create(tsp);
        assertTrue(result);
    }

    /**
     * Tests that create() sets the generated numTimeSlot on the object after insertion.
     * Given : a valid TimeSlotPunctual with startTime=11:00, duration=01:00,
     *         startDate=2026-08-01 and endDate=2026-08-05
     * When  : create() is called with this TimeSlotPunctual
     * Then  : getNumTimeSlot() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidTimeSlotPunctual_SetsGeneratedIdOnObject() throws SQLException {
        TimeSlotPunctual tsp = new TimeSlotPunctual(
                LocalTime.of(11, 0), LocalTime.of(1, 0),
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5)
        );
        daoTimeSlotPunctual.create(tsp);
        assertTrue(tsp.getNumTimeSlot() > 0);
    }

    // find //

    /**
     * Tests that find() returns the correct TimeSlotPunctual when an existing ID is passed.
     * Given : a TimeSlotPunctual inserted in setUp()
     * When  : find() is called with its generated numTimeSlot
     * Then  : the result must not be null and its numTimeSlot must match
     */
    @Test
    @Order(3)
    public void find_GivenExistingId_ReturnsMatchingTimeSlotPunctual() throws SQLException {
        int existingId = timeSlotPunctualTest.getNumTimeSlot();
        TimeSlotPunctual result = daoTimeSlotPunctual.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumTimeSlot());
    }

    /**
     * Tests that find() returns a TimeSlotPunctual with the correct time fields.
     * Given : a TimeSlotPunctual inserted in setUp() with startTime=09:00 and duration=01:00
     * When  : find() is called with its numTimeSlot
     * Then  : getStartTime() and getDuration() must match the inserted values
     */
    @Test
    @Order(4)
    public void find_GivenExistingId_ReturnsCorrectTimeFields() throws SQLException {
        TimeSlotPunctual result = daoTimeSlotPunctual.find(timeSlotPunctualTest.getNumTimeSlot());
        assertNotNull(result);
        assertEquals(START_TIME, result.getStartTime());
        assertEquals(DURATION, result.getDuration());
    }

    /**
     * Tests that find() returns a TimeSlotPunctual with the correct date fields.
     * Given : a TimeSlotPunctual inserted in setUp() with startDate=2026-06-10 and endDate=2026-06-12
     * When  : find() is called with its numTimeSlot
     * Then  : getStartDate() and getEndDate() must match the inserted values
     */
    @Test
    @Order(5)
    public void find_GivenExistingId_ReturnsCorrectDateFields() throws SQLException {
        TimeSlotPunctual result = daoTimeSlotPunctual.find(timeSlotPunctualTest.getNumTimeSlot());
        assertNotNull(result);
        assertEquals(START_DATE, result.getStartDate());
        assertEquals(END_DATE, result.getEndDate());
    }

    /**
     * Tests that find() returns null when a non-existing ID is passed.
     * Given : a non-existing numTimeSlot -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(6)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        TimeSlotPunctual result = daoTimeSlotPunctual.find(-1);
        assertNull(result);
    }

    // findAll //

    /**
     * Tests that findAll() never returns null.
     * Given : the TimeSlotPunctual table contains at least the slot inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(7)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<TimeSlotPunctual> result = daoTimeSlotPunctual.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that findAll() returns a list containing the slot inserted in setUp().
     * Given : a TimeSlotPunctual was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted slot
     */
    @Test
    @Order(8)
    public void findAll_GivenSlotWasInserted_ListContainsInsertedSlot() throws SQLException {
        List<TimeSlotPunctual> result = daoTimeSlotPunctual.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(tsp -> tsp.getNumTimeSlot() == timeSlotPunctualTest.getNumTimeSlot());
        assertTrue(found);
    }

    // update //

    /**
     * Tests that update() returns true when an existing TimeSlotPunctual is updated.
     * Given : the TimeSlotPunctual inserted in setUp() with startTime updated to 14:00
     * When  : update() is called with this TimeSlotPunctual
     * Then  : the result must be true
     */
    @Test
    @Order(9)
    public void update_GivenExistingTimeSlotPunctual_ReturnsTrue() throws SQLException {
        timeSlotPunctualTest.setStartTime(LocalTime.of(14, 0));
        boolean result = daoTimeSlotPunctual.update(timeSlotPunctualTest);
        assertTrue(result);
    }

    /**
     * Tests that update() persists all field changes in the database.
     * Given : the TimeSlotPunctual inserted in setUp() with startDate updated to 2026-09-01
     *         and endDate updated to 2026-09-05
     * When  : update() is called and then find() is called with the same numTimeSlot
     * Then  : getStartDate() must return 2026-09-01 and getEndDate() must return 2026-09-05
     */
    @Test
    @Order(10)
    public void update_GivenExistingTimeSlotPunctual_DateChangesShouldBePersisted() throws SQLException {
        LocalDate newStartDate = LocalDate.of(2026, 9, 1);
        LocalDate newEndDate   = LocalDate.of(2026, 9, 5);
        timeSlotPunctualTest.setEndDate(newEndDate);
        timeSlotPunctualTest.setStartDate(newStartDate);
        daoTimeSlotPunctual.update(timeSlotPunctualTest);

        TimeSlotPunctual result = daoTimeSlotPunctual.find(timeSlotPunctualTest.getNumTimeSlot());
        assertNotNull(result);
        assertEquals(newStartDate, result.getStartDate());
        assertEquals(newEndDate, result.getEndDate());
    }

    /**
     * Tests that update() returns false when a non-existing TimeSlotPunctual is passed.
     * Given : a TimeSlotPunctual with a non-existing numTimeSlot 0
     * When  : update() is called with this TimeSlotPunctual
     * Then  : the result must be false
     */
    @Test
    @Order(11)
    public void update_GivenNonExistingTimeSlotPunctual_ReturnsFalse() throws SQLException {
        TimeSlotPunctual nonExisting = new TimeSlotPunctual(
                LocalTime.of(8, 0), LocalTime.of(1, 0),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2)
        );
        nonExisting.setNumTimeSlot(0);
        boolean result = daoTimeSlotPunctual.update(nonExisting);
        assertFalse(result);
    }

    // delete //

    /**
     * Tests that delete() returns true when an existing TimeSlotPunctual is deleted.
     * Given : a valid TimeSlotPunctual inserted just before deletion
     * When  : delete() is called with this TimeSlotPunctual
     * Then  : the result must be true
     */
    @Test
    @Order(12)
    public void delete_GivenExistingTimeSlotPunctual_ReturnsTrue() throws SQLException {
        TimeSlotPunctual tspToDelete = new TimeSlotPunctual(
                LocalTime.of(7, 0), LocalTime.of(1, 0),
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 3)
        );
        daoTimeSlotPunctual.create(tspToDelete);
        boolean result = daoTimeSlotPunctual.delete(tspToDelete);
        assertTrue(result);
    }

    /**
     * Tests that delete() removes the TimeSlotPunctual from the database.
     * Given : a valid TimeSlotPunctual inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted numTimeSlot
     * Then  : find() must return null
     */
    @Test
    @Order(13)
    public void delete_GivenExistingTimeSlotPunctual_ObjectNoLongerExistsInDatabase() throws SQLException {
        TimeSlotPunctual tspToDelete = new TimeSlotPunctual(
                LocalTime.of(16, 0), LocalTime.of(2, 0),
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 4)
        );
        daoTimeSlotPunctual.create(tspToDelete);
        int deletedId = tspToDelete.getNumTimeSlot();
        daoTimeSlotPunctual.delete(tspToDelete);
        TimeSlotPunctual result = daoTimeSlotPunctual.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that delete() returns false when a non-existing TimeSlotPunctual is passed.
     * Given : a TimeSlotPunctual with a non-existing numTimeSlot 0
     * When  : delete() is called with this TimeSlotPunctual
     * Then  : the result must be false
     */
    @Test
    @Order(14)
    public void delete_GivenNonExistingTimeSlotPunctual_ReturnsFalse() throws SQLException {
        TimeSlotPunctual nonExisting = new TimeSlotPunctual(
                LocalTime.of(8, 0), LocalTime.of(1, 0),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2)
        );
        nonExisting.setNumTimeSlot(0);
        boolean result = daoTimeSlotPunctual.delete(nonExisting);
        assertFalse(result);
    }
}