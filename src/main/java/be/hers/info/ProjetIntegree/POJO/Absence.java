package be.hers.info.ProjetIntegree.POJO;
/**
 * @author Halet Louis
 * @reviewer Nicolas Jean-Francois
 */
public class Absence {


    private int numAbsence;
    private String status;
    private TimeSlotPunctual timeSlotPunctual;

    /**
     * Create an Absence with all attribute
     *
     * @param numAbsence       the numero of the absence
     * @param status           the status of the absence (it can only be among these values: 'en attente', 'accepte', 'refuse', 'absent')
     * @param timeSlotPunctual the time slot of absence
     * @throws BadStatusException   If the absence status is not among these values: 'en attente', 'accepte', 'refuse', 'absent'
     * @throws IllegalArgumentException if TimeSlotPunctual is null
     */
    public Absence(int numAbsence, String status, TimeSlotPunctual timeSlotPunctual) throws BadStatusException {
        if (!status.equals("en attente") && !status.equals("accepte") && !status.equals("refuse") && !status.equals("absent"))
            throw new BadStatusException("[POJOAbsence] Mauvais status de l'absence");
        if (timeSlotPunctual == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null ou vide");
        this.numAbsence = numAbsence;
        this.status = status;
        this.timeSlotPunctual = timeSlotPunctual;

    }

    /**
     * Create an Absence without his numAbsence
     *
     * @param status           the status of the absence (it can only be among these values: 'en attente', 'accepte', 'refuse', 'absent')
     * @param timeSlotPunctual the time slot of absence
     * @throws BadStatusException   If the absence status is not among these values: 'en attente', 'accepte', 'refuse', 'absent'
     * @throws IllegalArgumentException if TimeSlotPunctual is null
     */
    public Absence(String status, TimeSlotPunctual timeSlotPunctual) throws BadStatusException {
        if (!status.equals("en attente") && !status.equals("accepte") && !status.equals("refuse") && !status.equals("absent"))
            throw new BadStatusException("[POJOAbsence] Mauvais status de l'absence");
        if (timeSlotPunctual == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null ou vide");
        this.status = status;
        this.timeSlotPunctual = timeSlotPunctual;

    }

    /**
     * Create an Absence with 'en attente' for status
     *
     * @param timeSlotPunctual the time slot of absence
     * @throws IllegalArgumentException if TimeSlotPunctual is null
     */
    public Absence(TimeSlotPunctual timeSlotPunctual) {
        if (timeSlotPunctual == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null ou vide");
        this.status = "en attente";
        this.timeSlotPunctual = timeSlotPunctual;

    }

    /**
     * Create an Absence with 'en attente' for status (default constructor)
     */
    public Absence() {
        this.status = "en attente";
        this.timeSlotPunctual = null;

    }

    /**
     *
     * @return the time slot of absence
     */
    public TimeSlotPunctual getTimeSlotPunctual() {
        return timeSlotPunctual;
    }

    /**
     *
     * @param timeSlotPunctual the time slot of absence
     */
    public void setTimeSlotPunctual(TimeSlotPunctual timeSlotPunctual) {
        if (timeSlotPunctual == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null ou vide");
        this.timeSlotPunctual = timeSlotPunctual;
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

        if(timeSlotPunctual == null) {
            sb.append("\nTranche horaire : Non renseigné");
        } else {
            sb.append("\nTranche horaire  : ").append(timeSlotPunctual.toString());
        }
        return sb.toString();
    }

}
