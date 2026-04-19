package be.hers.info.ProjetIntegree.DAO;


import be.hers.info.ProjetIntegree.POJO.AcademicSkill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

/// @author Vanderheyden Quentin
/// @reviewer Nicolas Jean-Francois, Halet Louis

public class DAOAcademicSkill extends DAO<AcademicSkill>{



    public void closeStatement(PreparedStatement statement){
        if(statement != null){
            try{
                statement.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void closeResultSet(ResultSet resultSet){
        if(resultSet != null){
            try{
                resultSet.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Search for an AcademicSkill where objectToSearchInDB == numAcademicSkill.
     * @param objectToSearchInDB is the id of the AcademicSkill.
     * @return null if nothing was found, else an AcademicSkill.
     * @throws SQLException if an errors occurs during the database request.
     */
    @Override
    public AcademicSkill find(int objectToSearchInDB) throws SQLException{
        String query = "SELECT numAcademicSkill, designation" +
                        "FROM AcademicSkill " +
                        "WHERE numAcademicSkill = ?";
        AcademicSkill as = null;
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();
            if(rs.next()){
                as = new AcademicSkill(rs.getInt("numAcademicSkill"), rs.getString("designation"));
            }
        }
        finally {
            closeResultSet(rs);
            closeStatement(prStat);
        }
        return as;
    }

    /**
     * Get all the AcademicSkill in the table AcademicSkill.
     * @return an empty list if there is no line in AcademicSkill (table),
     * else return a initialise list with all the AcademicSkill.
     * @throws SQLException if an errors occurs during the database request.
     */
    @Override
    public List<AcademicSkill> findAll() throws SQLException{
        String query = "SELECT numAcademicSkill, designation " +
                        "FROM AcademicSkill";
        List<AcademicSkill> list = new ArrayList<AcademicSkill>();
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try {
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();
            while(rs.next()){
                AcademicSkill as = new AcademicSkill(rs.getInt("numAcademicSkill"), rs.getString("designation"));
                list.add(as);
            }
        }
        finally {
            closeResultSet(rs);
            closeStatement(prStat);
        }
        return list;
    }

    /**
     * Insert an AcademicSkill in the table
     * @param objectToInsertInDB is the AcademicSkill to insert
     * @return true if the AcademicSkill were inserted, otherwise false.
     * @throws SQLException if an errors occurs in the database request
     */
    @Override
    public boolean create(AcademicSkill objectToInsertInDB) throws SQLException {
        boolean isCreated = false;
        String query = "INSERT INTO Academic_skill(designation) VALUES (?)"; // est-ce qu'on doit mettre id ou alors auto incrémenté ?
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setString(1, objectToInsertInDB.getDesignation());
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0){
                isCreated = true;
            }
            rs = prStat.getGeneratedKeys();
            if (rs.next()) {
                int lastId = rs.getInt(1);
            }

        }
        finally{
            closeResultSet(rs);
            closeStatement(prStat);
        }
        return isCreated;
    }

    /**
     * Update the line where objectToUpdate.getId() == numAcademicSkill.
     * @param objectToUpdateInDB is the object to update.
     * @return true if the line was updated, else false.
     * @throws SQLException if an errors occurs during the database request.
     */
    @Override
    public boolean update(AcademicSkill objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        String query = "UPDATE AcademicSkill SET designation = ? WHERE numAcademicSkill = ?";
        PreparedStatement prStat = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setString(1, objectToUpdateInDB.getDesignation());
            prStat.setInt(2, objectToUpdateInDB.getNumAcademicSkill());
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0){
                isUpdated = true;
            }
        }
        finally{
            closeStatement(prStat);

        }
        return isUpdated;
    }

    /**
     * Delete the line in the table where the id and the designation are the same in objectToDeleteFormDB.
     * @param objectToDeleteFormDB is object to delete in the table.
     * @return true if the line was successfully deleted otherwise false.
     * @throws SQLException if an errors occurs during the database request.
     */
    @Override
    public boolean delete(AcademicSkill objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        String query = "DELETE FROM AcademicSkill WHERE numAcademicSkill = ? AND designation = ?";
        PreparedStatement prStat = null;
        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumAcademicSkill());
            prStat.setString(2, objectToDeleteFormDB.getDesignation());
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0){
                isDeleted = true;
            }
        }
        finally {
            closeStatement(prStat);
        }
        return isDeleted;
    }

}