package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.*;
import be.hers.info.ProjetIntegree.DTO.DTOInterpreterProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Interpreter;

import java.sql.SQLException;

/**
 * @author Halet Louis
 * @reviewer Nicolas Jean-Francois, Wellinger Chloé
 */

public class InterpreterProfileService {
    /**
     * Builds a {@link DTOInterpreterProfile} from the connected interpreter.
     * Flattens the nested Address object into individual fields so the Thymeleaf form can bind them directly.
     * If the interpreter has no address yet (first login), the address fields in the DTO will be left empty so the form displays blank fields to fill in.
     * Initialize the AcademicSkillList and ProfessionalSkillList with all the academic and professional skills present in the database.
     * Initialize the AcademicSkillListInterpreter and ProfessionalSkillListInterpreter with all the academic and professional skills related to the interpreter.
     * @param interpreter the currently connected interpreter, must not be null
     * @return a DTOInterpreterProfile populated with the interpreter's current data
     */
    public DTOInterpreterProfile buildProfileDTO(Interpreter interpreter) {
        DTOInterpreterProfile dto = new DTOInterpreterProfile();
        dto.setNumInterpreter(interpreter.getNumInterpreter());
        dto.setLogin(interpreter.getLogin());
        dto.setLastName(interpreter.getLastName());
        dto.setFirstName(interpreter.getFirstName());
        dto.setPhoneNumber(interpreter.getPhoneNumber());
        dto.setEmailAddress(interpreter.getEmailAddress());
        dto.setWeeklyWorkHours(interpreter.getWeeklyWorkHours());

        if (interpreter.getAddress() != null) {
            Address address = interpreter.getAddress();
            dto.setPostOfficeBox(address.getPostOfficeBox());
            dto.setPostcode(address.getPostcode());
            dto.setLocality(address.getLocality());
            dto.setHamlet(address.getHamlet() != null ? address.getHamlet() : "");
        }

        DAOInterpreter daoInterpreter = new DAOInterpreter();
        DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();
        DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();
        try{
            dto.setProfessionalSkillListInterpreter(daoInterpreter.getProfessionalSkill(interpreter.getNumInterpreter()));
            dto.setAcademicSkillListInterpreter(daoInterpreter.getAcademicSkill(interpreter.getNumInterpreter()));
            dto.setProfessionalSkillList(daoProfessionalSkill.findAll());
            dto.setAcademicSkillList(daoAcademicSkill.findAll());

        }catch (SQLException e){
            e.printStackTrace();
        }

        return dto;
    }
    /**
     * Saves the profile changes submitted by the interpreter.
     * Updates the editable fields of the interpreter and its Address in the database.
     * If the interpreter has no address yet (first login after account creation by a coordinator), a new Address is created in the database and linked to the interpreter.
     * If the interpreter already has an address, it is updated in place.
     * The login, numInterpreter and password are NOT updated here.
     *
     * @param interpreter the currently connected interpreter used as base object
     * @param dto         the form data submitted by the user
     * @throws SQLException if a database error occurs during the update or creation
     */
    public void saveProfile(Interpreter interpreter, DTOInterpreterProfile dto) throws SQLException {
        interpreter.setLastName(dto.getLastName());
        interpreter.setFirstName(dto.getFirstName());
        interpreter.setPhoneNumber(dto.getPhoneNumber());
        interpreter.setEmailAddress(dto.getEmailAddress());
        interpreter.setWeeklyWorkHours(dto.getWeeklyWorkHours());
        DAOAddress daoAddress = new DAOAddress();
        Address address = interpreter.getAddress();

        if (address == null) {
            address = new Address(
                    dto.getPostcode(),
                    dto.getPostOfficeBox(),
                    dto.getLocality(),
                    dto.getHamlet(),
                    null
            );
            daoAddress.create(address);
            interpreter.setAddress(address);
        } else {
            address.setPostOfficeBox(dto.getPostOfficeBox());
            address.setPostcode(dto.getPostcode());
            address.setLocality(dto.getLocality());
            address.setHamlet(dto.getHamlet());
            daoAddress.update(address);
        }

        DAOInterpreter daoInterpreter = new DAOInterpreter();
        daoInterpreter.update(interpreter);
    }
    /**
     * Changes the password of the connected interpreter.
     * Verifies that newPassword and confirmPassword match before applying the change.
     * The new password is stored as-is — the DB trigger will hash it on UPDATE.
     *
     * @param interpreter the currently connected interpreter
     * @param dto         the password change form data
     * @return true if the password was successfully changed,
     *         false if newPassword and confirmPassword do not match
     * @throws SQLException if a database error occurs during the update
     */
    public boolean changePassword(Interpreter interpreter, DTOPasswordChange dto) throws SQLException {
        if (dto.getNewPassword() == null || !dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return false;
        }

        DAOInterpreter daoInterpreter = new DAOInterpreter();

        if(!(daoInterpreter.checkOldPassword(interpreter.getNumInterpreter(), dto.getOldPassword()))) {
            return false;
        }

        interpreter.setPassword(dto.getNewPassword());
        daoInterpreter.updatePassword(interpreter);

        return true;
    }

    /**
     *
     * Adds the professional skill designated by its number to the interpreter in question
     * @param numInterpreter the interpreter's ID
     * @param numProfessionalSkill the ID of the professional skill
     * @return true if the addition was successful, false otherwise
     * @throws SQLException if a database error occurs during the add
     */
    public boolean addProfessionalSkill(int numInterpreter, int numProfessionalSkill) throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        return  daoInterpreter.addProfessionalSkillToInterpreter(numInterpreter,numProfessionalSkill);
    }
    /**
     * Delete the professional skill designated by its number to the interpreter in question
     * @param numInterpreter the interpreter's ID
     * @param numProfessionalSkill the ID of the professional skill
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database error occurs during the deleting
     */
    public boolean deleteProfessionalSkill(int numInterpreter, int numProfessionalSkill) throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        return daoInterpreter.deleteProfessionalSkillToInterpreter(numInterpreter,numProfessionalSkill);
    }
    /**
     * Add the academic skill designated by its number to the relevant interpreter
     * @param numInterpreter the interpreter's ID
     * @param numAcademicSkill the ID of the academic skill
     * @return true if the addition was successful, false otherwise
     * @throws SQLException if a database error occurs during the add
     */
    public boolean addAcademicSkill(int numInterpreter, int numAcademicSkill) throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        return daoInterpreter.addAcademicSkillToInterpreter(numInterpreter,numAcademicSkill);
    }
    /**
     * Removes the academic skill designated by its number from the interpreter in question.
     * @param numInterpreter the interpreter's ID
     * @param numAcademicSkill the ID of the academic skill
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database error occurs during the deleting
     */
    public boolean deleteAcademicSkill(int numInterpreter, int numAcademicSkill) throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        return daoInterpreter.deleteAcademicSkillToInterpreter(numInterpreter,numAcademicSkill);
    }
}
