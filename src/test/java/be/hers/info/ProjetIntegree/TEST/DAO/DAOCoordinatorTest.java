package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.DAOAddress;
import be.hers.info.ProjetIntegree.DAO.DAOCoordinator;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Coordinator;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOCoordinator}.
 * Verifies the correct behaviour of find, findAll, create, update and delete operations.
 * All tests run in a single transaction that is rolled back after all tests, so no data is persisted in the database.
 *
 * Important notes:
 * - A Coordinator depends on an existing Interpreter in the database.
 *   In setUp(), an Interpreter is first inserted via DAOInterpreter, then a Coordinator is created linked to that Interpreter.
 * - find() reconstructs the Coordinator by loading the linked Interpreter via DAOInterpreter.
 * - update() only updates the isAdmin field — not the linked Interpreter.
 * - The DB trigger hashes the Interpreter's password on INSERT/UPDATE — password is never checked directly after a find().
 * - The DB trigger may auto-generate the login — login is not checked after find().
 * - weeklyWorkHours must be strictly > 0 (DB constraint on Interpreter).
 * - emailAddress must match the format x@x.x (DB constraint on Interpreter).
 * - phoneNumber must contain digits only (DB constraint on Interpreter).
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOCoordinatorTest {

    private static DAOCoordinator daoCoordinator;
    private static DAOInterpreter daoInterpreter;
    private static Connection connect;
    private static Coordinator coordinatorTest;
    private static Interpreter interpreterTest;
    private static Address address;

    // Set Up //

    /**
     * Initializes the DAOs, disables auto-commit to allow rollback after all tests,
     * retrieves an existing Address, inserts a test Interpreter, then inserts a test Coordinator linked to that Interpreter.
     * The address with ID 1 must already exist in the database.
     */
    @BeforeAll
    public static void setUp() throws SQLException {
        daoCoordinator = new DAOCoordinator();
        daoInterpreter = new DAOInterpreter();
        connect = daoCoordinator.connect;
        connect.setAutoCommit(false);

        DAOAddress daoAddress = new DAOAddress();
        address = daoAddress.find(1);

        interpreterTest = new Interpreter(
                "coordlogin", "coordpassword", "CoordLastName", "CoordFirstName",
                "0477000001", "coord@mail.be", 38, address
        );
        daoInterpreter.create(interpreterTest);

        coordinatorTest = new Coordinator(
                interpreterTest.getNumInterpreter(),
                interpreterTest.getLogin(),
                interpreterTest.getPassword(),
                interpreterTest.getLastName(),
                interpreterTest.getFirstName(),
                interpreterTest.getEmailAddress(),
                interpreterTest.getPhoneNumber(),
                interpreterTest.getWeeklyWorkHours(),
                interpreterTest.getAddress()
        );
        coordinatorTest.setAdmin(false);
        daoCoordinator.create(coordinatorTest);
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
     * Tests that create() returns true when a valid Coordinator is inserted.
     * Given : a valid Interpreter already inserted in the database and a Coordinator linked to it
     * When  : create() is called with this Coordinator
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidCoordinator_ReturnsTrue() throws SQLException {
        Interpreter interpreter = new Interpreter(
                "loginC1", "passC1", "LastC1", "FirstC1",
                "0477111111", "c1@mail.be", 20, address
        );
        daoInterpreter.create(interpreter);

        Coordinator coordinator = new Coordinator(
                interpreter.getNumInterpreter(),
                interpreter.getLogin(),
                interpreter.getPassword(),
                interpreter.getLastName(),
                interpreter.getFirstName(),
                interpreter.getEmailAddress(),
                interpreter.getPhoneNumber(),
                interpreter.getWeeklyWorkHours(),
                interpreter.getAddress()
        );
        boolean result = daoCoordinator.create(coordinator);
        assertTrue(result);
    }

    /**
     * Tests that create() sets the generated numCoordinator on the object after insertion.
     * Given : a valid Interpreter already inserted and a Coordinator linked to it
     * When  : create() is called with this Coordinator
     * Then  : getNumCoordinator() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidCoordinator_SetsGeneratedIdOnObject() throws SQLException {
        Interpreter interpreter = new Interpreter(
                "loginC2", "passC2", "LastC2", "FirstC2",
                "0477222222", "c2@mail.be", 25, address
        );
        daoInterpreter.create(interpreter);

        Coordinator coordinator = new Coordinator(
                interpreter.getNumInterpreter(),
                interpreter.getLogin(),
                interpreter.getPassword(),
                interpreter.getLastName(),
                interpreter.getFirstName(),
                interpreter.getEmailAddress(),
                interpreter.getPhoneNumber(),
                interpreter.getWeeklyWorkHours(),
                interpreter.getAddress()
        );
        daoCoordinator.create(coordinator);
        assertTrue(coordinator.getNumCoordinator() > 0);
    }

    /**
     * Tests that create() correctly persists isAdmin=false.
     * Given : a Coordinator inserted in setUp() with isAdmin=false
     * When  : find() is called with its numCoordinator
     * Then  : isAdmin() must return false
     */
    @Test
    @Order(3)
    public void create_GivenCoordinatorWithIsAdminFalse_PersistsIsAdminFalse() throws SQLException {
        Coordinator result = daoCoordinator.find(coordinatorTest.getNumCoordinator());
        assertNotNull(result);
        assertFalse(result.isAdmin());
    }

    // find //

    /**
     * Tests that find() returns the correct Coordinator when an existing ID is passed.
     * Given : a Coordinator inserted in setUp()
     * When  : find() is called with its numCoordinator
     * Then  : the result must not be null and its numCoordinator must match
     */
    @Test
    @Order(4)
    public void find_GivenExistingId_ReturnsMatchingCoordinator() throws SQLException {
        int existingId = coordinatorTest.getNumCoordinator();
        Coordinator result = daoCoordinator.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumCoordinator());
    }

    /**
     * Tests that find() loads the linked Interpreter and correctly sets its fields.
     * Given : a Coordinator inserted in setUp() linked to an Interpreter with known lastName, firstName and weeklyWorkHours
     * When  : find() is called with its numCoordinator
     * Then  : lastName, firstName and weeklyWorkHours must match the inserted Interpreter
     * Note  : password and login are not checked due to DB triggers
     */
    @Test
    @Order(5)
    public void find_GivenExistingId_LoadsLinkedInterpreterWithCorrectFields() throws SQLException {
        Coordinator result = daoCoordinator.find(coordinatorTest.getNumCoordinator());
        assertNotNull(result);
        assertEquals(interpreterTest.getLastName(), result.getLastName());
        assertEquals(interpreterTest.getFirstName(), result.getFirstName());
        assertEquals(interpreterTest.getWeeklyWorkHours(), result.getWeeklyWorkHours());
    }

    /**
     * Tests that find() returns the correct isAdmin value.
     * Given : a Coordinator inserted in setUp() with isAdmin=false
     * When  : find() is called with its numCoordinator
     * Then  : isAdmin() must return false
     */
    @Test
    @Order(6)
    public void find_GivenExistingId_ReturnsCorrectIsAdminValue() throws SQLException {
        Coordinator result = daoCoordinator.find(coordinatorTest.getNumCoordinator());
        assertNotNull(result);
        assertFalse(result.isAdmin());
    }

    /**
     * Tests that find() returns null when a non-existing ID is passed.
     * Given : a non-existing numCoordinator -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(7)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        Coordinator result = daoCoordinator.find(-1);
        assertNull(result);
    }

    // findAll //

    /**
     * Tests that findAll() never returns null.
     * Given : the Coordinator table contains at least the coordinator inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(8)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<Coordinator> result = daoCoordinator.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that findAll() returns a list containing the coordinator inserted in setUp().
     * Given : a Coordinator was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted coordinator
     */
    @Test
    @Order(9)
    public void findAll_GivenCoordinatorWasInserted_ListContainsInsertedCoordinator() throws SQLException {
        List<Coordinator> result = daoCoordinator.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(c -> c.getNumCoordinator() == coordinatorTest.getNumCoordinator());
        assertTrue(found);
    }

    // update //

    /**
     * Tests that update() returns true when an existing Coordinator is updated.
     * Given : the Coordinator inserted in setUp() with isAdmin updated to true
     * When  : update() is called with this Coordinator
     * Then  : the result must be true
     */
    @Test
    @Order(10)
    public void update_GivenExistingCoordinator_ReturnsTrue() throws SQLException {
        coordinatorTest.setAdmin(true);
        boolean result = daoCoordinator.update(coordinatorTest);
        assertTrue(result);
    }

    /**
     * Tests that update() persists the isAdmin change in the database.
     * Given : the Coordinator inserted in setUp() with isAdmin updated to true
     * When  : update() is called and then find() is called with the same numCoordinator
     * Then  : isAdmin() must return true
     */
    @Test
    @Order(11)
    public void update_GivenExistingCoordinator_IsAdminChangesShouldBePersisted() throws SQLException {
        coordinatorTest.setAdmin(true);
        daoCoordinator.update(coordinatorTest);
        Coordinator result = daoCoordinator.find(coordinatorTest.getNumCoordinator());
        assertNotNull(result);
        assertTrue(result.isAdmin());
    }

    /**
     * Tests that update() returns false when a non-existing Coordinator is passed.
     * Given : a Coordinator with a numCoordinator that does not exist in the database
     * When  : update() is called with this Coordinator
     * Then  : the result must be false
     */
    @Test
    @Order(12)
    public void update_GivenNonExistingCoordinator_ReturnsFalse() throws SQLException {
        Coordinator nonExisting = new Coordinator();
        nonExisting.setNumCoordinator(0);
        nonExisting.setAdmin(false);
        boolean result = daoCoordinator.update(nonExisting);
        assertFalse(result);
    }

    // delete //

    /**
     * Tests that delete() returns true when an existing Coordinator is deleted.
     * Given : a valid Interpreter and Coordinator inserted just before deletion
     * When  : delete() is called with this Coordinator
     * Then  : the result must be true
     */
    @Test
    @Order(13)
    public void delete_GivenExistingCoordinator_ReturnsTrue() throws SQLException {
        Interpreter interpreter = new Interpreter(
                "loginDel", "passDel", "LastDel", "FirstDel",
                "0477333333", "del@mail.be", 15, address
        );
        daoInterpreter.create(interpreter);

        Coordinator coordinatorToDelete = new Coordinator(
                interpreter.getNumInterpreter(),
                interpreter.getLogin(),
                interpreter.getPassword(),
                interpreter.getLastName(),
                interpreter.getFirstName(),
                interpreter.getEmailAddress(),
                interpreter.getPhoneNumber(),
                interpreter.getWeeklyWorkHours(),
                interpreter.getAddress()
        );
        daoCoordinator.create(coordinatorToDelete);
        boolean result = daoCoordinator.delete(coordinatorToDelete);
        assertTrue(result);
    }

    /**
     * Tests that delete() removes the Coordinator from the database.
     * Given : a valid Interpreter and Coordinator inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted numCoordinator
     * Then  : find() must return null
     */
    @Test
    @Order(14)
    public void delete_GivenExistingCoordinator_ObjectNoLongerExistsInDatabase() throws SQLException {
        Interpreter interpreter = new Interpreter(
                "loginVerDel", "passVerDel", "LastVerDel", "FirstVerDel",
                "0477444444", "verdel@mail.be", 20, address
        );
        daoInterpreter.create(interpreter);

        Coordinator coordinatorToDelete = new Coordinator(
                interpreter.getNumInterpreter(),
                interpreter.getLogin(),
                interpreter.getPassword(),
                interpreter.getLastName(),
                interpreter.getFirstName(),
                interpreter.getEmailAddress(),
                interpreter.getPhoneNumber(),
                interpreter.getWeeklyWorkHours(),
                interpreter.getAddress()
        );
        daoCoordinator.create(coordinatorToDelete);
        int deletedId = coordinatorToDelete.getNumCoordinator();
        daoCoordinator.delete(coordinatorToDelete);
        Coordinator result = daoCoordinator.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that delete() returns false when a non-existing Coordinator is passed.
     * Given : a Coordinator with a numCoordinator that does not exist in the database
     * When  : delete() is called with this Coordinator
     * Then  : the result must be false
     */
    @Test
    @Order(15)
    public void delete_GivenNonExistingCoordinator_ReturnsFalse() throws SQLException {
        Coordinator nonExisting = new Coordinator();
        nonExisting.setNumCoordinator(0);
        boolean result = daoCoordinator.delete(nonExisting);
        assertFalse(result);
    }
}