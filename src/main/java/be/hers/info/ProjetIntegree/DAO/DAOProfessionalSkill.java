package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOProfessionalSkill extends DAO<ProfessionalSkill> {

    @Override
    public ProfessionalSkill find(int idToSearch) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet rs = null;
        ProfessionalSkill professionalSkillTrouve = null;

        String query = "SELECT numProfessionalSkill, designation " +
                        "FROM ProfessionalSkill " +
                        "WHERE numProfessionalSkill = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, idToSearch);
            rs =  prStat.executeQuery();

            if (rs.next()) {
                professionalSkillTrouve = new ProfessionalSkill(
                        rs.getInt("numProfessionalSkill"),
                        rs.getString("designation")
                );
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return professionalSkillTrouve;
    }

    @Override
    public List<ProfessionalSkill> findAll() throws SQLException {
        List<ProfessionalSkill> listeProfessionalSkills = new ArrayList<>();
        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "SELECT numProfessionalSkill, designation " +
                "FROM ProfessionalSkill";

        try {
            prStat = connect.prepareStatement(query);
            rs  =  prStat.executeQuery();

            while(rs.next()) {
                ProfessionalSkill professionalSkillTrouve = new ProfessionalSkill(
                        rs.getInt("numProfessionalSkill"),
                        rs.getString("designation")
                );
                listeProfessionalSkills.add(professionalSkillTrouve);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return  listeProfessionalSkills;
    }

    @Override
    public boolean create(ProfessionalSkill objectToInsertInDB) throws SQLException {
        boolean isInserted = false;
        PreparedStatement prStat = null;

        String query = "INSERT INTO ProfessionalSkill (numProfessionalSkill, designation) VALUES (?, ?)";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToInsertInDB.getNumProfessionalSkill());
            prStat.setString(2, objectToInsertInDB.getDesignation());

            int nbLinesInsert = prStat.executeUpdate();
            if (nbLinesInsert > 0) {
                isInserted = true;
            }
        } finally {
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return isInserted;
    }

    @Override
    public boolean update(ProfessionalSkill objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        PreparedStatement prStat = null;

        String query = "UPDATE ProfessionalSkill SET designation = ? WHERE numProfessionalSkill = ?";

        try{
            prStat = connect.prepareStatement(query);
            prStat.setString(1, objectToUpdateInDB.getDesignation());
            prStat.setInt(2, objectToUpdateInDB.getNumProfessionalSkill());

            int nbLinesUpdate = prStat.executeUpdate();
            if(nbLinesUpdate > 0) {
                isUpdated = true;
            }
        } finally {
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return isUpdated;
    }

    @Override
    public boolean delete(ProfessionalSkill objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        PreparedStatement prStat = null;

        String query = "DELETE FROM ProfessionalSkill WHERE numProfessionalSkill = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumProfessionalSkill());

            int nbLinesDelete = prStat.executeUpdate();
            if(nbLinesDelete > 0) {
                isDeleted = true;
            }
        } finally {
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return isDeleted;
    }

}
