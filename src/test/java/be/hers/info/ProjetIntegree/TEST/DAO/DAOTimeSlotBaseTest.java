package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.DAOTimeSlotBase;
import be.hers.info.ProjetIntegree.POJO.TimeSlotBase;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOTimeSlotBase}.
 * Verifies the correct behaviour of find, findAll, create, update and delete operations.
 * All tests run in a single transaction that is rolled back after all tests, so no data is persisted in the database.
 *
 * Important notes:
 * - startTime and duration are stored as DATE in Oracle and retrieved as Timestamp.
 *   Only the time part is used — the date part is ignored on retrieval.
 * - dayNumber must be between 1 (Monday) and 7 (Sunday) inclusive.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOTimeSlotBaseTest {

    private static DAOTimeSlotBase daoTimeSlotBase;
    private static Connection connect;
    private static TimeSlotBase timeSlotBaseTest;

    // Set Up //

    /**
     * Initializes the DAO, disables auto-commit to allow rollback after all tests, and inserts a test TimeSlotBase.
     */
    @BeforeAll
    public static void setUp() throws SQLException {
        daoTimeSlotBase = new DAOTimeSlotBase();
        connect = daoTimeSlotBase.connect;
        connect.setAutoCommit(false);

        timeSlotBaseTest = new TimeSlotBase(LocalTime.of(9, 0), LocalTime.of(1, 0), 1);
        daoTimeSlotBase.create(timeSlotBaseTest);
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
     * Tests that create() returns true when a valid TimeSlotBase is inserted.
     * Given : a valid TimeSlotBase with startTime=10:00, duration=01:00 and dayNumber=2
     * When  : create() is called with this TimeSlotBase
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidTimeSlotBase_ReturnsTrue() throws SQLException {
        TimeSlotBase tsb = new TimeSlotBase(LocalTime.of(10, 0), LocalTime.of(1, 0), 2);
        boolean result = daoTimeSlotBase.create(tsb);
        assertTrue(result);
    }

    /**
     * Tests that create() sets the generated numTimeSlot on the object after insertion.
     * Given : a valid TimeSlotBase with startTime=11:00, duration=02:00 and dayNumber=3
     * When  : create() is called with this TimeSlotBase
     * Then  : getNumTimeSlot() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidTimeSlotBase_SetsGeneratedIdOnObject() throws SQLException {
        TimeSlotBase tsb = new TimeSlotBase(LocalTime.of(11, 0), LocalTime.of(2, 0), 3);
        daoTimeSlotBase.create(tsb);
        assertTrue(tsb.getNumTimeSlot() > 0);
    }

    // find //

    /**
     * Tests that find() returns the correct TimeSlotBase when an existing ID is passed.
     * Given : a TimeSlotBase inserted in setUp()
     * When  : find() is called with its generated numTimeSlot
     * Then  : the result must not be null and its numTimeSlot must match
     */
    @Test
    @Order(3)
    public void find_GivenExistingId_ReturnsMatchingTimeSlotBase() throws SQLException {
        int existingId = timeSlotBaseTest.getNumTimeSlot();
        TimeSlotBase result = daoTimeSlotBase.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumTimeSlot());
    }

    /**
     * Tests that find() returns a TimeSlotBase with the correct fields.
     * Given : a TimeSlotBase inserted in setUp() with startTime=09:00, duration=01:00 and dayNumber=1
     * When  : find() is called with its numTimeSlot
     * Then  : getStartTime(), getDuration() and getDayNumber() must match the inserted values
     */
    @Test
    @Order(4)
    public void find_GivenExistingId_ReturnsCorrectFields() throws SQLException {
        TimeSlotBase result = daoTimeSlotBase.find(timeSlotBaseTest.getNumTimeSlot());
        assertNotNull(result);
        assertEquals(LocalTime.of(9, 0), result.getStartTime());
        assertEquals(LocalTime.of(1, 0), result.getDuration());
        assertEquals(1, result.getDayNumber());
    }

    /**
     * Tests that find() returns null when a non-existing ID is passed.
     * Given : a non-existing numTimeSlot -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(5)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        TimeSlotBase result = daoTimeSlotBase.find(-1);
        assertNull(result);
    }

    // findAll //

    /**
     * Tests that findAll() never returns null.
     * Given : the TimeSlotBase table contains at least the slot inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(6)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<TimeSlotBase> result = daoTimeSlotBase.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that findAll() returns a list containing the slot inserted in setUp().
     * Given : a TimeSlotBase was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted slot
     */
    @Test
    @Order(7)
    public void findAll_GivenSlotWasInserted_ListContainsInsertedSlot() throws SQLException {
        List<TimeSlotBase> result = daoTimeSlotBase.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(tsb -> tsb.getNumTimeSlot() == timeSlotBaseTest.getNumTimeSlot());
        assertTrue(found);
    }

    // update //

    /**
     * Tests that update() returns true when an existing TimeSlotBase is updated.
     * Given : the TimeSlotBase inserted in setUp() with startTime updated to 14:00
     * When  : update() is called with this TimeSlotBase
     * Then  : the result must be true
     */
    @Test
    @Order(8)
    public void update_GivenExistingTimeSlotBase_ReturnsTrue() throws SQLException {
        timeSlotBaseTest.setStartTime(LocalTime.of(14, 0));
        boolean result = daoTimeSlotBase.update(timeSlotBaseTest);
        assertTrue(result);
    }

    /**
     * Tests that update() persists all changes in the database.
     * Given : the TimeSlotBase inserted in setUp() with startTime=15:00, duration=02:00 and dayNumber=5
     * When  : update() is called and then find() is called with the same numTimeSlot
     * Then  : the found slot must have startTime=15:00, duration=02:00 and dayNumber=5
     */
    @Test
    @Order(9)
    public void update_GivenExistingTimeSlotBase_ChangesShouldBePersisted() throws SQLException {
        timeSlotBaseTest.setStartTime(LocalTime.of(15, 0));
        timeSlotBaseTest.setDuration(LocalTime.of(2, 0));
        timeSlotBaseTest.setDayNumber(5);
        daoTimeSlotBase.update(timeSlotBaseTest);

        TimeSlotBase result = daoTimeSlotBase.find(timeSlotBaseTest.getNumTimeSlot());
        assertNotNull(result);
        assertEquals(LocalTime.of(15, 0), result.getStartTime());
        assertEquals(LocalTime.of(2, 0), result.getDuration());
        assertEquals(5, result.getDayNumber());
    }

    /**
     * Tests that update() returns false when a non-existing TimeSlotBase is passed.
     * Given : a TimeSlotBase with a non-existing numTimeSlot 0
     * When  : update() is called with this TimeSlotBase
     * Then  : the result must be false
     */
    @Test
    @Order(10)
    public void update_GivenNonExistingTimeSlotBase_ReturnsFalse() throws SQLException {
        TimeSlotBase nonExisting = new TimeSlotBase(LocalTime.of(8, 0), LocalTime.of(1, 0), 1);
        nonExisting.setNumTimeSlot(0);
        boolean result = daoTimeSlotBase.update(nonExisting);
        assertFalse(result);
    }

    // delete //

    /**
     * Tests that delete() returns true when an existing TimeSlotBase is deleted.
     * Given : a valid TimeSlotBase inserted just before deletion
     * When  : delete() is called with this TimeSlotBase
     * Then  : the result must be true
     */
    @Test
    @Order(11)
    public void delete_GivenExistingTimeSlotBase_ReturnsTrue() throws SQLException {
        TimeSlotBase tsbToDelete = new TimeSlotBase(LocalTime.of(7, 0), LocalTime.of(1, 0), 2);
        daoTimeSlotBase.create(tsbToDelete);
        boolean result = daoTimeSlotBase.delete(tsbToDelete);
        assertTrue(result);
    }

    /**
     * Tests that delete() removes the TimeSlotBase from the database.
     * Given : a valid TimeSlotBase inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted numTimeSlot
     * Then  : find() must return null
     */
    @Test
    @Order(12)
    public void delete_GivenExistingTimeSlotBase_ObjectNoLongerExistsInDatabase() throws SQLException {
        TimeSlotBase tsbToDelete = new TimeSlotBase(LocalTime.of(16, 0), LocalTime.of(1, 0), 4);
        daoTimeSlotBase.create(tsbToDelete);
        int deletedId = tsbToDelete.getNumTimeSlot();
        daoTimeSlotBase.delete(tsbToDelete);
        TimeSlotBase result = daoTimeSlotBase.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that delete() returns false when a non-existing TimeSlotBase is passed.
     * Given : a TimeSlotBase with a non-existing numTimeSlot 0
     * When  : delete() is called with this TimeSlotBase
     * Then  : the result must be false
     */
    @Test
    @Order(13)
    public void delete_GivenNonExistingTimeSlotBase_ReturnsFalse() throws SQLException {
        TimeSlotBase nonExisting = new TimeSlotBase(LocalTime.of(8, 0), LocalTime.of(1, 0), 1);
        nonExisting.setNumTimeSlot(0);
        boolean result = daoTimeSlotBase.delete(nonExisting);
        assertFalse(result);
    }
}