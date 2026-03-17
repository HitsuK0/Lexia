package be.hers.info.ProjetIntegree.DAO;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;
import POJO.POJOAcademicSkill;


public class DAOAcademicSkill extends DAO<POJOAcademicSkill>{

    /**
     * Searches for the object whosppakce identifier matches the String passed as a parameter.
     * @param objectToSearchInDB the identifier of the object to search for in the table.
     * @return The object whose identifier matches the String passed as a parameter. null if there is no object matching the String passed as a parameter.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public POJOAcademicSkill find(String objectToSearchInDB) throws SQLException{
        String query = "select * from AcademicSkill where ID_academic_skill = ?";
        POJOAcademicSkill as = null;
        try{
            PreparedStatement prStat = ConnectionOracle.getInstance().prepareStatement(query);
            prStat.setString(1, objectToSearchInDB);
            ResultSet rs = prStat.executeQuery();
            if(rs.next()){
                as = new POJOAcademicSkill(rs.getInt("ID_academic_skill"), rs.getString("designation"));
            }
        }
        catch(SQLException e){
            throw new SQLException("Erreur : Couldn't find AcademicSkill -> "+e.getMessage());
            // à modifier.
        }
        finally {
            resultSet.close();
            prStat.close();
        }

        return as;
    }
    /**
     * Create a list containing all the objects in the table.
     * @return a list containing all the objects in the table or an empty list if the table is empty.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<POJOAcademicSkill> findAll() throws SQLException{
        String query = "select * from AcademicSkill";
        List<POJOAcademicSkill> list = new ArrayList<POJOAcademicSkill>();
        try {
            PreparedStatement prStat = ConnectionOracle.getInstance().prepareStatement(query);
            ResultSet rs = prStat.executeQuery();
            while(rs.next()){
                POJOAcademicSkill as = new POJOAcademicSkill(rs.getString("designation"));
                list.add(as);
            }
        }
        catch(SQLException e){
            throw new SQLException("Erreur : Couldn't find all AcademicSkill -> "+e.getMessage());
            // à modifier
        }
        finally {
            prStat.close();
            rs.close();
        }
        return list;
    }
    /**
     * Precondition: the object passed as a parameter cannot be null.
     * Adds the object passed as a parameter to the table.
     * @param objectToInsertInDB the object to be inserted into the table.
     * @return true if the object was successfully inserted, false otherwise.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public boolean create(POJOAcademicSkill objectToInsertInDB) throws SQLException {
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
        catch(SQLException e){
            throw new SQLException("Erreur : Couldn't find all AcademicSkill -> "+e.getMessage());
            // à modifier
        }
        finally{
            prStat.close();
            rs.close();
        }
        return isCreated;
    }
    /**
     * Precondition: the object passed as a parameter cannot be null.
     * Updates all object fields in the table (except its identifier) ​​that correspond to the object identifier passed as a parameter.
     * @param objectToUpdateInDB the object containing the identifier and the fields to be updated in the table.
     * @return true if the object has been successfully updated, false otherwise.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public boolean update(POJOAcademicSkill objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        String query = "update Academic_skill set designation = ?";
        try{
            PreparedStatement prStat = DAOConnection.getInstance().prepareStatement(query);
            prStat.setString(1, objectToUpdateInDB.getDesignation());
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0){
                isUpdated = true;
            }
        }
        catch(SQLException e){
            throw new SQLException("Erreur : Couldn't udpate AcademicSkill -> "+e.getMessage());
            // à modifier
        }
        finally{
            prStat.close();
        }
        return isUpdated;
    }
    /**
     * Precondition: the object passed as a parameter cannot be null.
     * Deletes the object where its identifier matches the identifier of the object passed as a parameter.
     * @param objectToDeleteFormDB the object to be deleted from the table.
     * @return true if the object was successfully deleted, false otherwise.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public boolean delete(POJOAcademicSkill objectToDeleteFormDB) throws SQLException {
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
        catch(SQLException e){
            throw new SQLException("Erreur : Couldn't delete AcademicSkill -> "+e.getMessage());
            // à modifier
        }
        finally {
            prStat.close();
        }
        return isDeleted;
    }

}