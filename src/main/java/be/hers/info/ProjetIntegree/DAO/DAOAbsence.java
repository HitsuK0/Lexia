package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vatafu Jean
 * @reviewer
 */

public class DAOAbsence {

    /**
     *
     * @throws BadStatusException if the absence found has an invalid status
     */
    public Absence find(int objectToSearchInDB) throws SQLException, BadStatusException {
        Connection connect = ConnectionOracle.getInstance();
        Absence absence = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT status, FKTimeSlotPunctual " +
                "FROM Absence " +
                "WHERE numAbsence = ?";

        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, objectToSearchInDB);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                TimeSlotPunctual timeSlotPunctual = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));

                absence = new Absence(objectToSearchInDB, resultSet.getString("status"),timeSlotPunctual);
            }
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return absence;
    }

    /**
     *
     * @throws BadStatusException if the absence found has an invalid status
     */
    public List<Absence> findAll() throws SQLException, BadStatusException {
        Connection connect = ConnectionOracle.getInstance();
        List<Absence> absenceList = new ArrayList<Absence>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT numAbsence, status, FKTimeSlotPunctual " +
                "FROM Absence";

        try {
            preparedStatement = connect.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();

            while(resultSet.next()) {
                Absence absence = new Absence(resultSet.getInt("numAbsence"), resultSet.getString("status"),
                        daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual")));

                absenceList.add(absence);
            }
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return absenceList;
    }

    /**
     *
     * @param otherObjectID The id of the Interpreter that this absence refers to
     * @throws IllegalArgumentException If otherObjectID is negative
     */
    public boolean create(Absence objectToInsertInDB, int otherObjectID) throws SQLException, IllegalArgumentException {
        Connection connect = ConnectionOracle.getInstance();
        boolean created = false;
        PreparedStatement preparedStatement = null;

        if(otherObjectID < 0) {
            throw new IllegalArgumentException("[DAOAbsence] L'id de l'interprète ne peut pas être négatif.");
        }

        String query = "INSERT INTO Absence(, status, FKTimeSlotPunctual, FKnumInterpreter) " +
                "VALUES ( ?, ?, ?)";

        try {
            preparedStatement = connect.prepareStatement(query);

            preparedStatement.setString(1, objectToInsertInDB.getStatus());
            preparedStatement.setInt(2, objectToInsertInDB.getTimeSlotPunctual().getNumTimeSlot());
            preparedStatement.setInt(3, otherObjectID);

            if(preparedStatement.executeUpdate() > 0) {
                created = true;
            }
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return created;
    }

    public boolean update(Absence objectToUpdateInDB) throws SQLException {
        Connection connect = ConnectionOracle.getInstance();
        boolean updated = false;
        PreparedStatement preparedStatement = null;

        String query = "UPDATE Absence " +
                "SET status = ?, FKTimeSlotPunctual = ? " +
                "WHERE numAbsence = ?";

        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, objectToUpdateInDB.getStatus());
            preparedStatement.setInt(2, objectToUpdateInDB.getTimeSlotPunctual().getNumTimeSlot());
            preparedStatement.setInt(3, objectToUpdateInDB.getNumAbsence());

            if(preparedStatement.executeUpdate() > 0) {
                updated = true;
            }
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                }  catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return updated;
    }

    public boolean delete(Absence objectToDeleteFormDB) throws SQLException {
        Connection connect = ConnectionOracle.getInstance();
        boolean isDeleted = false;
        PreparedStatement preparedStatement = null;

        String query = "DELETE FROM Absence " +
                "WHERE numAbsence = ?";

        try {
            preparedStatement = connect.prepareStatement(query);

            preparedStatement.setInt(1, objectToDeleteFormDB.getNumAbsence());

            if(preparedStatement.executeUpdate() > 0) {
                isDeleted = true;
            }
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isDeleted;
    }
}
