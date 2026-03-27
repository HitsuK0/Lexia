package be.hers.info.ProjetIntegree.POJO;

public class Absence {
    private int numAbsence;
    private String status;
    private TimeSlotPonctual timeSlotPonctual;
    private Interpreter refInterpreter;

    /**
     * Create an Absence with all attribute
     * @param numAbsence the numero of the absence
     * @param status the status of the absence (it can only be among these values: 'en attente', 'accepte', 'refuse', 'absent')
     * @param timeSlotPonctual the time slot of absence
     * @param refInterpreter The interpreter was absent.
     * @throws BadStatusException If the absence status is not among these values: 'en attente', 'accepte', 'refuse', 'absent'
     * @throws NullPointerException if timeSlotPonctual is null or refInterpreter is null
     */
    public Absence(int numAbsence,String status, TimeSlotPonctual timeSlotPonctual, Interpreter refInterpreter) throws BadStatusException {
        if(!status.equals("en attente") || !status.equals("accepte") || !status.equals("refuse") || !status.equals("absent")) throw new BadStatusException();
        if(timeSlotPonctual == null || refInterpreter == null) throw new NullPointerException();
        this.numAbsence = numAbsence;
        this.status = status;
        this.timeSlotPonctual = timeSlotPonctual;
        this.refInterpreter = refInterpreter;
    }
    /**
     * Create an Absence without his numAbsence
     * @param status the status of the absence (it can only be among these values: 'en attente', 'accepte', 'refuse', 'absent')
     * @param timeSlotPonctual the time slot of absence
     * @param refInterpreter The interpreter was absent.
     * @throws BadStatusException If the absence status is not among these values: 'en attente', 'accepte', 'refuse', 'absent'
     * @throws NullPointerException if timeSlotPonctual is null or refInterpreter is null
     */
    public Absence(String status, TimeSlotPonctual timeSlotPonctual, Interpreter refInterpreter) throws BadStatusException {
        if(!status.equals("en attente") || !status.equals("accepte") || !status.equals("refuse") || !status.equals("absent")) throw new BadStatusException();
        if(timeSlotPonctual == null || refInterpreter == null) throw new NullPointerException();
        this.status = status;
        this.timeSlotPonctual = timeSlotPonctual;
        this.refInterpreter = refInterpreter;
    }

    /**
     * Create an Absence with 'en attente' for status
     * @param timeSlotPonctual the time slot of absence
     * @param refInterpreter The interpreter was absent.
     * @throws NullPointerException if timeSlotPonctual is null or refInterpreter is null
     */
    public Absence(TimeSlotPonctual timeSlotPonctual, Interpreter refInterpreter) {
        if(timeSlotPonctual == null || refInterpreter == null) throw new NullPointerException();
        this.status = "en attente";
        this.timeSlotPonctual = timeSlotPonctual;
        this.refInterpreter = refInterpreter;
    }

    /**
     * Create an Absence with 'en attente' for status (default constructor)
     */
    public Absence(){
        this.status = "en attente";
        this.timeSlotPonctual = null;
        this.refInterpreter = null;
    }

    /**
     *
     * @return The interpreter was absent.
     */
    public Interpreter getRefInterpreter() {
        return refInterpreter;
    }

    /**
     *
     * @param refInterpreter The interpreter was absent.
     */
    public void setRefInterpreter(Interpreter refInterpreter) {
        if(refInterpreter == null) throw new NullPointerException();
        this.refInterpreter = refInterpreter;
    }

    /**
     *
     * @return the time slot of absence
     */
    public TimeSlotPonctual getTimeSlotPonctual() {
        return timeSlotPonctual;
    }

    /**
     *
     * @param timeSlotPonctual the time slot of absence
     */
    public void setTimeSlotPonctual(TimeSlotPonctual timeSlotPonctual) {
        if(timeSlotPonctual == null) throw new NullPointerException();
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
    public void setStatus(String status)throws BadStatusException {
        if(!status.equals("en attente") || !status.equals("accepte") || !status.equals("refuse") || !status.equals("absent")) throw new BadStatusException();
        this.status = status;
    }

    /**
     *
     * @return a string that contains all attribute of an absence
     */
    public String toString() {
        return "NumAbsence : " + numAbsence + "\nStatus de l'absence : " + status + "\n" + "Tranche horraire : " + timeSlotPonctual.toString() + "\n" + "Interprete référence : " + refInterpreter.toString();
    }

    /**
     *
     * @return the numero of the absence
     */
    public int getNumAbsence() {
        return numAbsence;
    }
}
