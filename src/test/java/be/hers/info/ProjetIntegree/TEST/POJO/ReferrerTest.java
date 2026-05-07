package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.POJO.Referrer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Referrer}.
 * Verifies the correct behaviour of constructors, getters, setters and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
public class ReferrerTest {

    private Referrer referrer;
    private Establishment establishment;

    // Set Up //

    /**
     * Initializes a valid {@link Establishment} and a {@link Referrer} with valid arguments before each test.
     */
    @BeforeEach
    void setUp() {
        establishment = new Establishment();
        referrer = new Referrer(establishment, "JF@mail.be", "0477000000", "Nicolas", "JF");
    }

    // Default Constructor //

    /**
     * Tests that the default constructor initializes all String fields to empty strings.
     * Given : no argument
     * When  : a Referrer is created with the default constructor
     * Then  : getName(), getSurname(), getPhoneNumber() and getAddressMail() must all return ""
     */
    @Test
    void defaultConstructor_AllStringFieldsAreEmpty() {
        Referrer r = new Referrer();
        assertEquals("", r.getName());
        assertEquals("", r.getSurname());
        assertEquals("", r.getPhoneNumber());
        assertEquals("", r.getAddressMail());
    }

    /**
     * Tests that the default constructor initializes refEstablishment to a non-null Establishment.
     * Given : no argument
     * When  : a Referrer is created with the default constructor
     * Then  : getRefEstablishment() must not be null
     */
    @Test
    void defaultConstructor_RefEstablishmentIsNotNull() {
        Referrer r = new Referrer();
        assertNotNull(r.getRefEstablishment());
    }

    // Constructor (refEstablishment, addressMail, phoneNumber, surname, name) //

    /**
     * Tests that the full constructor correctly sets all fields when valid arguments are provided.
     * Given : valid refEstablishment, addressMail="JF@mail.be", phoneNumber="0477000000", surname="Nicolas", name="JF"
     * When  : a Referrer is created with these arguments
     * Then  : all getters must return the expected values
     */
    @Test
    void constructor_WithValidArguments_SetsAllFields() {
        assertEquals("JF", referrer.getName());
        assertEquals("Nicolas", referrer.getSurname());
        assertEquals("0477000000", referrer.getPhoneNumber());
        assertEquals("JF@mail.be", referrer.getAddressMail());
        assertSame(establishment, referrer.getRefEstablishment());
    }

    /**
     * Tests that the full constructor throws a {@link IllegalArgumentException} when refEstablishment is null.
     * Given : a null refEstablishment
     * When  : a Referrer is created with null refEstablishment
     * Then  : a IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullRefEstablishment_RaisesAIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Referrer(null, "JF@mail.be", "0477000000", "Nicolas", "JF"));
    }

    /**
     * Tests that the full constructor throws a {@link IllegalArgumentException} when addressMail is null.
     * Given : a null addressMail
     * When  : a Referrer is created with null addressMail
     * Then  : a IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullAddressMail_RaisesAIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Referrer(establishment, null, "0477000000", "Nicolas", "JF"));
    }

    /**
     * Tests that the full constructor throws a {@link IllegalArgumentException} when phoneNumber is null.
     * Given : a null phoneNumber
     * When  : a Referrer is created with null phoneNumber
     * Then  : a IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullPhoneNumber_RaisesAIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Referrer(establishment, "JF@mail.be", null, "Nicolas", "JF"));
    }

    /**
     * Tests that the full constructor throws a {@link IllegalArgumentException} when surname is null.
     * Given : a null surname
     * When  : a Referrer is created with null surname
     * Then  : a IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullSurname_RaisesAIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Referrer(establishment, "JF@mail.be", "0477000000", null, "JF"));
    }

    /**
     * Tests that the full constructor throws a {@link IllegalArgumentException} when name is null.
     * Given : a null name
     * When  : a Referrer is created with null name
     * Then  : a IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullName_RaisesAIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Referrer(establishment, "JF@mail.be", "0477000000", "Nicolas", null));
    }

    // setName //

    /**
     * Tests that setName() correctly updates the name.
     * Given : a Referrer initialized with name="JF"
     * When  : setName("Sarah") is called
     * Then  : getName() must return "Sarah"
     */
    @Test
    void setName_UpdatesTheCorrectValue() {
        referrer.setName("Sarah");
        assertEquals("Sarah", referrer.getName());
    }

    /**
     * Tests that setName() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setName(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setName_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> referrer.setName(null));
    }

    // setSurname //

    /**
     * Tests that setSurname() correctly updates the surname.
     * Given : a Referrer initialized with surname="Nicolas"
     * When  : setSurname("Martin") is called
     * Then  : getSurname() must return "Martin"
     */
    @Test
    void setSurname_UpdatesTheCorrectValue() {
        referrer.setSurname("Martin");
        assertEquals("Martin", referrer.getSurname());
    }

    /**
     * Tests that setSurname() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setSurname(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setSurname_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> referrer.setSurname(null));
    }

    // setPhoneNumber //

    /**
     * Tests that setPhoneNumber() correctly updates the phone number.
     * Given : a Referrer initialized with phoneNumber="0477000000"
     * When  : setPhoneNumber("0477123456") is called
     * Then  : getPhoneNumber() must return "0800123456"
     */
    @Test
    void setPhoneNumber_UpdatesTheCorrectValue() {
        referrer.setPhoneNumber("0477123456");
        assertEquals("0477123456", referrer.getPhoneNumber());
    }

    /**
     * Tests that setPhoneNumber() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setPhoneNumber(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setPhoneNumber_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> referrer.setPhoneNumber(null));
    }

    // setAddressMail //

    /**
     * Tests that setAddressMail() correctly updates the mail address.
     * Given : a Referrer initialized with addressMail="JF@mail.be"
     * When  : setAddressMail("Sarah@mail.be") is called
     * Then  : getAddressMail() must return "Sarah@mail.be"
     */
    @Test
    void setAddressMail_UpdatesTheCorrectValue() {
        referrer.setAddressMail("Sarah@mail.be");
        assertEquals("Sarah@mail.be", referrer.getAddressMail());
    }

    /**
     * Tests that setAddressMail() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setAddressMail(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAddressMail_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> referrer.setAddressMail(null));
    }

    // setRefEstablishment //

    /**
     * Tests that setRefEstablishment() correctly updates the establishment.
     * Given : a new valid Establishment
     * When  : setRefEstablishment() is called with this establishment
     * Then  : getRefEstablishment() must return the new establishment
     */
    @Test
    void setRefEstablishment_UpdatesTheCorrectValue() {
        Establishment newEstablishment = new Establishment();
        referrer.setRefEstablishment(newEstablishment);
        assertSame(newEstablishment, referrer.getRefEstablishment());
    }

    /**
     * Tests that setRefEstablishment() throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setRefEstablishment(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setRefEstablishment_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> referrer.setRefEstablishment(null));
    }

    // toString //

    /**
     * Tests that toString() contains the label "Rérérant".
     * Given : a valid Referrer
     * When  : toString() is called
     * Then  : the result must contain "Rérérant"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(referrer.toString().contains("Rérérant"));
    }

    /**
     * Tests that toString() contains the name and surname of the referrer.
     * Given : a Referrer initialized with name="JF" and surname="Nicolas"
     * When  : toString() is called
     * Then  : the result must contain "JF" and "Nicolas"
     */
    @Test
    void toString_ContainsNameAndSurname() {
        assertTrue(referrer.toString().contains("JF"));
        assertTrue(referrer.toString().contains("Nicolas"));
    }
}