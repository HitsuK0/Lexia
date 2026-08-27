package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Vanderheyden Quentin
 * @reviewer Nicolas Jean-François, Halet Louis
 */
public class DAOTimeSlotPunctual extends DAO<TimeSlotPunctual> {

    /**
     * Search for a TimeSlotPunctual with the same id as objectToSearchInDB
     *
     * @param objectToSearchInDB is the id of the TimeSlotPunctual to search
     * @return null if nothing was find, else return a TimeSlotPuntual
     * @throws SQLException if an errors occurs during the database request
     */
    @Override
    public TimeSlotPunctual find(int objectToSearchInDB) throws SQLException {
        TimeSlotPunctual timeSlotPunctual = null;
        String query = "SELECT * " +
                "FROM TimeSlotPunctual " +
                "WHERE numTimeSlot = ?";

        PreparedStatement prStat = null;
        ResultSet rs = null;
        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();
            if (rs.next()) {
                timeSlotPunctual = new TimeSlotPunctual(
                        rs.getInt("numTimeSlot"),
                        rs.getTimestamp("startTime").toLocalDateTime().toLocalTime(),
                        rs.getTimestamp("duration").toLocalDateTime().toLocalTime(),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getDate("endDate").toLocalDate()
                );
            }
        } finally {
            closeStatementAndResultSet(prStat, rs);
        }
        return timeSlotPunctual;
    }

    /**
     * Search for all the TimeSlotPunctual
     *
     * @return an empty list if there were no TimeSlotPuncutal, else a list with all of them
     * @throws SQLException if an errors occurs during the database request
     */
    @Override
    public List<TimeSlotPunctual> findAll() throws SQLException {
        String query = "SELECT * " +
                "FROM TimeSlotPunctual";
        ArrayList<TimeSlotPunctual> timeSlotPunctuals = new ArrayList<>();
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try {
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();
            while (rs.next()) {
                TimeSlotPunctual timeSlotPunctual = new TimeSlotPunctual(
                        rs.getInt("numTimeSlot"),
                        rs.getTimestamp("startTime").toLocalDateTime().toLocalTime(),
                        rs.getTimestamp("duration").toLocalDateTime().toLocalTime(),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getDate("endDate").toLocalDate()
                );
                timeSlotPunctuals.add(timeSlotPunctual);
            }
        } finally {
            closeStatementAndResultSet(prStat, rs);
        }
        return timeSlotPunctuals;
    }

    /**
     * Insert a TimeSlotPunctual in the DB
     * The startTime is inserted with the StartDate in it.
     * The duration is registered with the StartDate in it.
     * The StartDate is inserted with the startTime in it.
     * The End Date is registered with 0 : 0 for the time.
     *
     * @param objectToInsertInDB is the object TimeSlotPunctual to insert
     * @return true if the insert did well, else false
     * @throws SQLException if an errors occurs during the database request
     */
    @Override
    public boolean create(TimeSlotPunctual objectToInsertInDB) throws SQLException {
        boolean isInserted = false;
        String query = "INSERT INTO TimeSlotPunctual(startTime, duration, startDate, endDate) " +
                "VALUES (?, ?, ?, ?)" +
                "RETURNING numTimeSlot";
        PreparedStatement prStat = null;
        ResultSet generateID = null;

        try {
            prStat = connect.prepareStatement(query);
            prStat.setTimestamp(1, Timestamp.valueOf(objectToInsertInDB.getStartTime().atDate(objectToInsertInDB.getStartDate())));
            prStat.setTimestamp(2, Timestamp.valueOf(objectToInsertInDB.getDuration().atDate(objectToInsertInDB.getStartDate())));
            prStat.setTimestamp(3, Timestamp.valueOf(objectToInsertInDB.getStartDate().atTime(objectToInsertInDB.getStartTime())));
            prStat.setTimestamp(4, Timestamp.valueOf(objectToInsertInDB.getEndDate().atTime(0, 0)));
            generateID = prStat.executeQuery();
            if (generateID.next()) {
                objectToInsertInDB.setNumTimeSlot(generateID.getInt(1));

                isInserted = true;

            }
        } finally {
            closeStatementAndResultSet(prStat, generateID);
        }
        return isInserted;
    }

    /**
     * Update all the field where numTimeSlot = objectToUpdateInDB.get
     *
     * @param objectToUpdateInDB is the object
     * @return true if the lines were updated else false
     * @throws SQLException if an errors occurs during the database request
     */
    @Override
    public boolean update(TimeSlotPunctual objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        String query = "UPDATE TimeSlotPunctual SET startTime = ?, duration = ?, " +
                "startDate = ?, endDate = ? " +
                "WHERE numTimeSlot = ?";
        PreparedStatement prStat = null;
        try {
            prStat = connect.prepareStatement(query);
            prStat.setDate(1, Date.valueOf(objectToUpdateInDB.getStartTime().atDate(objectToUpdateInDB.getStartDate()).toLocalDate()));
            prStat.setDate(2, Date.valueOf(objectToUpdateInDB.getDuration().atDate(objectToUpdateInDB.getStartDate()).toLocalDate()));
            prStat.setDate(3, Date.valueOf(objectToUpdateInDB.getStartDate()));
            prStat.setDate(4, Date.valueOf(objectToUpdateInDB.getEndDate()));
            prStat.setInt(5, objectToUpdateInDB.getNumTimeSlot());
            int nbreLine = prStat.executeUpdate();
            if (nbreLine > 0) {
                isUpdated = true;
            }
        } finally {
            closeStatement(prStat);
        }
        return isUpdated;
    }

    /**
     * Delete the lines where numTimeSlot == objectToDeleteForm.getNumTimeSlot()
     *
     * @param objectToDeleteFormDB is the TimeSlotPunctual to delete in the database
     * @return true if the lines were deleted, false otherwise
     * @throws SQLException if an errors occurs in the database request
     */
    @Override
    public boolean delete(TimeSlotPunctual objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        String query = "DELETE FROM TimeSlotPunctual " +
                "WHERE numTimeSlot = ?";
        PreparedStatement prStat = null;
        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumTimeSlot());
            int nbreLine = prStat.executeUpdate();
            if (nbreLine > 0) {
                isDeleted = true;
            }
        } finally {
            closeStatement(prStat);
        }
        return isDeleted;
    }
}
