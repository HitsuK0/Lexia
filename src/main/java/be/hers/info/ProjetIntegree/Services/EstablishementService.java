package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAddress;
import be.hers.info.ProjetIntegree.DAO.DAOEstablishment;
import be.hers.info.ProjetIntegree.DAO.DAOReferrer;
import be.hers.info.ProjetIntegree.DTO.DTOEstablishment;
import be.hers.info.ProjetIntegree.DTO.DTOReferrer;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.POJO.Referrer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EstablishementService {


    /**
     * This function get all the establishment available in DB.
     * It also convert the list of Establishment into a list of DTOEstablishment
     * for the front.
     * @return all the Establishment found in BD converted into DTOEtablishment.
     */
    public List<DTOEstablishment> getEtablissements(){
        DAOEstablishment daoEstablishment = new DAOEstablishment();
        List<Establishment> establishments = null;
        List<DTOEstablishment> dtoEstablishments = new ArrayList<>();
        try {
            establishments = daoEstablishment.findAllFullEstablishment();
        } catch (SQLException e) {
            establishments = null;
        }
        Iterator<Establishment> iterator = establishments.iterator();
        while(iterator.hasNext()){
            dtoEstablishments.add(new DTOEstablishment(iterator.next()));
        }
        return dtoEstablishments;
    }


    /**
     * This function create an establishment and an address in the database with the data in the param.
     * @param dtoEstablishment is the new establishment to register.
     * @throws SQLException if the database encountered an errors.
     */
    public void createEstablishment(DTOEstablishment dtoEstablishment) throws SQLException {
        Establishment establishment = new Establishment();
        establishment.setNameBuilding(dtoEstablishment.getNameBuilding());
        establishment.setPhoneNumber( dtoEstablishment.getPhoneNumber());
        establishment.setEducationLevel(dtoEstablishment.getEducationLevelInt());
        Address addresse = new Address(
                dtoEstablishment.getPostcode(),
                dtoEstablishment.getPostOfficeBox(),
                dtoEstablishment.getLocality(),
                dtoEstablishment.getHamlet(),
                null);
        DAOAddress daoAddress = new DAOAddress();
        daoAddress.create(addresse);
        establishment.setAddresses(
            List.of(
                    addresse
            )
        );


        DAOEstablishment daoEstablishment = new DAOEstablishment();

        daoEstablishment.create(establishment);
    }

    /**
     * This function create a referrer with all the data needed
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
        System.out.println(referrer);
        daoReferrer.create(referrer);
    }

    /** TODO : Faire un update de l'adresse.
     * This function makes an update of the Establishment
     * with the same id as dtoEstablishment.getNumEstablishment().
     * @param dtoEstablishment
     * @throws SQLException
     */
    public void updateEstablishment(DTOEstablishment dtoEstablishment) throws SQLException {
        DAOEstablishment daoEstablishment = new DAOEstablishment();
        daoEstablishment.update(
                new Establishment(dtoEstablishment.getNumEstablishment(),
                dtoEstablishment.getNameBuilding(),
                dtoEstablishment.getPhoneNumber())
        );
        DAOAddress daoAddress = new DAOAddress();
        Address address = daoAddress.find(dtoEstablishment.getNumAddress());
        daoAddress.update(address);
    }
}
