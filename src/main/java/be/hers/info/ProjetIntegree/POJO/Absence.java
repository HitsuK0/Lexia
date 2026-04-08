package be.hers.info.ProjetIntegree.POJO;
/**
 * @author Halet Louis
 * @reviewer Nicolas Jean-Francois
 */
public class Absence {


    private int numAbsence;
    private String status;
    private TimeSlotPunctual timeSlotPonctual;

    /**
     * Create an Absence with all attribute
     *
     * @param numAbsence       the numero of the absence
     * @param status           the status of the absence (it can only be among these values: 'en attente', 'accepte', 'refuse', 'absent')
     * @param timeSlotPonctual the time slot of absence
     * @throws BadStatusException   If the absence status is not among these values: 'en attente', 'accepte', 'refuse', 'absent'
     * @throws IllegalArgumentException if timeSlotPonctual is null
     */
    public Absence(int numAbsence, String status, TimeSlotPunctual timeSlotPonctual) throws BadStatusException {
        if (!status.equals("en attente") && !status.equals("accepte") && !status.equals("refuse") && !status.equals("absent"))
            throw new BadStatusException("[POJOAbsence] Mauvais status de l'absence");
        if (timeSlotPonctual == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null ou vide");
        this.numAbsence = numAbsence;
        this.status = status;
        this.timeSlotPonctual = timeSlotPonctual;

    }

    /**
     * Create an Absence without his numAbsence
     *
     * @param status           the status of the absence (it can only be among these values: 'en attente', 'accepte', 'refuse', 'absent')
     * @param timeSlotPonctual the time slot of absence
     * @throws BadStatusException   If the absence status is not among these values: 'en attente', 'accepte', 'refuse', 'absent'
     * @throws IllegalArgumentException if timeSlotPonctual is null
     */
    public Absence(String status, TimeSlotPunctual timeSlotPonctual) throws BadStatusException {
        if (!status.equals("en attente") && !status.equals("accepte") && !status.equals("refuse") && !status.equals("absent"))
            throw new BadStatusException("[POJOAbsence] Mauvais status de l'absence");
        if (timeSlotPonctual == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null ou vide");
        this.status = status;
        this.timeSlotPonctual = timeSlotPonctual;

    }

    /**
     * Create an Absence with 'en attente' for status
     *
     * @param timeSlotPonctual the time slot of absence
     * @throws IllegalArgumentException if timeSlotPonctual is null
     */
    public Absence(TimeSlotPunctual timeSlotPonctual) {
        if (timeSlotPonctual == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null ou vide");
        this.status = "en attente";
        this.timeSlotPonctual = timeSlotPonctual;

    }

    /**
     * Create an Absence with 'en attente' for status (default constructor)
     */
    public Absence() {
        this.status = "en attente";
        this.timeSlotPonctual = null;

    }

    /**
     *
     * @return the time slot of absence
     */
    public TimeSlotPunctual getTimeSlotPonctual() {
        return timeSlotPonctual;
    }

    /**
     *
     * @param timeSlotPonctual the time slot of absence
     */
    public void setTimeSlotPonctual(TimeSlotPunctual timeSlotPonctual) {
        if (timeSlotPonctual == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null ou vide");
        this.timeSlotPonctual = timeSlotPonctual;
    }

    /**
     *
     * @return the status of the absence (it can only be among these values: 'en attente', 'accepte', 'refuse', 'absent')
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status the status of the absence (it can only be among these values: 'en attente', 'accepte', 'refuse', 'absent')
     * @throws BadStatusException If the absence status is not among these values: 'en attente', 'accepte', 'refuse', 'absent'
     */
    public void setStatus(String status) throws BadStatusException {
        if (!status.equals("en attente") && !status.equals("accepte") && !status.equals("refuse") && !status.equals("absent"))
            throw new BadStatusException("[POJOAbsence] Mauvais status de l'absence");
        this.status = status;
    }

    /**
     *
     * @return the numero of the absence
     */
    public int getNumAbsence() {
        return numAbsence;
    }

    /**
     *
     * @param numAbsence the numero of the absence
     * @throws IllegalArgumentException if numAbsence is negative
     */
    public void setNumAbsence(int numAbsence) {
        if (numAbsence < 0)
            throw new IllegalArgumentException("[POJOAbsence] Le numéro de l'absence ne peut pas être négatif");
        this.numAbsence = numAbsence;
    }
    /**
     *
     * @return a string that contains all attribute of an absence
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Absence");
        sb.append("\nNumAbsence : ").append(numAbsence);
        sb.append("\nStatus de l'absence : ").append(status);

        if(timeSlotPonctual == null) {
            sb.append("\nTranche horaire : Non renseigné");
        } else {
            sb.append("\nTranche horaire  : ").append(timeSlotPonctual.toString());
        }
        return sb.toString();
    }

}
