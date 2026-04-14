package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Test class for {@link Address}
 * Verifies the correct behavior of constructor, getters, setters and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
public class AddressTest {

    private Address address;
    private List<Beneficiary> beneficiaries;
    private List<Interpreter> interpreters;

    // Set Up //

    /**
     * Initializes valid lists and a default {@link Address} before each test.
     */
    @BeforeEach
    void setUp(){
        beneficiaries = new ArrayList<>();
        interpreters = new ArrayList<>();
        address = new Address(1,6833, "BP1", "Ucimont", "14", "Bouillon", null, beneficiaries, interpreters);
    }

    // Constructor (numAddress, all fields) //

    /**
     * Tests that the full constructor with id correctly sets all fields when valid arguments are provided.
     * Given : valid numAddress, postcode, postOfficeBox, locality, numStreet, hamlet, establishment.
     * When  : an Address is created with these arguments
     * Then  : all getters must return the expected values
     */
    @Test
    void constuctor_WithValidArguments_SetsAllFields(){
        assertEquals(1, address.getNumAddress());
        assertEquals(6833, address.getPostcode());
        assertEquals("BP1", address.getPostOfficeBox());
        assertEquals("Ucimont", address.getLocality());
        assertEquals("14", address.getNumStreet());
        assertEquals("Bouillon", address.getHamlet());
        assertNull(address.getEstablishment());
        assertEquals(beneficiaries, address.getBeneficiaries());
        assertEquals(interpreters, address.getInterpreters());
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when postOfficeBox is null.
     * Given : a null postOfficeBox
     * When  : an Address is created with null postOfficeBox
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullPostOfficeBox_RaisesAnException(){
        assertThrows(IllegalArgumentException.class,
                () -> new Address(1, 6833, null, "Ucimont", "14", "Bouillon", null, beneficiaries, interpreters));
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when locality is null.
     * Given : a null locality
     * When  : an Address is created with null locality
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullLocality_RaisesAnException(){
        assertThrows(IllegalArgumentException.class,
                () -> new Address(1, 6833, "BP1", null, "14", "Bouillon", null, beneficiaries, interpreters));
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when numStreet is null.
     * Given : a null numStreet
     * When  : an Address is created with null numStreet
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullNumStreet_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Address(1, 6833, "BP1", "Ucimont", null, "Bouillon", null, beneficiaries, interpreters));
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when beneficiaries is null.
     * Given : a null beneficiaries list
     * When  : an Address is created with null beneficiaries
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullBeneficiaries_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Address(1, 6833, "BP1", "Ucimont", "14", "Bouillon", null, null, interpreters));
    }

    /**
     * Tests that the full constructor throws an {@link IllegalArgumentException} when interpreters is null.
     * Given : a null interpreters list
     * When  : an Address is created with null interpreters
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullInterpreters_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Address(1, 6833, "BP1", "Ucimont", "14", "Bouillon", null, beneficiaries, null));
    }

    //  Default Constructor //

    /**
     * Tests that the default constructor initializes numAddress and postcode to -1, all String fields to "" and both lists to empty lists.
     * Given : no argument
     * When  : an Address is created with the default constructor
     * Then  : getNumAddress() must return -1, getPostcode() must return -1,
     *         getPostOfficeBox(), getLocality(), getNumStreet() must return "" and both lists must be empty
     */
    @Test
    void defaultConstructor_AllFieldsAreDefaultValues() {
        Address a = new Address();
        assertEquals(-1, a.getNumAddress());
        assertEquals(-1, a.getPostcode());
        assertEquals("", a.getPostOfficeBox());
        assertEquals("", a.getLocality());
        assertEquals("", a.getNumStreet());
        assertTrue(a.getBeneficiaries().isEmpty());
        assertTrue(a.getInterpreters().isEmpty());
    }

    // setPostOfficeBox //

    /**
     * Tests that {@code setPostOfficeBox()} correctly updates the value.
     * Given : an Address initialized with postOfficeBox="BP1"
     * When  : setPostOfficeBox("BP2") is called
     * Then  : getPostOfficeBox() must return "BP2"
     */
    @Test
    void setPostOfficeBox_UpdatesTheCorrectValue() {
        address.setPostOfficeBox("BP2");
        assertEquals("BP2", address.getPostOfficeBox());
    }

    /**
     * Tests that {@code setPostOfficeBox()} throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setPostOfficeBox(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setPostOfficeBox_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> address.setPostOfficeBox(null));
    }

    // setLocality //

    /**
     * Tests that {@code setLocality()} correctly updates the value.
     * Given : an Address initialized with locality="Liège"
     * When  : setLocality("Namur") is called
     * Then  : getLocality() must return "Namur"
     */
    @Test
    void setLocality_UpdatesTheCorrectValue() {
        address.setLocality("Namur");
        assertEquals("Namur", address.getLocality());
    }

    /**
     * Tests that {@code setLocality()} throws an {@link IllegalArgumentException}
     * when null is passed.
     * Given : a null value
     * When  : setLocality(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setLocality_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> address.setLocality(null));
    }

    // setNumStreet //

    /**
     * Tests that {@code setNumStreet()} correctly updates the value.
     * Given : an Address initialized with numStreet="10"
     * When  : setNumStreet("20") is called
     * Then  : getNumStreet() must return "20"
     */
    @Test
    void setNumStreet_UpdatesTheCorrectValue() {
        address.setNumStreet("20");
        assertEquals("20", address.getNumStreet());
    }

    /**
     * Tests that {@code setNumStreet()} throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null value
     * When  : setNumStreet(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setNumStreet_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> address.setNumStreet(null));
    }

    // setBeneficiaries //

    /**
     * Tests that {@code setBeneficiaries()} correctly updates the list.
     * Given : a new list of beneficiaries
     * When  : setBeneficiaries() is called with this list
     * Then  : getBeneficiaries() must return the new list
     */
    @Test
    void setBeneficiaries_UpdatesTheCorrectValue() {
        List<Beneficiary> newList = new ArrayList<>();
        address.setBeneficiaries(newList);
        assertEquals(newList, address.getBeneficiaries());
    }

    /**
     * Tests that {@code setBeneficiaries()} throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null list
     * When  : setBeneficiaries(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setBeneficiaries_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> address.setBeneficiaries(null));
    }

    // setInterpreters //

    /**
     * Tests that {@code setInterpreters()} correctly updates the list.
     * Given : a new list of interpreters
     * When  : setInterpreters() is called with this list
     * Then  : getInterpreters() must return the new list
     */
    @Test
    void setInterpreters_UpdatesTheCorrectValue() {
        List<Interpreter> newList = new ArrayList<>();
        address.setInterpreters(newList);
        assertEquals(newList, address.getInterpreters());
    }

    /**
     * Tests that {@code setInterpreters()} throws an {@link IllegalArgumentException} when null is passed.
     * Given : a null list
     * When  : setInterpreters(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setInterpreters_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class, () -> address.setInterpreters(null));
    }

    // toString //

    /**
     * Tests that {@code toString()} contains the label "Adresse".
     * Given : a valid Address
     * When  : toString() is called
     * Then  : the result must contain "Adresse"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(address.toString().contains("Adresse"));
    }

    /**
     * Tests that {@code toString()} contains the locality.
     * Given : an Address initialized with locality="Liège"
     * When  : toString() is called
     * Then  : the result must contain "Liège"
     */
    @Test
    void toString_ContainsLocality() {
        assertTrue(address.toString().contains("Ucimont"));
    }
}