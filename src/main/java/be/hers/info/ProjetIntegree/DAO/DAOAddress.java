package be.hers.info.ProjetIntegree.DAO;

/*
@author Rosman Loïs
@reviewer Nicolas Jean-Francois, Halet Louis
 */

import be.hers.info.ProjetIntegree.POJO.Address;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DAOAddress implements DAO<Address> {

    public void closeStatementAndResultSet(PreparedStatement prStat, ResultSet rs){
        if(rs != null){
            try{
                rs.close();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }

        if(prStat != null){
            try{
                prStat.close();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
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
                addressFind = new Address(objectToSearchInDB, rs.getInt("postalCode"),
                              rs.getString("postalBox"), rs.getString("locality"),
                              rs.getInt("numStreet"), rs.getString("complementOfPlace"));
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
        return List.of();
    }

    @Override
    public boolean create(Address objectToInsertInDB) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Address objectToUpdateInDB) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Address objectToDeleteFormDB) throws SQLException {
        return false;
    }
}