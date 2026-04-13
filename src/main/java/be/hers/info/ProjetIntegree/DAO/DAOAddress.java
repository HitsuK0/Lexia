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

    //Bien ou pas ces deux méthodes ? Peut-être mettre les signatures dans la classe abstract DAO si on décide que tous le monde les adoptes
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
                DAOEstablishment daoEstablishment = new DAOEstablishment<>();
                Establishment establishmentFind = daoEstablishment.find(objectToSearchInDB); //renvoie l'établissement qui contient l'adresse d'id objectToSearchInDB
                addressFind = new Address(objectToSearchInDB, rs.getInt("postalCode"),
                              rs.getString("postalBox"), rs.getString("locality"), rs.getString("complementOfPlace"),
                              establishmentFind);
                //ajouter le constructeur adéquat (et supprimer le champ numStreet du POJO Address
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
                DAOEstablishment daoEstablishment = new DAOEstablishment<>();
                Establishment establishmentFind = daoEstablishment.find(rs.getInt("NumAddress")); //renvoie l'établissement qui contient l'adresse d'id objectToSearchInDB
                Address addressFind = new Address(rs.getInt("NumAddress"), rs.getInt("postalCode"),
                        rs.getString("postalBox"), rs.getString("locality"),
                        rs.getString("complementOfPlace"),
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
        String query = "INSERT INTO Address (numAddress, postalCode, postalBox, locality, complementOfPlace) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement prStat = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToInsertInDB.getNumAddress());
            prStat.setInt(2, objectToInsertInDB.getPostcode());
            prStat.setString(3, objectToInsertInDB.getPostOfficeBox());
            prStat.setString(4, objectToInsertInDB.getLocality());
            prStat.setString(5, objectToInsertInDB.getHamlet());

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
        String query = "UPDATE Address SET postalCode = ?, postalBox ? , locality = ?, complementOfPlace = ? WHERE numAddress = ?";

        PreparedStatement prStat = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToUpdateInDB.getPostcode());
            prStat.setString(2, objectToUpdateInDB.getPostOfficeBox());
            prStat.setString(3, objectToUpdateInDB.getLocality());
            prStat.setString(4, objectToUpdateInDB.getHamlet());
            prStat.setInt(5, objectToUpdateInDB.getNumAddress());

            if(prStat.executeUpdate() > 0)
                return true;
            return false;
        }
        finally{
            closeStatement(prStat);
        }
    }

    @Override
    public boolean delete(Address objectToDeleteFormDB) throws SQLException {
        String query = "DELETE FROM Address WHERE NumAddress = ?";

        PreparedStatement prStat = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumAddress());

            if(prStat.executeUpdate() > 0)
                return true;
            return false;
        }
        finally{
            closeStatement(prStat);
        }
    }
}