package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

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
class BeneficiaryTest {

    private Interpreter interpreter;
    private Address address;
    private List<String> languages;

    @BeforeEach
    void setUp() {
        interpreter = new Interpreter();
        address = new Address();
        languages = new ArrayList<>(Arrays.asList("Français", "Anglais"));
    }

    //  default constructor

    /**
     * Tests that the default constructor initializes all string fields to empty strings.
     * Given : a Beneficiary constructed with no arguments
     * When  : string getters are called
     * Then  : login, password, name, surname, phoneNumber and emailAddress must all equal ""
     */
    @Test
    void defaultConstructor_ShouldInitializeStringFieldsToEmpty() {
        Beneficiary b = new Beneficiary();
        assertEquals("", b.getLogin());
        assertEquals("", b.getPassword());
        assertEquals("", b.getName());
        assertEquals("", b.getSurname());
        assertEquals("", b.getPhoneNumber());
        assertEquals("", b.getEmailAddress());
    }

    /**
     * Tests that the default constructor initializes numeric fields to 0.
     * Given : a Beneficiary constructed with no arguments
     * When  : getHourQuota() and getEducationLevel() are called
     * Then  : both must equal 0
     */
    @Test
    void defaultConstructor_ShouldInitializeNumericFieldsToZero() {
        Beneficiary b = new Beneficiary();
        assertEquals(0, b.getHourQuota());
        assertEquals(0, b.getEducationLevel());
    }

