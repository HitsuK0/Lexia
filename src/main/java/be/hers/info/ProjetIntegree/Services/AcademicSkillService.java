package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAcademicSkill;
import be.hers.info.ProjetIntegree.POJO.AcademicSkill;

import java.sql.SQLException;
import java.util.List;

public class AcademicSkillService {

    public List<AcademicSkill> getAllAcademicSkills() throws SQLException {
        DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();

        return daoAcademicSkill.findAll();
    }
}
