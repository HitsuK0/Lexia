package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return List.of();
    }

    @Override
    public boolean create(ProfessionalSkill objectToInsertInDB) throws SQLException {
        return false;
    }

    @Override
    public boolean update(ProfessionalSkill objectToUpdateInDB) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(ProfessionalSkill objectToDeleteFormDB) throws SQLException {
        return false;
    }

}
