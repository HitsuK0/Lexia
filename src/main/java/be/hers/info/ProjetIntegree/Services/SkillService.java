package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAcademicSkill;
import be.hers.info.ProjetIntegree.DAO.DAOProfessionalSkill;
import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;

import java.sql.SQLException;
import java.util.List;

public class SkillService {
    public List<AcademicSkill> getAllAcademicSkills() throws SQLException {
        return new DAOAcademicSkill().findAll();
    }

    public List<ProfessionalSkill> getAllProfessionalSkills() throws SQLException {
        return new DAOProfessionalSkill().findAll();
    }
}
