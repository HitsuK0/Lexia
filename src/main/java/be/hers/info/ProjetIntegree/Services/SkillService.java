package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAcademicSkill;
import be.hers.info.ProjetIntegree.DAO.DAOProfessionalSkill;
import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;

import java.sql.SQLException;
import java.util.List;

public class SkillService {
    /** Retrieves all the AcademicSkills from the database
     *
     * @return a list containing all the AcademicSkills, or an empty list if the table is empty.
     * @throws SQLException in case of any SQL problems encountered while retrieving the AcademicSkills.
     */
    public List<AcademicSkill> getAllAcademicSkills() throws SQLException {
        return new DAOAcademicSkill().findAll();
    }

    /**
     * Retrieves all the ProfessionalSkills from the database
     *
     * @return a list containing all the ProfessionalSkills, or an empty list if the table is empty.
     * @throws SQLException in case of any SQL problems encountered while retrieving the ProfessionalSkills.
     */
    public List<ProfessionalSkill> getAllProfessionalSkills() throws SQLException {
        return new DAOProfessionalSkill().findAll();
    }
}
