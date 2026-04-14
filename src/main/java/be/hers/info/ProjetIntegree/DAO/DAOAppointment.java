package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.*;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;

public class DAOAppointment extends DAO<Appointment> {
    @Override
    public Appointment find(int idToSearchInDB) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        Appointment appointment = null;
        String query = "SELECT numAppointment,status,local,FKnumBeneficiary " +
                "FROM Appointment " +
                "WHERE numAppointment = ?";
        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, idToSearchInDB);
            resultSet = prStat.executeQuery();
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();

            if (resultSet.next()) {
                String local = resultSet.getString("local");
                List<String> listLocal = null;
                if (local != null) {
                    listLocal = Arrays.asList(local.split(","));

                }
                int numAppointment = resultSet.getInt("numAppointment");

                appointment = new Appointment(
                        numAppointment,
                        resultSet.getString("status"),
                        listLocal,
                        daoBeneficiary.find(resultSet.getInt("FKnumBeneficiary")));
            }
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return appointment;
    }

    @Override
    public List<Appointment> findAll() throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Appointment> appointmentList = new ArrayList<>();
        String query = "SELECT numAppointment,status,local,FKnumBeneficiary " +
                "FROM Appointment";
        try {
            prStat = connect.prepareStatement(query);
            resultSet = prStat.executeQuery();
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            while(resultSet.next()){
                String local = resultSet.getString("local");
                List<String> listLocal = null;
                if (local != null) {
                    listLocal = Arrays.asList(local.split(","));

                }
                int numAppointment = resultSet.getInt("numAppointment");
                appointmentList.add(new Appointment(
                        numAppointment,
                        resultSet.getString("status"),
                        listLocal,
                        daoBeneficiary.find(resultSet.getInt("FKnumBeneficiary"))));
            }
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return appointmentList;
    }
    //Precondition : timeslot existe dans la bd

    @Override
    public boolean create(Appointment objectToInsertInDB) throws SQLException {

        boolean isInserted = false;
        OraclePreparedStatement prStat = null;
        ResultSet rs = null;
        String local = null;

        if (objectToInsertInDB.getAppointmentLocals() != null){
            local = String.join(",", objectToInsertInDB.getAppointmentLocals());
        }
        String query = "INSERT INTO Appointment (status, local, FKnumEtablishment, FKnumBeneficiary, FKTimeSlotPunctual) " +
                "VALUES (?, ?, ?, ?, ?)";
        if(objectToInsertInDB.getTimeSlot() instanceof TimeSlotBase){
            query = "INSERT INTO Appointment (status, local, FKnumEtablishment, FKnumBeneficiary, FKTimeSlotBase) " +
                    "VALUES (?, ?, ?, ?, ?) returning numAppointment into ?";
            try{
                prStat = (OraclePreparedStatement)connect.prepareStatement(query);


                prStat.setString(1,objectToInsertInDB.getStatus());
                prStat.setString(2,local);
                prStat.setInt(3,objectToInsertInDB.getEtablishment().getNumEstablishment());
                prStat.setInt(4,objectToInsertInDB.getBeneficiary().getNumBeneficiary());
                prStat.setInt(5,objectToInsertInDB.getTimeSlot().getNumTimeSlot());
                prStat.registerReturnParameter(6, OracleTypes.INTEGER);
                int nbLinesInsert = prStat.executeUpdate();
                rs = prStat.getReturnResultSet();
                int id = rs.getInt(6);
                objectToInsertInDB.setNumAppointment(id);

                if(nbLinesInsert > 0) {
                    isInserted = true;
                }
                if(!objectToInsertInDB.getInterpreters().isEmpty()){
                    for(Interpreter i : objectToInsertInDB.getInterpreters()){
                        if(!addInterpreterAtAppointment(id, i.getNumInterpreter()))
                            throw new SQLException("[DAOAppointment] erreur lors de l'ajout dans la table RDVInterpreter");
                    }
                }
                if(!objectToInsertInDB.getAcademicSkillsNeeded().isEmpty()){
                    for(AcademicSkill a : objectToInsertInDB.getAcademicSkillsNeeded()){

                        if(!addAcademicSkillAtAppointment(id, a.getNumAcademicSkill()))
                            throw new SQLException("[DAOAppointment] erreur lors de l'ajout dans la table RequiredAcademicSkill");
                    }
                }
                if(!objectToInsertInDB.getProfessionalSkillsNeeded().isEmpty()){
                    for(ProfessionalSkill p : objectToInsertInDB.getProfessionalSkillsNeeded()){

                        if(!addProfessionalSkillAtAppointment(id, p.getNumProfessionalSkill()))
                            throw new SQLException("[DAOAppointment] erreur lors de l'ajout dans la table RequiredProfessionalSkill");
                    }
                }
            }finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if (prStat != null) {
                    try {
                        prStat.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return isInserted;
    }
    public boolean addInterpreterAtAppointment(int numAppointment, int numInterpreter) throws SQLException {
        boolean isInserted = false;
        PreparedStatement prStat = null;
        String query = "INSERT INTO RDVInterpreter (numAppointment, numInterpreter) " +
                "VALUES (?, ?)";
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1,numAppointment);
            prStat.setInt(2,numInterpreter);
            int nbLinesInsert = prStat.executeUpdate();
            if(nbLinesInsert > 0) {
                isInserted = true;
            }
        }finally {
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return isInserted;
    }
    public boolean addAcademicSkillAtAppointment(int numAppointment, int numAcademicSkill) throws SQLException {
        boolean isInserted = false;
        PreparedStatement prStat = null;
        String query = "INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) " +
                "VALUES (?, ?)";
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1,numAppointment);
            prStat.setInt(2,numAcademicSkill);
            int nbLinesInsert = prStat.executeUpdate();
            if(nbLinesInsert > 0) {
                isInserted = true;
            }
        }finally {
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return isInserted;
    }
    public boolean addProfessionalSkillAtAppointment(int numAppointment, int numProfessionalSkill) throws SQLException {
        boolean isInserted = false;
        PreparedStatement prStat = null;
        String query = "INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) " +
                "VALUES (?, ?)";
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1,numAppointment);
            prStat.setInt(2,numProfessionalSkill);
            int nbLinesInsert = prStat.executeUpdate();
            if(nbLinesInsert > 0) {
                isInserted = true;
            }
        }finally {
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return isInserted;
    }

    @Override
    public boolean delete(Appointment objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        PreparedStatement prStat = null;

        String query = "DELETE FROM Appointment " +
                "WHERE numAppointment = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumAppointment());

            int nbLinesDelete = prStat.executeUpdate();
            if(nbLinesDelete > 0) {
                isDeleted = true;
            }
        } finally {
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return isDeleted;
    }

    @Override
    public boolean update(Appointment objectToUpdateInDB) throws SQLException {
        return false;
    }

    // Dans ce DAO
    //findListInterpreterForAppointement(int numAppointment) Table RDVInterpreter
    //findListAcademicSkillsNeeded(int numAppointment) Table RequiredAcademicSkill
        }
