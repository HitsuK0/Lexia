package be.hers.info.ProjetIntegree.DAO;

/*
@author Rosman Loïs
@reviewer Nicolas Jean-Francois, Halet Louis
 */

import be.hers.info.ProjetIntegree.POJO.Establishment;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.internal.OraclePreparedStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOEstablishment extends DAO<Establishment>{
    @Override
    public Establishment find(int objectToSearchInDB) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet rs = null;
        Establishment establishmentFind = null;
        String query = """
                       SELECT numEstablishment, name, phoneNumber FROM Establishment
                       WHERE numEstablishment = ?
                       """;

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();

            if(rs.next()){
                establishmentFind = new Establishment(
                        rs.getInt("numEstablishment"),
                        rs.getString("name"),
                        rs.getString("phoneNumber")
                );
            }
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
        }

        return establishmentFind;
    }

    @Override
    public List findAll() throws SQLException {
        List<Establishment> listEstablishmentFind = new ArrayList();

        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "SELECT numEstablishment, name, phoneNumber FROM Establishment";

        try{
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();

            while(rs.next()) {
                Establishment establishmentFind = new Establishment(
                        rs.getInt("numEstablishment"),
                        rs.getString("name"),
                        rs.getString("phoneNumber")
                );

                listEstablishmentFind.add(establishmentFind);
            }
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
        }

        return listEstablishmentFind;
    }

    @Override
    public boolean create(Establishment objectToInsertInDB) throws SQLException {
        boolean isInserted = false;
        OraclePreparedStatement prStat = null;
        ResultSet rs = null;

        String query = """
                INSERT INTO Establishment (name, phoneNumber) VALUES (?, ?)
                RETURNING numEstablishment INTO ?
                """;

        try {
            prStat = (OraclePreparedStatement) connect.prepareStatement(query);
            prStat.setString(1, objectToInsertInDB.getNameBuilding());
            prStat.setString(2, objectToInsertInDB.getPhoneNumber());
            prStat.registerReturnParameter(3, OracleTypes.INTEGER);

            int nbLinesInsert = prStat.executeUpdate();
            if(nbLinesInsert > 0)
                isInserted = true;
        }
        finally {
            closeStatementAndResultSet(prStat, rs);
        }

        return isInserted;
    }

    @Override
    public boolean update(Establishment objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        PreparedStatement prStat = null;

        String query = """
                       UPDATE Establishment 
                       SET name = ?, phoneNumber = ?
                       WHERE numEstablishment = ?
                       """;

        try {
            prStat = connect.prepareStatement(query);
            prStat.setString(1, objectToUpdateInDB.getNameBuilding());
            prStat.setString(2, objectToUpdateInDB.getPhoneNumber());
            prStat.setInt(3, objectToUpdateInDB.getNumEstablishment());

            int nbLinesUpdate = prStat.executeUpdate();
            if(nbLinesUpdate > 0) {
                isUpdated = true;
            }
        } finally {
            closeStatement(prStat);
        }
        return isUpdated;
    }

    @Override
    public boolean delete(Establishment objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        PreparedStatement prStat = null;
        String query = "DELETE From Establishment WHERE NumEstablishment = ?";

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumEstablishment());
            if(prStat.executeUpdate() > 0)
                isDeleted = true;
        }
        finally{
            closeStatement(prStat);
        }

        return isDeleted;
    }
}
