package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.*;
import be.hers.info.ProjetIntegree.DTO.DTOBeneficiaryProfile;
import be.hers.info.ProjetIntegree.DTO.DTOInterpreterProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;

import java.sql.SQLException;
import java.util.List;

public class InterpreterProfileService {
    /**
     * Builds a {@link DTOInterpreterProfile} from the connected interpreter.
     * Flattens the nested Address object into individual fields so the Thymeleaf form can bind them directly.
     * If the interpreter has no address yet (first login), the address fields in the DTO will be left empty so the form displays blank fields to fill in.
     * Init the AcademicSkillList, ProfessionalSkillList avec tous les Academic et professional skill présent dans la bd.
     * Init the AcademicSkillListInterpreter, ProfessionalSkillListInterpreter avec tous les Academic et professional skill lié à l'interprete
     * @param interpreter the currently connected interpreter, must not be null
     * @return a DTOBeneficiaryProfile populated with the beneficiary's current data
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
    public boolean changePassword(Interpreter interpreter, DTOPasswordChange dto) throws SQLException {
        if (dto.getNewPassword() == null || !dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return false;
        }

        interpreter.setPassword(dto.getNewPassword());

        DAOInterpreter daoInterpreter = new DAOInterpreter();
        daoInterpreter.updatePassword(interpreter);

        return true;
    }
    public boolean addProfessionalSkill(int numInterpreter, int numProfessionalSkill) throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        boolean res = daoInterpreter.addProfessionalSkillToInterpreter(numInterpreter,numProfessionalSkill);
        return res;
    }
    public boolean deleteProfessionalSkill(int numInterpreter, int numProfessionalSkill) throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        boolean res = daoInterpreter.deleteProfessionalSkillToInterpreter(numInterpreter,numProfessionalSkill);
        return res;
    }
    public boolean addAcademicSkill(int numInterpreter, int numAcademicSkill) throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        boolean res = daoInterpreter.addAcademicSkillToInterpreter(numInterpreter,numAcademicSkill);
        return res;
    }
    public boolean deleteAcademicSkill(int numInterpreter, int numAcademicSkill) throws SQLException {
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        boolean res = daoInterpreter.deleteAcademicSkillToInterpreter(numInterpreter,numAcademicSkill);
        return res;
    }
}
