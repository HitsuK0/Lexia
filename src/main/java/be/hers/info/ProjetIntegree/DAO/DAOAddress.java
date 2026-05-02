package be.hers.info.ProjetIntegree.DAO;

/*
@author Rosman Loïs
@reviewer Wellinger Chloé
 */

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Establishment;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.internal.OraclePreparedStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOAddress implements DAO<Address> {
    @Override
    public Address find(int objectToSearchInDB) throws SQLException {
        Address addressFind = null;
        String query = """
                       SELECT * 
                       FROM Address 
                       WHERE numAddress = ?
                       """;
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();

            if(rs.next()){
                DAOEstablishment daoEstablishment = new DAOEstablishment();
                Establishment establishmentFind = daoEstablishment.find(objectToSearchInDB);
                addressFind = new Address(rs.getInt("numAddress"), rs.getInt("postalCode"),
                              rs.getString("postalBox"), rs.getString("locality"),
                              rs.getString("hamlet"), establishmentFind);
            }
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
        }

        return addressFind;
    }

    @Override
    public List<Address> findAll() throws SQLException {
        List<Address> listAddressFind = new ArrayList<>();
        String query = """
                SELECT * 
                FROM Address
                """;
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();

            while(rs.next()){
                DAOEstablishment daoEstablishment = new DAOEstablishment();
                Establishment establishmentFind = daoEstablishment.find(rs.getInt("NumAddress"));
                Address addressFind = new Address(rs.getInt("NumAddress"), rs.getInt("postalCode"),
                        rs.getString("postalBox"), rs.getString("locality"),
                        rs.getString("hamlet"),
                        establishmentFind);
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
        boolean isInserted = false;
        OraclePreparedStatement prStat = null;
        ResultSet generateID = null;

        String query = """
                       INSERT INTO Address (postalCode, postalBox, locality, hamlet)
                       VALUES (?, ?, ?, ?) 
                       RETURNING numAddress INTO ?
                       """;

        try{
            prStat = (OraclePreparedStatement) connect.prepareStatement(query);
            prStat.setInt(1, objectToInsertInDB.getPostcode());
            prStat.setString(2, objectToInsertInDB.getPostOfficeBox());
            prStat.setString(3, objectToInsertInDB.getLocality());
            prStat.setString(4, objectToInsertInDB.getHamlet());
            prStat.registerReturnParameter(5, OracleTypes.INTEGER);

            if(prStat.executeUpdate() > 0) {
                generateID = prStat.getReturnResultSet();
                if (!generateID.next()) {
                    throw new SQLException("[DAOAddress] Impossible de récupérer le numAddress généré.");
                }
                int numAddressGenerated = generateID.getInt(1);
                objectToInsertInDB.setNumAddress(numAddressGenerated);

                isInserted = true;
            }

        }
        finally{
            closeStatementAndResultSet(prStat, generateID);
        }
        return isInserted;
    }

    @Override
    public boolean update(Address objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        String query = """
                       UPDATE Address 
                       SET postalCode = ?, postalBox = ? , locality = ?, hamlet = ? 
                       WHERE numAddress = ?
                       """;

        PreparedStatement prStat = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToUpdateInDB.getPostcode());
            prStat.setString(2, objectToUpdateInDB.getPostOfficeBox());
            prStat.setString(3, objectToUpdateInDB.getLocality());
            prStat.setString(4, objectToUpdateInDB.getHamlet());
            prStat.setInt(5, objectToUpdateInDB.getNumAddress());

            if(prStat.executeUpdate() > 0)
                isUpdated = true;
        }
        finally{
            closeStatement(prStat);
        }

        return isUpdated;
    }

    @Override
    public boolean delete(Address objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;

        String query = """
                       DELETE FROM Address 
                       WHERE NumAddress = ?
                       """;

        PreparedStatement prStat = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumAddress());

            if(prStat.executeUpdate() > 0)
                isDeleted = true;
        }
        finally{
            closeStatement(prStat);
        }

        return isDeleted;
    }
}