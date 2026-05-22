package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOProfessionalSkill;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;

import java.sql.SQLException;
import java.util.List;

public class ProfessionalSkillService {

    public List<ProfessionalSkill> getAllProfessionalSkills() throws SQLException {
        DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();

        return daoProfessionalSkill.findAll();
    }
}
