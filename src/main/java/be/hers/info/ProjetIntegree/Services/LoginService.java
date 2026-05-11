package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.POJO.User;

import java.sql.SQLException;

public class LoginService {

    /**
     * Authenticates a user from their login and password. The first character of
     * the login determines the user's role
     * If the login is null, empty, or has an unknown prefix, null is returned
     * @param login the user's login
     * @param password the user's password
     * @return the authenticated User, or null if authentication fails
     */
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
