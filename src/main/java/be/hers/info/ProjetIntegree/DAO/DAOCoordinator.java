package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.DTO.DTOInterpreterUsers;
import be.hers.info.ProjetIntegree.POJO.*;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Halet Louis
 * @reviewer Nicolas Jean-Francois, Wellinger Chloé
 */
public class DAOCoordinator extends DAO<Coordinator> {

    /**
     * Search for a coordinator based on their coordinator number
     * Precondition :
     *  The Interpreter exist in the database
     *  The appointment number passed as a parameter cannot be negative.
     * The coordinator is initialized with the DAOInterpreter
     * The initialized fields are: numCoordinator, isAdmin and the Interpreter with heritage
     * @param idToSearchInDB the identifier of the object to search for in the table.
     * @return null if the coordinator does not exist in the database, the coordinator initialized with the attributes above.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public Coordinator find(int idToSearchInDB) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        Coordinator coordinator = null;
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        String query = "SELECT isAdmin,FKnumInterpreter " +
                "FROM Coordinator " +
                "WHERE numCoordinator = ?";
        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, idToSearchInDB);
            resultSet = prStat.executeQuery();

            if (resultSet.next()) {
                Interpreter interpreter= daoInterpreter.find(resultSet.getInt("FKnumInterpreter"));
                coordinator = new Coordinator(
                        interpreter.getNumInterpreter(),
                        interpreter.getLogin(),
                        interpreter.getPassword(),
                        interpreter.getLastName(),
                        interpreter.getFirstName(),
                        interpreter.getPhoneNumber(),
                        interpreter.getEmailAddress(),
                        interpreter.getWeeklyWorkHours(),
                        interpreter.getAddress());
                coordinator.setNumCoordinator(idToSearchInDB);
                int bool = resultSet.getInt("isAdmin");
                coordinator.setAdmin(bool!=0);
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return coordinator;
    }

    /**
     * Finds the Coordinator linked to the given FKnumInterpreter
     * @param idToSearchInDB the interpreter id used as foreign key in the Coordinator table
     * @return the matching Coordinator, or null if no row references this interpreter
     * @throws SQLException if a database access error occurs
     */
    public Coordinator findByFKnumInterpreter(int idToSearchInDB) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        Coordinator coordinator = null;

        DAOInterpreter daoInterpreter = new DAOInterpreter();
        String query = "SELECT numCoordinator,isAdmin,FKnumInterpreter " +
                "FROM Coordinator " +
                "WHERE FKnumInterpreter = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, idToSearchInDB);
            resultSet = prStat.executeQuery();

            if (resultSet.next()) {
                Interpreter interpreter= daoInterpreter.find(resultSet.getInt("FKnumInterpreter"));
                coordinator = new Coordinator(
                        interpreter.getNumInterpreter(),
                        interpreter.getLogin(),
                        interpreter.getPassword(),
                        interpreter.getLastName(),
                        interpreter.getFirstName(),
                        interpreter.getPhoneNumber(),
                        interpreter.getEmailAddress(),
                        interpreter.getWeeklyWorkHours(),
                        interpreter.getAddress());
                coordinator.setNumCoordinator(resultSet.getInt("numCoordinator"));
                int bool = resultSet.getInt("isAdmin");
                coordinator.setAdmin(bool!=0);
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return coordinator;
    }

