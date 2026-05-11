package be.hers.info.ProjetIntegree.POJO;

/**
 * @author Rosman Loïs
 * @reviewer Nicolas Jean-François
 */

public abstract class User {
    private String login;
    private String password;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String emailAddress;
    private Address address;

    /**
     * Initialize a User with no elements
     */
    public User(){
        this.login = "";
        this.password = "";
        this.lastName = "";
        this.firstName = "";
        this.phoneNumber = "";
        this.emailAddress = "";
        this.address = new Address();
    }

    /**
     * Initialize a User with login, password, lastName, firstName, phoneNumber and emailAddress
     * @param login the login of the user
     * @param password the password of the user
     * @param lastName the lastName of the user
     * @param firstName the firstName of the user
     * @param emailAddress the email address of the user
     * @throws IllegalArgumentException if the password is null or empty
     */
    public User(String login, String password, String lastName, String firstName, String emailAddress) {
        if(password == null || password.isEmpty())
            throw new IllegalArgumentException("[POJOUser] Le mot de passe ne peut pas être vide ou null");

        this.login = login;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = "";
        this.emailAddress = emailAddress;
        this.address = null;
    }

    /**
     * Initialize a User with login, password, lastName, firstName, phoneNumber, emailAddress and address
     * @param login The id login the user
     * @param password The password of the user
     * @param lastName the lastName of the user
     * @param firstName the firstName of the user
     * @param phoneNumber the phone number of the user
     * @param emailAddress the email address of the user
     * @param address the address of the user
     * @throws IllegalArgumentException if address is null
     *                                  if password is null or empty
     */
    public User(String login, String password, String lastName, String firstName, String phoneNumber, String emailAddress,
                Address address) {
        if (address == null)
            throw new IllegalArgumentException("[POJOUser] L'adresse de l'utilisateur ne peut pas être null");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("[POJOUser] Le mot de passe de l'utilisateur ne peut pas être vide ou null");

        this.login = login;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.address = address;
    }

    /**
     * @return the login of the user
     */
    public String getLogin() {
        return login;
    }

    /**
     * Set the login of the user
     * @param login The login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the user
     * @param password The password to set
     */
    public void setPassword(String password) {
        if(password == null || password.isEmpty())
            throw new IllegalArgumentException("[POJOUser] Le mot de passe de l'utilisateur ne peut pas être vide ou null");
        this.password = password;
    }

    /**
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of the user
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the user
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the phone number of the user
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the phone number of the user
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the email address of the user
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the email address of the user
     * @param emailAddress The email address to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    /**
     * @return the address of the user
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Set the address of the user
     * @param address The address to set
     * @throws IllegalArgumentException if address is null
     */
    public void setAddress(Address address) {
        if (address == null)
            throw new IllegalArgumentException("[POJOUser] L'adresse de l'utilisateur ne peut pas être null");
        this.address = address;
    }

    /**
     * @return a String containing the login, the password, the last name, the first name, the email address,
     *         the phone number and the address
     */
    public String toString() {
        return "\nNom d'utilisateur : " + login +
                "\nMot de passe : " + password +
                "\nNom : " + lastName +
                "\nPrenom : " + firstName +
                "\nNuméro de téléphone : " + phoneNumber +
                "\nAdresse email : " + emailAddress +
                "\n" + (address == null ? "Aucune adresse" : address.toString());
    }
}
