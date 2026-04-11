package be.hers.info.ProjetIntegree.DAO;


import be.hers.info.ProjetIntegree.POJO.AcademicSkill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;


public class DAOAcademicSkill extends DAO<AcademicSkill>{


    public AcademicSkill find(int objectToSearchInDB) throws SQLException{
        String query = "select * from AcademicSkill where numAcademicSkill = ?";
        AcademicSkill as = null;
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();
            if(rs.next()){
                as = new AcademicSkill(rs.getString("designation"));
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


    public List<AcademicSkill> findAll() throws SQLException{
        String query = "select * from AcademicSkill";
        List<AcademicSkill> list = new ArrayList<AcademicSkill>();
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try {
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();
            while(rs.next()){
                AcademicSkill as = new AcademicSkill(rs.getString("designation"));
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


    public boolean create(AcademicSkill objectToInsertInDB) throws SQLException {
        boolean isCreated = false;
        String query = "insert into Academic_skill(designation) values (?)";

        try{
            PreparedStatement prStat = ConnectionOracle.getInstance().prepareStatement(query);
            prStat.setString(1, objectToInsertInDB.getDesignation());
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0){
                isCreated = true;
            }
        }
        finally{
            try{
                prStat.close();
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return isCreated;
    }


    public boolean update(AcademicSkill objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        String query = "update Academic_skill set designation = ? where id = ?";
        try{
            PreparedStatement prStat = DAOConnection.getInstance().prepareStatement(query);
            prStat.setString(1, objectToUpdateInDB.getDesignation());
            prStat.setInt(2, objectToUpdateInDB.getId());
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0){
                isUpdated = true;
            }
        }
        finally{
            try{
                prStat.close();
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return isUpdated;
    }


    public boolean delete(AcademicSkill objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        String query = "delete from Academic_skill where id = ? and designation = ?";
        try {
            PreparedStatement prStat = DAOConnection.getInstance().prepareStatement(query);
            prStat.setString(1, objectToDeleteFormDB.getID());
            prStat.setString(2, objectToDeleteFormDB.getDesignation());
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0){
                isDeleted = true;
            }
        }
        finally {
            try{
                prStat.close();
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return isDeleted;
    }

}