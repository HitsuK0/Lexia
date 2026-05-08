package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Beneficiary}.
 * Verifies the correct behavior of constructors, getters, setters and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
public class BeneficiaryTest {

    private Interpreter interpreter;
    private Address address;
    private List<String> languages;

    // Set Up //

    /**
     * Initializes a valid {@link Address}, a default {@link Interpreter} and a language list before each test.
     */
    @BeforeEach
    void setUp() {
        interpreter = new Interpreter();
        address = new Address();
        languages = new ArrayList<>(Arrays.asList("Français", "Anglais"));
    }

    // Default Constructor //

    /**
     * Tests that the default constructor initializes all string fields to empty strings.
     * Given : a Beneficiary constructed with no arguments
     * When  : string getters are called
     * Then  : login, password, firstName, lastName, phoneNumber and emailAddress must all equal ""
     */
    @Test
    void defaultConstructor_AllStringFieldsAreEmpty() {
        Beneficiary b = new Beneficiary();
        assertEquals("", b.getLogin());
        assertEquals("", b.getPassword());
        assertEquals("", b.getFirstName());
        assertEquals("", b.getLastName());
        assertEquals("", b.getPhoneNumber());
        assertEquals("", b.getEmailAddress());
    }

    /**
     * Tests that the default constructor initializes hourQuota and educationLevel to 0.
     * Given : a Beneficiary constructed with no arguments
     * When  : getHourQuota() and getEducationLevel() are called
     * Then  : both must equal 0
     */
    @Test
    void defaultConstructor_NumericFieldsAreZero() {
        Beneficiary b = new Beneficiary();
        assertEquals(0, b.getHourQuota());
        assertEquals(0, b.getEducationLevel());
    }

    /**
     * Tests that the default constructor initializes communicationLanguage and appointmentList as empty (not null).
     * Given : a Beneficiary constructed with no arguments
     * When  : getCommunicationLanguage() and getAppointmentList() are called
     * Then  : both must be non-null and empty
     */
    @Test
    void defaultConstructor_ListsAreEmpty() {
        Beneficiary b = new Beneficiary();
        assertNotNull(b.getCommunicationLanguage());
        assertNotNull(b.getAppointmentList());
        assertTrue(b.getCommunicationLanguage().isEmpty());
        assertTrue(b.getAppointmentList().isEmpty());
    }

    /**
     * Tests that the default constructor initializes address and interpreter to null.
     * Given : a Beneficiary constructed with no arguments
     * When  : getAddress() and getInterpreter() are called
     * Then  : both must be null
     */
    @Test
    void defaultConstructor_AddressAndInterpreterAreNull() {
        Beneficiary b = new Beneficiary();
        assertNotNull(b.getAddress());
        assertNull(b.getInterpreter());
    }

    // Constructor (login, password, lastName, firstName, emailAddress) //

