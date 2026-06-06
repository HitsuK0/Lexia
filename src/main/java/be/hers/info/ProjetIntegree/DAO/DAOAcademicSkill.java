package be.hers.info.ProjetIntegree.DAO;


import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

/// @author Vanderheyden Quentin
/// @reviewer Nicolas Jean-Francois, Halet Louis

public class DAOAcademicSkill extends DAO<AcademicSkill>{

    /**
     * Search for an AcademicSkill where objectToSearchInDB == numAcademicSkill.
     * @param objectToSearchInDB is the id of the AcademicSkill.
     * @return null if nothing was found, else an AcademicSkill.
     * @throws SQLException if an errors occurs during the database request.
     */
    @Override
    public AcademicSkill find(int objectToSearchInDB) throws SQLException{
        String query = "SELECT numAcademicSkill, designation " +
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
            closeStatementAndResultSet(prStat, rs);
        }
        return as;
    }

    /**
     * Searches all the academic skills to the interpreter
     * @param numInterpreter the id of the interpreter we are looking for academic skills
     * @return a list containing all the academic skills to the interpreter
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<AcademicSkill> findByInterpreter(int numInterpreter) throws SQLException {
        List<AcademicSkill> academicSkills = new ArrayList<>();
        AcademicSkill academicSkill = null;
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM AcademicSkill ps " +
                "JOIN AcademicSkillInterpreter psi ON ps.numAcademicSkill = ips.numAcademicSkill " +
                "WHERE ips.numInterpreter = ?";
        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numInterpreter);
            resultSet = prStat.executeQuery();
            while (resultSet.next()) {
                academicSkill = new AcademicSkill(
                        resultSet.getInt("NumAcademicSkill"),
                        resultSet.getString("Designation"));
                academicSkills.add(academicSkill);
            }
        } finally {
            closeStatementAndResultSet(prStat, resultSet);
        }
        return academicSkills;
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
            closeStatementAndResultSet(prStat, rs);
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
        String query = "INSERT INTO AcademicSkill(designation) " +
                "VALUES (?) " +
                "RETURNING numAcademicSkill INTO ?";

        OraclePreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = (OraclePreparedStatement)connect.prepareStatement(query);
            prStat.setString(1, objectToInsertInDB.getDesignation());
            prStat.registerReturnParameter(2, OracleTypes.INTEGER);
            int nbreLigne = prStat.executeUpdate();
            if(nbreLigne > 0) {
                rs = prStat.getReturnResultSet();
                if (!rs.next()) {
                    throw new SQLException("[DAOAcademicSkill] Impossible de récupérer le numProfessionalSkill généré.");
                }
                objectToInsertInDB.setNumAcademicSkill(rs.getInt(1));
                isCreated = true;
            }
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
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
        String query = "UPDATE AcademicSkill " +
                "SET designation = ? " +
                "WHERE numAcademicSkill = ?";
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
     * Delete the line in the table where the id is the same in objectToDeleteFormDB.
     * @param objectToDeleteFormDB is object to delete in the table.
     * @return true if the line was successfully deleted otherwise false.
     * @throws SQLException if an errors occurs during the database request.
     */
    @Override
    public boolean delete(AcademicSkill objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        String query = "DELETE FROM AcademicSkill " +
                "WHERE numAcademicSkill = ?";
        PreparedStatement prStat = null;
        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumAcademicSkill());
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