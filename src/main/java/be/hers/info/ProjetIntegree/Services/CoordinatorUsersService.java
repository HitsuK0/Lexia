package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAddress;
import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DAO.DAOCoordinator;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.DTO.DTOUser;
import be.hers.info.ProjetIntegree.DTO.DTOUserAdd;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.POJO.Coordinator;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * @authors Rosman Loïs
 * @reviewer Nicolas Jean-François
 */
public class CoordinatorUsersService {

    private static final Logger logger = LoggerFactory.getLogger(CoordinatorUsersService.class);

    /**
     * @return A list containing all the beneficiaries without his password
     * @throws SQLException If an SQL error occurs with this method.
     */
    public List<DTOUser> findAllBeneficiary() throws SQLException {
        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        return daoBeneficiary.findAllDTOBeneficiaryUsers();
    }

    /**
     * @return A list containing all the interpreters without his password, his absences, his appointmentsList,
     * and his beneficiariesList
     * @throws SQLException If an SQL error occurs with this method.
     */
    public List<DTOUser> findAllInterpreters() throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        return daoInterpreter.findAllDTOInterpreterUsers();
    }

    /**
     * @return A list containing all the resas without his password, his absences, his appointmentsList,
     * and his beneficiariesList
     * @throws SQLException If an SQL error occurs with this method.
     */
    public List<DTOUser> findAllResas() throws SQLException {
        DAOCoordinator daoCoordinator = new DAOCoordinator();
        return daoCoordinator.findAllDTOResaUsers();
    }

    /**
     * @return A list containing all the coordinators without his password, his absences, his appointmentsList,
     * and his beneficiariesList
     * @throws SQLException If an SQL error occurs with this method.
     */
    public List<DTOUser> findAllCoordinators() throws SQLException {
        DAOCoordinator daoCoordinator = new DAOCoordinator();
        return daoCoordinator.findAllDTOCoordinatorUsers();
    }

    /**
     * Return the number of beneficiaries in the database
     *
     * @return the number of beneficiaries in the database
     * @throws SQLException If an SQL error occurs with this method.
     */
    public int countBeneficiaries() throws SQLException {
        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        return daoBeneficiary.countNumberBeneficiaries();
    }

    /**
     * Return the number of interpreters in the database
     *
     * @return the number of interpreters in the database
     * @throws SQLException If an SQL error occurs with this method.
     */
    public int countInterpreters() throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        return daoInterpreter.countNumberInterpreters();
    }

    /**
     * Return the number of resas in the database
     *
     * @return the number of resas in the database
     * @throws SQLException If an SQL error occurs with this method.
     */
    public int countResas() throws SQLException {
        DAOCoordinator daoCoordinator = new DAOCoordinator();
        return daoCoordinator.countNumberResas();
    }

    /**
     * Add a resa or a coordinator to the database
     *
     * @param dtoUserAdd the resa or the coordinator to add in the database
     * @return a string explaining if the add is a success or a failure
     * @throws SQLException If an SQL error occurs with this method.
     */
    public String addResaCoordinator(DTOUserAdd dtoUserAdd, boolean isAdmin) throws SQLException {
        Address newAddress = new Address(dtoUserAdd.getPostcode(), dtoUserAdd.getPostOfficeBox(),
                dtoUserAdd.getLocality(), dtoUserAdd.getHamlet());
        DAOAddress daoAddress = new DAOAddress();
        daoAddress.create(newAddress);

        Interpreter newInterpreter = new Interpreter(dtoUserAdd, newAddress);
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        try {
            if (!daoInterpreter.create(newInterpreter)) {
                daoAddress.delete(newAddress);
                return "Ajout interprète échoué";
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la création de l'interprète pour le resa/coordinateur", e);
            daoAddress.delete(newAddress);
            return "Ajout interprète échoué";
        }

        try {
            Coordinator newCoordinator = new Coordinator(dtoUserAdd, newAddress, isAdmin);
            DAOCoordinator daoCoordinator = new DAOCoordinator();
            if (!daoCoordinator.create(newCoordinator)) {
                daoInterpreter.delete(newInterpreter);
                daoAddress.delete(newAddress);
                return "Ajout coordinatrice échoué";
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la création du coordinateur/resa", e);
            daoInterpreter.delete(newInterpreter);
            daoAddress.delete(newAddress);
            return "Ajout coordinatrice échoué";
        }

        return "Ajout réussi";
    }

    /**
     * Add an interpreter to the database
     *
     * @param dtoUserAdd the interpreter to add in the database
     * @return a string explaining if the add is a success or a failure
     * @throws SQLException If an SQL error occurs with this method.
     */
    public String addInterpreter(DTOUserAdd dtoUserAdd) throws SQLException {
        Address newAddress = new Address(dtoUserAdd.getPostcode(), dtoUserAdd.getPostOfficeBox(),
                dtoUserAdd.getLocality(), dtoUserAdd.getHamlet());
        DAOAddress daoAddress = new DAOAddress();
        daoAddress.create(newAddress);

        Interpreter newInterpreter = new Interpreter(dtoUserAdd, newAddress);
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        try {
            if (!daoInterpreter.create(newInterpreter)) {
                daoAddress.delete(newAddress);
                return "Ajout interprète échoué";
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la création de l'interprète", e);
            daoAddress.delete(newAddress);
            return "Ajout interprète échoué";
        }

        return "Ajout réussi";
    }

    /**
     * Add a beneficiary to the database
     *
     * @param dtoUserAdd the beneficiary to add in the database
     * @return a string explaining if the add is a success or a failure
     * @throws SQLException If an SQL error occurs with this method.
     */
    public String addBeneficiary(DTOUserAdd dtoUserAdd) throws SQLException {
        Address newAddress = new Address(dtoUserAdd.getPostcode(), dtoUserAdd.getPostOfficeBox(),
                dtoUserAdd.getLocality(), dtoUserAdd.getHamlet());
        DAOAddress daoAddress = new DAOAddress();
        daoAddress.create(newAddress);

        Beneficiary newBeneficiary = new Beneficiary(dtoUserAdd, newAddress);
        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        try {
            if (!daoBeneficiary.create(newBeneficiary)) {
                daoAddress.delete(newAddress);
                return "Ajout bénéficiaire échoué";
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la création du bénéficiaire", e);
            daoAddress.delete(newAddress);
            return "Ajout bénéficiaire échoué";
        }

        return "Ajout réussi";
    }
}
