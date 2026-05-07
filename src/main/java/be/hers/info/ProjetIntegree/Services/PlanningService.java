package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAppointment;
import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.Appointment;
import be.hers.info.ProjetIntegree.POJO.Interpreter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanningService {
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
}
