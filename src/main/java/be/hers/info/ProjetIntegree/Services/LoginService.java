package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.POJO.User;

import java.sql.SQLException;

public class LoginService {

    public User getAuthentification(String login, String password) {
        User user = null;
        if(login != null && !login.isEmpty()) {
            char roleLetter = login.charAt(0);

            try {
                if(roleLetter == 'B') {
                    DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
                    user = daoBeneficiary.getBeneficiaryAuthentification(login, password);
                } else if(roleLetter == 'C' || roleLetter == 'I') {
                    DAOInterpreter daoInterpreter = new DAOInterpreter();
                    user = daoInterpreter.getInterpreterAuthentification(login, password);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }
}
