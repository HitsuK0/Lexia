package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOEstablishment;
import be.hers.info.ProjetIntegree.POJO.Establishment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstablishementService {


    public List<Establishment> getEtablissements(){
        DAOEstablishment daoEstablishment = new DAOEstablishment();
        List<Establishment> establishments = null;
        try {
            establishments = daoEstablishment.findAll();
        } catch (SQLException e) {
            establishments = null;
        }
        return establishments;
    }


    public void createEstablishment(Establishment establishment){

    }


}
