package be.hers.info.ProjetIntegree.POJO;

public class Coordinator {

    private boolean isAdmin;
    private Interpreter refInterpreter;

    /**
     * Create a coordinator.
     * @param isAdmin If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa
     * @param refInterpreter the interpreter who will be the resa or the main coordinator.
     */
    public Coordinator(boolean isAdmin, Interpreter refInterpreter) {
        if(refInterpreter == null) throw new NullPointerException();
        this.isAdmin = isAdmin;
        this.refInterpreter = refInterpreter;
    }

    /**
     * Create a resa coordinator.
     * @param refInterpreter the interpreter who will be the resa.
     */
    public Coordinator(Interpreter refInterpreter) {
        if(refInterpreter == null) throw new NullPointerException();
        this.isAdmin = false;
        this.refInterpreter = refInterpreter;
    }

    /**
     * Create a Coordinator object resa with no link to an Interpreter
     */
    public Coordinator() {
        this.isAdmin = false;
        this.refInterpreter = null;
    }

    /**
     *
     * @return the interpreter who will be the resa or the main coordinator.
     */
    public Interpreter getRefInterpreter() {
        return refInterpreter;
    }

    /**
     *
     * @param refInterpreter the interpreter who will be the resa or the main coordinator.
     */
    public void setRefInterpreter(Interpreter refInterpreter) {
        if(refInterpreter == null) throw new NullPointerException();
        this.refInterpreter = refInterpreter;
    }

    /**
     *
     * @return If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     *
     * @param isAdmin If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     *
     * @return a string that contains the interpreter and a textual interpretation of isAdmin.
     */
    public String toString() {
        return refInterpreter.toString() + (isAdmin ? "Coordinatrice/Coordinateur principale" : "Resa");
    }
}
