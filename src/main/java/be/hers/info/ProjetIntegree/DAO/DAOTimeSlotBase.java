package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.TimeSlotBase;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Leroy Rodriguez Ainhoa
 * @reviewer Nicolas Jean-Francois, Halet Louis
 */

public class DAOTimeSlotBase  extends DAO<TimeSlotBase> {


    /**
     * Searches for a TimeSlotBase by its numTimeSlot.
     * @param objectToSearchInDB the id of the TimeSlotBase to search for.
     * @return The TimeSlotBase if it's found, null otherwise.
     * @throws SQLException If an SQL error occurs with this method.
     */
    public TimeSlotBase find(int objectToSearchInDB)throws SQLException {
        TimeSlotBase timeSlotBase = null;
        String query = "SELECT * FROM TimeSlotBase WHERE numTimeSlot = ?";
        PreparedStatement prStat = null;
        ResultSet resultSet = null;

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            resultSet = prStat.executeQuery();

            if(resultSet.next()){
                LocalTime start = resultSet.getTimestamp("startTime").toLocalDateTime().toLocalTime();
                LocalTime duration = resultSet.getTimestamp("duration").toLocalDateTime().toLocalTime();
                timeSlotBase = new TimeSlotBase(objectToSearchInDB,start, duration, resultSet.getInt("dayNumber"));
            }
        }finally{
            closeStatementAndResultSet(prStat,resultSet);
        }

        return timeSlotBase;

    }

    /**
     *  Creates a list containing all TimeSlotBase objects.
     * @return a list containing all TimeSlotBase objects, or null if there are no TimeSlotBase objects.
     * @throws SQLException If an SQL error occurs with this method.
     */
    public List<TimeSlotBase> findAll() throws SQLException {
        List<TimeSlotBase> listTimeSlotBase = new ArrayList<>();
        String query = "SELECT * FROM TimeSlotBase";
        PreparedStatement prStat = null;
        ResultSet resultSet = null;

        try{
            prStat = connect.prepareStatement(query);
            resultSet = prStat.executeQuery();

            while(resultSet.next()){
                LocalTime start = resultSet.getTimestamp("startTime").toLocalDateTime().toLocalTime();
                LocalTime duration = resultSet.getTimestamp("duration").toLocalDateTime().toLocalTime();
                TimeSlotBase timeSlotBase = new TimeSlotBase(resultSet.getInt("numTimeSlot"),start,duration, resultSet.getInt("dayNumber"));
                listTimeSlotBase.add(timeSlotBase);
            }

        }finally{
            closeStatementAndResultSet(prStat,resultSet);
        }
        return listTimeSlotBase;

    }

    /**
     * Insert a new TimeSlotBase in the table.
     * @param objectToInsertInDB the TimeSlotBase we have to insert in the table.
     * @return true if the TimeSlotBase is inserted, else false.
     * @throws SQLException If an SQL error occurs with this method.
     */
    public boolean create(TimeSlotBase objectToInsertInDB) throws SQLException {
        boolean isInserted = false;
        String query = "INSERT INTO TimeSlotBase(startTime,duration,dayNumber)  VALUES (?,?,?)";
        OraclePreparedStatement prStat = null;
        ResultSet generateID = null;
        try{
            prStat = (OraclePreparedStatement)connect.prepareStatement(query);

            prStat.setTime(1, java.sql.Time.valueOf(objectToInsertInDB.getStartTime()));
            prStat.setTime(2, java.sql.Time.valueOf(objectToInsertInDB.getDuration()));
            prStat.setInt(3, objectToInsertInDB.getDayNumber());
            prStat.registerReturnParameter(4, OracleTypes.INTEGER);

            int nbLinesInsert = prStat.executeUpdate();
            if(nbLinesInsert > 0){
                generateID = prStat.getReturnResultSet();
                if(!generateID.next()) {
                    throw new SQLException("[DAOTimeSlotBase] Impossible de récupérer le numTimeSlot généré.");
                }

                int numTimeSlotGenerated = generateID.getInt(1);
                objectToInsertInDB.setNumTimeSlot(numTimeSlotGenerated);

                isInserted = true;
            }
        } finally {
            closeStatementAndResultSet(prStat,generateID);
        }

        return isInserted ;
    }

    /**
     * Updates a TimeSlotBase in the table.
     * @param objectToUpdateInDB the TimeSlotBase we have to update in the table.
     * @return true if the TimeSlotBase is updated, else false.
     * @throws SQLException If an SQL error occurs with this method.
     */
    public boolean update(TimeSlotBase objectToUpdateInDB) throws SQLException {

        boolean isUpdated = false;
        String query = "UPDATE TimeSlotBase SET startTime = ?, duration = ?, dayNumber = ? where numTimeSlot = ?";
        PreparedStatement prStat = null;

        try {
            prStat = connect.prepareStatement(query);

            prStat.setTime(1, java.sql.Time.valueOf(objectToUpdateInDB.getStartTime()));
            prStat.setTime(2, java.sql.Time.valueOf(objectToUpdateInDB.getDuration()));
            prStat.setInt(3, objectToUpdateInDB.getDayNumber());
            prStat.setInt(4, objectToUpdateInDB.getNumTimeSlot());

            int nbLinesUpdate = prStat.executeUpdate();
            if(nbLinesUpdate > 0){
                isUpdated = true;
            }
        }finally{
            closeStatement(prStat);
        }

        return isUpdated;
    }


    /**
     * Deletes a designated TimeSlotBase in the table.
     * @param objectToDeleteFormDB the TimeSlotBase we have to delete in the table.
     * @return true if the TimeSlotBase is deleted, else false.
     * @throws SQLException If an SQL error occurs with this method.
     */
    public boolean delete(TimeSlotBase objectToDeleteFormDB) throws SQLException {

        boolean isDeleted = false;
        String query = "DELETE FROM TimeSlotBase WHERE numTimeSlot = ?";
        PreparedStatement prStat = null;

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumTimeSlot());

            int nbLinesDelete = prStat.executeUpdate();
            if (nbLinesDelete > 0) {
                isDeleted = true;
            }

        } finally {
            closeStatement(prStat);
        }

        return isDeleted;
    }

}
