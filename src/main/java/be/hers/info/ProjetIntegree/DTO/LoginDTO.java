package be.hers.info.ProjetIntegree.DTO;

public class LoginDTO {
    private String login;
    private String password;

    public LoginDTO() {
        this.login = "";
        this.password = "";
    }

    public LoginDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {return this.login;}

    public String getPassword() {return this.password;}
}
