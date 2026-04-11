package be.hers.info.ProjetIntegree.DAO;


import be.hers.info.ProjetIntegree.POJO.AcademicSkill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

/// @author Vanderheyden Quentin
/// @reviewer

public class DAOAcademicSkill extends DAO<AcademicSkill>{


    @Override
    public AcademicSkill find(int objectToSearchInDB) throws SQLException{
        String query = "SELECT numAcademicSkill, designation" +
                        "FROM AcademicSkill " +
                        "WHERE numAcademicSkill = ?";
        AcademicSkill as = null;
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = ConnectionOracle.getInstance().prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();
            if(rs.next()){
                as = new AcademicSkill(rs.getInt("numAcademicSkill"), rs.getString("designation"));
            }
        }
        finally {
            if(rs != null){
                try{
                    rs.close();
                }
                catch(SQLException e){
                    System.out.println(e.getMessage());
                }
            }
            if(prStat != null){
                try{
                    prStat.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return as;
    }

    @Override
    public List<AcademicSkill> findAll() throws SQLException{
        String query = "SELECT numAcademicSkill, designation " +
                        "FROM AcademicSkill";
        List<AcademicSkill> list = new ArrayList<AcademicSkill>();
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try {
            prStat = ConnectionOracle.getInstance().prepareStatement(query);
            rs = prStat.executeQuery();
            while(rs.next()){
                AcademicSkill as = new AcademicSkill(rs.getInt("numAcademicSkill"), rs.getString("designation"));
                list.add(as);
            }
        }
        finally {
            if(rs != null){
                try{
                    rs.close();
                }
                catch(SQLException e){
                    System.out.println(e.getMessage());
                }
            }
            if(prStat != null){
                try{
                    prStat.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @Override
    public boolean create(AcademicSkill objectToInsertInDB) throws SQLException {
        boolean isCreated = false;
        String query = "INSERT INTO Academic_skill(designation) VALUES (?)"; // est-ce qu'on doit mettre id ou alors auto incrémenté ?
        PreparedStatement prStat = null;
        try{
            prStat = ConnectionOracle.getInstance().prepareStatement(query);
            prStat.setString(1, objectToInsertInDB.getDesignation());
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0){
                isCreated = true;
            }
        }
        finally{
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isCreated;
    }

    @Override
    public boolean update(AcademicSkill objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        String query = "UPDATE AcademicSkill SET designation = ? WHERE numAcademicSkill = ?";
        PreparedStatement prStat = null;
        try{
            prStat = ConnectionOracle.getInstance().prepareStatement(query);
            prStat.setString(1, objectToUpdateInDB.getDesignation());
            prStat.setInt(2, objectToUpdateInDB.getId());
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0){
                isUpdated = true;
            }
        }
        finally{
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return isUpdated;
    }

    @Override
    public boolean delete(AcademicSkill objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        String query = "DELETE FROM AcademicSkill WHERE numAcademicSkill = ? AND designation = ?";
        PreparedStatement prStat = null;
        try {
            prStat = ConnectionOracle.getInstance().prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getId());
            prStat.setString(2, objectToDeleteFormDB.getDesignation());
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0){
                isDeleted = true;
            }
        }
        finally {
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return isDeleted;
    }

}