package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.POJO.Interpreter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vatafu Jean
 * @reviewer
 */
public class DAOBeneficiary extends DAO<Beneficiary> {

    @Override
    public Beneficiary find(int objectToSearchInDB) throws SQLException {
        Beneficiary beneficiary = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet =  null;

        String query = "SELECT numBeneficiary, firstName, lastName, phoneNumber, " +
                "emailAddress, hourQuota, educationLevel, communicationLanguage, FKnumInterpreter, FKAddress " +
                "FROM Beneficiary " +
                "WHERE numBeneficiary = ?";

        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, objectToSearchInDB);
            resultSet = preparedStatement.executeQuery();

            DAOInterpreter interpreterDAO = new DAOInterpreter();
            DAOAddress addressDAO = new DAOAddress();

            if(resultSet.next()) {
                Address address = addressDAO.find(resultSet.getInt("FKAddress"));
                Interpreter interpreter = interpreterDAO.find(resultSet.getInt("FKnumInterpreter"));

                String langStr = resultSet.getString("communicationLanguage");
                List<String> languages = new ArrayList<>();
                if(langStr != null && !langStr.isEmpty()) {
                    languages = Arrays.stream(langStr.split(",")).collect(Collectors.toList());
                }

                beneficiary = new Beneficiary(resultSet.getInt("numBeneficiary"), resultSet.getString("firstName"),
                        resultSet.getString("lastName"), resultSet.getString("phoneNumber"),
                        resultSet.getInt("hourQuota"), resultSet.getString("emailAddress"), address,
                        resultSet.getInt("educationLevel"), interpreter, languages);
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
        return beneficiary;
    }

    @Override
    public List<Beneficiary> findAll() throws SQLException {
        List<Beneficiary> listBeneficiary = new ArrayList<>();
        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "SELECT numBeneficiary, firstName, lastName, phoneNumber, " +
                "emailAddress, hourQuota, educationLevel, communicationLanguage, FKnumInterpreter, FKAddress " +
                "FROM Beneficiary";

        try {
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();

            DAOInterpreter interpreterDAO = new DAOInterpreter();
            DAOAddress addressDAO = new DAOAddress();

            while(rs.next()) {
                Address address = addressDAO.find(rs.getInt("FKAddress"));
                Interpreter interpreter = interpreterDAO.find(rs.getInt("FKnumInterpreter"));

                String langStr = rs.getString("communicationLanguage");
                List<String> languages = new ArrayList<>();
                if(langStr != null && !langStr.isEmpty()) {
                    languages = Arrays.stream(langStr.split(",")).collect(Collectors.toList());
                }

                Beneficiary beneficiary = new Beneficiary(rs.getInt("numBeneficiary"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("phoneNumber"),
                        rs.getInt("hourQuota"), rs.getString("emailAddress"), address,
                        rs.getInt("educationLevel"), interpreter, languages);

                listBeneficiary.add(beneficiary);
            }

        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return listBeneficiary;
    }

    @Override
    public boolean create(Beneficiary objectToInsertInDB) throws SQLException {
        boolean isCreated = false;
        String query = "INSERT INTO Beneficiary (numBeneficiary, firstName, " +
                "lastName, phoneNumber, emailAddress, hourQuota, educationLevel, " +
                "communicationLanguage, FKnumInterpreter, FKAddress) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement prStat = null;

        try {
            prStat = connect.prepareStatement(query);

            String communicationLanguage = "";
            if (objectToInsertInDB.getCommunicationLanguage() != null) {
                communicationLanguage = objectToInsertInDB.getCommunicationLanguage()
                        .stream()
                        .collect(Collectors.joining(","));
            }

            prStat.setInt(1, objectToInsertInDB.getNumBeneficiary());
            prStat.setString(2, objectToInsertInDB.getName());
            prStat.setString(3, objectToInsertInDB.getSurname());
            prStat.setString(4, objectToInsertInDB.getPhoneNumber());
            prStat.setString(5, objectToInsertInDB.getEmailAddress());
            prStat.setInt(6, objectToInsertInDB.getHourQuota());
            prStat.setInt(7, objectToInsertInDB.getEducationLevel());
            prStat.setString(8, communicationLanguage);
            prStat.setInt(9, objectToInsertInDB.getInterpreter().getNumInterpreter());
            prStat.setInt(10, objectToInsertInDB.getAddress().getNumAddress());

            if(prStat.executeUpdate() > 0) {
                isCreated = true;
            }

        } finally {
            if(prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isCreated;
    }

    @Override
    public boolean update(Beneficiary objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        PreparedStatement preparedStatement = null;

        String query = "UPDATE Beneficiary SET firstName = ?, lastName = ?, phoneNumber = ?, emailAddress = ?, " +
                "hourQuota = ?, educationLevel = ?, communicationLanguage = ?, FKnumInterpreter = ?, FKAddress = ? " +
                "WHERE numBeneficiary = ?";

        try {
            preparedStatement = connect.prepareStatement(query);

            String communicationLanguage = "";
            if (objectToUpdateInDB.getCommunicationLanguage() != null) {
                communicationLanguage = objectToUpdateInDB.getCommunicationLanguage()
                        .stream()
                        .collect(Collectors.joining(","));
            }

            preparedStatement.setString(1, objectToUpdateInDB.getName());
            preparedStatement.setString(2, objectToUpdateInDB.getSurname());
            preparedStatement.setString(3, objectToUpdateInDB.getPhoneNumber());
            preparedStatement.setString(4, objectToUpdateInDB.getEmailAddress());
            preparedStatement.setInt(5, objectToUpdateInDB.getHourQuota());
            preparedStatement.setInt(6, objectToUpdateInDB.getEducationLevel());
            preparedStatement.setString(7, communicationLanguage);
            preparedStatement.setInt(8, objectToUpdateInDB.getInterpreter().getNumInterpreter());
            preparedStatement.setInt(9, objectToUpdateInDB.getAddress().getNumAddress());
            preparedStatement.setInt(10, objectToUpdateInDB.getNumBeneficiary());

            if(preparedStatement.executeUpdate() > 0) {
                isUpdated = true;
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
        return isUpdated;
    }

    @Override
    public boolean delete(Beneficiary objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        PreparedStatement preparedStatement = null;

        String query = "DELETE FROM Beneficiary WHERE numBeneficiary = ?";

        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, objectToDeleteFormDB.getNumBeneficiary());

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