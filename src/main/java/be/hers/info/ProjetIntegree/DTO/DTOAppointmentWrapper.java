package be.hers.info.ProjetIntegree.DTO;

/**
 * @authors Rosman Loïs
 * @reviewer Nicolas Jean-François, Halet Louis, Wellinger Chloé
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper used to allow Spring to bind a list of DTOAppointmentForm
 * sent from an HTML file because Spring cannot directly bind a List
 * with @ModelAttribute.
 */
public class DTOAppointmentWrapper {
    private List<DTOAppointmentForm> appointments = new ArrayList<>();

    /**
     * @return the list of appointments
     */
    public List<DTOAppointmentForm> getAppointments() {
        return appointments;
    }

    /**
     * Initialize the list of appointments
     * @param appointments the list of appointments
     */
    public void setAppointments(List<DTOAppointmentForm> appointments) {
        this.appointments = appointments;
    }
}
