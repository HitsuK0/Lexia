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
}
