package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Beneficiary {
    private int beneficiaryID;
    private String name;
    private String surname;
    private String phoneNumber;
    private String emailAdress;
    private Address adresse;
    private int hourQuota;              // what to do with this
    private String educationLevel;
    private List<String> communicationLanguage;
    private Interpreter referenceInterpreter;
    private List<Appointment> appointmentList;

    // TODO : Replace all exceptions by personalised ones
    // TODO : Specifications + Exceptions

    /**
     *  Minimal constructor : used by Coordinator to create a new user
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
     * Complet constructor, just in case
     */
    public Beneficiary(String name, String surname, String phoneNumber, String emailAdress, Address adresse, String educationLevel) {
        this(emailAdress, name, surname);
        this.phoneNumber = phoneNumber;
        this.adresse = adresse;
        this.educationLevel = educationLevel;
    }

    public int getBeneficiaryID() {
        return beneficiaryID;
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

    public Appointment getAppointment(Appointment a) { // TODO : add throws
        int index = appointmentList.indexOf(a);
        if(index != -1) {
            return appointmentList.get(index);
        }

        return null;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setBeneficiaryID(int beneficiaryID) {
        this.beneficiaryID = beneficiaryID;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public void setAdresse(Address adresse) {
        this.adresse = adresse;
    }

    public void setHourQuota(int hourQuota) {
        this.hourQuota = hourQuota;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public void setCommunicationLanguage(List<String> communicationLanguage) {
        this.communicationLanguage = communicationLanguage;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setReferenceInterpreter(Interpreter i) { // TODO : i have to verify that the interpreter exists
        if(referenceInterpreter == null) {
            referenceInterpreter = i;
        } // TODO : Add exception for the case where the interprete de reference is already set
    }

    public void removeReferenceInterpreter() {
        if(referenceInterpreter != null) {
            referenceInterpreter = null;
        } // TODO : Add exception for when the interprete de reference is not set yet
    }
}