package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;

import java.sql.SQLException;

public class CoordinatorManagingUsersService {
    public String deleteUser(String login) throws SQLException {
        boolean isDeleted = false;

        if(login.startsWith("B")){
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            isDeleted = daoBeneficiary.delete(login);
        }
        else if(login.startsWith("I") || login.startsWith("C")){
            DAOInterpreter daoInterpreter = new DAOInterpreter();
            isDeleted = daoInterpreter.delete(login);
        }
        else{

        }
    }
}
