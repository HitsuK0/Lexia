package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOProfessionalSkill extends DAO<ProfessionalSkill> {

    @Override // Mettre en int et pas en string ??
    public ProfessionalSkill find(String idToSearch) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet rs = null;
        ProfessionalSkill professionalSkillTrouve = null;

        String query = "SELECT numProfessionalSkill, designation " +
                        "FROM ProfessionalSkill" +
                        "WHERE numProfessionalSkill = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, Integer.parseInt(idToSearch));
            rs =  prStat.executeQuery();

            if (rs.next()) {
                professionalSkillTrouve = new ProfessionalSkill(
                        rs.getInt("numProfessionalSkill"),
                        rs.getString("designation")
                );
            }
        } catch (SQLException e) {
            System.out.println("[ERROR - DAOProfessionalSkill] ProfessionalSkill introuvable dans la BD");
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prStat != null) {
                prStat.close();
            }
        }
        return professionalSkillTrouve;
    }

    @Override
    public List<ProfessionalSkill> findAll() throws SQLException {
        List<ProfessionalSkill> listeProfessionalSkills = new ArrayList<>();
        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "SELECT numProfessionalSkill, designation" +
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
        } catch (SQLException e) {
            System.out.println("[ERROR - DAOProfessionalSkill] ProfessionalSkill introuvable dans la BD");
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prStat != null) {
                prStat.close();
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
        } catch (SQLException e) {
            System.out.println("[ERROR - DAOProfessionalSkill] Impossible d'insérer un ProfessionalSkill dans la BD");
        } finally {
            if  (prStat != null) {
                prStat.close();
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

            int nbLinesInsert = prStat.executeUpdate();
            if(nbLinesInsert > 0) {
                isUpdated = true;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR - DAOProfessionalSkill] ProfessionalSkill inchangé dans la BD");
        } finally {
            if (prStat != null){
                prStat.close();
            }
        }
        return isUpdated;
    }

    @Override
    public boolean delete(ProfessionalSkill objectToDeleteFormDB) throws SQLException {
        return false;
    }

}
