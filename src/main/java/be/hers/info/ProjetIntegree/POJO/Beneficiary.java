package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

public class Beneficiary {
    private int beneficiaryID;
    private String name;
    private String surname;
    private String phoneNumber;
    private String emailAddress;
    private Address address;
    private int hourQuota;
    private int educationLevel;
    private List<String> communicationLanguage;
    private List<Appointment> appointmentList;
    private Interpreter referenceInterpreter;

    private static final int DEFAULT_HOUR_QUOTA = 12;


    /**
     * Initialize a Beneficiary with no elements
     * beneficiaryID is set to -1 by default, waiting to be assigned by the database
     */
    public Beneficiary() {
        beneficiaryID = -1;
        name = null;
        surname = null;
        phoneNumber = null;
        emailAddress = null;
        address = null;
        hourQuota = DEFAULT_HOUR_QUOTA;
        educationLevel = 0;
        communicationLanguage = new ArrayList<String>();
        appointmentList = new ArrayList<Appointment>();
        referenceInterpreter = null;
    }

    /**
     * Initialize a Beneficiary with emailAddress, name and surname
     * @param beneficiaryID the id of the Beneficiary
     * @param emailAddress the email address of the Beneficiary
     * @param name the name of the Beneficiary
     * @param surname the surname of the Beneficiary
     * @throws NullPointerException if emailAddress, name or surname is null
     */
    public Beneficiary(int beneficiaryID, String emailAddress, String name, String surname) {
        if(emailAddress == null || name == null || surname == null) {
            throw new NullPointerException();
        }

        this.beneficiaryID = beneficiaryID;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.hourQuota = DEFAULT_HOUR_QUOTA;
        this.educationLevel = 0;
        this.appointmentList = new ArrayList<Appointment>();
        this.communicationLanguage = new ArrayList<String>();
    }

    /**
     * Initialize an establishment with emailAddress, name and surname
     * @param beneficiaryID the id of the Beneficiary
     * @param name the name
     * @param surname the surname
     * @param phoneNumber the phone number
     * @param emailAddress the email address
     * @param address the address
     * @param educationLevel the level of education
     * @param communicationLanguage the list of communication languages used by the beneficiary
     * @param appointmentList the list of Appointments, can be null
     * @param referenceInterpreter the Interpreter of reference
     * @throws NullPointerException if the name, surname, phoneNumber, emailAddress, address or communicationLanguage is null
     * @throws IllegalArgumentException if communicationLanguage is empty
     */
    public Beneficiary(int beneficiaryID, String name, String surname, String phoneNumber, String emailAddress, Address address, int educationLevel,
                       List<String> communicationLanguage, List<Appointment> appointmentList, Interpreter referenceInterpreter) {

        if(name == null || surname == null || phoneNumber == null || emailAddress == null || address == null || communicationLanguage == null ||
        referenceInterpreter == null) {
            throw new NullPointerException();
        }

        if(communicationLanguage.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.beneficiaryID = beneficiaryID;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.educationLevel = educationLevel;
        this.hourQuota = DEFAULT_HOUR_QUOTA;
        this.communicationLanguage = communicationLanguage;
        this.appointmentList = (appointmentList == null) ? new ArrayList<Appointment>() : appointmentList;
        this.referenceInterpreter = referenceInterpreter;
    }

    /**
     * @return the id of the Beneficiary
     */
    public int getBeneficiaryID() {
        return beneficiaryID;
    }

    /**
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {

        return surname;
    }

    /**
     * @return the phone number
     */
    public String getPhoneNumber() {

        return phoneNumber;
    }

    /**
     * @return the email address
     */
    public String getEmailAddress() {

        return emailAddress;
    }

    /**
     * @return the address
     */
    public Address getAddress() {

        return address;
    }

    /**
     * @return the number of quota hours
     */
    public int getHourQuota() {

        return hourQuota;
    }

    /**
     * @return the education level
     */
    public int getEducationLevel() {

        return educationLevel;
    }

    /**
     * @return the communication languages
     */
    public List<String> getCommunicationLanguage() {

        return communicationLanguage;
    }

    /**
     * @return the Interpreter of reference
     */
    public Interpreter getReferenceInterpreter() {

        return referenceInterpreter;
    }

    /**
     * @param appointment the appointment to search for
     * @return the appointment searched for
     * @throws NullPointerException if appointment is null
     * @throws IllegalArgumentException if appointment not found
     */
    public Appointment getAppointment(Appointment appointment) {
        if(appointment == null) {
            throw new NullPointerException();
        }

        int index = appointmentList.indexOf(appointment);
        if(index != -1) {
            return appointmentList.get(index);
        }

        throw new IllegalArgumentException();
    }

    /**
     * @return the list of appointments
     */
    public List<Appointment> getAppointmentList() {

        return appointmentList;
    }

    /**
     * @param beneficiaryID the id to set
     * @throws IllegalArgumentException if beneficiaryID is negative
     */
    public void setBeneficiaryID(int beneficiaryID) {
        if(beneficiaryID < 0) {
            throw new IllegalArgumentException();
        }

        this.beneficiaryID = beneficiaryID;
    }

    /**
     * @param phoneNumber the phone number to set
     * @throws NullPointerException if phoneNumber is null
     */
    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) {
            throw new NullPointerException();
        }

