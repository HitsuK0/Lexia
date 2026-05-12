package be.hers.info.ProjetIntegree.DTO;

public class LoginDTO {
    private String login;
    private String password;

    /**
     * Creates an empty LoginDTO object
     */
    public LoginDTO() {
        this.login = "";
        this.password = "";
    }

    /**
     * Creates a filled LoginDTO
     * @param login the user's login
     * @param password the user's password
     */
    public LoginDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Sets the login
     * @param login the user's login
     */
    public void setLogin(String login) {this.login = login;}

    /**
     * Sets the password
     * @param password the user's password
     */
    public void setPassword(String password) {this.password = password;}

    /**
     * @return the user's login
     */
    public String getLogin() {return this.login;}

    /**
     * @return the user's password
     */
    public String getPassword() {return this.password;}
}
