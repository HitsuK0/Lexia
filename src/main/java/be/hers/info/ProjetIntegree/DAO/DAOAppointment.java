package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.Appointment;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.POJO.TimeSlotBase;
import oracle.jdbc.internal.OraclePreparedStatement;
import oracle.jdbc.internal.OracleTypes;

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

    @Override
    public boolean create(Appointment objectToInsertInDB) throws SQLException {

        boolean isInserted = false;
        OraclePreparedStatement prStat = null;
        ResultSet rs = null;
        String local = null;
        DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
        DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
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
                        addInterpreterAtAppointment(i.getNumInterpreter(), id);
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
    }

    @Override
    public boolean delete(Appointment objectToDeleteFormDB) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Appointment objectToUpdateInDB) throws SQLException {
        return false;
    }

    // Dans ce DAO
    //findListInterpreterForAppointement(int numAppointment) Table RDVInterpreter
    //findListAcademicSkillsNeeded(int numAppointment) Table RequiredAcademicSkill
        }
