package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DAOTimeSlotPunctual extends DAO<TimeSlotPunctual> {

    public TimeSlotPunctual find(int objectToSearchInDB) throws SQLException{
        TimeSlotPunctual timeSlotPunctual = null;
        String query = "SELECT * FROM TimeSlotPunctual WHERE numTimeSlot = ?";

        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = ConnectionOracle.getInstance().prepareStatement;
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();
            if(rs.next()){
                timeSlotPunctual = new TimeSlotPunctual(
                        rs.getInt("numTimeSlot"),
                        rs.getTimestamp("startTime").toLocalDateTime().toLocalTime(),
                        rs.getTimestamp("duration").toLocalDateTime().toLocalTime(),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getDate("endDate").toLocalDate()
                );
            }
        }
        finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return timeSlotPunctual;
    }


    public List<TimeSlotPunctual> findAll() throws SQLException{
        String query = "SELECT * FROM TimeSlotPunctual";
        ArrayList<TimeSlotPunctual> timeSlotPunctuals = new ArrayList<>();
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = ConnectionOracle.getInstance().prepareStatement;
            rs = prStat.executeQuery();
            if(rs.next()){
                TimeSlotPunctual timeSlotPunctual = new TimeSlotPunctual(
                        rs.getInt("numTimeSlot"),
                        rs.getTimestamp("startTime").toLocalDateTime().toLocalTime(),
                        rs.getTimestamp("duration").toLocalDateTime().toLocalTime(),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getDate("endDate").toLocalDate()
                );
                timeSlotPunctuals.add(timeSlotPunctual);
            }
        }
        finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return timeSlotPunctuals;
    }


    public boolean create(TimeSlotPunctual objectToInsertInDB) throws SQLException{
        boolean isInserted = false;
        String query = "INSERT INTO TimeSlotPunctual(startTime, duration, startDate, endDate) " +
                        "VALUES (?, ?, ?, ?)";
        PreparedStatement prStat = null;

        try{
            prStat = ConnectionOracle.getInstance().prepareStatement(query);
            prStat.setDate(1, Date.valueOf(objectToInsertInDB.getStartTime().atDate(objectToInsertInDB.getStartDate()).toLocalDate()));
            prStat.setDate(2, Date.valueOf(objectToInsertInDB.getDuration().atDate(objectToInsertInDB.getStartDate()).toLocalDate())); // On ajoute le jour de début pour pouvoir convertir en date
            prStat.setDate(3, Date.valueOf(objectToInsertInDB.getStartDate()));
            prStat.setDate(4, Date.valueOf(objectToInsertInDB.getEndDate()));
            int nbreLine = prStat.executeUpdate();
            if(nbreLine > 0){
                isInserted = true;
            }
        }
        finally{
            if (prStat != null) {
                try {
                    prStat.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isInserted;
    }


    public boolean update(TimeSlotPunctual objectToUpdateInDB) throws SQLException{
        boolean isUpdated = false;
        String query = "UPDATE TimeSlotPunctual SET startTime = ?, duration = ?, " +
                        "startDate = ?, endDate = ? " +
                        "WHERE numTimeSlot = ?";
        PreparedStatement prStat = null;
        try{
            prStat = ConnectionOracle.getInstance().prepareStatement(query);
            prStat.setDate(1, Date.valueOf(objectToUpdateInDB.getStartTime().atDate(objectToUpdateInDB.getStartDate()).toLocalDate()));
            prStat.setDate(2, Date.valueOf(objectToUpdateInDB.getDuration().atDate(objectToUpdateInDB.getStartDate()).toLocalDate()));
            prStat.setDate(3, Date.valueOf(objectToUpdateInDB.getStartDate()));
            prStat.setDate(4, Date.valueOf(objectToUpdateInDB.getEndDate()));
            int nbreLine = prStat.executeUpdate();
            if(nbreLine > 0){
                isUpdated = true;
            }
        }
        finally{
            if (prStat != null) {
                try {
                    prStat.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return isUpdated;
    }


    public boolean delete(TimeSlotPunctual objectToDeleteFormDB) throws SQLException{
        boolean isDeleted = false;
        String query = "DELETE FROM TimeSlotPunctual WHERE numTimeSlot = ?";
        PreparedStatement prStat = null;
        try{
            prStat = ConnectionOracle.getInstance().prepareStatement();
            prStat.setInt(1, objectToDeleteFormDB.getNumTimeSlot());
            int nbreLine = prStat.executeUpdate();
            if(nbreLine > 0){
                isDeleted = true;
            }
        }
        finally{
            if (prStat != null) {
                try {
                    prStat.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isDeleted;
    }
}
