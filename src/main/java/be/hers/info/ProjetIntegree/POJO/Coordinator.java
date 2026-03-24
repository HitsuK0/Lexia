package be.hers.info.ProjetIntegree.POJO;

public class Coordinator {

    private boolean isAdmin;
    private Interpreter refInterpreter;

    /**
     * Construit un/une coordinateur/coordinatrice.
     * @param isAdmin si isAdmin est a true, la coordinatrice sera le/la coordinateur/coordinatrice principale. false, si c'est une resa
     * @param refInterpreter l'interprete qui sera resa ou coordinateur/coordinatrice principale.
     */
    public Coordinator(boolean isAdmin, Interpreter refInterpreter) {
        this.isAdmin = isAdmin;
        this.refInterpreter = refInterpreter;
    }

    /**
     * Construit un/une coordinateur/coordinatrice resa.
     * @param refInterpreter l'interprete qui sera resa ou coordinatrice principale.
     */
    public Coordinator(Interpreter refInterpreter) {
        this.isAdmin = false;
        this.refInterpreter = refInterpreter;
    }

    /**
     * Construit un objet Coordinator resa sans lien avec un Interpreter
     */
    public Coordinator() {
        this.isAdmin = false;
        this.refInterpreter = null;
    }

    public Interpreter getRefInterpreter() {
        return refInterpreter;
    }

    public void setRefInterpreter(Interpreter refInterpreter) {
        this.refInterpreter = refInterpreter;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

}
