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

    public boolean deleteAcademicSkill(int idAcademicSkill) throws SQLException {
        AcademicSkill academicSkill = new AcademicSkill();
        academicSkill.setNumAcademicSkill(idAcademicSkill);

        return new DAOAcademicSkill().delete(academicSkill);
    }

    public boolean addAcademicSkill(String designation) throws SQLException {
        AcademicSkill academicSkill = new AcademicSkill();
        academicSkill.setDesignation(designation);

        return new DAOAcademicSkill().create(academicSkill);
    }

    public boolean deleteProfessionalSkill(int idProfessionalSkill) throws SQLException {
        ProfessionalSkill professionalSkill = new ProfessionalSkill();
        professionalSkill.setNumProfessionalSkill(idProfessionalSkill);

        return new  DAOProfessionalSkill().delete(professionalSkill);
    }

    public boolean addProfessionalSkill(String designation) throws SQLException {
        ProfessionalSkill professionalSkill = new ProfessionalSkill();
        professionalSkill.setDesignation(designation);

        return new DAOProfessionalSkill().create(professionalSkill);
    }
}
