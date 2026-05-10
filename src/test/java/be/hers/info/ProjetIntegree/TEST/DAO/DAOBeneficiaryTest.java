package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.DAOAddress;
import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOBeneficiary}.
 * Verifies the correct behaviour of find, findAll, create, update, delete and updatePassword operations.
 * All tests run in a single transaction that is rolled back after all tests, so no data is persisted in the database.
 *
 * Important notes:
 * - A Beneficiary requires an existing Address and an existing Interpreter in the database.
 *   In setUp(), both are retrieved/inserted before the test Beneficiary is created.
 * - find() loads the Address and the reference Interpreter eagerly via their respective DAOs.
 *   Appointments are NOT loaded (lazy loading).
 * - update() does NOT update the password — use updatePassword() for that.
 * - The DB trigger hashes the Beneficiary's password on INSERT/UPDATE — password is never checked directly after a find().
 * - The DB trigger may auto-generate the login — login value may differ after find().
 * - communicationLanguage is stored as a comma-separated string in the DB and split back into a List on find().
 * - emailAddress must match the format x@x.x (DB constraint).
 * - phoneNumber must contain digits only (DB constraint).
 * - hourQuota must be > 0 (DB constraint).
 * - educationLevel must be between 0 and 4 included (DB constraint).
 * - The address with ID 1 must already exist in the database.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOBeneficiaryTest {

    private static DAOBeneficiary daoBeneficiary;
    private static DAOInterpreter daoInterpreter;
    private static Connection connect;
    private static Beneficiary beneficiaryTest;
    private static Interpreter interpreterTest;
    private static Address address;

    // Set Up //

    /**
     * Initializes the DAOs, disables auto-commit to allow rollback after all tests,
     * retrieves an existing Address, inserts a test Interpreter, then inserts a test Beneficiary
     * linked to that Interpreter and Address.
     * The address with ID 1 must already exist in the database.
     */
    @BeforeAll
    public static void setUp() throws SQLException {
        daoBeneficiary = new DAOBeneficiary();
        daoInterpreter = new DAOInterpreter();
        connect = daoBeneficiary.connect;
        connect.setAutoCommit(false);

        DAOAddress daoAddress = new DAOAddress();
        address = daoAddress.find(1);

        interpreterTest = new Interpreter(
                "interpLogin", "interpPassword", "InterpLastName", "InterpFirstName",
                "0477000001", "interp@mail.be", 38, address
        );
        daoInterpreter.create(interpreterTest);

        beneficiaryTest = new Beneficiary(
                "benLogin", "benPassword", "BenLastName", "BenFirstName",
                "0477000002", "ben@mail.be", address, 10, 2,
                interpreterTest, Arrays.asList("Français", "Anglais"), null
        );
        daoBeneficiary.create(beneficiaryTest);
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
     * Tests that create() returns true when a valid Beneficiary is inserted.
     * Given : a valid Beneficiary with all required fields and a valid Address and Interpreter
     * When  : create() is called with this Beneficiary
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidBeneficiary_ReturnsTrue() throws SQLException {
        Beneficiary beneficiary = new Beneficiary(
                "loginB1", "passB1", "LastB1", "FirstB1",
                "0477111111", "b1@mail.be", address, 5, 1,
                interpreterTest, List.of("Français"), null
        );
        boolean result = daoBeneficiary.create(beneficiary);
        assertTrue(result);
    }

    /**
     * Tests that create() sets the generated numBeneficiary on the object after insertion.
     * Given : a valid Beneficiary with all required fields
     * When  : create() is called with this Beneficiary
     * Then  : getNumBeneficiary() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidBeneficiary_SetsGeneratedIdOnObject() throws SQLException {
        Beneficiary beneficiary = new Beneficiary(
                "loginB2", "passB2", "LastB2", "FirstB2",
                "0477222222", "b2@mail.be", address, 8, 3,
                interpreterTest, List.of("Néerlandais"), null
        );
        daoBeneficiary.create(beneficiary);
        assertTrue(beneficiary.getNumBeneficiary() > 0);
    }

    // find //

    /**
     * Tests that find() returns the correct Beneficiary when an existing ID is passed.
     * Given : a Beneficiary inserted in setUp()
     * When  : find() is called with its generated numBeneficiary
     * Then  : the result must not be null and its numBeneficiary must match
     */
    @Test
    @Order(3)
    public void find_GivenExistingId_ReturnsMatchingBeneficiary() throws SQLException {
        int existingId = beneficiaryTest.getNumBeneficiary();
        Beneficiary result = daoBeneficiary.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumBeneficiary());
    }

    /**
     * Tests that find() returns a Beneficiary with the correct personal fields.
     * Given : a Beneficiary inserted in setUp() with known lastName, firstName, phone, email,
     *         hourQuota and educationLevel
     * When  : find() is called with its numBeneficiary
     * Then  : all those fields must match the inserted values
     * Note  : password is not checked due to the DB trigger that hashes it on INSERT
     */
    @Test
    @Order(4)
    public void find_GivenExistingId_ReturnsCorrectFields() throws SQLException {
        Beneficiary result = daoBeneficiary.find(beneficiaryTest.getNumBeneficiary());
        assertNotNull(result);
        assertEquals(beneficiaryTest.getLastName(), result.getLastName());
        assertEquals(beneficiaryTest.getFirstName(), result.getFirstName());
        assertEquals(beneficiaryTest.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(beneficiaryTest.getEmailAddress(), result.getEmailAddress());
        assertEquals(beneficiaryTest.getHourQuota(), result.getHourQuota());
        assertEquals(beneficiaryTest.getEducationLevel(), result.getEducationLevel());
    }

    /**
     * Tests that find() loads the Address of the Beneficiary eagerly.
     * Given : a Beneficiary inserted in setUp() with a valid Address
     * When  : find() is called with its numBeneficiary
     * Then  : getAddress() must not be null
     */
    @Test
    @Order(5)
    public void find_GivenExistingId_LoadsAddressEagerly() throws SQLException {
        Beneficiary result = daoBeneficiary.find(beneficiaryTest.getNumBeneficiary());
        assertNotNull(result);
        assertNotNull(result.getAddress());
    }

    /**
     * Tests that find() loads the reference Interpreter of the Beneficiary eagerly.
     * Given : a Beneficiary inserted in setUp() with a reference Interpreter
     * When  : find() is called with its numBeneficiary
     * Then  : getInterpreter() must not be null and its numInterpreter must match
     */
    @Test
    @Order(6)
    public void find_GivenExistingId_LoadsInterpreterEagerly() throws SQLException {
        Beneficiary result = daoBeneficiary.find(beneficiaryTest.getNumBeneficiary());
        assertNotNull(result);
        assertNotNull(result.getInterpreter());
        assertEquals(interpreterTest.getNumInterpreter(), result.getInterpreter().getNumInterpreter());
    }

    /**
     * Tests that find() correctly reconstructs the communicationLanguage list.
     * Given : a Beneficiary inserted in setUp() with communicationLanguage=["Français", "Anglais"]
     * When  : find() is called with its numBeneficiary
     * Then  : getCommunicationLanguage() must contain "Français" and "Anglais"
     */
    @Test
    @Order(7)
    public void find_GivenExistingId_ReturnsCorrectCommunicationLanguages() throws SQLException {
        Beneficiary result = daoBeneficiary.find(beneficiaryTest.getNumBeneficiary());
        assertNotNull(result);
        assertNotNull(result.getCommunicationLanguage());
        assertTrue(result.getCommunicationLanguage().contains("Français"));
        assertTrue(result.getCommunicationLanguage().contains("Anglais"));
    }

    /**
     * Tests that find() does not load the appointments (lazy loading).
     * Given : a Beneficiary inserted in setUp() with no appointments
     * When  : find() is called with its numBeneficiary
     * Then  : getAppointmentList() must be empty
     */
    @Test
    @Order(8)
    public void find_GivenExistingId_AppointmentListIsEmpty() throws SQLException {
        Beneficiary result = daoBeneficiary.find(beneficiaryTest.getNumBeneficiary());
        assertNotNull(result);
        assertTrue(result.getAppointmentList().isEmpty());
    }

    /**
     * Tests that find() returns null when a non-existing ID is passed.
     * Given : a non-existing numBeneficiary -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(9)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        Beneficiary result = daoBeneficiary.find(-1);
        assertNull(result);
    }

    // findAll //

    /**
     * Tests that findAll() never returns null.
     * Given : the Beneficiary table contains at least the beneficiary inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(10)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<Beneficiary> result = daoBeneficiary.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that indAll() returns a list containing the beneficiary inserted in setUp().
     * Given : a Beneficiary was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted beneficiary
     */
    @Test
    @Order(11)
    public void findAll_GivenBeneficiaryWasInserted_ListContainsInsertedBeneficiary() throws SQLException {
        List<Beneficiary> result = daoBeneficiary.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(b -> b.getNumBeneficiary() == beneficiaryTest.getNumBeneficiary());
        assertTrue(found);
    }

    // update //

    /**
     * Tests that update() returns true when an existing Beneficiary is updated.
     * Given : the Beneficiary inserted in setUp() with lastName updated to "UpdatedLastName"
     * When  : update() is called with this Beneficiary
     * Then  : the result must be true
     */
    @Test
    @Order(12)
    public void update_GivenExistingBeneficiary_ReturnsTrue() throws SQLException {
        beneficiaryTest.setLastName("UpdatedLastName");
        boolean result = daoBeneficiary.update(beneficiaryTest);
        assertTrue(result);
    }

    /**
     * Tests that update() persists the changes in the database.
     * Given : the Beneficiary inserted in setUp() with lastName updated to "VerifiedLastName"
     * When  : update() is called and then find() is called with the same numBeneficiary
     * Then  : the found beneficiary must have lastName "VerifiedLastName"
     */
    @Test
    @Order(13)
    public void update_GivenExistingBeneficiary_ChangesShouldBePersisted() throws SQLException {
        beneficiaryTest.setLastName("VerifiedLastName");
        daoBeneficiary.update(beneficiaryTest);
        Beneficiary result = daoBeneficiary.find(beneficiaryTest.getNumBeneficiary());
        assertNotNull(result);
        assertEquals("VerifiedLastName", result.getLastName());
    }

    /**
     * Tests that update() persists the communicationLanguage changes in the database.
     * Given : the Beneficiary inserted in setUp() with communicationLanguage updated to ["Allemand"]
     * When  : update() is called and then find() is called with the same numBeneficiary
     * Then  : getCommunicationLanguage() must contain "Allemand"
     */
    @Test
    @Order(14)
    public void update_GivenExistingBeneficiary_CommunicationLanguageChangesShouldBePersisted() throws SQLException {
        beneficiaryTest.setCommunicationLanguage(List.of("Allemand"));
        daoBeneficiary.update(beneficiaryTest);
        Beneficiary result = daoBeneficiary.find(beneficiaryTest.getNumBeneficiary());
        assertNotNull(result);
        assertTrue(result.getCommunicationLanguage().contains("Allemand"));
    }

    /**
     * Tests that update() returns false when a non-existing Beneficiary is passed.
     * Given : a Beneficiary with a non-existing numBeneficiary -1
     * When  : update() is called with this Beneficiary
     * Then  : the result must be false
     */
    @Test
    @Order(15)
    public void update_GivenNonExistingBeneficiary_ReturnsFalse() throws SQLException {
        Beneficiary nonExisting = new Beneficiary(
                "ghostLogin", "ghostPass", "GhostLast", "GhostFirst",
                "0000000000", "ghost@mail.be", address, 1, 0,
                interpreterTest, List.of("Français"), null
        );
        nonExisting.setNumBeneficiary(-1);
        boolean result = daoBeneficiary.update(nonExisting);
        assertFalse(result);
    }

    // updatePassword //

    /**
     * Tests that updatePassword() returns true when called on an existing Beneficiary.
     * Given : the Beneficiary inserted in setUp()
     * When  : updatePassword() is called with a new password
     * Then  : the result must be true
     * Note  : the stored password will be re-hashed by the DB trigger so the plain-text
     *         value cannot be verified directly after find()
     */
    @Test
    @Order(16)
    public void updatePassword_GivenExistingBeneficiary_ReturnsTrue() throws SQLException {
        beneficiaryTest.setPassword("newSecurePassword");
        boolean result = daoBeneficiary.updatePassword(beneficiaryTest);
        assertTrue(result);
    }

    /**
     * Tests that updatePassword() returns false when a non-existing Beneficiary is passed.
     * Given : a Beneficiary with a non-existing numBeneficiary -1
     * When  : updatePassword() is called with this Beneficiary
     * Then  : the result must be false
     */
    @Test
    @Order(17)
    public void updatePassword_GivenNonExistingBeneficiary_ReturnsFalse() throws SQLException {
        Beneficiary nonExisting = new Beneficiary();
        nonExisting.setNumBeneficiary(-1);
        nonExisting.setPassword("somePassword");
        boolean result = daoBeneficiary.updatePassword(nonExisting);
        assertFalse(result);
    }

    // delete //

    /**
     * Tests that delete() returns true when an existing Beneficiary is deleted.
     * Given : a valid Beneficiary inserted just before deletion
     * When  : delete() is called with this Beneficiary
     * Then  : the result must be true
     */
    @Test
    @Order(18)
    public void delete_GivenExistingBeneficiary_ReturnsTrue() throws SQLException {
        Beneficiary beneficiaryToDelete = new Beneficiary(
                "loginDel", "passDel", "LastDel", "FirstDel",
                "0477333333", "del@mail.be", address, 3, 1,
                interpreterTest, List.of("Français"), null
        );
        daoBeneficiary.create(beneficiaryToDelete);
        boolean result = daoBeneficiary.delete(beneficiaryToDelete);
        assertTrue(result);
    }

    /**
     * Tests that delete() removes the Beneficiary from the database.
     * Given : a valid Beneficiary inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted numBeneficiary
     * Then  : find() must return null
     */
    @Test
    @Order(19)
    public void delete_GivenExistingBeneficiary_ObjectNoLongerExistsInDatabase() throws SQLException {
        Beneficiary beneficiaryToDelete = new Beneficiary(
                "loginVerDel", "passVerDel", "LastVerDel", "FirstVerDel",
                "0477444444", "verdel@mail.be", address, 5, 2,
                interpreterTest, List.of("Anglais"), null
        );
        daoBeneficiary.create(beneficiaryToDelete);
        int deletedId = beneficiaryToDelete.getNumBeneficiary();
        daoBeneficiary.delete(beneficiaryToDelete);
        Beneficiary result = daoBeneficiary.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that delete() returns false when a non-existing Beneficiary is passed.
     * Given : a Beneficiary with a non-existing numBeneficiary -1
     * When  : delete() is called with this Beneficiary
     * Then  : the result must be false
     */
    @Test
    @Order(20)
    public void delete_GivenNonExistingBeneficiary_ReturnsFalse() throws SQLException {
        Beneficiary nonExisting = new Beneficiary();
        nonExisting.setNumBeneficiary(-1);
        boolean result = daoBeneficiary.delete(nonExisting);
        assertFalse(result);
    }
}