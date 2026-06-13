package be.hers.info.ProjetIntegree.DTO;

/**
 * DTO used to pass establishments from the DB to the frontend
 *
 * @authors Rosman Loïs
 * @reviewer Nicolas Jean-François
 */

public class DTOEstablishmentFormAppointment {

    private int numEstablishment;
    private String name;

    /**
     * Initialize an establishment with his id and his name
     *
     * @param numEstablishment the id of the establishment
     * @param name             the name of the establishment
     */
    public DTOEstablishmentFormAppointment(int numEstablishment, String name) {
        this.name = name;
        this.numEstablishment = numEstablishment;
    }

    /**
     * @return the id of the establishment
     */
    public int getNumEstablishment() {
        return numEstablishment;
    }

    /**
     * Initialize the id of the establishment
     *
     * @param numEstablishment the id of the establishment
     */
    public void setNumEstablishment(int numEstablishment) {
        this.numEstablishment = numEstablishment;
    }

    /**
     * @return the name of the establishment
     */
    public String getName() {
        return name;
    }

    /**
     * Initialize the name of the establishment
     *
     * @param name the name of the establishment
     */
    public void setName(String name) {
        this.name = name;
    }
}

