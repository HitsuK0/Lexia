package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOAbsence extends DAO<Absence> {

    @Override
    public Absence find(int objectToSearchInDB) throws SQLException {
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

            return absence;
        }
    }

    @Override
    public List<Absence> findAll() throws SQLException {
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

            return absenceList;
        }
    }

    @Override
    public boolean create(Absence objectToInsertInDB) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Absence objectToUpdateInDB) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Absence objectToDeleteFormDB) throws SQLException {
        return false;
    }
}