    /**
     * Tests that the short constructor correctly sets login, lastName, firstName and emailAddress.
     * Given : login="jdoe", password="secret", lastName="Doe", firstName="Jean", emailAddress="j@j.be"
     * When  : the corresponding getters are called
     * Then  : each field must match the provided argument
     */
    @Test
    void constructor_Short_SetsAllFields() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "Doe", "Jean", "j@j.be");
        assertEquals("jdoe", b.getLogin());
        assertEquals("Doe", b.getLastName());
        assertEquals("Jean", b.getFirstName());
        assertEquals("j@j.be", b.getEmailAddress());
    }

    /**
     * Tests that the short constructor stores the password as provided.
     * Given : a Beneficiary constructed with password="secret"
     * When  : getPassword() is called
     * Then  : the result must equal "secret"
     */
    @Test
    void constructor_Short_StoresPassword() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "Doe", "Jean", "j@j.be");
        assertEquals("secret", b.getPassword());
    }

    /**
     * Tests that the short constructor throws an {@link IllegalArgumentException} when password is null.
     * Given : password=null
     * When  : a Beneficiary is created with this password
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_Short_WithNullPassword_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", null, "Doe", "Jean", "j@j.be"));
    }

    /**
     * Tests that the short constructor throws an {@link IllegalArgumentException} when password is empty.
     * Given : password=""
     * When  : a Beneficiary is created with this password
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_Short_WithEmptyPassword_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "", "Doe", "Jean", "j@j.be"));
    }

    // Constructor (numBeneficiary, login, password, lastName, firstName, emailAddress) //

    /**
     * Tests that the constructor with ID correctly sets numBeneficiary.
     * Given : numBeneficiary=42 and valid arguments
     * When  : getNumBeneficiary() is called
     * Then  : the result must equal 42
     */
    @Test
    void constructor_WithId_SetsNumBeneficiary() {
        Beneficiary b = new Beneficiary(42, "jdoe", "secret", "Doe", "Jean", "j@j.be");
        assertEquals(42, b.getNumBeneficiary());
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when password is null.
     * Given : numBeneficiary=1 and password=null
     * When  : a Beneficiary is created with these arguments
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithNullPassword_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary(1, "jdoe", null, "Doe", "Jean", "j@j.be"));
    }

    /**
     * Tests that the constructor with ID throws an {@link IllegalArgumentException} when password is empty.
     * Given : numBeneficiary=1 and password=""
     * When  : a Beneficiary is created with these arguments
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithId_WithEmptyPassword_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary(1, "jdoe", "", "Doe", "Jean", "j@j.be"));
    }

    // Constructor full without numBeneficiary //

    /**
     * Tests that the full constructor without ID correctly sets all fields.
     * Given : all valid arguments and no numBeneficiary
     * When  : all getters are called
     * Then  : each field must match the provided argument
     */
    @Test
    void constructor_FullWithoutId_SetsAllFields() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "Doe", "Jean", "0477000000",
                "j@j.be", address, 20, 2, interpreter, languages, null);

        assertEquals("jdoe", b.getLogin());
        assertEquals("Doe", b.getLastName());
        assertEquals("Jean", b.getFirstName());
        assertEquals("0477000000", b.getPhoneNumber());
        assertEquals("j@j.be", b.getEmailAddress());
        assertEquals(20, b.getHourQuota());
        assertEquals(2, b.getEducationLevel());
        assertSame(address, b.getAddress());
        assertSame(interpreter, b.getInterpreter());
        assertSame(languages, b.getCommunicationLanguage());
    }

    /**
     * Tests that the full constructor without ID initializes an empty appointment list when null is passed.
     * Given : appointmentList=null
     * When  : getAppointmentList() is called
     * Then  : the result must be an empty list (not null)
     */
    @Test
    void constructor_FullWithoutId_WithNullAppointmentList_InitializesEmptyList() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "Doe", "Jean", "0477000000",
                "j@j.be", address, 20, 2, interpreter, languages, null);
        assertNotNull(b.getAppointmentList());
        assertTrue(b.getAppointmentList().isEmpty());
    }

    /**
     * Tests that the full constructor without ID throws an {@link IllegalArgumentException} when interpreter is null.
     * Given : interpreter=null
     * When  : a Beneficiary is created with this argument
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_FullWithoutId_WithNullInterpreter_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Doe", "Jean", "000",
                        "j@j.be", address, 0, 0, null, languages, null));
    }

    /**
     * Tests that the full constructor without ID throws an {@link IllegalArgumentException} when communicationLanguage is null.
     * Given : communicationLanguage=null
     * When  : a Beneficiary is created with this argument
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_FullWithoutId_WithNullCommunicationLanguage_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Doe", "Jean", "000",
                        "j@j.be", address, 0, 0, interpreter, null, null));
    }

    /**
     * Tests that the full constructor without ID throws an {@link IllegalArgumentException} when communicationLanguage is empty.
     * Given : an empty communicationLanguage list
     * When  : a Beneficiary is created with this argument
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_FullWithoutId_WithEmptyCommunicationLanguage_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Doe", "Jean", "000",
                        "j@j.be", address, 0, 0, interpreter, new ArrayList<>(), null));
    }

    /**
     * Tests that the full constructor without ID throws an {@link IllegalArgumentException} when hourQuota is negative.
     * Given : hourQuota=-1
     * When  : a Beneficiary is created with this argument
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_FullWithoutId_WithNegativeHourQuota_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Doe", "Jean", "000",
                        "j@j.be", address, -1, 0, interpreter, languages, null));
    }

    /**
     * Tests that the full constructor without ID throws an {@link IllegalArgumentException} when educationLevel is greater than 4.
     * Given : educationLevel=5
     * When  : a Beneficiary is created with this argument
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_FullWithoutId_WithEducationLevelTooHigh_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Doe", "Jean", "000",
                        "j@j.be", address, 0, 5, interpreter, languages, null));
    }

    /**
     * Tests that the full constructor without ID throws an {@link IllegalArgumentException} when educationLevel is less than 0.
     * Given : educationLevel=-1
     * When  : a Beneficiary is created with this argument
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_FullWithoutId_WithEducationLevelTooLow_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Doe", "Jean", "000",
                        "j@j.be", address, 0, -1, interpreter, languages, null));
    }

    // Constructor full with numBeneficiary //

    /**
     * Tests that the full constructor with ID correctly sets numBeneficiary.
     * Given : numBeneficiary=7 and all valid arguments
     * When  : getNumBeneficiary() is called
     * Then  : the result must equal 7
     */
    @Test
    void constructor_FullWithId_SetsNumBeneficiary() {
        Beneficiary b = new Beneficiary(7, "jdoe", "secret", "Doe", "Jean", "0477000000",
                "j@j.be", address, 20, 2, interpreter, languages, null);
        assertEquals(7, b.getNumBeneficiary());
    }

    /**
     * Tests that the full constructor with ID throws an {@link IllegalArgumentException} when hourQuota is negative.
     * Given : numBeneficiary=1 and hourQuota=-1
     * When  : a Beneficiary is created with these arguments
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_FullWithId_WithNegativeHourQuota_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary(1, "jdoe", "secret", "Doe", "Jean", "000",
                        "j@j.be", address, -1, 0, interpreter, languages, null));
    }

    /**
     * Tests that the full constructor with ID throws an {@link IllegalArgumentException} when interpreter is null.
     * Given : numBeneficiary=1 and interpreter=null
     * When  : a Beneficiary is created with these arguments
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_FullWithId_WithNullInterpreter_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary(1, "jdoe", "secret", "Doe", "Jean", "000",
                        "j@j.be", address, 0, 0, null, languages, null));
    }

    // setNumBeneficiary //

    /**
     * Tests that setNumBeneficiary() correctly updates the ID.
     * Given : a default Beneficiary
     * When  : setNumBeneficiary(99) is called
     * Then  : getNumBeneficiary() must return 99
     */
    @Test
    void setNumBeneficiary_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setNumBeneficiary(99);
        assertEquals(99, b.getNumBeneficiary());
    }

    // setLogin (inherited from User) //

    /**
     * Tests that setLogin() correctly updates the login.
     * Given : a default Beneficiary
     * When  : setLogin("newlogin") is called
     * Then  : getLogin() must return "newlogin"
     */
    @Test
    void setLogin_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setLogin("newlogin");
        assertEquals("newlogin", b.getLogin());
    }

    // setPassword (inherited from User) //

    /**
     * Tests that setPassword() correctly updates the password.
     * Given : a default Beneficiary
     * When  : setPassword("newpass") is called
     * Then  : getPassword() must return "newpass"
     */
    @Test
    void setPassword_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setPassword("newpass");
        assertEquals("newpass", b.getPassword());
    }

    // setFirstName (inherited from User) //

    /**
     * Tests that setFirstName() correctly updates the first name.
     * Given : a default Beneficiary
     * When  : setFirstName("Sara") is called
     * Then  : getFirstName() must return "Sara"
     */
    @Test
    void setFirstName_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setFirstName("Sara");
        assertEquals("Sara", b.getFirstName());
    }

    // setLastName (inherited from User) //

    /**
     * Tests that setLastName() correctly updates the last name.
     * Given : a default Beneficiary
     * When  : setLastName("Kowalski") is called
     * Then  : getLastName() must return "Kowalski"
     */
    @Test
    void setLastName_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setLastName("Kowalski");
        assertEquals("Kowalski", b.getLastName());
    }

    // setPhoneNumber (inherited from User) //

    /**
     * Tests that setPhoneNumber() correctly updates the phone number.
     * Given : a default Beneficiary
     * When  : setPhoneNumber("0477123456") is called
     * Then  : getPhoneNumber() must return "0477123456"
     */
    @Test
    void setPhoneNumber_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setPhoneNumber("0477123456");
        assertEquals("0477123456", b.getPhoneNumber());
    }

    // setEmailAddress (inherited from User) //

    /**
     * Tests that setEmailAddress() correctly updates the email address.
     * Given : a default Beneficiary
     * When  : setEmailAddress("new@mail.be") is called
     * Then  : getEmailAddress() must return "new@mail.be"
     */
    @Test
    void setEmailAddress_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setEmailAddress("new@mail.be");
        assertEquals("new@mail.be", b.getEmailAddress());
    }

    // setAddress (inherited from User) //

    /**
     * Tests that setAddress() correctly updates the address.
     * Given : a default Beneficiary and a non-null Address
     * When  : setAddress(address) is called
     * Then  : getAddress() must return the same address instance
     */
    @Test
    void setAddress_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setAddress(address);
        assertSame(address, b.getAddress());
    }

    /**
     * Tests that setAddress() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setAddress(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAddress_WithNull_RaisesAnException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setAddress(null));
    }

    // setHourQuota //

    /**
     * Tests that setHourQuota() correctly updates the hour quota.
     * Given : a default Beneficiary
     * When  : setHourQuota(10) is called
     * Then  : getHourQuota() must return 10
     */
    @Test
    void setHourQuota_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setHourQuota(10);
        assertEquals(10, b.getHourQuota());
    }

    /**
     * Tests that setHourQuota() throws an {@link IllegalArgumentException} when a negative value is passed.
     * Given : a negative value -5
     * When  : setHourQuota(-5) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setHourQuota_WithNegativeValue_RaisesAnException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setHourQuota(-5));
    }

    // setEducationLevel //

    /**
     * Tests that setEducationLevel() correctly updates the education level.
     * Given : a default Beneficiary
     * When  : setEducationLevel(3) is called
     * Then  : getEducationLevel() must return 3
     */
    @Test
    void setEducationLevel_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setEducationLevel(3);
        assertEquals(3, b.getEducationLevel());
    }

    /**
     * Tests that setEducationLevel() throws an {@link IllegalArgumentException} when value is greater than 4.
     * Given : a value 5
     * When  : setEducationLevel(5) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setEducationLevel_WithValueTooHigh_RaisesAnException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setEducationLevel(5));
    }

    /**
     * Tests that setEducationLevel() throws an {@link IllegalArgumentException} when value is less than 0.
     * Given : a value -1
     * When  : setEducationLevel(-1) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setEducationLevel_WithValueTooLow_RaisesAnException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setEducationLevel(-1));
    }

    // setInterpreter //

    /**
     * Tests that setInterpreter() correctly updates the interpreter.
     * Given : a default Beneficiary and a non-null Interpreter
     * When  : setInterpreter(interpreter) is called
     * Then  : getInterpreter() must return the same interpreter instance
     */
    @Test
    void setInterpreter_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setInterpreter(interpreter);
        assertSame(interpreter, b.getInterpreter());
    }

    /**
     * Tests that setInterpreter() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setInterpreter(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setInterpreter_WithNull_RaisesAnException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setInterpreter(null));
    }

    // setCommunicationLanguage //

    /**
     * Tests that setCommunicationLanguage() correctly updates the language list.
     * Given : a default Beneficiary and a non-empty language list
     * When  : setCommunicationLanguage(list) is called
     * Then  : getCommunicationLanguage() must return the same list instance
     */
    @Test
    void setCommunicationLanguage_UpdatesTheCorrectValue() {
        Beneficiary b = new Beneficiary();
        b.setCommunicationLanguage(languages);
        assertSame(languages, b.getCommunicationLanguage());
    }

    /**
     * Tests that setCommunicationLanguage() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null list
     * When  : setCommunicationLanguage(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setCommunicationLanguage_WithNull_RaisesAnException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setCommunicationLanguage(null));
    }

    /**
     * Tests that setCommunicationLanguage() throws an {@link IllegalArgumentException} when an empty list is passed.
     * Given : an empty list
     * When  : setCommunicationLanguage(empty list) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setCommunicationLanguage_WithEmptyList_RaisesAnException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class,
                () -> b.setCommunicationLanguage(new ArrayList<>()));
    }

    // setAppointmentList //

    /**
     * Tests that setAppointmentList() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null list
     * When  : setAppointmentList(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAppointmentList_WithNull_RaisesAnException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setAppointmentList(null));
    }

    /**
     * Tests that setAppointmentList() throws an {@link IllegalArgumentException} when an empty list is passed.
     * Given : an empty list
     * When  : setAppointmentList(empty list) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAppointmentList_WithEmptyList_RaisesAnException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class,
                () -> b.setAppointmentList(new ArrayList<>()));
    }

    // getAppointment //

    /**
     * Tests that getAppointment() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a default Beneficiary
     * When  : getAppointment(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void getAppointment_WithNull_RaisesAnException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.getAppointment(null));
    }

    // toString //

    /**
     * Tests that toString() contains the label "Bénéficiaire".
     * Given : a valid Beneficiary
     * When  : toString() is called
     * Then  : the result must contain "Bénéficiaire"
     */
    @Test
    void toString_ContainsLabel() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "Doe", "Jean", "j@j.be");
        assertTrue(b.toString().contains("Bénéficiaire"));
    }

    /**
     * Tests that toString() contains the first name and last name.
     * Given : a Beneficiary with firstName="Jean" and lastName="Doe"
     * When  : toString() is called
     * Then  : the result must contain "Jean" and "Doe"
     */
    @Test
    void toString_ContainsFirstNameAndLastName() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "Doe", "Jean", "j@j.be");
        assertTrue(b.toString().contains("Jean"));
        assertTrue(b.toString().contains("Doe"));
    }

    /**
     * Tests that toString() contains "Aucun rendez-vous" when the appointment list is empty.
     * Given : a Beneficiary with an empty appointment list
     * When  : toString() is called
     * Then  : the result must contain "Aucun rendez-vous"
     */
    @Test
    void toString_WhenNoAppointments_ContainsAucunRendezVous() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "Doe", "Jean", "j@j.be");
        assertTrue(b.toString().contains("Aucun rendez-vous"));
    }
}