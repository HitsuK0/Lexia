package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOEstablishment;
import be.hers.info.ProjetIntegree.DAO.DAOReferrer;
import be.hers.info.ProjetIntegree.DTO.DTOReferrer;
import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.POJO.Referrer;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Quentin Vanderheyden
 * @reviewer Nicolas Jean-Francois, Halet Louis
 */
public class ReferrerService {

    /**
     * This function find every Referrer in the DB
     *
     * @return a List of Referrer
     * @throws SQLException if the database encountered a problem
     */
    public List<Referrer> getAllReferrer() throws SQLException {
        return new DAOReferrer().findAll();
    }

    /**
     * This method attribute the numEstablishment
     * to all the Referrer who has there id in idReferrers
     *
     * @param idReferrers      represent all the id of the Referrer to update with a new Establishment.
     * @param numEstablishment is the new Establishement to link the Referrers.
     * @throws SQLException if the DB encountered a problem
     */
    public void attributeReferrer(List<Integer> idReferrers, int numEstablishment) throws SQLException {
        DAOReferrer daoReferrer = new DAOReferrer();
        for (Integer id : idReferrers) {
            Referrer referrer = daoReferrer.find(id);
            if (referrer != null) {
                daoReferrer.update(referrer, numEstablishment);
            }
        }
    }

    /**
     * This function create a referrer with all the data needed
     * It find the establishment where it is create an link it.
     *
     * @param dtoReferrer
     * @throws SQLException
     */
    public void createReferrer(DTOReferrer dtoReferrer) throws SQLException {
        Referrer referrer = new Referrer();
        referrer.setName(dtoReferrer.getNameNewReferrer());
        referrer.setSurname(dtoReferrer.getSurnameNewReferrer());
        referrer.setAddressMail(dtoReferrer.getMailReferrer());
        referrer.setPhoneNumber(dtoReferrer.getPhoneNumberReferrer());
        DAOEstablishment daoEstablishment = new DAOEstablishment();
        referrer.setRefEstablishment(daoEstablishment.find(dtoReferrer.getNumEstablishement()));
        DAOReferrer daoReferrer = new DAOReferrer();
        daoReferrer.create(referrer);
    }

    /**
     * Updates an existing Referrer in the database with the data from the given DTOReferrer.
     *
     * @param dtoReferrer the DTOReferrer containing the updated data of the Referrer.
     * @throws SQLException in case of any SQL problems encountered while retrieving the Establishment
     *                      or updating the Referrer.
     */
    public void updateReferrer(DTOReferrer dtoReferrer) throws SQLException {
        Establishment establishment = null;
        if (dtoReferrer.getNumEstablishement() != 0) {
            establishment = new DAOEstablishment().find(dtoReferrer.getNumEstablishement());
        }

        Referrer referrer = new Referrer(establishment, dtoReferrer.getMailReferrer(), dtoReferrer.getPhoneNumberReferrer(),
                dtoReferrer.getSurnameNewReferrer(), dtoReferrer.getNameNewReferrer());
        referrer.setNumReferrer(dtoReferrer.getIdReferrer());

        new DAOReferrer().update(referrer);
    }

    /**
     * Deletes a Referrer from the database using the id contained in the given DTOReferrer,
     * only the id of the Referrer is used
     *
     * @param dtoReferrer the DTOReferrer containing the id of the Referrer to delete.
     * @throws SQLException in case of any SQL problems encountered while deleting the Referrer.
     */
    public void deleteReferrer(DTOReferrer dtoReferrer) throws SQLException {
        Referrer referrer = new Referrer();
        referrer.setNumReferrer(dtoReferrer.getIdReferrer());

        new DAOReferrer().delete(referrer);
    }
}
