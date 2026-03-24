package be.hers.info.ProjetIntegree.POJO;

public class Absence {
    private String status;
    private TimeSlotPonctual timeSlotPonctual;
    private Interpreter refInterpreter;

    public Absence(String status, TimeSlotPonctual timeSlotPonctual, Interpreter refInterpreter) throws BadStatusException {
        if(!status.equals("en attente") || !status.equals("accepte") || !status.equals("refuse") || !status.equals("absent")) throw new BadStatusException();
        this.status = status;
        this.timeSlotPonctual = timeSlotPonctual;
        this.refInterpreter = refInterpreter;
    }

    public Absence(TimeSlotPonctual timeSlotPonctual, Interpreter refInterpreter) {
        this.status = "en attente";
        this.timeSlotPonctual = timeSlotPonctual;
        this.refInterpreter = refInterpreter;
    }
    public Absence(){
        this.status = "en attente";
        this.timeSlotPonctual = null;
        this.refInterpreter = null;
    }
    public Interpreter getRefInterpreter() {
        return refInterpreter;
    }

    public void setRefInterpreter(Interpreter refInterpreter) {
        this.refInterpreter = refInterpreter;
    }

    public TimeSlotPonctual getTimeSlotPonctual() {
        return timeSlotPonctual;
    }

    public void setTimeSlotPonctual(TimeSlotPonctual timeSlotPonctual) {
        this.timeSlotPonctual = timeSlotPonctual;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
