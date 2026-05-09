package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.DAOAddress;
import be.hers.info.ProjetIntegree.POJO.Address;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOAddress}.
 * Verifies the correct behaviour of find, findAll, create, update, delete and findAllByEstablishment operations.
 * All tests run in a single transaction that is rolled back after all tests, so no data is persisted in the database.
 *
 * Important notes:
 * - The Establishment is loaded eagerly inside find() and findAll() via DAOEstablishment.
 * - findAllByEstablishment() uses lazy loading (no Establishment object loaded).
 * - The test for findAllByEstablishment() requires an Establishment with a known nameBuilding to already exist in the database.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOAddressTest {

    private static DAOAddress daoAddress;
    private static Connection connect;
    private static Address addressTest;

    // Set Up //

    /**
     * Initializes the DAO, disables auto-commit to allow rollback after all tests, and inserts a test Address.
     */
    @BeforeAll
    public static void setUp() throws SQLException {
        daoAddress = new DAOAddress();
        connect = daoAddress.connect;
        connect.setAutoCommit(false);

        addressTest = new Address(6800, "BP1", "Libramont", "Centre", null);
        daoAddress.create(addressTest);
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
     * Tests that create() returns true when a valid Address is inserted.
     * Given : a valid Address with postcode=6900, postOfficeBox="BP2", locality="Marche", hamlet="Centre"
     * When  : create() is called with this Address
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidAddress_ReturnsTrue() throws SQLException {
        Address address = new Address(6900, "BP2", "Marche", "Centre", null);
        boolean result = daoAddress.create(address);
        assertTrue(result);
    }

    /**
     * Tests that create() sets the generated ID on the object after insertion.
     * Given : a valid Address with postcode=5000, postOfficeBox="BP3", locality="Namur", hamlet="Nord"
     * When  : create() is called with this Address
     * Then  : getNumAddress() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidAddress_SetsGeneratedIdOnObject() throws SQLException {
        Address address = new Address(5000, "BP3", "Namur", "Nord", null);
        daoAddress.create(address);
        assertTrue(address.getNumAddress() > 0);
    }

    // find //

    /**
     * Tests that find() returns the correct Address when an existing ID is passed.
     * Given : an Address inserted in setUp()
     * When  : find() is called with its generated ID
     * Then  : the result must not be null and its ID must match
     */
    @Test
    @Order(3)
    public void find_GivenExistingId_ReturnsMatchingAddress() throws SQLException {
        int existingId = addressTest.getNumAddress();
        Address result = daoAddress.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumAddress());
    }

    /**
     * Tests that find() returns an Address with all correct fields.
     * Given : an Address inserted in setUp() with postcode=6800, postOfficeBox="BP1", locality="Libramont"
     * When  : find() is called with its ID
     * Then  : postcode, postOfficeBox and locality must match the inserted values
     */
    @Test
    @Order(4)
    public void find_GivenExistingId_ReturnsAddressWithCorrectFields() throws SQLException {
        Address result = daoAddress.find(addressTest.getNumAddress());
        assertNotNull(result);
        assertEquals(6800, result.getPostcode());
        assertEquals("BP1", result.getPostOfficeBox());
        assertEquals("Libramont", result.getLocality());
        assertEquals("Centre", result.getHamlet());
    }

    /**
     * Tests that find() returns null when a non-existing ID is passed.
     * Given : a non-existing ID -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(5)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        Address result = daoAddress.find(-1);
        assertNull(result);
    }

    // findAll //

    /**
     * Tests that findAll() never returns null.
     * Given : the Address table contains at least the address inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(6)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<Address> result = daoAddress.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that findAll() returns a list containing the address inserted in setUp().
     * Given : an Address was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted address
     */
    @Test
    @Order(7)
    public void findAll_GivenAddressWasInserted_ListContainsInsertedAddress() throws SQLException {
        List<Address> result = daoAddress.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(a -> a.getNumAddress() == addressTest.getNumAddress());
        assertTrue(found);
    }

    // update //

    /**
     * Tests that update() returns true when an existing Address is updated.
     * Given : the Address inserted in setUp() with locality updated to "UpdatedLocality"
     * When  : update() is called with this Address
     * Then  : the result must be true
     */
    @Test
    @Order(8)
    public void update_GivenExistingAddress_ReturnsTrue() throws SQLException {
        addressTest.setLocality("UpdatedLocality");
        boolean result = daoAddress.update(addressTest);
        assertTrue(result);
    }

    /**
     * Tests that update() persists the changes in the database.
     * Given : the Address inserted in setUp() with locality updated to "VerifiedLocality"
     * When  : update() is called and then find() is called with the same ID
     * Then  : the found address must have locality "VerifiedLocality"
     */
    @Test
    @Order(9)
    public void update_GivenExistingAddress_ChangesShouldBePersisted() throws SQLException {
        addressTest.setLocality("VerifiedLocality");
        daoAddress.update(addressTest);
        Address result = daoAddress.find(addressTest.getNumAddress());
        assertNotNull(result);
        assertEquals("VerifiedLocality", result.getLocality());
    }

    /**
     * Tests that update() returns false when a non-existing Address is passed.
     * Given : an Address with a non-existing ID -1
     * When  : update() is called with this Address
     * Then  : the result must be false
     */
    @Test
    @Order(10)
    public void update_GivenNonExistingAddress_ReturnsFalse() throws SQLException {
        Address nonExisting = new Address(-1, 0, "BPX", "Unknown", "", null);
        boolean result = daoAddress.update(nonExisting);
        assertFalse(result);
    }

    // delete //

    /**
     * Tests that delete() returns true when an existing Address is deleted.
     * Given : a valid Address inserted just before deletion
     * When  : delete() is called with this Address
     * Then  : the result must be true
     */
    @Test
    @Order(11)
    public void delete_GivenExistingAddress_ReturnsTrue() throws SQLException {
        Address addressToDelete = new Address(4000, "BPDel", "Liège", "Est", null);
        daoAddress.create(addressToDelete);
        boolean result = daoAddress.delete(addressToDelete);
        assertTrue(result);
    }

    /**
     * Tests that delete() removes the Address from the database.
     * Given : a valid Address inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted ID
     * Then  : find() must return null
     */
    @Test
    @Order(12)
    public void delete_GivenExistingAddress_ObjectNoLongerExistsInDatabase() throws SQLException {
        Address addressToDelete = new Address(1000, "BPVerDel", "Bruxelles", "Centre", null);
        daoAddress.create(addressToDelete);
        int deletedId = addressToDelete.getNumAddress();
        daoAddress.delete(addressToDelete);
        Address result = daoAddress.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that delete() returns false when a non-existing Address is passed.
     * Given : an Address with a non-existing ID -1
     * When  : delete() is called with this Address
     * Then  : the result must be false
     */
    @Test
    @Order(13)
    public void delete_GivenNonExistingAddress_ReturnsFalse() throws SQLException {
        Address nonExisting = new Address(-1, 0, "BPX", "Unknown", "", null);
        boolean result = daoAddress.delete(nonExisting);
        assertFalse(result);
    }

    // findAllByEstablishment //

    /**
     * Tests that findAllByEstablishment() never returns null.
     * Given : any establishment name (even one that does not exist)
     * When  : findAllByEstablishment() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(14)
    public void findAllByEstablishment_GivenAnyName_DoesNotReturnNull() throws SQLException {
        List<Address> result = daoAddress.findAllByEstablishment("SomeEstablishment");
        assertNotNull(result);
    }

    /**
     * Tests that findAllByEstablishment() returns an empty list when no establishment matches.
     * Given : an establishment name that does not exist in the database
     * When  : findAllByEstablishment() is called with this name
     * Then  : the result must be an empty list
     */
    @Test
    @Order(15)
    public void findAllByEstablishment_GivenNonExistingName_ReturnsEmptyList() throws SQLException {
        List<Address> result = daoAddress.findAllByEstablishment("EstablishmentThatDoesNotExist");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests that findAllByEstablishment() does not load the Establishment on returned addresses.
     * Given : a valid establishment name that exists in the database
     * When  : findAllByEstablishment() is called and at least one address is returned
     * Then  : the Establishment field of each returned Address must be null (lazy loading)
     */
    @Test
    @Order(16)
    public void findAllByEstablishment_GivenExistingName_ReturnsAddressesWithNullEstablishment() throws SQLException {
        List<Address> result = daoAddress.findAllByEstablishment("Maternelle Soleil");
        if (!result.isEmpty()) {
            result.forEach(a -> assertNull(a.getEstablishment()));
        }
    }
}