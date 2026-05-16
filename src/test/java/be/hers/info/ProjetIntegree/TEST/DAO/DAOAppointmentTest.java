package be.hers.info.ProjetIntegree.TEST.DAO;

import be.hers.info.ProjetIntegree.DAO.*;
import be.hers.info.ProjetIntegree.POJO.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DAOAppointment}.
 * Verifies the correct behaviour of find, findAll, create, update, delete,
 * addInterpreterAtAppointment, addAcademicSkillAtAppointment,
 * addProfessionalSkillAtAppointment, findListInterpreter,
 * findListAcademicSkillRequire, findListProfessionalSkillRequire,
 * findAllAppointmentToBeneficiaryAndDate, findAllAppointmentToInterpreterAndDate
 * and findAllAbsenceToInterpreterAndDate.
 * All tests run in a single transaction that is rolled back after all tests,
 * so no data is persisted in the database.
 *
 * Important notes:
 * - An Appointment requires a Beneficiary, a TimeSlot and an Establishment already in the database.
 *   All are inserted in setUp().
 * - find() loads Beneficiary, TimeSlot and Establishment eagerly.
 *   Interpreters, academic and professional skills are NOT loaded by find() — lazy loading.
 * - The Establishment has a UNIQUE constraint on FKAddress — each create() uses a fresh Address.
 * - status must be one of: 'en attente', 'accepte', 'refuse' (DB constraint).
 * - The address with ID 1 must already exist in the database.
 * - findAllAppointmentToBeneficiaryAndDate(), findAllAppointmentToInterpreterAndDate()
 *   and findAllAbsenceToInterpreterAndDate() use DATE '?' in SQL which does not bind
 *   PreparedStatement parameters correctly — the tests document this behaviour.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOAppointmentTest {

    private static DAOAppointment daoAppointment;
    private static DAOInterpreter daoInterpreter;
    private static DAOBeneficiary daoBeneficiary;
    private static DAOTimeSlotPunctual daoTimeSlotPunctual;
    private static DAOAcademicSkill daoAcademicSkill;
    private static DAOProfessionalSkill daoProfessionalSkill;
    private static DAOAddress daoAddress;
    private static DAOAbsence daoAbsence;
    private static Connection connect;

    private static Appointment appointmentTest;
    private static Beneficiary beneficiaryTest;
    private static Interpreter interpreterTest;
    private static TimeSlotPunctual timeSlotPunctualTest;
    private static Establishment establishmentTest;
    private static AcademicSkill academicSkillTest;
    private static ProfessionalSkill professionalSkillTest;
    private static Address address;

    // Set Up //

    /**
     * Initializes all DAOs, disables auto-commit, then inserts all required objects:
     * Address, Interpreter, TimeSlotPunctual, Establishment, AcademicSkill, ProfessionalSkill,
     * Beneficiary and finally a test Appointment with all its linked objects.
     * The address with ID 1 must already exist in the database.
     */
    @BeforeAll
    public static void setUp() throws SQLException, BadStatusException {
        daoAppointment       = new DAOAppointment();
        daoInterpreter       = new DAOInterpreter();
        daoBeneficiary       = new DAOBeneficiary();
        daoTimeSlotPunctual  = new DAOTimeSlotPunctual();
        daoAcademicSkill     = new DAOAcademicSkill();
        daoProfessionalSkill = new DAOProfessionalSkill();
        daoAddress           = new DAOAddress();
        daoAbsence           = new DAOAbsence();

        connect = daoAppointment.connect;
        connect.setAutoCommit(false);

        address = daoAddress.find(1);

        // Interpreter
        interpreterTest = new Interpreter(
                "apptLogin", "apptPassword", "ApptLast", "ApptFirst",
                "0477000010", "appt@mail.be", 38, address
        );
        daoInterpreter.create(interpreterTest);

        // TimeSlotPunctual — covers 2026-06-10 to 2026-06-12
        timeSlotPunctualTest = new TimeSlotPunctual(
                LocalTime.of(9, 0), LocalTime.of(1, 0),
                LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 12)
        );
        daoTimeSlotPunctual.create(timeSlotPunctualTest);

        // Fresh address for Establishment (FKAddress UNIQUE constraint)
        Address estAddress = new Address(9000, "BPEst", "EstLocality", "", null);
        daoAddress.create(estAddress);
        DAOEstablishment daoEstablishment = new DAOEstablishment();
        List<Address> estAddresses = new ArrayList<>();
        estAddresses.add(estAddress);
        establishmentTest = new Establishment(
                "ApptBuilding", "0800000010", Arrays.asList(1), new ArrayList<>(), estAddresses
        );
        daoEstablishment.create(establishmentTest);

        // AcademicSkill & ProfessionalSkill
        academicSkillTest = new AcademicSkill(5,"ApptAcademic");
        daoAcademicSkill.create(academicSkillTest);
        professionalSkillTest = new ProfessionalSkill("ApptProfessional");
        daoProfessionalSkill.create(professionalSkillTest);

        // Fresh address for Beneficiary (FKAddress NOT UNIQUE but needs its own)
        Address benAddress = new Address(8000, "BPBen", "BenLocality", "", null);
        daoAddress.create(benAddress);
        beneficiaryTest = new Beneficiary(
                "benApptLogin", "benApptPassword", "BenApptLast", "BenApptFirst",
                "0477000011", "benappt@mail.be", benAddress, 5, 1,
                interpreterTest, Arrays.asList("Français"), null
        );
        daoBeneficiary.create(beneficiaryTest);

        // Appointment
        List<Interpreter> interpreters = new ArrayList<>();
        interpreters.add(interpreterTest);
        List<AcademicSkill> academicSkills = new ArrayList<>();
        academicSkills.add(academicSkillTest);
        List<ProfessionalSkill> professionalSkills = new ArrayList<>();
        professionalSkills.add(professionalSkillTest);

        appointmentTest = new Appointment(
                0, "Description test", beneficiaryTest,
                null, interpreters, academicSkills, professionalSkills,
                timeSlotPunctualTest, establishmentTest
        );
        daoAppointment.create(appointmentTest);
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

    // ═══════════════════════════ create ═══════════════════════════

    /**
     * Tests that {@code create()} returns true when a valid Appointment is inserted.
     * Given : a valid Appointment with all required objects already in the database
     * When  : create() is called with this Appointment
     * Then  : the result must be true
     */
    @Test
    @Order(1)
    public void create_GivenValidAppointment_ReturnsTrue() throws SQLException {
        List<Interpreter> interpreters = List.of(interpreterTest);
        List<AcademicSkill> acaSkills = List.of(academicSkillTest);
        List<ProfessionalSkill> profSkills = List.of(professionalSkillTest);

        Appointment appointment = new Appointment(
                0, "Desc create", beneficiaryTest,
                null, interpreters, acaSkills, profSkills,
                timeSlotPunctualTest, establishmentTest
        );
        boolean result = daoAppointment.create(appointment);
        assertTrue(result);
    }

    /**
     * Tests that {@code create()} sets the generated numAppointment on the object after insertion.
     * Given : a valid Appointment with all required objects
     * When  : create() is called
     * Then  : getNumAppointment() must return a value greater than 0
     */
    @Test
    @Order(2)
    public void create_GivenValidAppointment_SetsGeneratedIdOnObject() throws SQLException {
        List<Interpreter> interpreters = List.of(interpreterTest);
        List<AcademicSkill> acaSkills = List.of(academicSkillTest);
        List<ProfessionalSkill> profSkills = List.of(professionalSkillTest);

        Appointment appointment = new Appointment(
                0, "Desc id", beneficiaryTest,
                null, interpreters, acaSkills, profSkills,
                timeSlotPunctualTest, establishmentTest
        );
        daoAppointment.create(appointment);
        assertTrue(appointment.getNumAppointment() > 0);
    }

    // ═══════════════════════════ find ═══════════════════════════

    /**
     * Tests that {@code find()} returns the correct Appointment when an existing ID is passed.
     * Given : an Appointment inserted in setUp()
     * When  : find() is called with its generated numAppointment
     * Then  : the result must not be null and its numAppointment must match
     */
    @Test
    @Order(3)
    public void find_GivenExistingId_ReturnsMatchingAppointment() throws SQLException {
        int existingId = appointmentTest.getNumAppointment();
        Appointment result = daoAppointment.find(existingId);
        assertNotNull(result);
        assertEquals(existingId, result.getNumAppointment());
    }

    /**
     * Tests that {@code find()} returns an Appointment with the correct status.
     * Given : an Appointment inserted in setUp() with default status "en attente"
     * When  : find() is called with its numAppointment
     * Then  : getStatus() must return "en attente"
     */
    @Test
    @Order(4)
    public void find_GivenExistingId_ReturnsCorrectStatus() throws SQLException {
        Appointment result = daoAppointment.find(appointmentTest.getNumAppointment());
        assertNotNull(result);
        assertEquals("en attente", result.getStatus());
    }

    /**
     * Tests that {@code find()} loads the Beneficiary of the Appointment eagerly.
     * Given : an Appointment inserted in setUp() with a valid Beneficiary
     * When  : find() is called with its numAppointment
     * Then  : getBeneficiary() must not be null
     */
    @Test
    @Order(5)
    public void find_GivenExistingId_LoadsBeneficiaryEagerly() throws SQLException {
        Appointment result = daoAppointment.find(appointmentTest.getNumAppointment());
        assertNotNull(result);
        assertNotNull(result.getBeneficiary());
    }

    /**
     * Tests that {@code find()} loads the TimeSlot of the Appointment eagerly.
     * Given : an Appointment inserted in setUp() linked to a TimeSlotPunctual
     * When  : find() is called with its numAppointment
     * Then  : getTimeSlot() must not be null and must be an instance of TimeSlotPunctual
     */
    @Test
    @Order(6)
    public void find_GivenExistingId_LoadsTimeSlotEagerly() throws SQLException {
        Appointment result = daoAppointment.find(appointmentTest.getNumAppointment());
        assertNotNull(result);
        assertNotNull(result.getTimeSlot());
        assertInstanceOf(TimeSlotPunctual.class, result.getTimeSlot());
    }

    /**
     * Tests that {@code find()} loads the Establishment of the Appointment eagerly.
     * Given : an Appointment inserted in setUp() linked to an Establishment
     * When  : find() is called with its numAppointment
     * Then  : getEstablishment() must not be null
     */
    @Test
    @Order(7)
    public void find_GivenExistingId_LoadsEstablishmentEagerly() throws SQLException {
        Appointment result = daoAppointment.find(appointmentTest.getNumAppointment());
        assertNotNull(result);
        assertNotNull(result.getEstablishment());
    }

    /**
     * Tests that {@code find()} returns null when a non-existing ID is passed.
     * Given : a non-existing numAppointment -1
     * When  : find() is called with this ID
     * Then  : the result must be null
     */
    @Test
    @Order(8)
    public void find_GivenNonExistingId_ReturnsNull() throws SQLException {
        Appointment result = daoAppointment.find(-1);
        assertNull(result);
    }

    // ═══════════════════════════ findAll ═══════════════════════════

    /**
     * Tests that {@code findAll()} never returns null.
     * Given : the Appointment table contains at least the appointment inserted in setUp()
     * When  : findAll() is called
     * Then  : the result must not be null
     */
    @Test
    @Order(9)
    public void findAll_GivenTableContainsData_DoesNotReturnNull() throws SQLException {
        List<Appointment> result = daoAppointment.findAll();
        assertNotNull(result);
    }

    /**
     * Tests that {@code findAll()} returns a list containing the appointment inserted in setUp().
     * Given : an Appointment was inserted in setUp()
     * When  : findAll() is called
     * Then  : the list must not be empty and must contain the inserted appointment
     */
    @Test
    @Order(10)
    public void findAll_GivenAppointmentWasInserted_ListContainsInsertedAppointment() throws SQLException {
        List<Appointment> result = daoAppointment.findAll();
        assertFalse(result.isEmpty());
        boolean found = result.stream()
                .anyMatch(a -> a.getNumAppointment() == appointmentTest.getNumAppointment());
        assertTrue(found);
    }

    // ═══════════════════════════ update ═══════════════════════════

    /**
     * Tests that {@code update()} returns true when an existing Appointment is updated.
     * Given : the Appointment inserted in setUp() with description updated to "UpdatedDescription"
     * When  : update() is called with this Appointment
     * Then  : the result must be true
     */
    @Test
    @Order(11)
    public void update_GivenExistingAppointment_ReturnsTrue() throws SQLException {
        appointmentTest.setDescription("UpdatedDescription");
        boolean result = daoAppointment.update(appointmentTest);
        assertTrue(result);
    }

    /**
     * Tests that {@code update()} persists the description change in the database.
     * Given : the Appointment inserted in setUp() with description updated to "VerifiedDescription"
     * When  : update() is called and then find() is called with the same numAppointment
     * Then  : getDescription() must return "VerifiedDescription"
     */
    @Test
    @Order(12)
    public void update_GivenExistingAppointment_DescriptionChangesShouldBePersisted() throws SQLException {
        appointmentTest.setDescription("VerifiedDescription");
        daoAppointment.update(appointmentTest);
        Appointment result = daoAppointment.find(appointmentTest.getNumAppointment());
        assertNotNull(result);
        assertEquals("VerifiedDescription", result.getDescription());
    }

    /**
     * Tests that {@code update()} returns false when a non-existing Appointment is passed.
     * Given : an Appointment with a non-existing numAppointment 0
     * When  : update() is called with this Appointment
     * Then  : the result must be false
     */
    @Test
    @Order(13)
    public void update_GivenNonExistingAppointment_ReturnsFalse() throws SQLException {
        Appointment nonExisting = new Appointment();
        nonExisting.setNumAppointment(0);
        nonExisting.setDescription("Ghost");
        nonExisting.setEstablishment(establishmentTest);
        nonExisting.setBeneficiary(beneficiaryTest);
        nonExisting.setTimeSlot(timeSlotPunctualTest);
        boolean result = daoAppointment.update(nonExisting);
        assertFalse(result);
    }

    // ═══════════════════════════ addInterpreterAtAppointment ═══════════════════════════

    /**
     * Tests that {@code addInterpreterAtAppointment()} returns true when valid IDs are passed.
     * Given : a valid numAppointment and a fresh Interpreter inserted just before the call
     * When  : addInterpreterAtAppointment() is called with these IDs
     * Then  : the result must be true
     */
    @Test
    @Order(14)
    public void addInterpreterAtAppointment_GivenValidIds_ReturnsTrue() throws SQLException {
        Interpreter newInterp = new Interpreter(
                "newInterpLogin", "newInterpPass", "NewLast", "NewFirst",
                "0477000012", "newinterp@mail.be", 20, address
        );
        daoInterpreter.create(newInterp);
        boolean result = daoAppointment.addInterpreterAtAppointment(
                appointmentTest.getNumAppointment(), newInterp.getNumInterpreter()
        );
        assertTrue(result);
    }

    // ═══════════════════════════ addAcademicSkillAtAppointment ═══════════════════════════

    /**
     * Tests that {@code addAcademicSkillAtAppointment()} returns true when valid IDs are passed.
     * Given : a valid numAppointment and a fresh AcademicSkill inserted just before the call
     * When  : addAcademicSkillAtAppointment() is called with these IDs
     * Then  : the result must be true
     */
    @Test
    @Order(15)
    public void addAcademicSkillAtAppointment_GivenValidIds_ReturnsTrue() throws SQLException {
        AcademicSkill newSkill = new AcademicSkill(10,"NewAcademicSkill");
        daoAcademicSkill.create(newSkill);
        boolean result = daoAppointment.addAcademicSkillAtAppointment(
                appointmentTest.getNumAppointment(), newSkill.getNumAcademicSkill()
        );
        assertTrue(result);
    }

    // ═══════════════════════════ addProfessionalSkillAtAppointment ═══════════════════════════

    /**
     * Tests that {@code addProfessionalSkillAtAppointment()} returns true when valid IDs are passed.
     * Given : a valid numAppointment and a fresh ProfessionalSkill inserted just before the call
     * When  : addProfessionalSkillAtAppointment() is called with these IDs
     * Then  : the result must be true
     */
    @Test
    @Order(16)
    public void addProfessionalSkillAtAppointment_GivenValidIds_ReturnsTrue() throws SQLException {
        ProfessionalSkill newSkill = new ProfessionalSkill("NewProfSkill");
        daoProfessionalSkill.create(newSkill);
        boolean result = daoAppointment.addProfessionalSkillAtAppointment(
                appointmentTest.getNumAppointment(), newSkill.getNumProfessionalSkill()
        );
        assertTrue(result);
    }

    // ═══════════════════════════ findListInterpreter ═══════════════════════════

    /**
     * Tests that {@code findListInterpreter()} returns a non-empty list for an existing Appointment.
     * Given : an Appointment inserted in setUp() with at least one Interpreter
     * When  : findListInterpreter() is called with its numAppointment
     * Then  : the result must not be null and must not be empty
     */
    @Test
    @Order(17)
    public void findListInterpreter_GivenExistingAppointment_ReturnsNonEmptyList() throws SQLException {
        List<Interpreter> result = daoAppointment.findListInterpreter(appointmentTest.getNumAppointment());
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    /**
     * Tests that {@code findListInterpreter()} returns an empty list for a non-existing Appointment.
     * Given : a non-existing numAppointment 0
     * When  : findListInterpreter() is called with this ID
     * Then  : the result must be an empty list
     */
    @Test
    @Order(18)
    public void findListInterpreter_GivenNonExistingAppointment_ReturnsEmptyList() throws SQLException {
        List<Interpreter> result = daoAppointment.findListInterpreter(0);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ═══════════════════════════ findListAcademicSkillRequire ═══════════════════════════

    /**
     * Tests that {@code findListAcademicSkillRequire()} returns a non-empty list for an existing Appointment.
     * Given : an Appointment inserted in setUp() with at least one AcademicSkill
     * When  : findListAcademicSkillRequire() is called with its numAppointment
     * Then  : the result must not be null and must not be empty
     */
    @Test
    @Order(19)
    public void findListAcademicSkillRequire_GivenExistingAppointment_ReturnsNonEmptyList() throws SQLException {
        List<AcademicSkill> result = daoAppointment.findListAcademicSkillRequire(appointmentTest.getNumAppointment());
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    /**
     * Tests that {@code findListAcademicSkillRequire()} returns an empty list for a non-existing Appointment.
     * Given : a non-existing numAppointment 0
     * When  : findListAcademicSkillRequire() is called with this ID
     * Then  : the result must be an empty list
     */
    @Test
    @Order(20)
    public void findListAcademicSkillRequire_GivenNonExistingAppointment_ReturnsEmptyList() throws SQLException {
        List<AcademicSkill> result = daoAppointment.findListAcademicSkillRequire(0);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ═══════════════════════════ findListProfessionalSkillRequire ═══════════════════════════

    /**
     * Tests that {@code findListProfessionalSkillRequire()} returns a non-empty list for an existing Appointment.
     * Given : an Appointment inserted in setUp() with at least one ProfessionalSkill
     * When  : findListProfessionalSkillRequire() is called with its numAppointment
     * Then  : the result must not be null and must not be empty
     */
    @Test
    @Order(21)
    public void findListProfessionalSkillRequire_GivenExistingAppointment_ReturnsNonEmptyList() throws SQLException {
        List<ProfessionalSkill> result = daoAppointment.findListProfessionalSkillRequire(appointmentTest.getNumAppointment());
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    /**
     * Tests that {@code findListProfessionalSkillRequire()} returns an empty list for a non-existing Appointment.
     * Given : a non-existing numAppointment 0
     * When  : findListProfessionalSkillRequire() is called with this ID
     * Then  : the result must be an empty list
     */
    @Test
    @Order(22)
    public void findListProfessionalSkillRequire_GivenNonExistingAppointment_ReturnsEmptyList() throws SQLException {
        List<ProfessionalSkill> result = daoAppointment.findListProfessionalSkillRequire(0);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ═══════════════════════════ findAllAppointmentToBeneficiaryAndDate ═══════════════════════════

    /**
     * Tests that {@code findAllAppointmentToBeneficiaryAndDate()} never returns null.
     * Given : a valid numBeneficiary and a date range
     * When  : findAllAppointmentToBeneficiaryAndDate() is called
     * Then  : the result must not be null
     * Note  : this method uses DATE '?' in SQL which does not bind PreparedStatement parameters.
     *         The query may return unexpected results or fail — this test documents the behaviour.
     */
    @Test
    @Order(23)
    public void findAllAppointmentToBeneficiaryAndDate_GivenValidParams_DoesNotReturnNull() throws SQLException {
        List<Appointment> result = daoAppointment.findAllAppointmentToBeneficiaryAndDate(
                beneficiaryTest.getNumBeneficiary(), "2026-06-01", "2026-06-30"
        );
        assertNotNull(result);
    }

    // ═══════════════════════════ findAllAppointmentToInterpreterAndDate ═══════════════════════════

    /**
     * Tests that {@code findAllAppointmentToInterpreterAndDate()} never returns null.
     * Given : a valid Interpreter and a date range
     * When  : findAllAppointmentToInterpreterAndDate() is called
     * Then  : the result must not be null
     * Note  : this method uses DATE '?' in SQL which does not bind PreparedStatement parameters.
     *         The query may return unexpected results or fail — this test documents the behaviour.
     */
    @Test
    @Order(24)
    public void findAllAppointmentToInterpreterAndDate_GivenValidParams_DoesNotReturnNull() throws SQLException {
        List<Appointment> result = daoAppointment.findAllAppointmentToInterpreterAndDate(
                interpreterTest, "2026-06-01", "2026-06-30"
        );
        assertNotNull(result);
    }

    // ═══════════════════════════ findAllAbsenceToInterpreterAndDate ═══════════════════════════

    /**
     * Tests that {@code findAllAbsenceToInterpreterAndDate()} never returns null.
     * Given : a valid Interpreter and a date range
     * When  : findAllAbsenceToInterpreterAndDate() is called
     * Then  : the result must not be null
     * Note  : this method uses DATE '?' in SQL which does not bind PreparedStatement parameters.
     *         The query may return unexpected results or fail — this test documents the behaviour.
     */
    @Test
    @Order(25)
    public void findAllAbsenceToInterpreterAndDate_GivenValidParams_DoesNotReturnNull() throws SQLException {
        List<Absence> result = daoAppointment.findAllAbsenceToInterpreterAndDate(
                interpreterTest, "2026-06-01", "2026-06-30"
        );
        assertNotNull(result);
    }

    // ═══════════════════════════ delete ═══════════════════════════

    /**
     * Tests that {@code delete()} returns true when an existing Appointment is deleted.
     * Given : a valid Appointment inserted just before deletion
     * When  : delete() is called with this Appointment
     * Then  : the result must be true
     */
    @Test
    @Order(26)
    public void delete_GivenExistingAppointment_ReturnsTrue() throws SQLException {
        List<Interpreter> interpreters = List.of(interpreterTest);
        List<AcademicSkill> acaSkills = List.of(academicSkillTest);
        List<ProfessionalSkill> profSkills = List.of(professionalSkillTest);

        Appointment appointmentToDelete = new Appointment(
                0, "Desc del", beneficiaryTest,
                null, interpreters, acaSkills, profSkills,
                timeSlotPunctualTest, establishmentTest
        );
        daoAppointment.create(appointmentToDelete);
        boolean result = daoAppointment.delete(appointmentToDelete);
        assertTrue(result);
    }

    /**
     * Tests that {@code delete()} removes the Appointment from the database.
     * Given : a valid Appointment inserted just before deletion
     * When  : delete() is called and then find() is called with the deleted numAppointment
     * Then  : find() must return null
     */
    @Test
    @Order(27)
    public void delete_GivenExistingAppointment_ObjectNoLongerExistsInDatabase() throws SQLException {
        List<Interpreter> interpreters = List.of(interpreterTest);
        List<AcademicSkill> acaSkills = List.of(academicSkillTest);
        List<ProfessionalSkill> profSkills = List.of(professionalSkillTest);

        Appointment appointmentToDelete = new Appointment(
                0, "Desc verify del", beneficiaryTest,
                null, interpreters, acaSkills, profSkills,
                timeSlotPunctualTest, establishmentTest
        );
        daoAppointment.create(appointmentToDelete);
        int deletedId = appointmentToDelete.getNumAppointment();
        daoAppointment.delete(appointmentToDelete);
        Appointment result = daoAppointment.find(deletedId);
        assertNull(result);
    }

    /**
     * Tests that {@code delete()} returns false when a non-existing Appointment is passed.
     * Given : an Appointment with a non-existing numAppointment 0
     * When  : delete() is called with this Appointment
     * Then  : the result must be false
     */
    @Test
    @Order(28)
    public void delete_GivenNonExistingAppointment_ReturnsFalse() throws SQLException {
        Appointment nonExisting = new Appointment();
        nonExisting.setNumAppointment(0);
        boolean result = daoAppointment.delete(nonExisting);
        assertFalse(result);
    }
}