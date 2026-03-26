package be.hers.info.ProjetIntegree.POJO;

public class Coordinator extends Interpreter {
    private int numCoordinator;
    private boolean isAdmin;


    /**
     * Create a coordinator.
     * @param numCoordinator the numero of the coordinator
     * @param isAdmin If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa
     */
    public Coordinator(String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours, Address address,int numCoordinator,boolean isAdmin) {
        super(lastName,firstName,email,phoneNumber,weeklyWorkHours,address);
        this.numCoordinator=numCoordinator;
        this.isAdmin = isAdmin;

    }

    /**
     * Create a coordinator.
     * @param isAdmin If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa

     */
    public Coordinator(String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours, Address address,boolean isAdmin) {
        super(lastName,firstName,email,phoneNumber,weeklyWorkHours,address);
        this.isAdmin = isAdmin;
    }
    /**
     * Create a resa coordinator.
     */
    public Coordinator(String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours, Address address) {
        super(lastName,firstName,email,phoneNumber,weeklyWorkHours,address);
        this.isAdmin = false;

    }
    /**
     * Create a coordinator.
     * @param numCoordinator the numero of the coordinator
     * @param isAdmin If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa
     */
    public Coordinator(int numInterpreter, String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours, Address address,int numCoordinator,boolean isAdmin) {
        super(numInterpreter, lastName,firstName,email,phoneNumber,weeklyWorkHours,address);
        this.numCoordinator=numCoordinator;
        this.isAdmin = isAdmin;

    }

    /**
     * Create a coordinator.
     * @param isAdmin If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa

     */
    public Coordinator(int numInterpreter,String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours, Address address,boolean isAdmin) {
        super(numInterpreter, lastName,firstName,email,phoneNumber,weeklyWorkHours,address);
        this.isAdmin = isAdmin;
    }
    /**
     * Create a resa coordinator.

     */
    public Coordinator(int numInterpreter,String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours, Address address) {
        super(numInterpreter, lastName,firstName,email,phoneNumber,weeklyWorkHours,address);
        this.isAdmin = false;

    }
    /**
     * Create a Coordinator object resa with no link to an Interpreter
     */
    public Coordinator() {
        super();
        this.isAdmin = false;

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
     * @return the numero of the coordinator
     */
    public int getNumCoordinator() {
        return numCoordinator;
    }
    /**
     *
     * @return a string that contains the interpreter and a textual interpretation of isAdmin.
     */
    @Override
    public String toString() {
        return "NumCoordinator : " + numCoordinator + (isAdmin ? "Coordinatrice/Coordinateur principale" : "Resa") + "\n" + super.toString();
    }
}
