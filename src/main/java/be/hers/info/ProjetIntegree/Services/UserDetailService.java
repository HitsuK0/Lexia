package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DAO.DAOCoordinator;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.POJO.*;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */
public class UserDetailService {

    /**
     * Finds an Interpreter by its numInterpreter.
     *
     * @param numInterpreter the id of the interpreter to find
     * @return the Interpreter if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Interpreter findInterpreterById(int numInterpreter) throws SQLException {
        return new DAOInterpreter().find(numInterpreter);
    }

    /**
     * Finds a Beneficiary by its numBeneficiary.
     *
     * @param numBeneficiary the id of the beneficiary to find
     * @return the Beneficiary if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Beneficiary findBeneficiaryById(int numBeneficiary) throws SQLException {
        return new DAOBeneficiary().find(numBeneficiary);
    }

    /**
     * Finds the Coordinator linked to the given interpreter via FKnumInterpreter.
     * Returns null if the interpreter is not a coordinator.
     *
     * @param numInterpreter the id of the interpreter to check
     * @return the Coordinator if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Coordinator findCoordinatorByInterpreter(int numInterpreter) throws SQLException {
        return new DAOCoordinator().findByFKnumInterpreter(numInterpreter);
    }

    /**
     * Determines the user role string from a numInterpreter.
     * Returns "COORDINATOR" if isAdmin = true, "RESA" if isAdmin = false, "INTERPRETER" otherwise.
     *
     * @param numInterpreter the id of the interpreter to check
     * @return the role string : "COORDINATOR", "RESA" or "INTERPRETER"
     * @throws SQLException if a database error occurs
     */
    public String resolveInterpreterRole(int numInterpreter) throws SQLException {
        Coordinator coordinator = findCoordinatorByInterpreter(numInterpreter);
        if (coordinator != null) {
            return coordinator.isAdmin() ? "COORDINATOR" : "RESA";
        }
        return "INTERPRETER";
    }

    /**
     * Returns the list of ProfessionalSkills owned by the given interpreter.
     *
     * @param numInterpreter the id of the interpreter
     * @return the list of ProfessionalSkills, or an empty list if none
     * @throws SQLException if a database error occurs
     */
    public List<ProfessionalSkill> getProfessionalSkillsOfInterpreter(int numInterpreter) throws SQLException {
        return new DAOInterpreter().getProfessionalSkill(numInterpreter);
    }

    /**
     * Returns the list of AcademicSkills owned by the given interpreter.
     *
     * @param numInterpreter the id of the interpreter
     * @return the list of AcademicSkills, or an empty list if none
     * @throws SQLException if a database error occurs
     */
    public List<AcademicSkill> getAcademicSkillsOfInterpreter(int numInterpreter) throws SQLException {
        return new DAOInterpreter().getAcademicSkill(numInterpreter);
    }

    /**
     * Returns the full list of all interpreters.
     *
     * @return the list of all Interpreters
     * @throws SQLException if a database error occurs
     */
    public List<Interpreter> findAllInterpreters() throws SQLException {
        return new DAOInterpreter().findAll();
    }

    /**
     * Updates the password of the given interpreter in the database.
     * The DB trigger will hash the new password on UPDATE.
     *
     * @param interpreter the interpreter whose password is updated
     * @throws SQLException if a database error occurs
     */
    public void updateInterpreterPassword(Interpreter interpreter) throws SQLException {
        new DAOInterpreter().updatePassword(interpreter);
    }

    /**
     * Updates the password of the given beneficiary in the database.
     * The DB trigger will hash the new password on UPDATE.
     *
     * @param beneficiary the beneficiary whose password is updated
     * @throws SQLException if a database error occurs
     */
    public void updateBeneficiaryPassword(Beneficiary beneficiary) throws SQLException {
        new DAOBeneficiary().updatePassword(beneficiary);
    }

    /**
     * Updates the Beneficiary in the database.
     *
     * @param beneficiary the beneficiary to update
     * @throws SQLException if a database error occurs
     */
    public void updateBeneficiary(Beneficiary beneficiary) throws SQLException {
        new DAOBeneficiary().update(beneficiary);
    }

    /**
     * Updates the login and personal data of the given interpreter in the database.
     *
     * @param interpreter the interpreter to update
     * @throws SQLException if a database error occurs
     */
    public void updateInterpreter(Interpreter interpreter) throws SQLException {
        new DAOInterpreter().update(interpreter);
    }

    /**
     * Updates the isAdmin field of the given coordinator in the database.
     *
     * @param coordinator the coordinator to update
     * @throws SQLException if a database error occurs
     */
    public void updateCoordinator(Coordinator coordinator) throws SQLException {
        new DAOCoordinator().update(coordinator);
    }

    /**
     * Inserts a new Coordinator row in the database.
     *
     * @param coordinator the coordinator to create
     * @throws SQLException if a database error occurs
     */
    public void createCoordinator(Coordinator coordinator) throws SQLException {
        new DAOCoordinator().create(coordinator);
    }

    /**
     * Deletes the Coordinator row from the database.
     *
     * @param coordinator the coordinator to delete
     * @throws SQLException if a database error occurs
     */
    public void deleteCoordinator(Coordinator coordinator) throws SQLException {
        new DAOCoordinator().delete(coordinator);
    }
}