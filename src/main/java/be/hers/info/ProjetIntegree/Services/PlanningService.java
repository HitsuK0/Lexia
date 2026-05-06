package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAppointment;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.POJO.Appointment;
import be.hers.info.ProjetIntegree.POJO.Interpreter;

import java.time.LocalDate;
import java.util.List;

public class PlanningService {
    public List<Appointment> getListAppointmentWithDateAndInterpreter(Interpreter inter, LocalDate date){
        DAOAppointment daoAppointment = new DAOAppointment();




    }
}
