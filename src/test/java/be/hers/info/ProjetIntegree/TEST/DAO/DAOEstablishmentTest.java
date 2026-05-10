package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.DAOAddress;
import be.hers.info.ProjetIntegree.DAO.DAOEstablishment;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Establishment;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOEstablishment}.
 * Verifies the correct behaviour of find, findAll, findListEducationLevel, create, update and delete operations.
 * All tests run in a single transaction that is rolled back after all tests, so no data is persisted in the database.
 *
 * Important notes:
 * - find() and findAll() use LAZY loading: only numEstablishment, name and phoneNumber
 *   are loaded. educationLevel, referrers and addresses are NOT loaded.
 * - The column FKAddress in Establishment has a UNIQUE constraint, meaning each Address
 *   can only be linked to ONE Establishment. Every create() call in this test class
 *   must use a freshly inserted Address to avoid ORA-00001 violations.
 * - educationLevel is stored as a comma-separated string in the DB column.
 * - The phoneNumber must contain digits only (DB constraint).
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOEstablishmentTest {

    private static DAOEstablishment daoEstablishment;
    private static DAOAddress daoAddress;
    private static Connection connect;
    private static Establishment establishmentTest;

    // Set Up //

    /**
     * Initializes the DAOs, disables auto-commit to allow rollback after all tests,
     * creates a fresh Address for the test Establishment and inserts it.
     */
    @BeforeAll
    public static void setUp() throws SQLException {
        daoEstablishment = new DAOEstablishment();
        daoAddress = new DAOAddress();
        connect = daoEstablishment.connect;
        connect.setAutoCommit(false);

        Address address = new Address(6800, "BP1", "Libramont", "Centre", null);
        daoAddress.create(address);

        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        establishmentTest = new Establishment(
                "TestBuilding", "0800000001", List.of(1), new ArrayList<>(), addresses
        );
        daoEstablishment.create(establishmentTest);
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

    /**
     * Helper method — creates and inserts a fresh Address, then wraps it in a list.
     * Each call to this method produces a unique Address to satisfy the FKAddress UNIQUE constraint.
     * @param postcode the postcode of the address to create
     * @return a list containing the newly created Address
     */
    private List<Address> createFreshAddress(int postcode) throws SQLException {
        Address address = new Address(postcode, "BP" + postcode, "Locality" + postcode, "", null);
        daoAddress.create(address);
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        return addresses;
    }

    // create //

    /**
     * Tests that create() returns true when a valid Establishment is inserted.
     * Given : a valid Establishment with a fresh Address, nameBuilding, phoneNumber and educationLevel
     * When  : create() is called with this Establishment
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidEstablishment_ReturnsTrue() throws SQLException {
        Establishment establishment = new Establishment(
                "CreateBuilding", "0800000002", List.of(3), new ArrayList<>(), createFreshAddress(6900)
        );
        boolean result = daoEstablishment.create(establishment);
        assertTrue(result);
    }

    /**
     * Tests that create() sets the generated numEstablishment on the object after insertion.
     * Given : a valid Establishment with a fresh Address and all required fields
     * When  : create() is called with this Establishment
     * Then  : getNumEstablishment() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidEstablishment_SetsGeneratedIdOnObject() throws SQLException {
        Establishment establishment = new Establishment(
                "IdBuilding", "0800000003", List.of(0), new ArrayList<>(), createFreshAddress(5000)
        );
        daoEstablishment.create(establishment);
        assertTrue(establishment.getNumEstablishment() > 0);
    }

    // find //

    /**
     * Tests that find() returns the correct Establishment when an existing ID is passed.
     * Given : an Establishment inserted in setUp()
     * When  : find() is called with its generated numEstablishment
     * Then  : the result must not be null and its numEstablishment must match
     */
    @Test
    @Order(3)
    public void find_GivenExistingId_ReturnsMatchingEstablishment() throws SQLException {
        int existingId = establishmentTest.getNumEstablishment();
        Establishment result = daoEstablishment.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumEstablishment());
    }

    /**
     * Tests that find() returns an Establishment with the correct nameBuilding and phoneNumber.
     * Given : an Establishment inserted in setUp() with nameBuilding="TestBuilding" and phoneNumber="0800000001"
     * When  : find() is called with its numEstablishment
     * Then  : getNameBuilding() must return "TestBuilding" and getPhoneNumber() must return "0800000001"
     */
    @Test
    @Order(4)
    public void find_GivenExistingId_ReturnsCorrectFields() throws SQLException {
        Establishment result = daoEstablishment.find(establishmentTest.getNumEstablishment());
        assertNotNull(result);
        assertEquals("TestBuilding", result.getNameBuilding());
        assertEquals("0800000001", result.getPhoneNumber());
    }

    /**
     * Tests that find() uses lazy loading and does not load educationLevel, referrers or addresses.
     * Given : an Establishment inserted in setUp() with educationLevel=[1, 2]
     * When  : find() is called with its numEstablishment
     * Then  : getEducationLevel(), getReferrers() and getAddresses() must all be empty
     */
    @Test
    @Order(5)
    public void find_GivenExistingId_LazyLoadsEducationLevelReferrersAndAddresses() throws SQLException {
        Establishment result = daoEstablishment.find(establishmentTest.getNumEstablishment());
        assertNotNull(result);
        assertTrue(result.getEducationLevel().isEmpty());
        assertTrue(result.getReferrers().isEmpty());
        assertTrue(result.getAddresses().isEmpty());
    }

    /**
     * Tests that find() returns null when a non-existing ID is passed.
     * Given : a non-existing numEstablishment -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(6)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        Establishment result = daoEstablishment.find(-1);
        assertNull(result);
    }

    // findAll //

    /**
     * Tests that findAll() never returns null.
     * Given : the Establishment table contains at least the establishment inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(7)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<Establishment> result = daoEstablishment.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that findAll() returns a list containing the establishment inserted in setUp().
     * Given : an Establishment was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted establishment
     */
    @Test
    @Order(8)
    public void findAll_GivenEstablishmentWasInserted_ListContainsInsertedEstablishment() throws SQLException {
        List<Establishment> result = daoEstablishment.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(e -> e.getNumEstablishment() == establishmentTest.getNumEstablishment());
        assertTrue(found);
    }

    // findListEducationLevel //

    /**
     * Tests that findListEducationLevel() returns the correct education levels for an existing Establishment.
     * Given : an Establishment inserted in setUp() with educationLevel=[1, 2]
     * When  : findListEducationLevel() is called with its numEstablishment
     * Then  : the result must contain 1 and 2
     */
    @Test
    @Order(9)
    public void findListEducationLevel_GivenExistingId_ReturnsCorrectLevels() throws SQLException {
        List<Integer> result = daoEstablishment.findListEducationLevel(establishmentTest.getNumEstablishment());
        assertNotNull(result);
        assertTrue(result.contains(1));
    }

    /**
     * Tests that findListEducationLevel() returns an empty list when a non-existing ID is passed.
     * Given : a non-existing numEstablishment -1
     * When  : findListEducationLevel() is called with this ID
     * Then  : the result must be an empty list
     */
    @Test
    @Order(10)
    public void findListEducationLevel_GivenNonExistingId_ReturnsEmptyList() throws SQLException {
        List<Integer> result = daoEstablishment.findListEducationLevel(-1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // update //

    /**
     * Tests that update() returns true when an existing Establishment is updated.
     * Given : the Establishment inserted in setUp() with nameBuilding updated to "UpdatedBuilding"
     * When  : update() is called with this Establishment
     * Then  : the result must be true
     */
    @Test
    @Order(11)
    public void update_GivenExistingEstablishment_ReturnsTrue() throws SQLException {
        establishmentTest.setNameBuilding("UpdatedBuilding");
        boolean result = daoEstablishment.update(establishmentTest);
        assertTrue(result);
    }

    /**
     * Tests that update() persists the nameBuilding change in the database.
     * Given : the Establishment inserted in setUp() with nameBuilding updated to "VerifiedBuilding"
     * When  : update() is called and then find() is called with the same numEstablishment
     * Then  : getNameBuilding() must return "VerifiedBuilding"
     */
    @Test
    @Order(12)
    public void update_GivenExistingEstablishment_NameBuildingChangesShouldBePersisted() throws SQLException {
        establishmentTest.setNameBuilding("VerifiedBuilding");
        daoEstablishment.update(establishmentTest);
        Establishment result = daoEstablishment.find(establishmentTest.getNumEstablishment());
        assertNotNull(result);
        assertEquals("VerifiedBuilding", result.getNameBuilding());
    }

    /**
     * Tests that update() persists the phoneNumber change in the database.
     * Given : the Establishment inserted in setUp() with phoneNumber updated to "0800999999"
     * When  : update() is called and then find() is called with the same numEstablishment
     * Then  : getPhoneNumber() must return "0800999999"
     */
    @Test
    @Order(13)
    public void update_GivenExistingEstablishment_PhoneNumberChangesShouldBePersisted() throws SQLException {
        establishmentTest.setPhoneNumber("0800999999");
        daoEstablishment.update(establishmentTest);
        Establishment result = daoEstablishment.find(establishmentTest.getNumEstablishment());
        assertNotNull(result);
        assertEquals("0800999999", result.getPhoneNumber());
    }

    /**
     * Tests that update() returns false when a non-existing Establishment is passed.
     * Given : an Establishment with a non-existing numEstablishment 0
     * When  : update() is called with this Establishment
     * Then  : the result must be false
     */
    @Test
    @Order(14)
    public void update_GivenNonExistingEstablishment_ReturnsFalse() throws SQLException {
        Establishment nonExisting = new Establishment(0, "Ghost", "0000000000");
        boolean result = daoEstablishment.update(nonExisting);
        assertFalse(result);
    }

    // delete //

    /**
     * Tests that delete() returns true when an existing Establishment is deleted.
     * Given : a valid Establishment with a fresh Address inserted just before deletion
     * When  : delete() is called with this Establishment
     * Then  : the result must be true
     */
    @Test
    @Order(15)
    public void delete_GivenExistingEstablishment_ReturnsTrue() throws SQLException {
        Establishment establishmentToDelete = new Establishment(
                "DeleteBuilding", "0800000004", List.of(2), new ArrayList<>(), createFreshAddress(4000)
        );
        daoEstablishment.create(establishmentToDelete);
        boolean result = daoEstablishment.delete(establishmentToDelete);
        assertTrue(result);
    }

    /**
     * Tests that delete() removes the Establishment from the database.
     * Given : a valid Establishment with a fresh Address inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted numEstablishment
     * Then  : find() must return null
     */
    @Test
    @Order(16)
    public void delete_GivenExistingEstablishment_ObjectNoLongerExistsInDatabase() throws SQLException {
        Establishment establishmentToDelete = new Establishment(
                "VerifyDeleteBuilding", "0800000005", List.of(3), new ArrayList<>(), createFreshAddress(1000)
        );
        daoEstablishment.create(establishmentToDelete);
        int deletedId = establishmentToDelete.getNumEstablishment();
        daoEstablishment.delete(establishmentToDelete);
        Establishment result = daoEstablishment.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that delete() returns false when a non-existing Establishment is passed.
     * Given : an Establishment with a non-existing numEstablishment 0
     * When  : delete() is called with this Establishment
     * Then  : the result must be false
     */
    @Test
    @Order(17)
    public void delete_GivenNonExistingEstablishment_ReturnsFalse() throws SQLException {
        Establishment nonExisting = new Establishment(0, "Ghost", "0000000000");
        boolean result = daoEstablishment.delete(nonExisting);
        assertFalse(result);
    }
}