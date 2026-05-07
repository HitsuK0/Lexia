package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Interpreter}.
 * Verifies the correct behaviour of constructors, getters, setters and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
public class InterpreterTest {

    private Interpreter interpreter;
    private Address address;

    // Set Up //

    /**
     * Initializes a valid {@link Address} and a default {@link Interpreter} before each test.
     */
    @BeforeEach
    void setUp() {
        address = new Address();
        interpreter = new Interpreter();
    }

    // Default Constructor //

    /**
     * Tests that the default constructor initializes all String fields to empty strings.
     * Given : no argument
     * When  : an Interpreter is created with the default constructor
     * Then  : getLogin(), getPassword(), getLastName(), getFirstName(), getEmail() and getPhoneNumber() must all return ""
     */
    @Test
    void defaultConstructor_AllStringFieldsAreEmpty() {
        assertEquals("", interpreter.getLogin());
        assertEquals("", interpreter.getPassword());
        assertEquals("", interpreter.getLastName());
        assertEquals("", interpreter.getFirstName());
        assertEquals("", interpreter.getEmailAddress());
        assertEquals("", interpreter.getPhoneNumber());
    }

    /**
     * Tests that the default constructor initializes weeklyWorkHours to 0.
     * Given : no argument
     * When  : an Interpreter is created with the default constructor
     * Then  : getWeeklyWorkHours() must return 0
     */
    @Test
    void defaultConstructor_WeeklyWorkHoursIsZero() {
        assertEquals(0, interpreter.getWeeklyWorkHours());
    }

    /**
     * Tests that the default constructor initializes address to a non-null Address.
     * Given : no argument
     * When  : an Interpreter is created with the default constructor
     * Then  : getAddress() must not be null
     */
    @Test
    void defaultConstructor_AddressIsNotNull() {
        assertNotNull(interpreter.getAddress());
    }

    /**
     * Tests that the default constructor initializes all lists to empty lists (not null).
     * Given : no argument
     * When  : an Interpreter is created with the default constructor
     * Then  : getAbsences(), getAppointmentsList(), getProfessionalSkillsList(),
     *         getAcademicSkillsList() and getBeneficiariesList() must all be empty and not null
     */
    @Test
    void defaultConstructor_AllListsAreEmpty() {
        assertNotNull(interpreter.getAbsences());
        assertNotNull(interpreter.getAppointmentsList());
        assertNotNull(interpreter.getProfessionalSkillsList());
        assertNotNull(interpreter.getAcademicSkillsList());
        assertNotNull(interpreter.getBeneficiariesList());
        assertTrue(interpreter.getAbsences().isEmpty());
        assertTrue(interpreter.getAppointmentsList().isEmpty());
        assertTrue(interpreter.getProfessionalSkillsList().isEmpty());
        assertTrue(interpreter.getAcademicSkillsList().isEmpty());
        assertTrue(interpreter.getBeneficiariesList().isEmpty());
    }

    // Constructor (login, password, lastName, firstName, email, phoneNumber, weeklyWorkHours, address) //

    /**
     * Tests that the constructor without ID correctly sets all fields when valid arguments are provided.
     * Given : valid login, password, lastName, firstName, email, phoneNumber, weeklyWorkHours=38 and address
     * When  : an Interpreter is created with these arguments
     * Then  : all getters must return the expected values
     */
    @Test
    void constructor_WithoutId_SetsAllFields() {
        Interpreter i = new Interpreter("e0002", "secret", "Nicolas", "JF",  "0477000000", "JF@mail.be", 38, address);
        assertEquals("e0002", i.getLogin());
        assertEquals("secret", i.getPassword());
        assertEquals("Nicolas", i.getLastName());
        assertEquals("JF", i.getFirstName());
        assertEquals("JF@mail.be", i.getEmailAddress());
        assertEquals("0477000000", i.getPhoneNumber());
        assertEquals(38, i.getWeeklyWorkHours());
        assertSame(address, i.getAddress());
    }