    /**
     * Tests that the default constructor initializes lists as empty (not null).
     * Given : a Beneficiary constructed with no arguments
     * When  : getCommunicationLanguage() and getAppointmentList() are called
     * Then  : both must be non-null and empty
     */
    @Test
    void defaultConstructor_ShouldInitializeListsAsEmpty() {
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
    void defaultConstructor_ShouldInitializeAddressAndInterpreterToNull() {
        Beneficiary b = new Beneficiary();
        assertNull(b.getAddress());
        assertNull(b.getInterpreter());
    }

    //  Constructor (login, password, emailAddress, name, surname)

    /**
     * Tests that the short constructor sets login, name, surname and emailAddress correctly.
     * Given : a Beneficiary constructed with login="jdoe", password="secret", emailAddress="j@j.be", name="Jean", surname="Doe"
     * When  : the corresponding getters are called
     * Then  : each field must match the provided argument
     */
    @Test
    void shortConstructor_ShouldSetLoginNameSurnameEmail() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "j@j.be", "Jean", "Doe");
        assertEquals("jdoe", b.getLogin());
        assertEquals("Jean", b.getName());
        assertEquals("Doe", b.getSurname());
        assertEquals("j@j.be", b.getEmailAddress());
    }

    /**
     * Tests that the short constructor hashes the password with BCrypt.
     * Given : a Beneficiary constructed with password="secret"
     * When  : BCrypt.checkpw("secret", getPassword()) is called
     * Then  : the result must be true
     */
    @Test
    void shortConstructor_ShouldHashPassword() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "j@j.be", "Jean", "Doe");
        assertTrue(BCrypt.checkpw("secret", b.getPassword()));
    }

    /**
     * Tests that the short constructor throws an exception when password is null.
     * Given : a Beneficiary constructed with password=null
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void shortConstructor_WhenPasswordIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", null, "j@j.be", "Jean", "Doe"));
    }

    /**
     * Tests that the short constructor throws an exception when password is empty.
     * Given : a Beneficiary constructed with password=""
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void shortConstructor_WhenPasswordIsEmpty_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "", "j@j.be", "Jean", "Doe"));
    }

    //  Constructor (numBeneficiary, login, password, emailAddress, name, surname)

    /**
     * Tests that the constructor with ID sets numBeneficiary correctly.
     * Given : a Beneficiary constructed with numBeneficiary=42
     * When  : getNumBeneficiary() is called
     * Then  : the result must equal 42
     */
    @Test
    void constructorWithId_ShouldSetNumBeneficiary() {
        Beneficiary b = new Beneficiary(42, "jdoe", "secret", "j@j.be", "Jean", "Doe");
        assertEquals(42, b.getNumBeneficiary());
    }

    /**
     * Tests that the constructor with ID hashes the password with BCrypt.
     * Given : a Beneficiary constructed with numBeneficiary=42 and password="secret"
     * When  : BCrypt.checkpw("secret", getPassword()) is called
     * Then  : the result must be true
     */
    @Test
    void constructorWithId_ShouldHashPassword() {
        Beneficiary b = new Beneficiary(42, "jdoe", "secret", "j@j.be", "Jean", "Doe");
        assertTrue(BCrypt.checkpw("secret", b.getPassword()));
    }

    /**
     * Tests that the constructor with ID throws an exception when password is null.
     * Given : a Beneficiary constructed with numBeneficiary=1 and password=null
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructorWithId_WhenPasswordIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary(1, "jdoe", null, "j@j.be", "Jean", "Doe"));
    }

    /**
     * Tests that the constructor with ID throws an exception when password is empty.
     * Given : a Beneficiary constructed with numBeneficiary=1 and password=""
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructorWithId_WhenPasswordIsEmpty_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary(1, "jdoe", "", "j@j.be", "Jean", "Doe"));
    }

    //  Complete constructor without numBeneficiary

    /**
     * Tests that the full constructor without ID sets all fields correctly.
     * Given : a Beneficiary constructed with all valid arguments and no numBeneficiary
     * When  : all getters are called
     * Then  : each field must match the provided argument
     */
    @Test
    void fullConstructorWithoutId_ShouldSetAllFields() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "Jean", "Doe", "0477000000",
                20, "j@j.be", address, 2, interpreter, languages, null);

        assertEquals("jdoe", b.getLogin());
        assertEquals("Jean", b.getName());
        assertEquals("Doe", b.getSurname());
        assertEquals("0477000000", b.getPhoneNumber());
        assertEquals(20, b.getHourQuota());
        assertEquals("j@j.be", b.getEmailAddress());
        assertEquals(2, b.getEducationLevel());
        assertSame(address, b.getAddress());
        assertSame(interpreter, b.getInterpreter());
        assertSame(languages, b.getCommunicationLanguage());
    }

    /**
     * Tests that the full constructor without ID hashes the password correctly.
     * Given : a Beneficiary constructed with password="monMotDePasse"
     * When  : BCrypt.checkpw("monMotDePasse", getPassword()) is called
     * Then  : the result must be true
     */
    @Test
    void fullConstructorWithoutId_ShouldHashPassword() {
        Beneficiary b = new Beneficiary("jdoe", "monMotDePasse", "Jean", "Doe", "0477000000",
                20, "j@j.be", address, 2, interpreter, languages, null);
        assertTrue(BCrypt.checkpw("monMotDePasse", b.getPassword()));
    }

    /**
     * Tests that the full constructor without ID initializes an empty appointment list when null is passed.
     * Given : a Beneficiary constructed with appointmentList=null
     * When  : getAppointmentList() is called
     * Then  : the result must be an empty list (not null)
     */
    @Test
    void fullConstructorWithoutId_WhenAppointmentListIsNull_ShouldInitializeEmptyList() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "Jean", "Doe", "0477000000",
                20, "j@j.be", address, 2, interpreter, languages, null);
        assertNotNull(b.getAppointmentList());
        assertTrue(b.getAppointmentList().isEmpty());
    }

    /**
     * Tests that the full constructor without ID throws an exception when address is null.
     * Given : a Beneficiary constructed with address=null
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithoutId_WhenAddressIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Jean", "Doe", "000",
                        0, "j@j.be", null, 0, interpreter, languages, null));
    }

    /**
     * Tests that the full constructor without ID throws an exception when interpreter is null.
     * Given : a Beneficiary constructed with interpreter=null
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithoutId_WhenInterpreterIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Jean", "Doe", "000",
                        0, "j@j.be", address, 0, null, languages, null));
    }

    /**
     * Tests that the full constructor without ID throws an exception when communicationLanguage is null.
     * Given : a Beneficiary constructed with communicationLanguage=null
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithoutId_WhenCommunicationLanguageIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Jean", "Doe", "000",
                        0, "j@j.be", address, 0, interpreter, null, null));
    }

    /**
     * Tests that the full constructor without ID throws an exception when communicationLanguage is empty.
     * Given : a Beneficiary constructed with an empty communicationLanguage list
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithoutId_WhenCommunicationLanguageIsEmpty_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Jean", "Doe", "000",
                        0, "j@j.be", address, 0, interpreter, new ArrayList<>(), null));
    }

    /**
     * Tests that the full constructor without ID throws an exception when password is null.
     * Given : a Beneficiary constructed with password=null
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithoutId_WhenPasswordIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", null, "Jean", "Doe", "000",
                        0, "j@j.be", address, 0, interpreter, languages, null));
    }

    /**
     * Tests that the full constructor without ID throws an exception when password is empty.
     * Given : a Beneficiary constructed with password=""
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithoutId_WhenPasswordIsEmpty_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "", "Jean", "Doe", "000",
                        0, "j@j.be", address, 0, interpreter, languages, null));
    }

    /**
     * Tests that the full constructor without ID throws an exception when hourQuota is negative.
     * Given : a Beneficiary constructed with hourQuota=-1
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithoutId_WhenHourQuotaIsNegative_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Jean", "Doe", "000",
                        -1, "j@j.be", address, 0, interpreter, languages, null));
    }

    /**
     * Tests that the full constructor without ID throws an exception when educationLevel is greater than 4.
     * Given : a Beneficiary constructed with educationLevel=5
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithoutId_WhenEducationLevelTooHigh_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Jean", "Doe", "000",
                        0, "j@j.be", address, 5, interpreter, languages, null));
    }

    /**
     * Tests that the full constructor without ID throws an exception when educationLevel is less than 0.
     * Given : a Beneficiary constructed with educationLevel=-1
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithoutId_WhenEducationLevelTooLow_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary("jdoe", "secret", "Jean", "Doe", "000",
                        0, "j@j.be", address, -1, interpreter, languages, null));
    }

    //  Complete constructor with numBeneficiary

    /**
     * Tests that the full constructor with ID sets numBeneficiary correctly.
     * Given : a Beneficiary constructed with numBeneficiary=7 and all valid arguments
     * When  : getNumBeneficiary() is called
     * Then  : the result must equal 7
     */
    @Test
    void fullConstructorWithId_ShouldSetNumBeneficiary() {
        Beneficiary b = new Beneficiary(7, "jdoe", "secret", "Jean", "Doe", "0477000000",
                20, "j@j.be", address, 2, interpreter, languages, null);
        assertEquals(7, b.getNumBeneficiary());
    }

    /**
     * Tests that the full constructor with ID hashes the password correctly.
     * Given : a Beneficiary constructed with numBeneficiary=7 and password="secret"
     * When  : BCrypt.checkpw("secret", getPassword()) is called
     * Then  : the result must be true
     */
    @Test
    void fullConstructorWithId_ShouldHashPassword() {
        Beneficiary b = new Beneficiary(7, "jdoe", "secret", "Jean", "Doe", "0477000000",
                20, "j@j.be", address, 2, interpreter, languages, null);
        assertTrue(BCrypt.checkpw("secret", b.getPassword()));
    }

    /**
     * Tests that the full constructor with ID throws an exception when password is null.
     * Given : a Beneficiary constructed with numBeneficiary=1 and password=null
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithId_WhenPasswordIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary(1, "jdoe", null, "Jean", "Doe", "000",
                        0, "j@j.be", address, 0, interpreter, languages, null));
    }

    /**
     * Tests that the full constructor with ID throws an exception when hourQuota is negative.
     * Given : a Beneficiary constructed with numBeneficiary=1 and hourQuota=-1
     * When  : the constructor is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void fullConstructorWithId_WhenHourQuotaIsNegative_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Beneficiary(1, "jdoe", "secret", "Jean", "Doe", "000",
                        -1, "j@j.be", address, 0, interpreter, languages, null));
    }

    //  Setters

    /**
     * Tests that setLogin correctly updates the login.
     * Given : a default Beneficiary
     * When  : setLogin("newlogin") is called
     * Then  : getLogin() must return "newlogin"
     */
    @Test
    void setLogin_ShouldUpdateLogin() {
        Beneficiary b = new Beneficiary();
        b.setLogin("newlogin");
        assertEquals("newlogin", b.getLogin());
    }

    /**
     * Tests that setPassword hashes and stores the new password correctly.
     * Given : a default Beneficiary
     * When  : setPassword("newpass") is called
     * Then  : BCrypt.checkpw("newpass", getPassword()) must return true
     */
    @Test
    void setPassword_ShouldHashAndStoreNewPassword() {
        Beneficiary b = new Beneficiary();
        b.setPassword("newpass");
        assertTrue(BCrypt.checkpw("newpass", b.getPassword()));
    }

    /**
     * Tests that setName correctly updates the name.
     * Given : a default Beneficiary
     * When  : setName("Sara") is called
     * Then  : getName() must return "Sara"
     */
    @Test
    void setName_ShouldUpdateName() {
        Beneficiary b = new Beneficiary();
        b.setName("Sara");
        assertEquals("Sara", b.getName());
    }

    /**
     * Tests that setName throws an exception when null is passed.
     * Given : a default Beneficiary
     * When  : setName(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setName_WhenNull_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setName(null));
    }

    /**
     * Tests that setSurname correctly updates the surname.
     * Given : a default Beneficiary
     * When  : setSurname("Kowalski") is called
     * Then  : getSurname() must return "Kowalski"
     */
    @Test
    void setSurname_ShouldUpdateSurname() {
        Beneficiary b = new Beneficiary();
        b.setSurname("Kowalski");
        assertEquals("Kowalski", b.getSurname());
    }

    /**
     * Tests that setSurname throws an exception when null is passed.
     * Given : a default Beneficiary
     * When  : setSurname(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setSurname_WhenNull_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setSurname(null));
    }

    /**
     * Tests that setPhoneNumber correctly updates the phone number.
     * Given : a default Beneficiary
     * When  : setPhoneNumber("0477123456") is called
     * Then  : getPhoneNumber() must return "0477123456"
     */
    @Test
    void setPhoneNumber_ShouldUpdatePhoneNumber() {
        Beneficiary b = new Beneficiary();
        b.setPhoneNumber("0477123456");
        assertEquals("0477123456", b.getPhoneNumber());
    }

    /**
     * Tests that setPhoneNumber throws an exception when null is passed.
     * Given : a default Beneficiary
     * When  : setPhoneNumber(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setPhoneNumber_WhenNull_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setPhoneNumber(null));
    }

    /**
     * Tests that setEmailAddress correctly updates the email address.
     * Given : a default Beneficiary
     * When  : setEmailAddress("new@mail.be") is called
     * Then  : getEmailAddress() must return "new@mail.be"
     */
    @Test
    void setEmailAddress_ShouldUpdateEmailAddress() {
        Beneficiary b = new Beneficiary();
        b.setEmailAddress("new@mail.be");
        assertEquals("new@mail.be", b.getEmailAddress());
    }

    /**
     * Tests that setEmailAddress throws an exception when null is passed.
     * Given : a default Beneficiary
     * When  : setEmailAddress(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setEmailAddress_WhenNull_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setEmailAddress(null));
    }

    /**
     * Tests that setAddress correctly updates the address.
     * Given : a default Beneficiary and a non-null Address
     * When  : setAddress(address) is called
     * Then  : getAddress() must return the same address instance
     */
    @Test
    void setAddress_ShouldUpdateAddress() {
        Beneficiary b = new Beneficiary();
        b.setAddress(address);
        assertSame(address, b.getAddress());
    }

    /**
     * Tests that setAddress throws an exception when null is passed.
     * Given : a default Beneficiary
     * When  : setAddress(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAddress_WhenNull_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setAddress(null));
    }

    /**
     * Tests that setHourQuota correctly updates the hour quota.
     * Given : a default Beneficiary
     * When  : setHourQuota(10) is called
     * Then  : getHourQuota() must return 10
     */
    @Test
    void setHourQuota_ShouldUpdateHourQuota() {
        Beneficiary b = new Beneficiary();
        b.setHourQuota(10);
        assertEquals(10, b.getHourQuota());
    }

    /**
     * Tests that setHourQuota throws an exception when a negative value is passed.
     * Given : a default Beneficiary
     * When  : setHourQuota(-5) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setHourQuota_WhenNegative_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setHourQuota(-5));
    }

    /**
     * Tests that setEducationLevel correctly updates the education level.
     * Given : a default Beneficiary
     * When  : setEducationLevel(3) is called
     * Then  : getEducationLevel() must return 3
     */
    @Test
    void setEducationLevel_ShouldUpdateEducationLevel() {
        Beneficiary b = new Beneficiary();
        b.setEducationLevel(3);
        assertEquals(3, b.getEducationLevel());
    }

    /**
     * Tests that setEducationLevel throws an exception when value is greater than 4.
     * Given : a default Beneficiary
     * When  : setEducationLevel(5) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setEducationLevel_WhenTooHigh_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setEducationLevel(5));
    }

    /**
     * Tests that setEducationLevel throws an exception when value is less than 0.
     * Given : a default Beneficiary
     * When  : setEducationLevel(-1) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setEducationLevel_WhenTooLow_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setEducationLevel(-1));
    }

    /**
     * Tests that setInterpreter correctly updates the interpreter.
     * Given : a default Beneficiary and a non-null Interpreter
     * When  : setInterpreter(interpreter) is called
     * Then  : getInterpreter() must return the same interpreter instance
     */
    @Test
    void setInterpreter_ShouldUpdateInterpreter() {
        Beneficiary b = new Beneficiary();
        b.setInterpreter(interpreter);
        assertSame(interpreter, b.getInterpreter());
    }

    /**
     * Tests that setInterpreter throws an exception when null is passed.
     * Given : a default Beneficiary
     * When  : setInterpreter(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setInterpreter_WhenNull_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setInterpreter(null));
    }

    /**
     * Tests that setCommunicationLanguage correctly updates the language list.
     * Given : a default Beneficiary and a non-empty language list
     * When  : setCommunicationLanguage(list) is called
     * Then  : getCommunicationLanguage() must return the same list instance
     */
    @Test
    void setCommunicationLanguage_ShouldUpdateList() {
        Beneficiary b = new Beneficiary();
        b.setCommunicationLanguage(languages);
        assertSame(languages, b.getCommunicationLanguage());
    }

    /**
     * Tests that setCommunicationLanguage throws an exception when null is passed.
     * Given : a default Beneficiary
     * When  : setCommunicationLanguage(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setCommunicationLanguage_WhenNull_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setCommunicationLanguage(null));
    }

    /**
     * Tests that setCommunicationLanguage throws an exception when an empty list is passed.
     * Given : a default Beneficiary
     * When  : setCommunicationLanguage(empty list) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setCommunicationLanguage_WhenEmpty_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class,
                () -> b.setCommunicationLanguage(new ArrayList<>()));
    }

    /**
     * Tests that setAppointmentList throws an exception when null is passed.
     * Given : a default Beneficiary
     * When  : setAppointmentList(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAppointmentList_WhenNull_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.setAppointmentList(null));
    }

    /**
     * Tests that setAppointmentList throws an exception when an empty list is passed.
     * Given : a default Beneficiary
     * When  : setAppointmentList(empty list) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAppointmentList_WhenEmpty_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class,
                () -> b.setAppointmentList(new ArrayList<>()));
    }

    /**
     * Tests that setNumBeneficiary correctly updates the ID.
     * Given : a default Beneficiary
     * When  : setNumBeneficiary(99) is called
     * Then  : getNumBeneficiary() must return 99
     */
    @Test
    void setNumBeneficiary_ShouldUpdateId() {
        Beneficiary b = new Beneficiary();
        b.setNumBeneficiary(99);
        assertEquals(99, b.getNumBeneficiary());
    }

    //  getAppointment

    /**
     * Tests that getAppointment throws an exception when null is passed.
     * Given : a default Beneficiary
     * When  : getAppointment(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void getAppointment_WhenNull_ShouldThrowIllegalArgumentException() {
        Beneficiary b = new Beneficiary();
        assertThrows(IllegalArgumentException.class, () -> b.getAppointment(null));
    }

    //  toString

    /**
     * Tests that {@code toString()} contains the login, name and surname.
     * Given : a Beneficiary initialized with login="jdoe", name="Jean", surname="Doe"
     * When  : toString() is called
     * Then  : the result must contain "jdoe", "Jean" and "Doe"
     */
    @Test
    void toString_ShouldContainLoginNameAndSurname() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "j@j.be", "Jean", "Doe");
        String result = b.toString();
        assertTrue(result.contains("jdoe"));
        assertTrue(result.contains("Jean"));
        assertTrue(result.contains("Doe"));
    }

    /**
     * Tests that {@code toString()} displays "Non renseigné" for phone number when it is empty.
     * Given : a Beneficiary constructed via the short constructor (phoneNumber defaults to "")
     * When  : toString() is called
     * Then  : the result must contain "Non renseigné"
     */
    @Test
    void toString_WhenPhoneNumberEmpty_ShouldDisplayNonRenseigne() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "j@j.be", "Jean", "Doe");
        assertTrue(b.toString().contains("Non renseigné"));
    }

    /**
     * Tests that {@code toString()} displays "Aucun rendez-vous" when the appointment list is empty.
     * Given : a Beneficiary constructed via the short constructor (appointmentList is empty by default)
     * When  : toString() is called
     * Then  : the result must contain "Aucun rendez-vous"
     */
    @Test
    void toString_WhenNoAppointments_ShouldDisplayAucunRendezVous() {
        Beneficiary b = new Beneficiary("jdoe", "secret", "j@j.be", "Jean", "Doe");
        assertTrue(b.toString().contains("Aucun rendez-vous"));
    }
}