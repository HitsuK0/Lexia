package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

public class Beneficiary {
    private String name;
    private String surname;
    private String phoneNumber;
    private String emailAdress;
    private Address adresse;
    private int hourQuota;              // what to do with this
    private String educationLevel;
    private List<String> communicationLanguage;
    private List<Appointment> appointmentList;
    private Interpreter referenceInterpreter;


    /**
     *  Default constructor : TODO
     */
    public Beneficiary() {
        this.name = null;
        this.surname = null;
        this.phoneNumber = null;
        this.emailAdress = null;
        this.adresse = null;
        this.hourQuota = 0;
        this.educationLevel = null;
        this.communicationLanguage = new ArrayList<String>();
        this.appointmentList = new ArrayList<Appointment>();
        this.referenceInterpreter = null;
    }

    /**
     *  Minimal constructor : used by Coordinator to create a new user --> TODO : to complete
     */
    public Beneficiary(String emailAdress, String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.emailAdress = emailAdress;
        this.hourQuota = 0;
        this.appointmentList = new ArrayList<Appointment>();
        this.communicationLanguage = new ArrayList<String>();
    }

    /**
     * Complet constructor : TODO
     */
    public Beneficiary(String name, String surname, String phoneNumber, String emailAdress, Address adresse, String educationLevel) {
        this(emailAdress, name, surname);
        this.phoneNumber = phoneNumber;
        this.adresse = adresse;
        this.educationLevel = educationLevel;
    }

    public String getName() {

        return name;
    }

    public String getSurname() {

        return surname;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public String getEmailAdress() {

        return emailAdress;
    }

    public Address getAdresse() {

        return adresse;
    }

    public int getHourQuota() {

        return hourQuota;
    }

    public String getEducationLevel() {

        return educationLevel;
    }

    public List<String> getCommunicationLanguage() {

        return communicationLanguage;
    }

    public Interpreter getReferenceInterpreter() {

        return referenceInterpreter;
    }

    public Appointment getAppointment(Appointment appointment) { // TODO : add exception to throw when appointment not found
        if(appointment == null) {
            throw new NullPointerException();
        }

        int index = appointmentList.indexOf(a);
        if(index != -1) {
            return appointmentList.get(index);
        }

        return null; // TODO : erase
    }

    public List<Appointment> getAppointmentList() {

        return appointmentList;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) {
            throw new NullPointerException();
        }

        this.phoneNumber = phoneNumber;
    }

    public void setEmailAdress(String emailAdress) {
        if(emailAdress == null) {
            throw new NullPointerException();
        }

        this.emailAdress = emailAdress;
    }

    public void setAdresse(Address adresse) {
        if(adresse == null) {
            throw new NullPointerException();
        }

        this.adresse = adresse;
    }

    public void setHourQuota(int hourQuota) { // TODO : maybe modify ?
        if(hourQuota < 0) {
            throw new IllegalArgumentException();
        }

        this.hourQuota = hourQuota;
    }

    public void setEducationLevel(String educationLevel) {
        if(educationLevel == null) {
            throw new NullPointerException();
        }

        this.educationLevel = educationLevel;
    }

    public void setCommunicationLanguage(List<String> communicationLanguage) {
        if(communicationLanguage == null) {
            throw new NullPointerException();
        }

        if(communicationLanguage.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.communicationLanguage = communicationLanguage;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        if(appointmentList == null) {
            throw new NullPointerException();
        }

        if(appointmentList.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.appointmentList = appointmentList;
    }

    public void setName(String name) {
        if(name == null) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    public void setSurname(String surname) {
        if(surname == null) {
            throw new NullPointerException();
        }

        this.surname = surname;
    }

    public void setReferenceInterpreter(Interpreter interpreter) { // TODO : i have to verify that the interpreter exists
        if(interpreter == null) {
            throw new NullPointerException();
        }

        if(referenceInterpreter == null) {
            referenceInterpreter = interpreter;
        } else {
            throw new IllegalArgumentException(); // TODO : add exception for this case
        }
    }

    public void removeReferenceInterpreter() {
        if(referenceInterpreter != null) {
            referenceInterpreter = null;
        } else {
            throw new IllegalArgumentException(); // TODO : add exception for this case
        }
    }
}