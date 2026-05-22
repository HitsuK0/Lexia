package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAppointment;
import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.Appointment;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.POJO.Interpreter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanningService {
    /**
     * Searches for all Appointments belonging to the interpreter as a parameter over a period defined by start and end.
     * @param inter The interpreter linked to the appointment on the list
     * @param start the date retrieved via the URL
     * @param end the date retrieved via the URL
     * @return The appointment list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Appointment> getListAppointmentWithDateAndInterpreter(Interpreter inter, String start, String end){
        DAOAppointment daoAppointment = new DAOAppointment();
        List<Appointment> list =new ArrayList<>();
        try{
            list = daoAppointment.findAllAppointmentToInterpreterAndDate(inter, start,end);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Searches for all Absences belonging to the interpreter as a parameter over a period defined by start and end.
     * @param inter The interpreter linked to the appointment on the list
     * @param start the date retrieved via the URL
     * @param end the date retrieved via the URL
     * @return The Absences list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Absence> getListAbsenceWithDateAndInterpreter(Interpreter inter, String start, String end){
        DAOAppointment daoAppointment = new DAOAppointment();
        List<Absence> list =new ArrayList<>();
        try{
            list = daoAppointment.findAllAbsenceToInterpreterAndDate(inter, start,end);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Searches for all Appointments belonging to the beneficiary as a parameter over a period defined by start and end.
     * @param numBeneficiary The beneficiary linked to the appointment on the list
     * @param start the date retrieved via the URL
     * @param end the date retrieved via the URL
     * @return The Absences list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Appointment> getListAppointmentsToBeneficiaryAndDate(int numBeneficiary, String start, String end){
        DAOAppointment daoAppointment = new DAOAppointment();
        List<Appointment> list = new ArrayList<>();
        try{
            list = daoAppointment.findAllAppointmentToBeneficiaryAndDate(numBeneficiary, start,end);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }


    public List<Appointment> findPlanningForBeneficiary(Beneficiary beneficiary) throws SQLException {
        return new DAOAppointment().findAllAppointmentsForBeneficiaryPlanning(beneficiary.getNumBeneficiary());
    }
}