    /**
     * Creates a list containing all the Coordinator not admin in the table.
     * Precondition :
     * For each Coordinator, the Interpreter exist in database
     * @return a list containing all the Coordinator in the table. An empty list is returned if the table is empty.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<DTOInterpreterUsers> findAllDTOResaUsers() throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<DTOInterpreterUsers> resaList = new ArrayList<>();
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();
        DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();
        String query = "SELECT numCoordinator, FKNumInterpreter" +
                       "FROM Coordinator " +
                       "WHERE isAdmin = 1";
        try {
            prStat = connect.prepareStatement(query);
            resultSet = prStat.executeQuery();
            while(resultSet.next()){
                Interpreter interpreter = daoInterpreter.find(resultSet.getInt("FKnumInterpreter"));
                List<ProfessionalSkill> listProfessionalSkills = daoProfessionalSkill.findByInterpreter(
                        interpreter.getNumInterpreter());

                List<AcademicSkill> listAcademicSkills = daoAcademicSkill.findByInterpreter(
                        interpreter.getNumInterpreter());

                DTOInterpreterUsers resa = new DTOInterpreterUsers(
                        resultSet.getInt("numCoordinator"),
                        interpreter.getLogin(),
                        interpreter.getLastName(),
                        interpreter.getFirstName(),
                        interpreter.getPhoneNumber(),
                        interpreter.getEmailAddress(),
                        interpreter.getAddress(),
                        interpreter.getWeeklyWorkHours(),
                        listProfessionalSkills,
                        listAcademicSkills
                        );
                resaList.add(resa);
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return resaList;
    }

    /**
     * Creates a list containing all the Coordinator admin in the table.
     * Precondition :
     * For each Coordinator, the Interpreter exist in database
     * @return a list containing all the Coordinator in the table. An empty list is returned if the table is empty.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<DTOInterpreterUsers> findAllDTOCoordinatorUsers() throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<DTOInterpreterUsers> coordinatorList = new ArrayList<>();
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();
        DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();
        String query = "SELECT numCoordinator, FKNumInterpreter" +
                "FROM Coordinator " +
                "WHERE isAdmin = 0";
        try {
            prStat = connect.prepareStatement(query);
            resultSet = prStat.executeQuery();
            while(resultSet.next()){
                Interpreter interpreter = daoInterpreter.find(resultSet.getInt("FKnumInterpreter"));
                List<ProfessionalSkill> listProfessionalSkills = daoProfessionalSkill.findByInterpreter(
                        interpreter.getNumInterpreter());

                List<AcademicSkill> listAcademicSkills = daoAcademicSkill.findByInterpreter(
                        interpreter.getNumInterpreter());

                DTOInterpreterUsers coordinator = new DTOInterpreterUsers(
                        resultSet.getInt("numCoordinator"),
                        interpreter.getLogin(),
                        interpreter.getLastName(),
                        interpreter.getFirstName(),
                        interpreter.getPhoneNumber(),
                        interpreter.getEmailAddress(),
                        interpreter.getAddress(),
                        interpreter.getWeeklyWorkHours(),
                        listProfessionalSkills,
                        listAcademicSkills
                );
                coordinatorList.add(coordinator);
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return coordinatorList;
    }

    /**
     * Creates a list containing all the Coordinator in the table.
     * Precondition :
     * For each Coordinator, the Interpreter exist in database
     *
     * The initialized fields are: numCoordinator, isAdmin and the Interpreter with heritage
     * @return a list containing all the Coordinator in the table. An empty list is returned if the table is empty.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public List<Coordinator> findAll() throws SQLException {
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        List<Coordinator> coordinatorList = new ArrayList<>();
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        String query = "SELECT numCoordinator,isAdmin,FKnumInterpreter " +
                "FROM Coordinator ";
        try {
            prStat = connect.prepareStatement(query);
            resultSet = prStat.executeQuery();
            while(resultSet.next()){
                Interpreter interpreter= daoInterpreter.find(resultSet.getInt("FKnumInterpreter"));
                Coordinator coordinator = new Coordinator(
                        interpreter.getNumInterpreter(),
                        interpreter.getLogin(),
                        interpreter.getPassword(),
                        interpreter.getLastName(),
                        interpreter.getFirstName(),
                        interpreter.getPhoneNumber(),
                        interpreter.getEmailAddress(),
                        interpreter.getWeeklyWorkHours(),
                        interpreter.getAddress());
                coordinator.setNumCoordinator(resultSet.getInt("numCoordinator"));
                int bool = resultSet.getInt("isAdmin");
                coordinator.setAdmin(bool!=0);
                coordinatorList.add(coordinator);
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return coordinatorList;
    }
    /**
     * Insert the Coordinator object passed as a parameter into the database
     * Precondition :
     *  The Coordinator passed as a parameter cannot be null
     *  the Interpreter exist in database
     *  the Interpreter have their number initialized
     * @param objectToInsertInDB the object to be inserted into the database
     * @return true if the Coordinator is correctly inserted into the database, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public boolean create(Coordinator objectToInsertInDB) throws SQLException {

        boolean isInserted = false;
        OraclePreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "INSERT INTO Coordinator (isAdmin,FKnumInterpreter) " +
                "VALUES (?, ?) "+
                "RETURNING numCoordinator INTO ?";

        try{
            prStat = (OraclePreparedStatement)connect.prepareStatement(query);
            prStat.setInt(1,(objectToInsertInDB.isAdmin() ? 1 : 0));
            prStat.setInt(2,objectToInsertInDB.getNumInterpreter());
            prStat.registerReturnParameter(3, OracleTypes.INTEGER);
            int nbLinesInsert = prStat.executeUpdate();

            if(nbLinesInsert > 0) {
                rs = prStat.getReturnResultSet();
                if(!rs.next()){
                    throw new SQLException("[DAOCoordinator] Impossible de récupérer le numCoordinator généré.");
                }
                int id = rs.getInt(1);
                objectToInsertInDB.setNumCoordinator(id);
                isInserted = true;
            }
        } finally {
            closeStatementAndResultSet(prStat, rs);
        }
        return isInserted;
    }
    /**
     * Deletes the Coordinator whose numCoordinator matches the numCoordinator
     * Precondition :
     *  The Coordinator passed as a parameter cannot be null
     *  The numCoordinator of the Coordinator passed as a parameter is initialized with its value in the database.
     * @param objectToDeleteFormDB the object to delete from the database
     * @return true if the Coordinator is successfully deleted, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public boolean delete(Coordinator objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        PreparedStatement prStat = null;

        String query = "DELETE FROM Coordinator " +
                "WHERE numCoordinator = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumCoordinator());

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
     * Updates the isAdmin  fields in the table (not the numCoordinator and his list)
     * Precondition :
     *  The numCoordinator of the Coordinator passed as a parameter is initialized with its value in the database.
     * @param objectToUpdateInDB the object to modify in the database
     * @return true if the Coordinator is successfully modified, false otherwise
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public boolean update(Coordinator objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        PreparedStatement prStat = null;

        String query = "UPDATE Coordinator " +
                "SET isAdmin = ?" +
                " WHERE numCoordinator = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, (objectToUpdateInDB.isAdmin() ? 1 : 0));
            prStat.setInt(2, objectToUpdateInDB.getNumCoordinator());

            int nbLinesUpdate = prStat.executeUpdate();
            if(nbLinesUpdate > 0) {
                isUpdated = true;
            }

        } finally {
            closeStatement(prStat);
        }
        return isUpdated;
    }

    /**
     * Return the number of resas in the coordinator table
     * @return the number of resas in the coordinator table
     * @throws SQLException In case of any SQL problems encountered with this method
     */
    public int countNumberResas() throws SQLException {
        int numberResas = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) FROM Coordinator " +
                       "WHERE isadmin = 0";

        try {
            preparedStatement = connect.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
                numberResas = resultSet.getInt(1);
        } finally {
            closeStatementAndResultSet(preparedStatement, resultSet);
        }
        return numberResas;
    }
}
