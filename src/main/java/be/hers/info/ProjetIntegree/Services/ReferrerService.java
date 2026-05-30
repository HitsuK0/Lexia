package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOEstablishment;
import be.hers.info.ProjetIntegree.DAO.DAOReferrer;
import be.hers.info.ProjetIntegree.DTO.DTOReferrer;
import be.hers.info.ProjetIntegree.POJO.Referrer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Quentin Vanderheyden
 * @reviewer Nicolas Jean-Francois, Halet Louis
 */
public class ReferrerService {

    /**
     * This function find every Referrer in the DB and convert them into DTOReferrer
     * @return a List of DTOReferrer with all Referrer
     * @throws SQLException if the database encountered a problem
     */
    public List<DTOReferrer> getAllReferrer() throws SQLException {
        DAOReferrer daoReferrer = new DAOReferrer();
        List<Referrer> list = daoReferrer.findAllWithoutEstablishment();
        List<DTOReferrer> listDTOReferrer = new ArrayList<DTOReferrer>();
        for (Referrer referrer : list) {
            DTOReferrer dtoReferrer = new DTOReferrer(referrer);
            listDTOReferrer.add(dtoReferrer);
        }
        return listDTOReferrer;
    }


    /**
     * This method attribute the numEstablishment
     * to all the Referrer who has there id in idReferrers
     * @param idReferrers represent all the id of the Referrer to update with a new Establishment.
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
}
