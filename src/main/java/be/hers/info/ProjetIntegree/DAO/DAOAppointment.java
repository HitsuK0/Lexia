package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.*;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Halet Louis
 * @reviewer Nicolas Jean-Francois
 */
public class DAOAppointment extends DAO<Appointment> {

    /**
     * Precondition :
     * The beneficiary, TimeSlot, and Establishment exist in the database
     * The appointment number passed as a parameter cannot be negative.
     * Search for an appointment based on their appointment number
     * The beneficiary is initialized with the DAOBeneficiary
     * The establishment is initialized with the DAOEstablishment
     * The timeSlot is initialized either with the DAOTimeSlotPunctual or with the DAOTimeSlotBase, respectively if it is a TimeSlotPunctual or TimeSlotBase object.
     * The initialized fields are: numAppointment, status, appointmentLocals, beneficiary, timeSlot and establishment.
     *
     * @param idToSearchInDB the identifier of the object to search for in the table.
     * @return null if the appointment does not exist in the database, the appointment initialized with the attributes above.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public Appointment find(int idToSearchInDB) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        Appointment appointment = null;

        String query = "SELECT description,status,local,FKnumBeneficiary,FKTimeSlotBase,FKTimeSlotPunctual,FKnumEstablishment " +
                "FROM Appointment " +
                "WHERE numAppointment = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, idToSearchInDB);
            resultSet = prStat.executeQuery();

            if (resultSet.next()) {
                DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
                DAOEstablishment daoEstablishment = new DAOEstablishment();
                String local = resultSet.getString("local");
                List<String> listLocal = null;

                if (local != null) {
                    listLocal = Arrays.asList(local.split(","));

                }
                TimeSlot timeSlot = null;

                if (resultSet.getObject("FKTimeSlotBase") == null) {
                    DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                    timeSlot = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));
                } else {
                    DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
                    timeSlot = daoTimeSlotBase.find(resultSet.getInt("FKTimeSlotBase"));
                }

                appointment = new Appointment(
                        idToSearchInDB,
                        resultSet.getString("description"),
                        resultSet.getString("status"),
                        listLocal,
                        daoBeneficiary.find(resultSet.getInt("FKnumBeneficiary")),
                        timeSlot,
                        daoEstablishment.find(resultSet.getInt("FKnumEstablishment"))
                );
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return appointment;
    }

    /**
     * Precondition :
     * The beneficiary number passed as a parameter cannot be negative.
     * Search for all appointments related to the beneficiary referenced by their number and indicating the start and end dates
     *
     * @param numBeneficiary the number of the beneficiary
     * @param start          the date of the first day of the week
     * @param end            the date of the last day of the week
     * @return the list of appointments related to the beneficiary referenced by their number and indicating the start and end dates
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<Appointment> findAllAppointmentToBeneficiaryAndDate(int numBeneficiary, String start, String end) throws SQLException {
        List<Appointment> appointmentList = new ArrayList<>();
        Appointment appointmentFind = null;
        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        Beneficiary beneficiary = null;
        TimeSlot timeSlot = null;
        DAOEstablishment daoEstablishment = new DAOEstablishment();
        Establishment establishment = null;
        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = """
                 SELECT *
                 FROM Appointment a
                 LEFT JOIN TimeSlotBase tsb ON tsb.numTimeSlot = a.FKTimeSlotBase
                 LEFT JOIN TimeSlotPunctual tsp ON tsp.numTimeSlot = a.FKTimeSlotPunctual
                 WHERE a.FKNumBeneficiary = ?
                 AND ((tsp.startDate IS NOT NULL AND tsp.startDate <= TO_DATE(?, 'YYYY-MM-DD') AND tsp.endDate >= TO_DATE(?, 'YYYY-MM-DD')) 
                 OR tsb.numTimeSlot IS NOT NULL)
                """;

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numBeneficiary);
            prStat.setString(2, end);
            prStat.setString(3, start);
            rs = prStat.executeQuery();

            while (rs.next()) {
                String local = rs.getString("local");
                List<String> listLocal = null;

                if (local != null)
                    listLocal = Arrays.asList(local.split(","));

                beneficiary = daoBeneficiary.find(rs.getInt("FKNumBeneficiary"));
                establishment = daoEstablishment.find(rs.getInt("FKNumEstablishment"));

                if (rs.getObject("FKTimeSlotBase") == null) {
                    DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                    timeSlot = daoTimeSlotPunctual.find(rs.getInt("FKTimeSlotPunctual"));
                } else {
                    DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
                    timeSlot = daoTimeSlotBase.find(rs.getInt("FKTimeSlotBase"));
                }

                int numAppointment = rs.getInt("numAppointment");

                appointmentFind = new Appointment(
                        numAppointment,
                        rs.getString("description"),
                        rs.getString("status"),
                        beneficiary,
                        listLocal,
                        findListInterpreter(numAppointment),
                        findListAcademicSkillRequire(numAppointment),
                        findListProfessionalSkillRequire(numAppointment),
                        timeSlot,
                        establishment
                );

                appointmentList.add(appointmentFind);
            }
        } finally {
            closeStatementAndResultSet(prStat, rs);
        }

        return appointmentList;
    }

    /**
     * Creates a list containing all the Appointment in the table.
     * Precondition :
     * For each Appointment, the Beneficiary, TimeSlot, and Establishment exist in the database.
     * <p>
     * The beneficiary is initialized with the DAOBeneficiary
     * The establishment is initialized with the DAOEstablishment
     * The timeSlot is initialized either with the DAOTimeSlotPunctual or with the DAOTimeSlotBase, respectively if it is a TimeSlotPunctual or TimeSlotBase object.
     * The initialized fields are: numAppointment, status, appointmentLocals, beneficiary, timeSlot and establishment.
     *
     * @return a list containing all the Appointment in the table. An empty list is returned if the table is empty.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public List<Appointment> findAll() throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Appointment> appointmentList = new ArrayList<>();
        String query = "SELECT *" +
                "FROM Appointment";

        try {
            prStat = connect.prepareStatement(query);
            resultSet = prStat.executeQuery();

            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            DAOEstablishment daoEstablishment = new DAOEstablishment();

            while (resultSet.next()) {
                String local = resultSet.getString("local");
                List<String> listLocal = null;

                if (local != null) {
                    listLocal = Arrays.asList(local.split(","));

                }

                TimeSlot timeSlot = null;
                int numAppointment = resultSet.getInt("numAppointment");

                if (resultSet.getObject("FKTimeSlotBase") == null) {
                    DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                    timeSlot = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));
                } else {
                    DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
                    timeSlot = daoTimeSlotBase.find(resultSet.getInt("FKTimeSlotBase"));
                }

                appointmentList.add(new Appointment(
                        numAppointment,
                        resultSet.getString("description"),
                        resultSet.getString("status"),
                        listLocal,
                        daoBeneficiary.find(resultSet.getInt("FKnumBeneficiary")),
                        timeSlot,
                        daoEstablishment.find(resultSet.getInt("FKnumEstablishment"))));
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return appointmentList;
    }

    /**
     * Insert the Appointment object passed as a parameter into the database along with its associated lists
     * Precondition :
     * The Appointment passed as a parameter cannot be null
     * The beneficiary, TimeSlot, and Establishment exist in the database
     * All attribute objects have their numbers initialized
     *
     * @param objectToInsertInDB the object to be inserted into the database
     * @return true if the Appointment is correctly inserted into the database, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public boolean create(Appointment objectToInsertInDB) throws SQLException {

        boolean isInserted = false;
        OraclePreparedStatement prStat = null;
        ResultSet rs = null;
        String local = null;

        if (objectToInsertInDB.getAppointmentLocals() != null) {
            local = String.join(",", objectToInsertInDB.getAppointmentLocals());
        }

        String query = "INSERT INTO Appointment (description,status, local, FKnumEstablishment, FKnumBeneficiary, FKTimeSlotPunctual) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING numAppointment INTO ?";
        if (objectToInsertInDB.getTimeSlot() instanceof TimeSlotBase) {
            query = "INSERT INTO Appointment (description,status, local, FKnumEstablishment, FKnumBeneficiary, FKTimeSlotBase) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING numAppointment INTO ?";
        }

        try {
            prStat = (OraclePreparedStatement) connect.prepareStatement(query);
            prStat.setString(1, objectToInsertInDB.getDescription());
            prStat.setString(2, objectToInsertInDB.getStatus());
            prStat.setString(3, local);
            prStat.setInt(4, objectToInsertInDB.getEstablishment().getNumEstablishment());
            prStat.setInt(5, objectToInsertInDB.getBeneficiary().getNumBeneficiary());
            prStat.setInt(6, objectToInsertInDB.getTimeSlot().getNumTimeSlot());
            prStat.registerReturnParameter(7, OracleTypes.INTEGER);

            int nbLinesInsert = prStat.executeUpdate();
            rs = prStat.getReturnResultSet();
            if (rs.next()) {
                int id = rs.getInt(1);
                objectToInsertInDB.setNumAppointment(id);

                if (nbLinesInsert > 0) {
                    isInserted = true;
                }

                if (!objectToInsertInDB.getInterpreters().isEmpty()) {
                    for (Interpreter i : objectToInsertInDB.getInterpreters()) {
                        if (!addInterpreterAtAppointment(id, i.getNumInterpreter()))
                            throw new SQLException("[DAOAppointment] erreur lors de l'ajout dans la table RDVInterpreter");
                    }
                }

                if (!objectToInsertInDB.getAcademicSkillsNeeded().isEmpty()) {
                    for (AcademicSkill a : objectToInsertInDB.getAcademicSkillsNeeded()) {

                        if (!addAcademicSkillAtAppointment(id, a.getNumAcademicSkill()))
                            throw new SQLException("[DAOAppointment] erreur lors de l'ajout dans la table RequiredAcademicSkill");
                    }
                }

                if (!objectToInsertInDB.getProfessionalSkillsNeeded().isEmpty()) {
                    for (ProfessionalSkill p : objectToInsertInDB.getProfessionalSkillsNeeded()) {

                        if (!addProfessionalSkillAtAppointment(id, p.getNumProfessionalSkill()))
                            throw new SQLException("[DAOAppointment] erreur lors de l'ajout dans la table RequiredProfessionalSkill");
                    }
                }
            }


        } finally {
            closeStatementAndResultSet(prStat, rs);
        }

        return isInserted;
    }

    /**
     * Insert a line into the database linking an interpreter to an Appointment.
     * Precondition :
     * The appointment designated by numAppointment exists in the database
     * The interpreter designated by numInterpreter exists in the database
     *
     * @param numAppointment the appointment number
     * @param numInterpreter the Interpreter's number
     * @return true if the row is successfully inserted into the database, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public boolean addInterpreterAtAppointment(int numAppointment, int numInterpreter) throws SQLException {

        boolean isInserted = false;
        PreparedStatement prStat = null;
        String query = "INSERT INTO RDVInterpreter (numAppointment, numInterpreter) " +
                "VALUES (?, ?)";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numAppointment);
            prStat.setInt(2, numInterpreter);
            int nbLinesInsert = prStat.executeUpdate();
            if (nbLinesInsert > 0) {
                isInserted = true;
            }
        } finally {
            closeStatement(prStat);
        }

        return isInserted;
    }

    /**
     * Insert a line in the database linking an AcademicSkill to an Appointment.
     * Precondition :
     * The appointment designated by numAppointment exists in the database
     * The AcademicSkill designated by numAcademicSkill exists in the database
     *
     * @param numAppointment   the appointment number
     * @param numAcademicSkill the AcademicSkill number
     * @return true if the line is correctly inserted into the database, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public boolean addAcademicSkillAtAppointment(int numAppointment, int numAcademicSkill) throws SQLException {

        boolean isInserted = false;
        PreparedStatement prStat = null;
        String query = "INSERT INTO RequiredAcademicSkill (numAppointment, numAcademicSkill) " +
                "VALUES (?, ?)";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numAppointment);
            prStat.setInt(2, numAcademicSkill);
            int nbLinesInsert = prStat.executeUpdate();
            if (nbLinesInsert > 0) {
                isInserted = true;
            }
        } finally {
            closeStatement(prStat);
        }

        return isInserted;
    }

    /**
     * Insert a line in the database linking a numProfessionalSkill to an Appointment.
     * Precondition :
     * The appointment designated by numAppointment exists in the database
     * The ProfessionalSkill designated by numProfessionalSkill exists in the database
     *
     * @param numAppointment       the appointment number
     * @param numProfessionalSkill the ProfessionalSkill number
     * @return true if the line is correctly inserted into the database, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public boolean addProfessionalSkillAtAppointment(int numAppointment, int numProfessionalSkill) throws SQLException {

        boolean isInserted = false;
        PreparedStatement prStat = null;
        String query = "INSERT INTO RequiredProfessionalSkill (numAppointment, numProfessionalSkill) " +
                "VALUES (?, ?)";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numAppointment);
            prStat.setInt(2, numProfessionalSkill);
            int nbLinesInsert = prStat.executeUpdate();
            if (nbLinesInsert > 0) {
                isInserted = true;
            }
        } finally {
            closeStatement(prStat);
        }

        return isInserted;
    }

    /**
     * Deletes the Appointment whose numAppointment matches the numAppointment
     * Precondition :
     * The Appointment passed as a parameter cannot be null
     * The numAppointment of the Appointment passed as a parameter is initialized with its value in the database.
     *
     * @param objectToDeleteFormDB the object to delete from the database
     * @return true if the Appointment is successfully deleted, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
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
            if (nbLinesDelete > 0) {
                isDeleted = true;
            }
        } finally {
            closeStatement(prStat);
        }

        return isDeleted;
    }

    /**
     * Updates all Appointment fields in the table (except its numInterpreter and his list)
     * Precondition :
     * The numAppointment of the Appointment passed as a parameter is initialized with its value in the database.
     *
     * @param objectToUpdateInDB the object to modify in the database
     * @return true if the Appointment is successfully modified, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public boolean update(Appointment objectToUpdateInDB) throws SQLException {

        boolean isUpdated = false;
        PreparedStatement prStat = null;
        String local = null;

        if (objectToUpdateInDB.getAppointmentLocals() != null) {
            local = String.join(",", objectToUpdateInDB.getAppointmentLocals());
        }

        String query = "UPDATE Appointment " +
                "SET description = ? ,status = ?, local = ?, FKnumEstablishment = ?, FKnumBeneficiary = ?, FKTimeSlotBase = ?, FKTimeSlotPunctual = ?" +
                " WHERE numAppointment = ?";

        Integer timeSlotBase = null;
        Integer timeSlotPunctual = null;

        if (objectToUpdateInDB.getTimeSlot() instanceof TimeSlotBase) {
            timeSlotBase = objectToUpdateInDB.getTimeSlot().getNumTimeSlot();
        } else {
            timeSlotPunctual = objectToUpdateInDB.getTimeSlot().getNumTimeSlot();
        }

        try {
            prStat = connect.prepareStatement(query);
            prStat.setString(1, objectToUpdateInDB.getDescription());
            prStat.setString(2, objectToUpdateInDB.getStatus());
            prStat.setString(3, local);
            prStat.setInt(4, objectToUpdateInDB.getEstablishment().getNumEstablishment());
            prStat.setInt(5, objectToUpdateInDB.getBeneficiary().getNumBeneficiary());
            if (timeSlotBase == null) {
                prStat.setNull(6, java.sql.Types.INTEGER);
            } else {
                prStat.setInt(6, timeSlotBase);
            }
            if ((timeSlotPunctual == null)) {
                prStat.setNull(7, java.sql.Types.INTEGER);
            } else {
                prStat.setInt(7, timeSlotPunctual);
            }
            prStat.setInt(8, objectToUpdateInDB.getNumAppointment());

            int nbLinesUpdate = prStat.executeUpdate();
            if (nbLinesUpdate > 0) {
                isUpdated = true;
            }

        } finally {
            closeStatement(prStat);
        }

        return isUpdated;
    }

    /**
     * Searches for all Appointments linked to the interpreter as a parameter available in the start and end slots.
     *
     * @param i     the interpreter who will be linked to the appointment sought
     * @param start the date in YYYY-MM-DD format
     * @param end   the date in YYYY-MM-DD format
     * @return The appointment list meets the constraints; an empty list is returned if no object is found.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<Appointment> findAllAppointmentToInterpreterAndDate(Interpreter i, String start, String end) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Appointment> appointmentList = new ArrayList<>();
        String query = "SELECT ap.numAppointment, ap.description, ap.status, ap.local,ap.FKnumEstablishment,ap.FKTimeSlotBase,ap.FKTimeSlotPunctual,ap.FKnumBeneficiary " +
                "FROM Appointment ap " +
                "JOIN RDVInterpreter rdv ON rdv.numAppointment = ap.numAppointment " +
                "LEFT JOIN TimeSlotBase tsb ON tsb.numTimeSlot = ap.FKTimeSlotBase " +
                "LEFT JOIN TimeSlotPunctual tsp ON tsp.numTimeSlot = ap.FKTimeSlotPunctual " +
                "JOIN Establishment e ON e.numEstablishment = ap.FKnumEstablishment " +
                "JOIN Beneficiary b ON b.numBeneficiary = ap.FKnumBeneficiary " +
                "WHERE rdv.numInterpreter = ? " +
                "  AND ap.status <> 'en attente' " +
                "  AND ( " +
                "      (tsp.startDate IS NOT NULL AND tsp.startDate <= TO_DATE(?, 'YYYY-MM-DD') AND tsp.endDate >= TO_DATE(?, 'YYYY-MM-DD')) " +
                "      OR " +
                "      tsb.numTimeSlot IS NOT NULL" +
                ")";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, i.getNumInterpreter());
            prStat.setString(2, end);
            prStat.setString(3, start);
            resultSet = prStat.executeQuery();
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            DAOEstablishment daoEstablishment = new DAOEstablishment();
            DAOReferrer daoReferrer = new DAOReferrer();
            DAOAddress daoAddress = new DAOAddress();
            Appointment a = new Appointment();
            while (resultSet.next()) {
                String local = resultSet.getString("local");
                List<String> listLocal = null;

                if (local != null) {
                    listLocal = Arrays.asList(local.split(","));
                }

                TimeSlot timeSlot = null;
                int numAppointment = resultSet.getInt("numAppointment");

                if (resultSet.getObject("FKTimeSlotBase") == null) {
                    DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                    timeSlot = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));
                } else {
                    DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
                    timeSlot = daoTimeSlotBase.find(resultSet.getInt("FKTimeSlotBase"));
                }
                Establishment e = daoEstablishment.find(resultSet.getInt("FKnumEstablishment"));
                e.setEducationLevel(daoEstablishment.findListEducationLevel(resultSet.getInt("FKnumEstablishment")));
                e.setReferrers(daoReferrer.findAllByEstablishment(resultSet.getInt("FKnumEstablishment")));
                e.setAddresses(daoAddress.findAllByEstablishment(e.getNameBuilding()));

                a = new Appointment(
                        numAppointment,
                        resultSet.getString("description"),
                        daoBeneficiary.find(resultSet.getInt("FKnumBeneficiary")),
                        listLocal,
                        findListInterpreter(numAppointment),
                        findListAcademicSkillRequire(numAppointment),
                        findListProfessionalSkillRequire(numAppointment),
                        timeSlot,
                        e);
                try {
                    a.setStatus(resultSet.getString("status"));
                } catch (BadStatusException ex) {

                }
                appointmentList.add(a);

            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return appointmentList;
    }

    /**
     * Searches for all Appointments linked to the interpreter as a parameter available in the start and end slots.
     *
     * @param numInterpreter the numero of interpreter who will be linked to the appointment sought
     * @param start          the date in YYYY-MM-DD format
     * @param end            the date in YYYY-MM-DD format
     * @return The appointment list meets the constraints; an empty list is returned if no object is found.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<Appointment> findAllAppointmentToInterpreterAndDate(int numInterpreter, String start, String end) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Appointment> appointmentList = new ArrayList<>();
        String query = "SELECT ap.numAppointment, ap.description, ap.status, ap.local,ap.FKnumEstablishment,ap.FKTimeSlotBase,ap.FKTimeSlotPunctual,ap.FKnumBeneficiary " +
                "FROM Appointment ap " +
                "JOIN RDVInterpreter rdv ON rdv.numAppointment = ap.numAppointment " +
                "LEFT JOIN TimeSlotBase tsb ON tsb.numTimeSlot = ap.FKTimeSlotBase " +
                "LEFT JOIN TimeSlotPunctual tsp ON tsp.numTimeSlot = ap.FKTimeSlotPunctual " +
                "JOIN Establishment e ON e.numEstablishment = ap.FKnumEstablishment " +
                "JOIN Beneficiary b ON b.numBeneficiary = ap.FKnumBeneficiary " +
                "WHERE rdv.numInterpreter = ? " +
                "  AND ap.status <> 'en attente' " +
                "  AND ( " +
                "      (tsp.startDate IS NOT NULL AND tsp.startDate <= TO_DATE(?, 'YYYY-MM-DD') AND tsp.endDate >= TO_DATE(?, 'YYYY-MM-DD')) " +
                "      OR " +
                "      tsb.numTimeSlot IS NOT NULL" +
                ")";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numInterpreter);
            prStat.setString(2, end);
            prStat.setString(3, start);
            resultSet = prStat.executeQuery();
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            DAOEstablishment daoEstablishment = new DAOEstablishment();
            DAOReferrer daoReferrer = new DAOReferrer();
            DAOAddress daoAddress = new DAOAddress();
            Appointment a = new Appointment();
            while (resultSet.next()) {
                String local = resultSet.getString("local");
                List<String> listLocal = null;

                if (local != null) {
                    listLocal = Arrays.asList(local.split(","));
                }

                TimeSlot timeSlot = null;
                int numAppointment = resultSet.getInt("numAppointment");

                if (resultSet.getObject("FKTimeSlotBase") == null) {
                    DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                    timeSlot = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));
                } else {
                    DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
                    timeSlot = daoTimeSlotBase.find(resultSet.getInt("FKTimeSlotBase"));
                }
                Establishment e = daoEstablishment.find(resultSet.getInt("FKnumEstablishment"));
                e.setEducationLevel(daoEstablishment.findListEducationLevel(resultSet.getInt("FKnumEstablishment")));
                e.setReferrers(daoReferrer.findAllByEstablishment(resultSet.getInt("FKnumEstablishment")));
                e.setAddresses(daoAddress.findAllByEstablishment(e.getNameBuilding()));

                a = new Appointment(
                        numAppointment,
                        resultSet.getString("description"),
                        daoBeneficiary.find(resultSet.getInt("FKnumBeneficiary")),
                        listLocal,
                        findListInterpreter(numAppointment),
                        findListAcademicSkillRequire(numAppointment),
                        findListProfessionalSkillRequire(numAppointment),
                        timeSlot,
                        e);
                try {
                    a.setStatus(resultSet.getString("status"));
                } catch (BadStatusException ex) {

                }
                appointmentList.add(a);

            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return appointmentList;
    }

    /**
     * Create a list of Interpreters linked to the appointment.
     *
     * @param numAppointment the appointment number
     * @return The list of Interpreters linked to the appointment; an empty list is returned if no object is found
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<Interpreter> findListInterpreter(int numAppointment) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Interpreter> interpreterList = new ArrayList<>();
        String query = "SELECT numInterpreter " +
                "FROM RDVInterpreter " +
                "WHERE numAppointment = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numAppointment);
            resultSet = prStat.executeQuery();
            DAOInterpreter daoInterpreter = new DAOInterpreter();

            while (resultSet.next()) {
                interpreterList.add(daoInterpreter.find(resultSet.getInt("numInterpreter")));
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return interpreterList;
    }

    /**
     * Create a list of AcademicSkills related to the appointment.
     *
     * @param numAppointment the appointment number
     * @return The AcademicSkill list linked to the appointment; an empty list is returned if no item is found.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<AcademicSkill> findListAcademicSkillRequire(int numAppointment) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<AcademicSkill> academicSkillList = new ArrayList<>();
        String query = "SELECT numAcademicSkill " +
                "FROM RequiredAcademicSkill " +
                "WHERE numAppointment = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numAppointment);
            resultSet = prStat.executeQuery();
            DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();

            while (resultSet.next()) {
                academicSkillList.add(daoAcademicSkill.find(resultSet.getInt("numAcademicSkill")));
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }

        return academicSkillList;
    }

    /**
     * Create a list of Professional Skills related to the appointment.
     *
     * @param numAppointment the appointment number
     * @return The ProfessionalSkill list linked to the appointment is returned as empty if no object is found.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<ProfessionalSkill> findListProfessionalSkillRequire(int numAppointment) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<ProfessionalSkill> ProfessionalSkillList = new ArrayList<>();
        String query = "SELECT numProfessionalSkill " +
                "FROM RequiredProfessionalSkill " +
                "WHERE numAppointment = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numAppointment);
            resultSet = prStat.executeQuery();
            DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();

            while (resultSet.next()) {
                ProfessionalSkillList.add(daoProfessionalSkill.find(resultSet.getInt("numProfessionalSkill")));
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }

        return ProfessionalSkillList;
    }

    /**
     * Searches for all absences related to the interpreter as a parameter available in the start and end slots.
     *
     * @param i     the interpreter who will be linked to the appointment sought
     * @param start the date in YYYY-MM-DD format
     * @param end   the date in YYYY-MM-DD format
     * @return The Absence list meets the constraints; an empty list is returned if no object is found.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<Absence> findAllAbsenceToInterpreterAndDate(Interpreter i, String start, String end) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Absence> absenceList = new ArrayList<>();
        String query = "SELECT ab.numAbsence, ab.status, ab.reasons, ab.privateReason,ab.FKTimeSlotBase,ab.FKTimeSlotPunctual,ab.FKnumInterpreter " +
                "FROM Absence ab " +
                "LEFT JOIN TimeSlotBase tsb ON tsb.numTimeSlot = ab.FKTimeSlotBase " +
                "LEFT JOIN TimeSlotPunctual tsp ON tsp.numTimeSlot = ab.FKTimeSlotPunctual " +
                "WHERE ab.FKnumInterpreter = ? " +
                "  AND ( " +
                "      (tsp.startDate IS NOT NULL AND tsp.startDate <= TO_DATE(?, 'YYYY-MM-DD') AND tsp.endDate >= TO_DATE(?, 'YYYY-MM-DD')) " +
                "      OR " +
                "      tsb.numTimeSlot IS NOT NULL)";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, i.getNumInterpreter());
            prStat.setString(2, end);
            prStat.setString(3, start);
            resultSet = prStat.executeQuery();

            TimeSlot timeSlot = null;
            while (resultSet.next()) {
                if (resultSet.getObject("FKTimeSlotBase") != null) {
                    DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
                    timeSlot = daoTimeSlotBase.find(resultSet.getInt("FKTimeSlotBase"));
                } else {
                    DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                    timeSlot = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));
                }
                try {
                    absenceList.add(new Absence(resultSet.getInt("numAbsence"), resultSet.getString("status"), timeSlot,
                            resultSet.getString("reasons"), resultSet.getBoolean("privateReason")));
                } catch (BadStatusException e) {
                }
            }
        } finally {
            closeStatement(prStat);
        }
        return absenceList;
    }

    /**
     * Searches for all absences related to the interpreter as a parameter available in the start and end slots.
     *
     * @param numInterpreter the numero of interpreter who will be linked to the appointment sought
     * @param start          the date in YYYY-MM-DD format
     * @param end            the date in YYYY-MM-DD format
     * @return The Absence list meets the constraints; an empty list is returned if no object is found.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<Absence> findAllAbsenceToInterpreterAndDate(int numInterpreter, String start, String end) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Absence> absenceList = new ArrayList<>();
        String query = "SELECT ab.numAbsence, ab.status, ab.reasons, ab.privateReason,ab.FKTimeSlotBase,ab.FKTimeSlotPunctual,ab.FKnumInterpreter " +
                "FROM Absence ab " +
                "LEFT JOIN TimeSlotBase tsb ON tsb.numTimeSlot = ab.FKTimeSlotBase " +
                "LEFT JOIN TimeSlotPunctual tsp ON tsp.numTimeSlot = ab.FKTimeSlotPunctual " +
                "WHERE ab.FKnumInterpreter = ? " +
                "  AND ( " +
                "      (tsp.startDate IS NOT NULL AND tsp.startDate <= TO_DATE(?, 'YYYY-MM-DD') AND tsp.endDate >= TO_DATE(?, 'YYYY-MM-DD')) " +
                "      OR " +
                "      tsb.numTimeSlot IS NOT NULL)";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numInterpreter);
            prStat.setString(2, end);
            prStat.setString(3, start);
            resultSet = prStat.executeQuery();

            TimeSlot timeSlot = null;
            while (resultSet.next()) {
                if (resultSet.getObject("FKTimeSlotBase") != null) {
                    DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
                    timeSlot = daoTimeSlotBase.find(resultSet.getInt("FKTimeSlotBase"));
                } else {
                    DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                    timeSlot = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));
                }
                try {
                    absenceList.add(new Absence(resultSet.getInt("numAbsence"), resultSet.getString("status"), timeSlot,
                            resultSet.getString("reasons"), resultSet.getBoolean("privateReason")));
                } catch (BadStatusException e) {
                }
            }
        } finally {
            closeStatement(prStat);
        }
        return absenceList;
    }

    /**
     * Retrieves all appointment requests submitted by a Beneficiary, optionally filtered by status.
     * Only the fields needed for the request list view are loaded (numAppointment, status,
     * timeSlot, required academic and professional skills).
     *
     * @param numBeneficiary the id of the Beneficiary whose requests are being retrieved
     * @param status         the status to filter on, or null/empty for no filter
     * @return the list of appointment requests matching the criteria, an empty list if none
     * @throws SQLException In case of any SQL problems encountered with this method
     */
    public List<Appointment> findAllRequestsByBeneficiaryAndOptionalStatus(int numBeneficiary, String status) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Appointment> appointmentList = new ArrayList<>();
        List<String> listLocal = null;
        Establishment establishment = null;

