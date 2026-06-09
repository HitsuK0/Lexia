package be.hers.info.ProjetIntegree.Services;

/**
 * @author Vatafu Jean
 * @reviewer Halet Louis
 */

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

    /** Creates a new AcademicSkill in the database with the given designation.
     *
     * @param designation the designation of the AcademicSkill to create
     * @throws SQLException in case of any SQL problems encountered while creating the AcademicSkill
     */
    public void addAcademicSkill(String designation) throws SQLException {
        AcademicSkill academicSkill = new AcademicSkill();
        academicSkill.setDesignation(designation);

        new DAOAcademicSkill().create(academicSkill);
    }

    /** Creates a new ProfessionalSkill in the database with the given designation.
     *
     * @param designation the designation of the ProfessionalSkill to create
     * @throws SQLException in case of any SQL problems encountered while creating the ProfessionalSkill
     */
    public void addProfessionalSkill(String designation) throws SQLException {
        ProfessionalSkill professionalSkill = new ProfessionalSkill();
        professionalSkill.setDesignation(designation);

        new DAOProfessionalSkill().create(professionalSkill);
    }
}
