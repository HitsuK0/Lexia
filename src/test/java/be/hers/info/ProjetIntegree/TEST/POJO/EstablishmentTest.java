package be.hers.info.ProjetIntegree.TEST.POJO;

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.POJO.Referrer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Establishment}
 * Verifies the correct behavior of constructor, getters, setters and toString.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis
 */
public class EstablishmentTest {

    private Establishment establishment;
    private List<Integer> educationLevels;
    private List<Referrer> referrers;
    private List<Address> addresses;

    // Set Up //

    /**
     * Initializes a valid {@link Establishment} before each test.
     */
    @BeforeEach
    void setUp() {
        educationLevels = new ArrayList<>();
        educationLevels.add(2);
        educationLevels.add(3);
        referrers = new ArrayList<>();
        addresses = new ArrayList<>();
        addresses.add(new Address(1, 6833, "14", "Ucimont", "Bouillon", null));
        establishment = new Establishment(1, "Institut Saint-Joseph", "061234567", educationLevels, referrers, addresses);
    }

    // Full Constructor //

    /**
     * Tests that the full constructor correctly sets all fields when valid arguments are provided.
     * Given : valid arguments
     * When  : an Establishment is created
     * Then  : all getters must return expected values
     */
    @Test
    void constructor_WithValidArguments_SetsAllFields() {
        assertEquals(1, establishment.getNumEstablishment());
        assertEquals("Institut Saint-Joseph", establishment.getNameBuilding());
        assertEquals("061234567", establishment.getPhoneNumber());
        assertEquals(educationLevels, establishment.getEducationLevel());
        assertEquals(referrers, establishment.getReferrers());
        assertEquals(addresses, establishment.getAddresses());
    }

    /**
     * Tests that constructor throws an {@link IllegalArgumentException} when educationLevel is null.
     * Given : a null educationLevel list
     * When  : an Establishment is created
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullEducationLevel_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Establishment(1, "School", "061111111", null, referrers, addresses));
    }

    /**
     * Tests that constructor throws an {@link IllegalArgumentException} when referrers is null.
     * Given : a null referrers list
     * When  : an Establishment is created
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullReferrers_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Establishment(1, "School", "061111111", educationLevels, null, addresses));
    }

    /**
     * Tests that constructor throws an {@link IllegalArgumentException} when addresses is null.
     * Given : a null addresses list
     * When  : an Establishment is created
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithNullAddresses_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Establishment(1, "School", "061111111", educationLevels, referrers, null));
    }

    /**
     * Tests that constructor throws an {@link IllegalArgumentException} when addresses is empty.
     * Given : an empty addresses list
     * When  : an Establishment is created
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void constructor_WithEmptyAddresses_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Establishment(1, "School", "061111111", educationLevels, referrers, new ArrayList<>()));
    }

    // Default Constructor //

    /**
     * Tests that default constructor initializes fields with default values.
     * Given : no argument
     * When  : an Establishment is created
     * Then  : fields must contain default values
     */
    @Test
    void defaultConstructor_AllFieldsAreDefaultValues() {
        Establishment e = new Establishment();
        assertEquals("", e.getNameBuilding());
        assertEquals("", e.getPhoneNumber());
        assertTrue(e.getEducationLevel().isEmpty());
        assertTrue(e.getReferrers().isEmpty());
        assertTrue(e.getAddresses().isEmpty());
    }

    // setNameBuilding //

    /**
     * Tests that code setNameBuilding() correctly updates the value.
     * Given : a valid Establishment
     * When  : setNameBuilding("Athénée Royal") is called
     * Then  : getNameBuilding() must return "Athénée Royal"
     */
    @Test
    void setNameBuilding_UpdatesTheCorrectValue() {
        establishment.setNameBuilding("Athénée Royal");
        assertEquals("Athénée Royal", establishment.getNameBuilding());
    }

    // setPhoneNumber //

    /**
     * Tests that setPhoneNumber() correctly updates the value.
     * Given : a valid Establishment
     * When  : setPhoneNumber("0499999999") is called
     * Then  : getPhoneNumber() must return "0499999999"
     */
    @Test
    void setPhoneNumber_UpdatesTheCorrectValue() {
        establishment.setPhoneNumber("0499999999");
        assertEquals("0499999999", establishment.getPhoneNumber());
    }

    // setEducationLevel //

    /**
     * Tests that setEducationLevel() correctly updates the value.
     * Given : a valid list
     * When  : setEducationLevel() is called
     * Then  : getter must return updated list
     */
    @Test
    void setEducationLevel_UpdatesTheCorrectValue() {
        List<Integer> newLevels = List.of(1, 4);
        establishment.setEducationLevel(newLevels);
        assertEquals(newLevels, establishment.getEducationLevel());
    }

    /**
     * Tests that setEducationLevel() throws an {@link IllegalArgumentException} when null is passed.
     * Given : null
     * When  : setEducationLevel(null) is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setEducationLevel_WithNull_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> establishment.setEducationLevel(null));
    }

    // setAddresses //

    /**
     * Tests that setAddresses() throws an {@link IllegalArgumentException} when an empty list is passed.
     * Given : empty list
     * When  : setAddresses() is called
     * Then  : an IllegalArgumentException must be thrown
     */
    @Test
    void setAddresses_WithEmptyList_RaisesAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> establishment.setAddresses(new ArrayList<>()));
    }

    // toString //

    /**
     * Tests that toString() contains the label "Etablissement".
     * Given : a valid Establishment
     * When  : toString() is called
     * Then  : result must contain "Etablissement"
     */
    @Test
    void toString_ContainsLabel() {
        assertTrue(establishment.toString().contains("Etablissement"));
    }

    /**
     * Tests that toString() contains the building name.
     * Given : a valid Establishment
     * When  : toString() is called
     * Then  : result must contain building name
     */
    @Test
    void toString_ContainsNameBuilding() {
        assertTrue(establishment.toString().contains("Institut Saint-Joseph"));
    }
}