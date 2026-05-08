package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.DAOAddress;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOInterpreter}.
 * Verifies the correct behaviour of find, findAll, create, update and delete operations.
 * All tests run in a single transaction that is rolled back after all tests, so no data is persisted in the database.
 *
 * Important notes about the database constraints:
 * - The trigger trg_hash_password_interprete hashes the password on INSERT/UPDATE,
 *   so getPassword() after a find() will return a hash, not the original plain text.
 * - The trigger trg_generate_login_interpreter auto-generates the login if null is passed.
 * - weeklyWorkHours must be strictly greater than 0 (DB constraint).
 * - emailAddress must match the format x@x.x (DB constraint).
 * - phoneNumber must contain digits only (DB constraint).
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOInterpreterTest {

    private static DAOInterpreter daoInterpreter;
    private static Connection connect;
    private static Interpreter interpreterTest;
    private static Address address;

    // Set Up //

    /**
     * Initializes the DAO, disables auto-commit to allow rollback after all tests,
     * retrieves an existing Address from the database and inserts a test Interpreter.
     * The address with ID 1 must already exist in the database.
     */
    @BeforeAll
    public static void setUp() throws SQLException {
        daoInterpreter = new DAOInterpreter();
        connect = daoInterpreter.connect;
        connect.setAutoCommit(false);

        DAOAddress daoAddress = new DAOAddress();
        address = daoAddress.find(1);

        interpreterTest = new Interpreter(
                "testlogin", "testpassword", "TestLastName", "TestFirstName",
                "0477000000", "test@mail.be", 38, address
        );
        daoInterpreter.create(interpreterTest);
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
     * Tests that create() returns true when a valid Interpreter is inserted.
     * Given : a valid Interpreter with a valid email, phone (digits only) and weeklyWorkHours > 0
     * When  : create() is called with this Interpreter
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidInterpreter_ReturnsTrue() throws SQLException {
        Interpreter interpreter = new Interpreter(
                "loginCreate", "passCreate", "LastCreate", "FirstCreate",
                "0477111111", "create@mail.be", 20, address
        );
        boolean result = daoInterpreter.create(interpreter);
        assertTrue(result);
    }

    /**
     * Tests that create() sets the generated ID on the object after insertion.
     * Given : a valid Interpreter with all required fields
     * When  : create() is called with this Interpreter
     * Then  : getNumInterpreter() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidInterpreter_SetsGeneratedIdOnObject() throws SQLException {
        Interpreter interpreter = new Interpreter(
                "loginId", "passId", "LastId", "FirstId",
                "0477222222", "id@mail.be", 25, address
        );
        daoInterpreter.create(interpreter);
        assertTrue(interpreter.getNumInterpreter() > 0);
    }

    // find //

    /**
     * Tests that find() returns the correct Interpreter when an existing ID is passed.
     * Given : an Interpreter inserted in setUp()
     * When  : find() is called with its generated ID
     * Then  : the result must not be null and its ID must match
     */
    @Test
    @Order(3)
    public void find_GivenExistingId_ReturnsMatchingInterpreter() throws SQLException {
        int existingId = interpreterTest.getNumInterpreter();
        Interpreter result = daoInterpreter.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumInterpreter());
    }

    /**
     * Tests that find() returns an Interpreter with the correct personal fields.
     * Given : an Interpreter inserted in setUp() with known lastName, firstName, email and phone
     * When  : find() is called with its ID
     * Then  : lastName, firstName, emailAddress, phoneNumber and weeklyWorkHours must match the inserted values
     * Note  : password is not checked here because the DB trigger hashes it automatically on INSERT
     */
    @Test
    @Order(4)
    public void find_GivenExistingId_ReturnsInterpreterWithCorrectFields() throws SQLException {
        Interpreter result = daoInterpreter.find(interpreterTest.getNumInterpreter());
        assertNotNull(result);
        assertEquals(interpreterTest.getLastName(), result.getLastName());
        assertEquals(interpreterTest.getFirstName(), result.getFirstName());
        assertEquals(interpreterTest.getEmailAddress(), result.getEmailAddress());
        assertEquals(interpreterTest.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(interpreterTest.getWeeklyWorkHours(), result.getWeeklyWorkHours());
    }

    /**
     * Tests that find() loads the Address of the Interpreter eagerly.
     * Given : an Interpreter inserted in setUp() with a valid Address
     * When  : find() is called with its ID
     * Then  : getAddress() must not be null
     */
    @Test
    @Order(5)
    public void find_GivenExistingId_LoadsAddressEagerly() throws SQLException {
        Interpreter result = daoInterpreter.find(interpreterTest.getNumInterpreter());
        assertNotNull(result);
        assertNotNull(result.getAddress());
    }

    /**
     * Tests that find() returns null when a non-existing ID is passed.
     * Given : a non-existing ID -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(6)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        Interpreter result = daoInterpreter.find(-1);
        assertNull(result);
    }

    // findAll //

    /**
     * Tests that findAll() never returns null.
     * Given : the Interpreter table contains at least the interpreter inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(7)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<Interpreter> result = daoInterpreter.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that findAll() returns a list containing the interpreter inserted in setUp().
     * Given : an Interpreter was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted interpreter
     */
    @Test
    @Order(8)
    public void findAll_GivenInterpreterWasInserted_ListContainsInsertedInterpreter() throws SQLException {
        List<Interpreter> result = daoInterpreter.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(i -> i.getNumInterpreter() == interpreterTest.getNumInterpreter());
        assertTrue(found);
    }

    // update //

    /**
     * Tests that update() returns true when an existing Interpreter is updated.
     * Given : the Interpreter inserted in setUp() with lastName updated to "UpdatedLastName"
     * When  : update() is called with this Interpreter
     * Then  : the result must be true
     */
    @Test
    @Order(9)
    public void update_GivenExistingInterpreter_ReturnsTrue() throws SQLException {
        interpreterTest.setLastName("UpdatedLastName");
        boolean result = daoInterpreter.update(interpreterTest);
        assertTrue(result);
    }

    /**
     * Tests that update() persists the changes in the database.
     * Given : the Interpreter inserted in setUp() with lastName updated to "VerifiedLastName"
     * When  : update() is called and then find() is called with the same ID
     * Then  : the found interpreter must have lastName "VerifiedLastName"
     * Note  : password is not verified here because the DB trigger re-hashes it on UPDATE
     */
    @Test
    @Order(10)
    public void update_GivenExistingInterpreter_ChangesShouldBePersisted() throws SQLException {
        interpreterTest.setLastName("VerifiedLastName");
        daoInterpreter.update(interpreterTest);
        Interpreter result = daoInterpreter.find(interpreterTest.getNumInterpreter());
        assertNotNull(result);
        assertEquals("VerifiedLastName", result.getLastName());
    }

    /**
     * Tests that update() returns false when a non-existing Interpreter is passed.
     * Given : an Interpreter with a non-existing ID -1
     * When  : update() is called with this Interpreter
     * Then  : the result must be false
     */
    @Test
    @Order(11)
    public void update_GivenNonExistingInterpreter_ReturnsFalse() throws SQLException {
        Interpreter nonExisting = new Interpreter(
                "ghostlogin", "ghostpass", "GhostLast", "GhostFirst",
                "0000000000", "ghost@mail.be", 10, address
        );
        nonExisting.setNumInterpreter(-1);
        boolean result = daoInterpreter.update(nonExisting);
        assertFalse(result);
    }

    // delete //

    /**
     * Tests that delete() returns true when an existing Interpreter is deleted.
     * Given : a valid Interpreter inserted just before deletion
     * When  : delete() is called with this Interpreter
     * Then  : the result must be true
     */
    @Test
    @Order(12)
    public void delete_GivenExistingInterpreter_ReturnsTrue() throws SQLException {
        Interpreter interpreterToDelete = new Interpreter(
                "loginDel", "passDel", "LastDel", "FirstDel",
                "0477333333", "del@mail.be", 15, address
        );
        daoInterpreter.create(interpreterToDelete);
        boolean result = daoInterpreter.delete(interpreterToDelete);
        assertTrue(result);
    }

    /**
     * Tests that delete() removes the Interpreter from the database.
     * Given : a valid Interpreter inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted ID
     * Then  : find() must return null
     */
    @Test
    @Order(13)
    public void delete_GivenExistingInterpreter_ObjectNoLongerExistsInDatabase() throws SQLException {
        Interpreter interpreterToDelete = new Interpreter(
                "loginDel", "passDel", "LastDel", "FirstDel",
                "0477444444", "del@mail.be", 20, address
        );
        daoInterpreter.create(interpreterToDelete);
        int deletedId = interpreterToDelete.getNumInterpreter();
        daoInterpreter.delete(interpreterToDelete);
        Interpreter result = daoInterpreter.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that delete() returns false when a non-existing Interpreter is passed.
     * Given : an Interpreter with a non-existing ID -1
     * When  : delete() is called with this Interpreter
     * Then  : the result must be false
     */
    @Test
    @Order(14)
    public void delete_GivenNonExistingInterpreter_ReturnsFalse() throws SQLException {
        Interpreter nonExisting = new Interpreter(
                "ghostDel", "ghostDelPass", "GhostDelLast", "GhostDelFirst",
                "0000000001", "ghostdel@mail.be", 5, address
        );
        nonExisting.setNumInterpreter(-1);
        boolean result = daoInterpreter.delete(nonExisting);
        assertFalse(result);
    }
}