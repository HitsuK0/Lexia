package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.DAOAcademicSkill;
import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOAcademicSkill}.
 * Verifies the correct behaviour of find, findAll, create, update and delete operations.
 * All tests run in a single transaction that is rolled back after all tests,
 * so no data is persisted in the database.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOAcademicSkillTest {

    private static DAOAcademicSkill daoAcademicSkill;
    private static Connection connect;
    private static AcademicSkill academicSkillTest;

    // Set Up //

    /**
     * Initializes the DAO, disables auto-commit to allow rollback after all tests, and creates a test AcademicSkill.
     */
    @BeforeAll
    public static void setUp() throws SQLException {
        daoAcademicSkill = new DAOAcademicSkill();
        connect = daoAcademicSkill.connect;
        connect.setAutoCommit(false);
        academicSkillTest = new AcademicSkill(1, "TestDesignation");
        daoAcademicSkill.create(academicSkillTest);
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
     * Tests that create() returns true when a valid AcademicSkill is inserted.
     * Given : a valid AcademicSkill with designation="ToCreate"
     * When  : create() is called with this AcademicSkill
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidAcademicSkill_ReturnsTrue() throws SQLException {
        AcademicSkill skillToCreate = new AcademicSkill(2, "ToCreate");
        boolean result = daoAcademicSkill.create(skillToCreate);
        assertTrue(result);
    }

    /**
     * Tests that create() sets the generated ID on the object after insertion.
     * Given : a valid AcademicSkill with designation="DesignationWithId"
     * When  : create() is called with this AcademicSkill
     * Then  : getNumAcademicSkill() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidAcademicSkill_SetsGeneratedIdOnObject() throws SQLException {
        AcademicSkill skillToCreate = new AcademicSkill();
        skillToCreate.setDesignation("DesignationWithId");
        daoAcademicSkill.create(skillToCreate);
        assertTrue(skillToCreate.getNumAcademicSkill() > 0);
    }

    // find //

    /**
     * Tests that find() returns the correct AcademicSkill when an existing ID is passed.
     * Given : an AcademicSkill that was inserted in setUp()
     * When  : find() is called with its ID
     * Then  : the result must not be null, its ID and designation must match the inserted skill
     */
    @Test
    @Order(3)
    public void find_GivenExistingId_ReturnsMatchingAcademicSkill() throws SQLException {
        int existingId = academicSkillTest.getNumAcademicSkill();
        AcademicSkill result = daoAcademicSkill.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumAcademicSkill());
        assertEquals(academicSkillTest.getDesignation(), result.getDesignation());
    }

    /**
     * Tests that find() returns null when a non-existing ID is passed.
     * Given : a non-existing ID -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(4)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        AcademicSkill result = daoAcademicSkill.find(-1);
        assertNull(result);
    }

    // findAll //

    /**
     * Tests that findAll() never returns null.
     * Given : the AcademicSkill table contains at least the skill inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(5)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<AcademicSkill> result = daoAcademicSkill.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that findAll() returns a list containing the skill inserted in setUp().
     * Given : an AcademicSkill was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted skill
     */
    @Test
    @Order(6)
    public void findAll_GivenSkillWasInserted_ListContainsInsertedSkill() throws SQLException {
        List<AcademicSkill> result = daoAcademicSkill.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(as -> as.getNumAcademicSkill() == academicSkillTest.getNumAcademicSkill());
        assertTrue(found);
    }

    // update //

    /**
     * Tests that update() returns true when an existing AcademicSkill is updated.
     * Given : the AcademicSkill inserted in setUp() with a modified designation "ModifiedDesignation"
     * When  : update() is called with this AcademicSkill
     * Then  : the result must be true
     */
    @Test
    @Order(7)
    public void update_GivenExistingAcademicSkill_ReturnsTrue() throws SQLException {
        academicSkillTest.setDesignation("ModifiedDesignation");
        boolean result = daoAcademicSkill.update(academicSkillTest);
        assertTrue(result);
    }

    /**
     * Tests that update() persists the changes in the database.
     * Given : the AcademicSkill inserted in setUp() with designation updated to "VerifiedDesignation"
     * When  : update() is called and then find() is called with the same ID
     * Then  : the found skill must have designation "VerifiedDesignation"
     */
    @Test
    @Order(8)
    public void update_GivenExistingAcademicSkill_ChangesShouldBePersisted() throws SQLException {
        academicSkillTest.setDesignation("VerifiedDesignation");
        daoAcademicSkill.update(academicSkillTest);
        AcademicSkill result = daoAcademicSkill.find(academicSkillTest.getNumAcademicSkill());
        assertNotNull(result);
        assertEquals("VerifiedDesignation", result.getDesignation());
    }

    /**
     * Tests that update() returns false when a non-existing AcademicSkill is passed.
     * Given : an AcademicSkill with a non-existing ID -1
     * When  : update() is called with this AcademicSkill
     * Then  : the result must be false
     */
    @Test
    @Order(9)
    public void update_GivenNonExistingAcademicSkill_ReturnsFalse() throws SQLException {
        AcademicSkill nonExistingSkill = new AcademicSkill(3,"NonExistingDesignation");
        nonExistingSkill.setNumAcademicSkill(-1);
        boolean result = daoAcademicSkill.update(nonExistingSkill);
        assertFalse(result);
    }

    // delete //

    /**
     * Tests that delete() returns true when an existing AcademicSkill is deleted.
     * Given : a valid AcademicSkill inserted just before deletion
     * When  : delete() is called with this AcademicSkill
     * Then  : the result must be true
     */
    @Test
    @Order(10)
    public void delete_GivenExistingAcademicSkill_ReturnsTrue() throws SQLException {
        AcademicSkill skillToDelete = new AcademicSkill(4,"ToDelete");
        daoAcademicSkill.create(skillToDelete);
        boolean result = daoAcademicSkill.delete(skillToDelete);
        assertTrue(result);
    }

    /**
     * Tests that delete() removes the AcademicSkill from the database.
     * Given : a valid AcademicSkill inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted ID
     * Then  : find() must return null
     */
    @Test
    @Order(11)
    public void delete_GivenExistingAcademicSkill_ObjectNoLongerExistsInDatabase() throws SQLException {
        AcademicSkill skillToDelete = new AcademicSkill(5,"ToVerifyDeletion");
        daoAcademicSkill.create(skillToDelete);
        int deletedId = skillToDelete.getNumAcademicSkill();
        daoAcademicSkill.delete(skillToDelete);
        AcademicSkill result = daoAcademicSkill.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that delete() returns false when a non-existing AcademicSkill is passed.
     * Given : an AcademicSkill with a non-existing ID -1
     * When  : delete() is called with this AcademicSkill
     * Then  : the result must be false
     */
    @Test
    @Order(12)
    public void delete_GivenNonExistingAcademicSkill_ReturnsFalse() throws SQLException {
        AcademicSkill nonExistingSkill = new AcademicSkill(6,"NonExisting");
        nonExistingSkill.setNumAcademicSkill(-1);
        boolean result = daoAcademicSkill.delete(nonExistingSkill);
        assertFalse(result);
    }
}