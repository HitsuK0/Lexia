package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    // Set Up //

    /**
     * Initializes a valid {@link Address} before each test.
     */
    @BeforeEach
    void setUp(){
        address = new Address(1, 6833, "14", "Ucimont", "Bouillon", null);
    }

    // Constructor (numAddress, all fields) //

    /**
     * Tests that the full constructor with id correctly sets all fields when valid arguments are provided.
     * Given : valid numAddress, postcode, postOfficeBox, locality, hamlet, establishment.
     * When  : an Address is created with these arguments
     * Then  : all getters must return the expected values
     */
    @Test
    void constuctor_WithValidArguments_SetsAllFields(){
        assertEquals(1, address.getNumAddress());
        assertEquals(6833, address.getPostcode());
        assertEquals("14", address.getPostOfficeBox());
        assertEquals("Ucimont", address.getLocality());
        assertEquals("Bouillon", address.getHamlet());
        assertNull(address.getEstablishment());
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
                () -> new Address(1, 6833, null, "Ucimont", "Bouillon", null));
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
                () -> new Address(1, 6833, "14", null, "Bouillon", null));
    }

    //  Default Constructor //

    /**
     * Tests that the default constructor initializes numAddress and postcode to -1 and all String fields to "".
     * Given : no argument
     * When  : an Address is created with the default constructor
     * Then  : getNumAddress() must return -1, getPostcode() must return -1,
     *         getPostOfficeBox(), getLocality() and getHamlet() must return ""
     */
    @Test
    void defaultConstructor_AllFieldsAreDefaultValues() {
        Address a = new Address();
        assertEquals(-1, a.getNumAddress());
        assertEquals(-1, a.getPostcode());
        assertEquals("", a.getPostOfficeBox());
        assertEquals("", a.getLocality());
        assertEquals("", a.getHamlet());
        assertNull(a.getEstablishment());
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
        address.setPostOfficeBox("15");
        assertEquals("15", address.getPostOfficeBox());
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
     * Given : an Address initialized with locality="Ucimont"
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
     * Given : an Address initialized with locality="Ucimont"
     * When  : toString() is called
     * Then  : the result must contain "Ucimont"
     */
    @Test
    void toString_ContainsLocality() {
        assertTrue(address.toString().contains("Ucimont"));
    }
}