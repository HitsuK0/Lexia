package be.hers.info.ProjetIntegree.Services;

/**
 * @authors Rosman Loïs
 * @reviewer Nicolas Jean-François, Halet Louis
 */

import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DAO.DAOCoordinator;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.DTO.DTOBeneficiaryUsers;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorUsersService {
    public List<Beneficiary> findAllBeneficiary(){
        List<DTOBeneficiaryUsers> beneficiaries = new ArrayList<>();
    }

    /**
     * Delete a user with his login
     * @param login the login of the user to delete
     * @return true if the user is deleted. Otherwise, return false
     * @throws SQLException If an SQL error occurs with this method.
     */
    public boolean deleteUser(String login) throws SQLException {
        boolean isDeleted = false;

        if(login.startsWith("B")){
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            isDeleted = daoBeneficiary.delete(login);
        }
        else if(login.startsWith("I") || login.startsWith("C")){
            DAOInterpreter daoInterpreter = new DAOInterpreter();
            isDeleted = daoInterpreter.delete(login);
        }

        return isDeleted;
    }

    /**
     * Return the number of beneficiaries in the database
     * @return the number of beneficiaries in the database
     * @throws SQLException If an SQL error occurs with this method.
     */
    public int countBeneficiaries() throws SQLException {
        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        return daoBeneficiary.countNumberBeneficiaries();
    }

    /**
     * Return the number of interpreters in the database
     * @return the number of interpreters in the database
     * @throws SQLException If an SQL error occurs with this method.
     */
    public int countInterpreters() throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        return daoInterpreter.countNumberInterpreters();
    }

    /**
     * Return the number of resas in the database
     * @return the number of resas in the database
     * @throws SQLException If an SQL error occurs with this method.
     */
    public int countResas() throws SQLException {
        DAOCoordinator daoCoordinator = new DAOCoordinator();
        return daoCoordinator.countNumberResas();
    }
}
