package be.hers.info.ProjetIntegree.POJO;

public class Interpreter {
    private int numInterperter;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    private int weeklyWorkHours;

    public Interpreter(int numInterperter, String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours) {
        this.numInterperter = numInterperter;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.weeklyWorkHours = weeklyWorkHours;
    }

    public Interpreter(String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.weeklyWorkHours = weeklyWorkHours;
    }

    public Interpreter(String lastName, String firstName, String phoneNumber, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Interpreter() {
        this.lastName = "default";
        this.firstName = "default";
        this.email = "default";
        this.phoneNumber = "default";
    }

    public int getNumInterperter() {
        return numInterperter;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getWeeklyWorkHours() {
        return weeklyWorkHours;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWeeklyWorkHours(int weeklyWorkHours) {
        this.weeklyWorkHours = weeklyWorkHours;
    }
}