        this.phoneNumber = phoneNumber;
    }

    /**
     * @param emailAddress the email address to set
     * @throws NullPointerException if emailAddress is null
     */
    public void setEmailAddress(String emailAddress) {
        if(emailAddress == null) {
            throw new NullPointerException();
        }

        this.emailAddress = emailAddress;
    }

    /**
     * @param address the address to set
     * @throws NullPointerException if address is null
     */
    public void setAddress(Address address) {
        if(address == null) {
            throw new NullPointerException();
        }

        this.address = address;
    }

    /**
     * @param hourQuota the hour quota to set
     * @throws IllegalArgumentException if hourQuota is negative
     */
    public void setHourQuota(int hourQuota) { // TODO : maybe modify ?
        if(hourQuota < 0) {
            throw new IllegalArgumentException();
        }

        this.hourQuota = hourQuota;
    }

    /**
     * @param educationLevel the level of education to set
     * @throws IllegalArgumentException if educationLevel is negative
     */
    public void setEducationLevel(int educationLevel) {
        if(educationLevel < 0) {
            throw new IllegalArgumentException();
        }

        this.educationLevel = educationLevel;
    }

    /**
     * @param communicationLanguage the list of communication languages to set
     * @throws NullPointerException if communicationLanguages is null
     * @throws IllegalArgumentException if communicationLanguages is empty
     */
    public void setCommunicationLanguage(List<String> communicationLanguage) {
        if(communicationLanguage == null) {
            throw new NullPointerException();
        }

        if(communicationLanguage.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.communicationLanguage = communicationLanguage;
    }

    /**
     * @param appointmentList the list of appointments to set
     * @throws NullPointerException if appointmentList is null
     * @throws IllegalArgumentException if appointmentList is empty
     */
    public void setAppointmentList(List<Appointment> appointmentList) {
        if(appointmentList == null) {
            throw new NullPointerException();
        }

        if(appointmentList.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.appointmentList = appointmentList;
    }

    /**
     * @param name the name to set
     * @throws NullPointerException if name is null
     */
    public void setName(String name) {
        if(name == null) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * @param surname the surname to set
     * @throws NullPointerException if surname is null
     */
    public void setSurname(String surname) {
        if(surname == null) {
            throw new NullPointerException();
        }

        this.surname = surname;
    }

    /**
     * @param interpreter the interpreter of reference to set
     * @throws NullPointerException if interpreter is null
     * @throws IllegalArgumentException if referenceInterpreter is not null
     */
    public void setReferenceInterpreter(Interpreter interpreter) {
        if(interpreter == null) {
            throw new NullPointerException();
        }

        if(referenceInterpreter != null) {
            throw new IllegalArgumentException();
        }

        referenceInterpreter = interpreter;
    }
}