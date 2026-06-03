package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DAO.DAOCoordinator;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;

import java.sql.SQLException;

public class CoordinatorUsersService {
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

    public int countBeneficiaries() throws SQLException {
        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        return daoBeneficiary.countNumberBeneficiaries();
    }

    public int countInterpreters() throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        return daoInterpreter.countNumberInterpreters();
    }

    public int countResas() throws SQLException {
        DAOCoordinator daoCoordinator = new DAOCoordinator();
        return daoCoordinator.countNumberResas();
    }
}
