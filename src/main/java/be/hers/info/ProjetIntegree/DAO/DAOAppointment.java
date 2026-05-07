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

/**
 * @author Halet Louis
 * @reviewer Nicolas Jean-Francois
 */
public class DAOAppointment extends DAO<Appointment> {

    /**
     * Precondition :
     *  The beneficiary, TimeSlot, and Establishment exist in the database
     *  The appointment number passed as a parameter cannot be negative.
     * Search for an appointment based on their appointment number
     * The beneficiary is initialized with the DAOBeneficiary
     * The establishment is initialized with the DAOEstablishment
     * The timeSlot is initialized either with the DAOTimeSlotPunctual or with the DAOTimeSlotBase, respectively if it is a TimeSlotPunctual or TimeSlotBase object.
     * The initialized fields are: numAppointment, status, appointmentLocals, beneficiary, timeSlot and establishment.
     * @param idToSearchInDB the identifier of the object to search for in the table.
     * @return null if the appointment does not exist in the database, the appointment initialized with the attributes above.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public Appointment find(int idToSearchInDB) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        Appointment appointment = null;

        String query = "SELECT status,local,FKnumBeneficiary,FKTimeSlotBase,FKTimeSlotPunctual,FKnumEtablishment " +
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

                if(resultSet.getObject("FKTimeSlotBase") == null){
                    DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                    timeSlot = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));
                }else{
                    DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
                    timeSlot = daoTimeSlotBase.find(resultSet.getInt("FKTimeSlotBase"));
                }

                appointment = new Appointment(
                        idToSearchInDB,
                        resultSet.getString("status"),
                        listLocal,
                        daoBeneficiary.find(resultSet.getInt("FKnumBeneficiary")),
                        timeSlot,
                        daoEstablishment.find(resultSet.getInt("FKnumEtablishment"))
                        );
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return appointment;
    }

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
                        WHERE a.numBeneficiary = ?
                        AND ((tsp.startDate IS NOT NULL AND tsp.startDate <= DATE '?' AND tsp.endDate >= DATE '?') 
                        OR tsb.numTimeSlot IS NOT NULL)
                       """;

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numBeneficiary);
            prStat.setString(2, end);
            prStat.setString(3, start);
            rs = prStat.executeQuery();

            while(rs.next()){
                String local = rs.getString("local");
                List<String> listLocal = null;

                if (local != null)
                    listLocal = Arrays.asList(local.split(","));

                beneficiary = daoBeneficiary.find(rs.getInt("FKNumBeneficiary"));
                establishment = daoEstablishment.find(rs.getInt("FKNumEstablishment"));

                if(rs.getObject("FKTimeSlotBase") == null){
                    DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                    timeSlot = daoTimeSlotPunctual.find(rs.getInt("FKTimeSlotPunctual"));
                }else{
                    DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
                    timeSlot = daoTimeSlotBase.find(rs.getInt("FKTimeSlotBase"));
                }

                appointmentFind = new Appointment(
                        rs.getInt("numAppointment"),
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
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
        }

        return appointmentList;
    }

    /**
     * Creates a list containing all the Appointment in the table.
     * Precondition :
     * For each Appointment, the Beneficiary, TimeSlot, and Establishment exist in the database.
     *
     * The beneficiary is initialized with the DAOBeneficiary
     * The establishment is initialized with the DAOEstablishment
     * The timeSlot is initialized either with the DAOTimeSlotPunctual or with the DAOTimeSlotBase, respectively if it is a TimeSlotPunctual or TimeSlotBase object.
     * The initialized fields are: numAppointment, status, appointmentLocals, beneficiary, timeSlot and establishment.
     * @return a list containing all the Appointment in the table. An empty list is returned if the table is empty.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public List<Appointment> findAll() throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Appointment> appointmentList = new ArrayList<>();
        String query = "SELECT numAppointment,status,local,FKnumBeneficiary,FKTimeSlotBase " +
                "FROM Appointment";

        try {
            prStat = connect.prepareStatement(query);
            resultSet = prStat.executeQuery();

            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            DAOEstablishment daoEstablishment = new DAOEstablishment();

            while(resultSet.next()){
                String local = resultSet.getString("local");
                List<String> listLocal = null;

                if (local != null) {
                    listLocal = Arrays.asList(local.split(","));

                }

                TimeSlot timeSlot = null;
                int numAppointment = resultSet.getInt("numAppointment");

                if(resultSet.getObject("FKTimeSlotBase") == null){
                    DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
                    timeSlot = daoTimeSlotPunctual.find(resultSet.getInt("FKTimeSlotPunctual"));
                }else{
                    DAOTimeSlotBase daoTimeSlotBase = new DAOTimeSlotBase();
                    timeSlot = daoTimeSlotBase.find(resultSet.getInt("FKTimeSlotBase"));
                }

                appointmentList.add(new Appointment(
                        numAppointment,
                        resultSet.getString("status"),
                        listLocal,
                        daoBeneficiary.find(resultSet.getInt("FKnumBeneficiary")),
                        timeSlot,
                        daoEstablishment.find(resultSet.getInt("FKnumEtablishment"))));
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return appointmentList;
    }

    /**
     * Insert the Appointment object passed as a parameter into the database along with its associated lists
     * Precondition :
     *  The Appointment passed as a parameter cannot be null
     *  The beneficiary, TimeSlot, and Establishment exist in the database
     *  All attribute objects have their numbers initialized
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

        if (objectToInsertInDB.getAppointmentLocals() != null){
            local = String.join(",", objectToInsertInDB.getAppointmentLocals());
        }

        String query = "INSERT INTO Appointment (status, local, FKnumEtablishment, FKnumBeneficiary, FKTimeSlotPunctual) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING numAppointment INTO ?";
        if(objectToInsertInDB.getTimeSlot() instanceof TimeSlotBase){
            query = "INSERT INTO Appointment (status, local, FKnumEtablishment, FKnumBeneficiary, FKTimeSlotBase) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING numAppointment INTO ?";
        }

        try{
            prStat = (OraclePreparedStatement)connect.prepareStatement(query);
            prStat.setString(1,objectToInsertInDB.getStatus());
            prStat.setString(2,local);
            prStat.setInt(3,objectToInsertInDB.getEstablishment().getNumEstablishment());
            prStat.setInt(4,objectToInsertInDB.getBeneficiary().getNumBeneficiary());
            prStat.setInt(5,objectToInsertInDB.getTimeSlot().getNumTimeSlot());
            prStat.registerReturnParameter(6, OracleTypes.INTEGER);

            int nbLinesInsert = prStat.executeUpdate();
            rs = prStat.getReturnResultSet();
            if(rs.next()){
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
            }


        }finally {
            closeStatementAndResultSet(prStat, rs);
        }

        return isInserted;
    }

    /**
     * Insert a line into the database linking an interpreter to an Appointment.
     * Precondition :
     *  The appointment designated by numAppointment exists in the database
     *  The interpreter designated by numInterpreter exists in the database
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

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1,numAppointment);
            prStat.setInt(2,numInterpreter);
            int nbLinesInsert = prStat.executeUpdate();
            if(nbLinesInsert > 0) {
                isInserted = true;
            }
        }finally {
            closeStatement(prStat);
        }

        return isInserted;
    }

    /**
     * Insert a line in the database linking an AcademicSkill to an Appointment.
     * Precondition :
     *  The appointment designated by numAppointment exists in the database
     *  The AcademicSkill designated by numAcademicSkill exists in the database
     * @param numAppointment the appointment number
     * @param numAcademicSkill the AcademicSkill number
     * @return true if the line is correctly inserted into the database, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
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
            closeStatement(prStat);
        }

        return isInserted;
    }

    /**
     * Insert a line in the database linking a numProfessionalSkill to an Appointment.
     * Precondition :
     *  The appointment designated by numAppointment exists in the database
     *  The ProfessionalSkill designated by numProfessionalSkill exists in the database
     * @param numAppointment the appointment number
     * @param numProfessionalSkill the ProfessionalSkill number
     * @return true if the line is correctly inserted into the database, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
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
            closeStatement(prStat);
        }

        return isInserted;
    }

    /**
     * Deletes the Appointment whose numAppointment matches the numAppointment
     * Precondition :
     *  The Appointment passed as a parameter cannot be null
     *  The numAppointment of the Appointment passed as a parameter is initialized with its value in the database.
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
            if(nbLinesDelete > 0) {
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
     *  The numAppointment of the Appointment passed as a parameter is initialized with its value in the database.
     * @param objectToUpdateInDB the object to modify in the database
     * @return true if the Appointment is successfully modified, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public boolean update(Appointment objectToUpdateInDB) throws SQLException {

        boolean isUpdated = false;
        PreparedStatement prStat = null;
        String local = null;

        if (objectToUpdateInDB.getAppointmentLocals() != null){
            local = String.join(",", objectToUpdateInDB.getAppointmentLocals());
        }

        String query = "UPDATE Appointment " +
                "SET status = ?, local = ?, FKnumEtablishment = ?, FKnumBeneficiary = ?, FKTimeSlotBase = ?, FKTimeSlotPunctual = ?" +
                " WHERE numAppointment = ?";

        Integer timeSlotBase = null;
        Integer timeSlotPunctual = null;

        if(objectToUpdateInDB.getTimeSlot() instanceof TimeSlotBase){
            timeSlotBase = objectToUpdateInDB.getTimeSlot().getNumTimeSlot();
        }else{
            timeSlotPunctual = objectToUpdateInDB.getTimeSlot().getNumTimeSlot();
        }

        try {
            prStat = connect.prepareStatement(query);
            prStat.setString(1, objectToUpdateInDB.getStatus());
            prStat.setString(2, local);
            prStat.setInt(3, objectToUpdateInDB.getEstablishment().getNumEstablishment());
            prStat.setInt(4, objectToUpdateInDB.getBeneficiary().getNumBeneficiary());
            if(timeSlotBase == null){
                prStat.setNull(5, java.sql.Types.INTEGER);
            }else{
                prStat.setInt(5,timeSlotBase);
            }
            if((timeSlotPunctual == null)){
                prStat.setNull(6, java.sql.Types.INTEGER);
            }else{
                prStat.setInt(6, timeSlotPunctual);
            }
            prStat.setInt(7, objectToUpdateInDB.getNumAppointment());

            int nbLinesUpdate = prStat.executeUpdate();
            if(nbLinesUpdate > 0) {
                isUpdated = true;
            }

        } finally {
            closeStatement(prStat);
        }

        return isUpdated;
    }

}
