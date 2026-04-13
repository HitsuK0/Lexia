package be.hers.info.ProjetIntegree.DAO;

/*
@author Rosman Loïs
@reviewer Nicolas Jean-Francois, Halet Louis
 */

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Establishment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOAddress implements DAO<Address> {

    public void closeStatement(PreparedStatement prStat){
        if(prStat != null){
            try{
                prStat.close();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public void closeStatementAndResultSet(PreparedStatement prStat, ResultSet rs){
        if(rs != null){
            try{
                rs.close();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }

        closeStatement(prStat);
    }

    @Override
    public Address find(int objectToSearchInDB) throws SQLException {
        Address addressFind = null;
        String query = "SELECT * FROM Address WHERE numAddress = ?";
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();

            if(rs.next()){
                Establishment establishmentFind = daoEstablishment.find(objectToSearchInDB); //renvoie l'établissement qui contient l'adresse d'id objectToSearchInDB
                addressFind = new Address(objectToSearchInDB, rs.getInt("postalCode"),
                              rs.getString("postalBox"), rs.getString("locality"),
                              rs.getInt("numStreet"), rs.getString("complementOfPlace"),
                              establishmentFind); //A voir comment Louis appelle le champ NumStreet
                //ajouter le constructeur adéquat
            }
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
        }

        return addressFind;
    }

    @Override
    public List findAll() throws SQLException {
        List<Address> listAddressFind = new ArrayList<>();
        String query = "SELECT * FROM Address";
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();

            if(rs.next()){
                Establishment establishmentFind = daoEstablishment.find(rs.getInt("NumAddress")); //renvoie l'établissement qui contient l'adresse d'id objectToSearchInDB
                Address addressFind = new Address(rs.getInt("NumAddress"), rs.getInt("postalCode"),
                        rs.getString("postalBox"), rs.getString("locality"),
                        rs.getInt("numStreet"), rs.getString("complementOfPlace"),
                        establishmentFind); //A voir comment Louis appelle le champ NumStreet
                //ajouter le constructeur adéquat
                listAddressFind.add(addressFind);
            }
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
        }

        return listAddressFind;
    }

    /**
     * Precondition: the establishment at the address provided is already in the database
     */
    @Override
    public boolean create(Address objectToInsertInDB) throws SQLException {
        String query = "INSERT INTO Address (numAddress, postalCode, postalBox, locality, numStreet, complementOfPlace) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement prStat = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToInsertInDB.getNumAddress());
            prStat.setInt(2, objectToInsertInDB.getPostcode());
            prStat.setString(3, objectToInsertInDB.getPostOfficeBox());
            prStat.setString(4, objectToInsertInDB.getLocality());
            prStat.setInt(5, objectToInsertInDB.getNumAddress());
            prStat.setString(6, objectToInsertInDB.getHamlet());

            if(prStat.executeUpdate() > 0)
                return true;
            return false;
        }
        finally{
            closeStatement(prStat);
        }
    }

    @Override
    public boolean update(Address objectToUpdateInDB) throws SQLException {

    }

    @Override
    public boolean delete(Address objectToDeleteFormDB) throws SQLException {
        return false;
    }
}