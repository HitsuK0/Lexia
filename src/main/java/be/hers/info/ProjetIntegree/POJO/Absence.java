package be.hers.info.ProjetIntegree.POJO;

/**
 * @author Halet Louis
 * @reviewer Nicolas Jean-Francois
 */
public class Absence {

    private int numAbsence;
    private String status;
    private String reason;
    private boolean privateReason;
    private TimeSlot timeSlot;

    /**
     * Create an Absence with all attribute
     *
     * @param numAbsence the numero of the absence
     * @param status the status of the absence (it can only be among these values: 'en attente', 'accepte' or 'refuse')
     * @param timeSlot the time slot of absence
     * @param reason the reason of absence, can be null
     * @param privateReason the privateReason of absence, defines if the reason is private or not
     * @throws BadStatusException If the absence status is not among these values: 'en attente', 'accepte' or 'refuse'
     * @throws IllegalArgumentException if timeSlot is null
     */
    public Absence(int numAbsence, String status, TimeSlot timeSlot, String reason, boolean privateReason) throws BadStatusException {
        if (!status.equals("en attente") && !status.equals("accepte") && !status.equals("refuse"))
            throw new BadStatusException("[POJOAbsence] Mauvais status de l'absence");
        if (timeSlot == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null");
        this.numAbsence = numAbsence;
        this.status = status;
        this.timeSlot = timeSlot;
        this.reason = reason;
        this.privateReason = privateReason;
    }

    /**
     * Create an Absence without his numAbsence, no reason and privateReason is false
     *
     * @param status the status of the absence (it can only be among these values: 'en attente', 'accepte' or 'refuse')
     * @param timeSlot the time slot of absence
     * @throws BadStatusException   If the absence status is not among these values: 'en attente', 'accepte' or 'refuse'
     * @throws IllegalArgumentException if timeSlot is null
     */
    public Absence(String status, TimeSlot timeSlot) throws BadStatusException {
        if (!status.equals("en attente") && !status.equals("accepte") && !status.equals("refuse"))
            throw new BadStatusException("[POJOAbsence] Mauvais status de l'absence");
        if (timeSlot == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null");
        this.status = status;
        this.timeSlot = timeSlot;
        this.reason = "";
        this.privateReason = false;
    }

    /**
     * Create an Absence with 'en attente' for status, no reason and privateReason is false
     *
     * @param timeSlot the time slot of absence
     * @throws IllegalArgumentException if timeSlot is null
     */
    public Absence(TimeSlot timeSlot) {
        if (timeSlot == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null ou vide");
        this.status = "en attente";
        this.reason = "";
        this.privateReason = false;
        this.timeSlot = timeSlot;
    }

    /**
     * Create an Absence with 'en attente' for status, no reason, no time slot and the privateReason is false (default constructor)
     */
    public Absence() {
        this.status = "en attente";
        this.reason = "";
        this.privateReason = false;
        this.timeSlot = null;
    }

    /**
     *
     * @return the reason of absence
     */
    public String getReason() {
        return reason;
    }

    /**
     *
     * @param reason the reason of absence
     */
    public void setReason(String reason) {
        if(reason == null) {
            this.reason = "";
        } else {
            this.reason = reason;
        }
    }

    /**
     *
     * @return privateReason which defines if the reason is private or not
     */
    public boolean isPrivateReason() {
        return privateReason;
    }

    /**
     *
     * @param privateReason the privateReason state of absence
     */
    public void setPrivateReason(boolean privateReason) {
        this.privateReason = privateReason;
    }

    /**
     *
     * @return the time slot of absence
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     *
     * @param timeSlot the time slot of absence
     */
    public void setTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null");

        this.timeSlot = timeSlot;
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
        sb.append("\nNumAbsence: ").append(this.numAbsence);
        sb.append("\nStatus de l'absence: ").append(this.status);

        if(this.reason == null || this.reason.isEmpty()) {
            sb.append("\nRaison de l'absence: Non renseignée");
        } else {
            sb.append("\nRaison de l'absence: ").append(this.reason);
        }

        if(this.privateReason) {
            sb.append("\nRaison privée: Oui");
        } else {
            sb.append("\nRaison privée: Non");
        }

        if(this.timeSlot == null) {
            sb.append("\nTranche horaire: Non renseigné");
        } else {
            sb.append("\nTranche horaire: ").append(this.timeSlot.toString());
        }
        return sb.toString();
    }
}