        boolean filterByStatus = status != null && !status.isEmpty();

        String query = "SELECT numAppointment, description, status, local, FKNumEstablishment, FKTimeSlotPunctual " +
                "FROM Appointment " +
                "WHERE FKnumBeneficiary = ? " +
                "AND FKTimeSlotPunctual IS NOT NULL";

        if (filterByStatus) {
            query += " AND status = ?";
        }

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numBeneficiary);
            if (filterByStatus) {
                prStat.setString(2, status);
            }

            resultSet = prStat.executeQuery();

            DAOEstablishment daoEstablishment = new DAOEstablishment();
            DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();

            while (resultSet.next()) {
                int numAppointment = resultSet.getInt("numAppointment");

                listLocal = null;
                String local =  resultSet.getString("local");
                if (local != null)
                    listLocal = Arrays.asList(local.split(","));

                int fkEstablishment = resultSet.getInt("FKNumEstablishment");
                if (!resultSet.wasNull())
                    establishment = daoEstablishment.find(fkEstablishment);

                TimeSlot timeSlot = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));

                Appointment appointment = new Appointment(
                        numAppointment,
                        resultSet.getString("description"),
                        resultSet.getString("status"),
                        listLocal,
                        timeSlot,
                        establishment,
                        findListAcademicSkillRequire(numAppointment),
                        findListProfessionalSkillRequire(numAppointment)
                );
                appointmentList.add(appointment);
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return appointmentList;
    }

    /**
     * Retrieves all appointment requests with a TimeSlotPunctual matching the given status,
     * across all beneficiaries.
     * Loads beneficiary, establishment, timeSlot, academic and professional skills for each appointment.
     * Precondition: status cannot be null or empty.
     *
     * @param status the status to filter on ("en attente", "accepte", "refuse")
     * @return a list of Appointments matching the status, or an empty list if none found
     * @throws SQLException In case of any SQL problems encountered with this method
     */
    public List<Appointment> findAllRequestsByOptionalStatus(String status) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Appointment> appointmentList = new ArrayList<>();

        String query = "SELECT a.numAppointment, a.description, a.status, a.local, " +
                "a.FKnumBeneficiary, a.FKnumEstablishment, a.FKTimeSlotPunctual " +
                "FROM Appointment a " +
                "WHERE a.FKTimeSlotPunctual IS NOT NULL " +
                "AND a.status = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setString(1, status);
            resultSet = prStat.executeQuery();

            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            DAOEstablishment daoEstablishment = new DAOEstablishment();
            DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();

            while (resultSet.next()) {
                int numAppointment = resultSet.getInt("numAppointment");

                String local = resultSet.getString("local");
                List<String> listLocal = null;
                if (local != null)
                    listLocal = Arrays.asList(local.split(","));

                TimeSlot timeSlot = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));

                Appointment appointment = new Appointment(
                        numAppointment,
                        resultSet.getString("description"),
                        resultSet.getString("status"),
                        listLocal,
                        daoBeneficiary.find(resultSet.getInt("FKnumBeneficiary")),
                        timeSlot,
                        daoEstablishment.find(resultSet.getInt("FKnumEstablishment"))
                );
                appointmentList.add(appointment);
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return appointmentList;
    }
}