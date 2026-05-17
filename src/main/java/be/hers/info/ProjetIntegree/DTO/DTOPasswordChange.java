package be.hers.info.ProjetIntegree.DTO;

/**
 * DTO used to carry the password change form data between the HTML modal and the controller.
 * The old password is verified before the new one is applied.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */
public class DTOPasswordChange {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    /**
     * Creates an empty DTOPasswordChange.
     */
    public DTOPasswordChange() {
    }

    /**
     * Creates a fully initialised DTOPasswordChange.
     *
     * @param oldPassword     the current password entered by the user
     * @param newPassword     the new password chosen by the user
     * @param confirmPassword the confirmation of the new password
     */
    public DTOPasswordChange(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    /** @return the current password entered by the user */
    public String getOldPassword() { return oldPassword; }

    /** @param oldPassword the current password entered by the user */
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    /** @return the new password chosen by the user */
    public String getNewPassword() { return newPassword; }

    /** @param newPassword the new password chosen by the user */
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    /** @return the confirmation of the new password */
    public String getConfirmPassword() { return confirmPassword; }

    /** @param confirmPassword the confirmation of the new password */
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}