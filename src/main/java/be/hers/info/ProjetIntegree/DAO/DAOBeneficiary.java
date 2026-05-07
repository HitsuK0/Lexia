package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.*;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vatafu Jean
 * @reviewer Wellinger Chloe, Nicolas Jean-François, Halet Louis
 */
public class DAOBeneficiary extends DAO<Beneficiary> {

    /**
     * Searches for a Beneficiary with its id
     * The Address is loaded via DAOAddress
     * The Interpreter of reference is loaded via DAOInterpreter
     *  Appointments are not loaded
     * @param objectToSearchInDB the id to search for
     * @return The Beneficiary if found, null otherwise
     * @throws SQLException In case of any SQL problems encountered with this method
     */
    @Override
    public Beneficiary find(int objectToSearchInDB) throws SQLException {
        Beneficiary beneficiary = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet =  null;
        String query = "SELECT numBeneficiary, login, password, firstName, lastName, phoneNumber, " +
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

                beneficiary = new Beneficiary(resultSet.getInt("numBeneficiary"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getString("lastName"),
                        resultSet.getString("firstName"), resultSet.getString("phoneNumber"),
                        resultSet.getString("emailAddress"), address, resultSet.getInt("hourQuota"),
                        resultSet.getInt("educationLevel"), interpreter, languages);
            }
        } finally {
            closeStatementAndResultSet(preparedStatement, resultSet);
        }
        return beneficiary;
    }

    /**
     * Creates a list containing all the Beneficiaries in the Beneficiary table
     * For each Beneficiary, the Address is loaded via DAOAddress and the Interpreter
     * of reference is loaded via DAOInterpreter
     * Appointments are not loaded
     * @return A list containing all the Beneficiaries, an empty list if the table is empty
     * @throws SQLException In case of any SQL problems encountered with this method
     */
    @Override
    public List<Beneficiary> findAll() throws SQLException {
        List<Beneficiary> listBeneficiary = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT numBeneficiary, login, password, firstName, lastName, phoneNumber, " +
                "emailAddress, hourQuota, educationLevel, communicationLanguage, FKnumInterpreter, FKAddress " +
                "FROM Beneficiary";

        try {
            preparedStatement = connect.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            DAOInterpreter interpreterDAO = new DAOInterpreter();
            DAOAddress addressDAO = new DAOAddress();

            while(resultSet.next()) {
                Address address = addressDAO.find(resultSet.getInt("FKAddress"));
                Interpreter interpreter = interpreterDAO.find(resultSet.getInt("FKnumInterpreter"));

                String langStr = resultSet.getString("communicationLanguage");
                List<String> languages = new ArrayList<>();
                if(langStr != null && !langStr.isEmpty()) {
                    languages = Arrays.stream(langStr.split(",")).collect(Collectors.toList());
                }

                Beneficiary beneficiary = new Beneficiary(resultSet.getInt("numBeneficiary"), resultSet.getString("login"),
                        resultSet.getString("password"), resultSet.getString("lastName"),
                        resultSet.getString("firstName"), resultSet.getString("phoneNumber"),
                        resultSet.getString("emailAddress"), address, resultSet.getInt("hourQuota"),
                        resultSet.getInt("educationLevel"), interpreter, languages);

                listBeneficiary.add(beneficiary);
            }
        } finally {
            closeStatementAndResultSet(preparedStatement, resultSet);
        }
        return listBeneficiary;
    }

    /**
     * Adds the Beneficiary passed as a parameter to the database
     * If the Beneficiary contains a list of Appointments, these are also inserted into the Appointment table
     * For each inserted Appointment, its associated interpreters are linked via the RDVInterpreter junction table
     * The numBeneficiary and numAppointment identifiers are auto-generated by Oracle and retrieved
     * Precondition: the Beneficiary passed as a parameter cannot be null
     * Precondition: the Address of the Beneficiary must already exist in the database
     * Precondition: the reference Interpreter of the Beneficiary, if any, must already exist
     * Precondition: for each Appointment, the associated Establishment and TimeSlot must already exist
     * Precondition: for each Appointment, all Interpreters in its list must already exist
     * @param objectToInsertInDB the Beneficiary to insert, with its optional appointments
     * @return true if the Beneficiary was successfully inserted, false otherwise
     * @throws SQLException In case of any SQL constraints violations or connection issues
     */
    @Override
    public boolean create(Beneficiary objectToInsertInDB) throws SQLException {
        boolean isCreated = false;
        OraclePreparedStatement preparedStatementBeneficiary = null;
        OraclePreparedStatement preparedStatementAppointment = null;
        PreparedStatement preparedStatementInterpreter = null;
        ResultSet generateBeneficiaryID = null;
        ResultSet generateAppointmentID = null;

        String queryBeneficiary = "INSERT INTO Beneficiary (login, password, firstName, " +
                "lastName, phoneNumber, emailAddress, hourQuota, educationLevel, " +
                "communicationLanguage, FKnumInterpreter, FKAddress) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING numBeneficiary INTO ?";

        String queryAppointment = "INSERT INTO Appointment (status, local, " +
                "FKnumEtablishment, FKnumBeneficiary, FKTimeSlotBase, FKTimeSlotPunctual) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "RETURNING numAppointment INTO ?";

        String queryInterpreter = "INSERT INTO RDVInterpreter (numAppointment, numInterpreter) " +
                "VALUES (?, ?)";

        try {
            preparedStatementBeneficiary = (OraclePreparedStatement) connect.prepareStatement(queryBeneficiary);

            String communicationLanguage = "";
            if (objectToInsertInDB.getCommunicationLanguage() != null) {
                communicationLanguage = objectToInsertInDB.getCommunicationLanguage()
                        .stream()
                        .collect(Collectors.joining(","));
            }

            preparedStatementBeneficiary.setString(1, objectToInsertInDB.getLogin());
            preparedStatementBeneficiary.setString(2, objectToInsertInDB.getPassword());
            preparedStatementBeneficiary.setString(3, objectToInsertInDB.getFirstName());
            preparedStatementBeneficiary.setString(4, objectToInsertInDB.getLastName());
            preparedStatementBeneficiary.setString(5, objectToInsertInDB.getPhoneNumber());
            preparedStatementBeneficiary.setString(6, objectToInsertInDB.getEmailAddress());
            preparedStatementBeneficiary.setInt(7, objectToInsertInDB.getHourQuota());
            preparedStatementBeneficiary.setInt(8, objectToInsertInDB.getEducationLevel());
            preparedStatementBeneficiary.setString(9, communicationLanguage);

            if (objectToInsertInDB.getInterpreter() != null) {
                preparedStatementBeneficiary.setInt(10, objectToInsertInDB.getInterpreter().getNumInterpreter());
            } else {
                preparedStatementBeneficiary.setNull(10, java.sql.Types.INTEGER);
            }

            preparedStatementBeneficiary.setInt(11, objectToInsertInDB.getAddress().getNumAddress());
            preparedStatementBeneficiary.registerReturnParameter(12, OracleTypes.INTEGER);

            if(preparedStatementBeneficiary.executeUpdate() > 0) {
                generateBeneficiaryID = preparedStatementBeneficiary.getReturnResultSet();

                if(!generateBeneficiaryID.next()) {
                    throw new SQLException("[DAOBeneficiary] Impossible de récupérer le numBeneficiary généré.");
                }

                int numBeneficiaryGenerated = generateBeneficiaryID.getInt(1);
                objectToInsertInDB.setNumBeneficiary(numBeneficiaryGenerated);

                if (objectToInsertInDB.getAppointmentList() != null && !objectToInsertInDB.getAppointmentList().isEmpty()) {
                    preparedStatementAppointment = (OraclePreparedStatement) connect.prepareStatement(queryAppointment);

                    preparedStatementInterpreter = connect.prepareStatement(queryInterpreter);

                    for (Appointment appt : objectToInsertInDB.getAppointmentList()) {
                        preparedStatementAppointment.setString(1, appt.getStatus());

                        if (appt.getAppointmentLocals() != null && !appt.getAppointmentLocals().isEmpty()) {
                            preparedStatementAppointment.setString(2, String.join(",", appt.getAppointmentLocals()));
                        } else {
                            /*
                             * Sets to 'null' in the database in case there's no locals for this Appointment,
                             * same thing for every java.sql.Types. ...
                             */
                            preparedStatementAppointment.setNull(2, java.sql.Types.VARCHAR);
                        }

                        preparedStatementAppointment.setInt(3, appt.getEstablishment().getNumEstablishment());
                        preparedStatementAppointment.setInt(4, numBeneficiaryGenerated);

                        if (appt.getTimeSlot() != null) {
                            if (appt.getTimeSlot() instanceof TimeSlotBase) {
                                preparedStatementAppointment.setInt(5, appt.getTimeSlot().getNumTimeSlot());
                                preparedStatementAppointment.setNull(6, java.sql.Types.INTEGER);
                            } else if (appt.getTimeSlot() instanceof TimeSlotPunctual) {
                                preparedStatementAppointment.setNull(5, java.sql.Types.INTEGER);
                                preparedStatementAppointment.setInt(6, appt.getTimeSlot().getNumTimeSlot());
                            }
                        } else {
                            preparedStatementAppointment.setNull(5, java.sql.Types.INTEGER);
                            preparedStatementAppointment.setNull(6, java.sql.Types.INTEGER);
                        }

                        preparedStatementAppointment.registerReturnParameter(7, OracleTypes.INTEGER);

                        if (preparedStatementAppointment.executeUpdate() > 0) {
                            generateAppointmentID = preparedStatementAppointment.getReturnResultSet();
                            if (generateAppointmentID.next()) {

                                int numAppointmentGenerated = generateAppointmentID.getInt(1);
                                appt.setNumAppointment(numAppointmentGenerated);

                                if (appt.getInterpreters() != null && !appt.getInterpreters().isEmpty()) {
                                    for (Interpreter interpreter : appt.getInterpreters()) {
                                        preparedStatementInterpreter.setInt(1, numAppointmentGenerated);
                                        preparedStatementInterpreter.setInt(2, interpreter.getNumInterpreter());
                                        preparedStatementInterpreter.executeUpdate();
                                    }
                                }
                            }
                        }
                        generateAppointmentID.close();
                        generateAppointmentID = null;
                    }
                }
                isCreated = true;
            }
        } finally {
            closeStatementAndResultSet(preparedStatementBeneficiary, generateBeneficiaryID);
            closeStatementAndResultSet(preparedStatementAppointment, generateAppointmentID);
            closeStatement(preparedStatementInterpreter);
        }
        return isCreated;
    }

    /**
     * Updates all Beneficiary fields in the table (except its id and its password),
     * if the Beneficiary has no Interpreter of reference then it will be set to null
     * in the database
     * Precondition: the Beneficiary passed as a parameter cannot be null
     * @param objectToUpdateInDB the Beneficiary to update
     * @return true if the Beneficiary was successfully updated, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method
     */
    @Override
    public boolean update(Beneficiary objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        PreparedStatement preparedStatement = null;
        String query = "UPDATE Beneficiary " +
                "SET login = ?, firstName = ?, lastName = ?, phoneNumber = ?, emailAddress = ?, " +
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

            preparedStatement.setString(1, objectToUpdateInDB.getLogin());
            preparedStatement.setString(2, objectToUpdateInDB.getFirstName());
            preparedStatement.setString(3, objectToUpdateInDB.getLastName());
            preparedStatement.setString(4, objectToUpdateInDB.getPhoneNumber());
            preparedStatement.setString(5, objectToUpdateInDB.getEmailAddress());
            preparedStatement.setInt(6, objectToUpdateInDB.getHourQuota());
            preparedStatement.setInt(7, objectToUpdateInDB.getEducationLevel());
            preparedStatement.setString(8, communicationLanguage);

            if (objectToUpdateInDB.getInterpreter() != null) {
                preparedStatement.setInt(9, objectToUpdateInDB.getInterpreter().getNumInterpreter());
            } else {
                preparedStatement.setNull(9, java.sql.Types.INTEGER);
            }

            preparedStatement.setInt(10, objectToUpdateInDB.getAddress().getNumAddress());
            preparedStatement.setInt(11, objectToUpdateInDB.getNumBeneficiary());

            if(preparedStatement.executeUpdate() > 0) {
                isUpdated = true;
            }
        } finally {
            closeStatement(preparedStatement);
        }
        return isUpdated;
    }

    /**
     * Deletes the Beneficiary whose numBeneficiary matches the id
     * of the Beneficiary passed as a parameter
     * Precondition: the Beneficiary passed as a parameter cannot be null
     * @param objectToDeleteFormDB the Beneficiary to delete
     * @return true if the Beneficiary was successfully deleted, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method
     */
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
            closeStatement(preparedStatement);
        }
        return isDeleted;
    }

    /**
     * Updates the Beneficiary's password whose numBeneficiary matches the id
     * of the Beneficiary passed as a parameter
     * Precondition: the Beneficiary passed as a parameter cannot be null
     * @param objectToUpdatePassword the Beneficiary whose password needs to be updated
     * @return true if the Beneficiary's password was successfully updated, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method
     */
    public boolean updatePassword(Beneficiary objectToUpdatePassword) throws SQLException {
        boolean passwordUpdated = false;
        PreparedStatement preparedStatement = null;
        String query = "UPDATE Beneficiary SET password = ? WHERE numBeneficiary = ?";

        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, objectToUpdatePassword.getPassword());
            preparedStatement.setInt(2, objectToUpdatePassword.getNumBeneficiary());

            if(preparedStatement.executeUpdate() > 0) {
                passwordUpdated = true;
            }
        } finally {
            closeStatement(preparedStatement);
        }
        return passwordUpdated;
    }
}