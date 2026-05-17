package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOEstablishment;
import be.hers.info.ProjetIntegree.DTO.DTOEstablishment;
import be.hers.info.ProjetIntegree.POJO.Establishment;

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


    public void createEstablishment(Establishment establishment){

    }


}