    /**
     * Tests that the constructor without ID initializes all lists to empty.
     * Given : valid arguments
     * When  : an Interpreter is created without ID
     * Then  : all list getters must return empty lists
     */
    @Test
    void constructor_WithoutId_AllListsAreEmpty() {
        Interpreter i = new Interpreter("e0002", "secret", "Nicolas", "JF", "JF@mail.be", "0477000000", 38, address);
        assertTrue(i.getAbsences().isEmpty());
        assertTrue(i.getAppointmentsList().isEmpty());
        assertTrue(i.getProfessionalSkillsList().isEmpty());
        assertTrue(i.getAcademicSkillsList().isEmpty());
        assertTrue(i.getBeneficiariesList().isEmpty());
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when weeklyWorkHours is negative.
     * Given : weeklyWorkHours=-1
     * When  : an Interpreter is created with this value
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithNegativeWeeklyWorkHours_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Interpreter("e0002", "secret", "Nicolas", "JF", "JF@mail.be", "0477000000", -1, address));
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when address is null.
     * Given : a null address
     * When  : an Interpreter is created with null address
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithNullAddress_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Interpreter("e0002", "secret", "Nicolas", "JF", "JF@mail.be", "0477000000", 38, null));
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when password is null.
     * Given : a null password
     * When  : an Interpreter is created with null password
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithNullPassword_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Interpreter("e0002", null, "Nicolas", "JF", "JF@mail.be", "0477000000", 38, address));
    }

    /**
     * Tests that the constructor without ID throws an {@link IllegalArgumentException} when password is empty.
     * Given : an empty password
     * When  : an Interpreter is created with an empty password
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithoutId_WithEmptyPassword_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Interpreter("e0002", "", "Nicolas", "JF", "JF@mail.be", "0477000000", 38, address));
    }

    // Constructor (numInterpreter, login, password, lastName, firstName, email, phoneNumber, weeklyWorkHours, address) //

    /**
     * Tests that the constructor with ID correctly sets numInterpreter and all other fields.
     * Given : numInterpreter=5 and valid arguments
     * When  : an Interpreter is created with these arguments
     * Then  : getNumInterpreter() must return 5 and all other getters must return the expected values
     */
    @Test
    void constructor_WithId_SetsAllFields() {
        Interpreter i = new Interpreter(5, "e0002", "secret", "Nicolas", "JF", "JF@mail.be", "0477000000", 38, address);
        assertEquals(5, i.getNumInterpreter());
        assertEquals("e0002", i.getLogin());
        assertEquals("Nicolas", i.getLastName());
        assertEquals("JF", i.getFirstName());
        assertEquals(38, i.getWeeklyWorkHours());
        assertSame(address, i.getAddress());
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when weeklyWorkHours is negative.
     * Given : weeklyWorkHours=-1
     * When  : an Interpreter is created with this value
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithNegativeWeeklyWorkHours_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Interpreter(1, "e0002", "secret", "Nicolas", "JF", "JF@mail.be", "0477000000", -1, address));
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when address is null.
     * Given : a null address
     * When  : an Interpreter is created with null address
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithNullAddress_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Interpreter(1, "e0002", "secret", "Nicolas", "JF", "JF@mail.be", "0477000000", 38, null));
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when password is null.
     * Given : a null password
     * When  : an Interpreter is created with null password
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithNullPassword_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Interpreter(1, "e0002", null, "Nicolas", "JF", "JF@mail.be", "0477000000", 38, address));
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when password is empty.
     * Given : an empty password
     * When  : an Interpreter is created with an empty password
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithEmptyPassword_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Interpreter(1, "e0002", "", "Nicolas", "JF", "JF@mail.be", "0477000000", 38, address));
    }

    // Constructor full with ID (all fields + lists) //

    /**
     * Tests that the full constructor with ID correctly sets all lists.
     * Given : valid arguments with non-null lists
     * When  : an Interpreter is created with these arguments
     * Then  : all list getters must return the provided lists
     */
    @Test
    void constructor_WithIdAndLists_SetsAllLists() {
        List<Absence> absences = new ArrayList<>();
        List<Appointment> appointments = new ArrayList<>();
        List<ProfessionalSkill> profSkills = new ArrayList<>();
        List<AcademicSkill> acaSkills = new ArrayList<>();
        List<Beneficiary> beneficiaries = new ArrayList<>();

        Interpreter i = new Interpreter(1, "e0002", "secret", "Nicolas", "JF", "JF@mail.be", "0477000000", address, 38,
                absences, appointments, profSkills, acaSkills, beneficiaries);

        assertSame(absences, i.getAbsences());
        assertSame(appointments, i.getAppointmentsList());
        assertSame(profSkills, i.getProfessionalSkillsList());
        assertSame(acaSkills, i.getAcademicSkillsList());
        assertSame(beneficiaries, i.getBeneficiariesList());
    }

    // Constructor full without ID (all fields + lists) //

    /**
     * Tests that the full constructor without ID correctly sets all lists.
     * Given : valid arguments with non-null lists and no numInterpreter
     * When  : an Interpreter is created with these arguments
     * Then  : all list getters must return the provided lists
     */
    @Test
    void constructor_WithoutIdAndLists_SetsAllLists() {
        List<Absence> absences = new ArrayList<>();
        List<Appointment> appointments = new ArrayList<>();
        List<ProfessionalSkill> profSkills = new ArrayList<>();
        List<AcademicSkill> acaSkills = new ArrayList<>();
        List<Beneficiary> beneficiaries = new ArrayList<>();

        Interpreter i = new Interpreter("e0002", "secret", "Nicolas", "JF", "JF@mail.be", "0477000000", address, 38,
                absences, appointments, profSkills, acaSkills, beneficiaries);

        assertSame(absences, i.getAbsences());
        assertSame(appointments, i.getAppointmentsList());
        assertSame(profSkills, i.getProfessionalSkillsList());
        assertSame(acaSkills, i.getAcademicSkillsList());
        assertSame(beneficiaries, i.getBeneficiariesList());
    }

    // setNumInterpreter //

    /**
     * Tests that setNumInterpreter() correctly updates the ID.
     * Given : a default Interpreter
     * When  : setNumInterpreter(7) is called
     * Then  : getNumInterpreter() must return 7
     */
    @Test
    void setNumInterpreter_UpdatesTheCorrectValue() {
        interpreter.setNumInterpreter(7);
        assertEquals(7, interpreter.getNumInterpreter());
    }

    // setLogin //

    /**
     * Tests that setLogin() correctly updates the login.
     * Given : a default Interpreter
     * When  : setLogin("newlogin") is called
     * Then  : getLogin() must return "newlogin"
     */
    @Test
    void setLogin_UpdatesTheCorrectValue() {
        interpreter.setLogin("newlogin");
        assertEquals("newlogin", interpreter.getLogin());
    }

    // setPassword //

    /**
     * Tests that setPassword() correctly updates the password.
     * Given : a default Interpreter
     * When  : setPassword("newpass") is called
     * Then  : getPassword() must return "newpass"
     */
    @Test
    void setPassword_UpdatesTheCorrectValue() {
        interpreter.setPassword("newpass");
        assertEquals("newpass", interpreter.getPassword());
    }

    /**
     * Tests that setPassword() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setPassword(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setPassword_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> interpreter.setPassword(null));
    }

    /**
     * Tests that setPassword() throws an {@link IllegalArgumentException} when an empty string is passed.
     * Given : an empty string
     * When  : setPassword("") is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setPassword_WithEmptyString_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> interpreter.setPassword(""));
    }

    // setLastName //

    /**
     * Tests that setLastName() correctly updates the last name.
     * Given : a default Interpreter
     * When  : setLastName("Dubelloy") is called
     * Then  : getLastName() must return "Dubelloy"
     */
    @Test
    void setLastName_UpdatesTheCorrectValue() {
        interpreter.setLastName("Dubelloy");
        assertEquals("Dubelloy", interpreter.getLastName());
    }

    // setFirstName //

    /**
     * Tests that setFirstName() correctly updates the first name.
     * Given : a default Interpreter
     * When  : setFirstName("sarah") is called
     * Then  : getFirstName() must return "sarah"
     */
    @Test
    void setFirstName_UpdatesTheCorrectValue() {
        interpreter.setFirstName("sarah");
        assertEquals("sarah", interpreter.getFirstName());
    }

    // setEmail //

    /**
     * Tests that setEmail() correctly updates the email.
     * Given : a default Interpreter
     * When  : setEmail("sarah@mail.be") is called
     * Then  : getEmail() must return "sarah@mail.be"
     */
    @Test
    void setEmail_UpdatesTheCorrectValue() {
        interpreter.setEmailAddress("sarah@mail.be");
        assertEquals("sarah@mail.be", interpreter.getEmailAddress());
    }

    // setPhoneNumber //

    /**
     * Tests that setPhoneNumber() correctly updates the phone number.
     * Given : a default Interpreter
     * When  : setPhoneNumber("0477123456") is called
     * Then  : getPhoneNumber() must return "0477123456"
     */
    @Test
    void setPhoneNumber_UpdatesTheCorrectValue() {
        interpreter.setPhoneNumber("0477123456");
        assertEquals("0477123456", interpreter.getPhoneNumber());
    }

    // setWeeklyWorkHours //

    /**
     * Tests that setWeeklyWorkHours() correctly updates the weekly work hours.
     * Given : a default Interpreter
     * When  : setWeeklyWorkHours(40) is called
     * Then  : getWeeklyWorkHours() must return 40
     */
    @Test
    void setWeeklyWorkHours_UpdatesTheCorrectValue() {
        interpreter.setWeeklyWorkHours(40);
        assertEquals(40, interpreter.getWeeklyWorkHours());
    }

    /**
     * Tests that setWeeklyWorkHours() throws an {@link IllegalArgumentException} when a negative value is passed.
     * Given : a negative value -1
     * When  : setWeeklyWorkHours(-1) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setWeeklyWorkHours_WithNegativeValue_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> interpreter.setWeeklyWorkHours(-1));
    }

    // setAddress //

    /**
     * Tests that setAddress() correctly updates the address.
     * Given : a new valid Address
     * When  : setAddress() is called with this address
     * Then  : getAddress() must return the new address
     */
    @Test
    void setAddress_UpdatesTheCorrectValue() {
        Address newAddress = new Address();
        interpreter.setAddress(newAddress);
        assertSame(newAddress, interpreter.getAddress());
    }

    /**
     * Tests that setAddress() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setAddress(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAddress_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> interpreter.setAddress(null));
    }

    // setAbsences //

    /**
     * Tests that etAbsences() correctly updates the absences list.
     * Given : a new list of absences
     * When  : setAbsences() is called with this list
     * Then  : getAbsences() must return the new list
     */
    @Test
    void setAbsences_UpdatesTheCorrectValue() {
        List<Absence> absences = new ArrayList<>();
        interpreter.setAbsences(absences);
        assertSame(absences, interpreter.getAbsences());
    }

    // setAppointmentsList //

    /**
     * Tests that setAppointmentsList() correctly updates the appointments list.
     * Given : a new list of appointments
     * When  : setAppointmentsList() is called with this list
     * Then  : getAppointmentsList() must return the new list
     */
    @Test
    void setAppointmentsList_UpdatesTheCorrectValue() {
        List<Appointment> appointments = new ArrayList<>();
        interpreter.setAppointmentsList(appointments);
        assertSame(appointments, interpreter.getAppointmentsList());
    }

    // setProfessionalSkillsList //

    /**
     * Tests that setProfessionalSkillsList() correctly updates the professional skills list.
     * Given : a new list of professional skills
     * When  : setProfessionalSkillsList() is called with this list
     * Then  : getProfessionalSkillsList() must return the new list
     */
    @Test
    void setProfessionalSkillsList_UpdatesTheCorrectValue() {
        List<ProfessionalSkill> skills = new ArrayList<>();
        interpreter.setProfessionalSkillsList(skills);
        assertSame(skills, interpreter.getProfessionalSkillsList());
    }

    // setAcademicSkillsList //

    /**
     * Tests that setAcademicSkillsList() correctly updates the academic skills list.
     * Given : a new list of academic skills
     * When  : setAcademicSkillsList() is called with this list
     * Then  : getAcademicSkillsList() must return the new list
     */
    @Test
    void setAcademicSkillsList_UpdatesTheCorrectValue() {
        List<AcademicSkill> skills = new ArrayList<>();
        interpreter.setAcademicSkillsList(skills);
        assertSame(skills, interpreter.getAcademicSkillsList());
    }

    // setBeneficiariesList //

    /**
     * Tests that setBeneficiariesList() correctly updates the beneficiaries list.
     * Given : a new list of beneficiaries
     * When  : setBeneficiariesList() is called with this list
     * Then  : getBeneficiariesList() must return the new list
     */
    @Test
    void setBeneficiariesList_UpdatesTheCorrectValue() {
        List<Beneficiary> beneficiaries = new ArrayList<>();
        interpreter.setBeneficiariesList(beneficiaries);
        assertSame(beneficiaries, interpreter.getBeneficiariesList());
    }

    // toString //

    /**
     * Tests that toString() contains the label "Interpreter".
     * Given : a valid Interpreter
     * When  : toString() is called
     * Then  : the result must contain "Interpreter"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(interpreter.toString().contains("Interpreter"));
    }

    /**
     * Tests that toString() contains the last name and first name.
     * Given : an Interpreter initialized with lastName="Nicolas" and firstName="JF"
     * When  : toString() is called
     * Then  : the result must contain "Nicolas" and "JF"
     */
    @Test
    void toString_ContainsLastNameAndFirstName() {
        Interpreter i = new Interpreter("e0002", "secret", "Nicolas", "JF", "JF@mail.be", "0477000000", 38, address);
        assertTrue(i.toString().contains("Nicolas"));
        assertTrue(i.toString().contains("JF"));
    }
}