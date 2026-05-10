package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.DAOProfessionalSkill;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOProfessionalSkill}.
 * Verifies the correct behaviour of find, findAll, create, update and delete operations.
 * All tests run in a single transaction that is rolled back after all tests, so no data is persisted in the database.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOProfessionalSkillTest {

    private static DAOProfessionalSkill daoProfessionalSkill;
    private static Connection connect;
    private static ProfessionalSkill professionalSkillTest;

    // Set Up //

    /**
     * Initializes the DAO, disables auto-commit to allow rollback after all tests,
     * and inserts a test ProfessionalSkill.
     */
    @BeforeAll
    public static void setUp() throws SQLException {
        daoProfessionalSkill = new DAOProfessionalSkill();
        connect = daoProfessionalSkill.connect;
        connect.setAutoCommit(false);

        professionalSkillTest = new ProfessionalSkill("TestDesignation");
        daoProfessionalSkill.create(professionalSkillTest);
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
     * Tests that create() returns true when a valid ProfessionalSkill is inserted.
     * Given : a valid ProfessionalSkill with designation="ToCreate"
     * When  : create() is called with this ProfessionalSkill
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidProfessionalSkill_ReturnsTrue() throws SQLException {
        ProfessionalSkill skill = new ProfessionalSkill("ToCreate");
        boolean result = daoProfessionalSkill.create(skill);
        assertTrue(result);
    }

    /**
     * Tests that create() sets the generated numProfessionalSkill on the object after insertion.
     * Given : a valid ProfessionalSkill with designation="DesignationWithId"
     * When  : create() is called with this ProfessionalSkill
     * Then  : getNumProfessionalSkill() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidProfessionalSkill_SetsGeneratedIdOnObject() throws SQLException {
        ProfessionalSkill skill = new ProfessionalSkill("DesignationWithId");
        daoProfessionalSkill.create(skill);
        assertTrue(skill.getNumProfessionalSkill() > 0);
    }

    // find //

    /**
     * Tests that find() returns the correct ProfessionalSkill when an existing ID is passed.
     * Given : a ProfessionalSkill inserted in setUp()
     * When  : find() is called with its generated numProfessionalSkill
     * Then  : the result must not be null and its numProfessionalSkill must match
     */
    @Test
    @Order(3)
    public void find_GivenExistingId_ReturnsMatchingProfessionalSkill() throws SQLException {
        int existingId = professionalSkillTest.getNumProfessionalSkill();
        ProfessionalSkill result = daoProfessionalSkill.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumProfessionalSkill());
    }

    /**
     * Tests that find() returns a ProfessionalSkill with the correct designation.
     * Given : a ProfessionalSkill inserted in setUp() with designation="TestDesignation"
     * When  : find() is called with its numProfessionalSkill
     * Then  : getDesignation() must return "TestDesignation"
     */
    @Test
    @Order(4)
    public void find_GivenExistingId_ReturnsCorrectDesignation() throws SQLException {
        ProfessionalSkill result = daoProfessionalSkill.find(professionalSkillTest.getNumProfessionalSkill());
        assertNotNull(result);
        assertEquals("TestDesignation", result.getDesignation());
    }

    /**
     * Tests that find() returns null when a non-existing ID is passed.
     * Given : a non-existing numProfessionalSkill -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(5)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        ProfessionalSkill result = daoProfessionalSkill.find(-1);
        assertNull(result);
    }

    // findAll //

    /**
     * Tests that findAll() never returns null.
     * Given : the ProfessionalSkill table contains at least the skill inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(6)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<ProfessionalSkill> result = daoProfessionalSkill.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that findAll() returns a list containing the skill inserted in setUp().
     * Given : a ProfessionalSkill was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted skill
     */
    @Test
    @Order(7)
    public void findAll_GivenSkillWasInserted_ListContainsInsertedSkill() throws SQLException {
        List<ProfessionalSkill> result = daoProfessionalSkill.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(ps -> ps.getNumProfessionalSkill() == professionalSkillTest.getNumProfessionalSkill());
        assertTrue(found);
    }

    // update //

    /**
     * Tests that update() returns true when an existing ProfessionalSkill is updated.
     * Given : the ProfessionalSkill inserted in setUp() with designation updated to "UpdatedDesignation"
     * When  : update() is called with this ProfessionalSkill
     * Then  : the result must be true
     */
    @Test
    @Order(8)
    public void update_GivenExistingProfessionalSkill_ReturnsTrue() throws SQLException {
        professionalSkillTest.setDesignation("UpdatedDesignation");
        boolean result = daoProfessionalSkill.update(professionalSkillTest);
        assertTrue(result);
    }

    /**
     * Tests that update() persists the designation change in the database.
     * Given : the ProfessionalSkill inserted in setUp() with designation updated to "VerifiedDesignation"
     * When  : update() is called and then find() is called with the same numProfessionalSkill
     * Then  : getDesignation() must return "VerifiedDesignation"
     */
    @Test
    @Order(9)
    public void update_GivenExistingProfessionalSkill_ChangesShouldBePersisted() throws SQLException {
        professionalSkillTest.setDesignation("VerifiedDesignation");
        daoProfessionalSkill.update(professionalSkillTest);
        ProfessionalSkill result = daoProfessionalSkill.find(professionalSkillTest.getNumProfessionalSkill());
        assertNotNull(result);
        assertEquals("VerifiedDesignation", result.getDesignation());
    }

    /**
     * Tests that update() returns false when a non-existing ProfessionalSkill is passed.
     * Given : a ProfessionalSkill with a non-existing numProfessionalSkill 0
     * When  : update() is called with this ProfessionalSkill
     * Then  : the result must be false
     */
    @Test
    @Order(10)
    public void update_GivenNonExistingProfessionalSkill_ReturnsFalse() throws SQLException {
        ProfessionalSkill nonExisting = new ProfessionalSkill("NonExistingDesignation");
        nonExisting.setNumProfessionalSkill(0);
        boolean result = daoProfessionalSkill.update(nonExisting);
        assertFalse(result);
    }

    // delete //

    /**
     * Tests that delete() returns true when an existing ProfessionalSkill is deleted.
     * Given : a valid ProfessionalSkill inserted just before deletion
     * When  : delete() is called with this ProfessionalSkill
     * Then  : the result must be true
     */
    @Test
    @Order(11)
    public void delete_GivenExistingProfessionalSkill_ReturnsTrue() throws SQLException {
        ProfessionalSkill skillToDelete = new ProfessionalSkill("ToDelete");
        daoProfessionalSkill.create(skillToDelete);
        boolean result = daoProfessionalSkill.delete(skillToDelete);
        assertTrue(result);
    }

    /**
     * Tests that delete() removes the ProfessionalSkill from the database.
     * Given : a valid ProfessionalSkill inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted numProfessionalSkill
     * Then  : find() must return null
     */
    @Test
    @Order(12)
    public void delete_GivenExistingProfessionalSkill_ObjectNoLongerExistsInDatabase() throws SQLException {
        ProfessionalSkill skillToDelete = new ProfessionalSkill("ToVerifyDeletion");
        daoProfessionalSkill.create(skillToDelete);
        int deletedId = skillToDelete.getNumProfessionalSkill();
        daoProfessionalSkill.delete(skillToDelete);
        ProfessionalSkill result = daoProfessionalSkill.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that delete() returns false when a non-existing ProfessionalSkill is passed.
     * Given : a ProfessionalSkill with a non-existing numProfessionalSkill 0
     * When  : delete() is called with this ProfessionalSkill
     * Then  : the result must be false
     */
    @Test
    @Order(13)
    public void delete_GivenNonExistingProfessionalSkill_ReturnsFalse() throws SQLException {
        ProfessionalSkill nonExisting = new ProfessionalSkill("NonExisting");
        nonExisting.setNumProfessionalSkill(0);
        boolean result = daoProfessionalSkill.delete(nonExisting);
        assertFalse(result);
    }
}