package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAddress;
import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DTO.DTOBeneficiaryProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;

import java.sql.SQLException;

/**
 * Service handling the beneficiary's profile operations:
 * building the profile DTO from a connected beneficiary, saving profile changes and changing the password.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */
public class BeneficiaryProfileService {

    /**
     * Builds a {@link DTOBeneficiaryProfile} from the connected beneficiary.
     * Flattens the nested Address object into individual fields so the Thymeleaf form can bind them directly.
     * If the beneficiary has no address yet (first login), the address fields
     * in the DTO will be left empty so the form displays blank fields to fill in.
     *
     * @param beneficiary the currently connected beneficiary, must not be null
     * @return a DTOBeneficiaryProfile populated with the beneficiary's current data
     */
    public DTOBeneficiaryProfile buildProfileDTO(Beneficiary beneficiary) {
        DTOBeneficiaryProfile dto = new DTOBeneficiaryProfile();
        dto.setNumBeneficiary(beneficiary.getNumBeneficiary());
        dto.setLogin(beneficiary.getLogin());
        dto.setLastName(beneficiary.getLastName());
        dto.setFirstName(beneficiary.getFirstName());
        dto.setPhoneNumber(beneficiary.getPhoneNumber());
        dto.setEmailAddress(beneficiary.getEmailAddress());
        dto.setHourQuota(beneficiary.getHourQuota());
        dto.setEducationLevel(beneficiary.getEducationLevel());
        dto.setCommunicationLanguage(beneficiary.getCommunicationLanguage());

        if (beneficiary.getAddress() != null) {
            Address address = beneficiary.getAddress();
            dto.setPostOfficeBox(address.getPostOfficeBox());
            dto.setPostcode(address.getPostcode());
            dto.setLocality(address.getLocality());
            dto.setHamlet(address.getHamlet() != null ? address.getHamlet() : "");
        }

        return dto;
    }

    /**
     * Saves the profile changes submitted by the beneficiary.
     * Updates all editable fields of the Beneficiary and its Address in the database,
     * including hourQuota, educationLevel and communicationLanguage.
     * If the beneficiary has no address yet (first login after account creation by
     * a coordinator), a new Address is created in the database and linked to the beneficiary.
     * If the beneficiary already has an address, it is updated in place.
     * The login, numBeneficiary and password are NOT updated here.
     *
     * @param beneficiary the currently connected beneficiary used as base object
     * @param dto         the form data submitted by the user
     * @throws SQLException if a database error occurs during the update or creation
     */
    public void saveProfile(Beneficiary beneficiary, DTOBeneficiaryProfile dto) throws SQLException {
        beneficiary.setLastName(dto.getLastName());
        beneficiary.setFirstName(dto.getFirstName());
        beneficiary.setPhoneNumber(dto.getPhoneNumber());
        beneficiary.setEmailAddress(dto.getEmailAddress());
        beneficiary.setHourQuota(dto.getHourQuota());
        beneficiary.setEducationLevel(dto.getEducationLevel());
        beneficiary.setCommunicationLanguage(dto.getCommunicationLanguage());

        DAOAddress daoAddress = new DAOAddress();
        Address address = beneficiary.getAddress();

        if (address == null) {
            address = new Address(
                    dto.getPostcode(),
                    dto.getPostOfficeBox(),
                    dto.getLocality(),
                    dto.getHamlet(),
                    null
            );
            daoAddress.create(address);
            beneficiary.setAddress(address);
        } else {
            address.setPostOfficeBox(dto.getPostOfficeBox());
            address.setPostcode(dto.getPostcode());
            address.setLocality(dto.getLocality());
            address.setHamlet(dto.getHamlet());
            daoAddress.update(address);
        }

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        daoBeneficiary.update(beneficiary);
    }

    /**
     * Changes the password of the connected beneficiary.
     * Verifies that newPassword and confirmPassword match before applying the change.
     * The new password is stored as-is — the DB trigger will hash it on UPDATE.
     *
     * @param beneficiary the currently connected beneficiary
     * @param dto         the password change form data
     * @return true if the password was successfully changed,
     *         false if newPassword and confirmPassword do not match
     * @throws SQLException if a database error occurs during the update
     */
    public boolean changePassword(Beneficiary beneficiary, DTOPasswordChange dto) throws SQLException {
        if (dto.getNewPassword() == null || !dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return false;
        }

        beneficiary.setPassword(dto.getNewPassword());

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        daoBeneficiary.updatePassword(beneficiary);

        return true;
    }
}