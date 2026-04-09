package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Appointment;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.POJO.Interpreter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DAOBeneficiary extends DAO<Beneficiary> {

    @Override
    public Beneficiary find(String objectToSearchInDB) throws SQLException {
        return null;
    }

    @Override
    public List<Beneficiary> findAll() throws SQLException {
        List<Beneficiary> listBeneficiary = new ArrayList<Beneficiary>();
        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "SELECT numBeneficiary, login, firstName, lastName, phoneNumber, " +
                "emailAddress, hourQuota, educationLevel, communicationLanguage, " +
                "FKnumInterpreter, FKAddress FROM Beneficiary";

        try {
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();

            InterpreterDAO interpreterDAO = new InterpreterDAO();
            AddressDAO addressDAO = new AddressDAO();
            AppointmentDAO appointmentDAO = new AppointmentDAO();

            while(rs.next()) {
                String langStr = rs.getString("communicationLanguage");
                List<String> communicationLanguage = new ArrayList<String>();
                if(langStr != null && !langStr.isEmpty()) {
                    communicationLanguage = Arrays.stream(langStr.split(","))
                                                        .toList();
                }

                Interpreter interpreter = interpreterDAO.find(rs.getInt("FKnumInterpreter"));
                List<Appointment> appointmentList = appointmentDAO.findAllByNumBeneficiary(rs.getInt("numBeneficiary"));
                Address address = addressDAO.find(rs.getInt("FKAddress"));

                Beneficiary beneficiary = new Beneficiary(rs.getInt("numBeneficiary"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("phoneNumber"), rs.getInt("hourQuota"),
                        rs.getString("emailAddress"), address,
                        rs.getInt("educationLevel"), interpreter, communicationLanguage, appointmentList);

                listBeneficiary.add(beneficiary);
            }

        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return listBeneficiary;
        }
    }

    @Override
    public boolean create(Beneficiary objectToInsertInDB) throws SQLException {
        boolean isCreated = false;
        String query = "INSERT INTO Beneficiary (numBeneficiary, firstName, " +
                "lastName, phoneNumber, emailAddress, hourQuota, educationLevel, " +
                "communicationLanguage, FKnumInterpreter, FKAddress) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement prStat = null;

        try {
            prStat = connect.prepareStatement(query);

            prStat.setInt(1, objectToInsertInDB.getNumBeneficiary());
            prStat.setString(2, objectToInsertInDB.getName());
            prStat.setString(3, objectToInsertInDB.getSurname());
            prStat.setString(4, objectToInsertInDB.getPhoneNumber());
            prStat.setString(5, objectToInsertInDB.getEmailAddress());
            prStat.setInt(6, objectToInsertInDB.getHourQuota());
            prStat.setInt(7, objectToInsertInDB.getEducationLevel());

            String communicationLanguage = objectToInsertInDB.getCommunicationLanguage()
                    .stream()
                    .collect(Collectors.joining(",", "", ""));

            prStat.setString(8, communicationLanguage);
            prStat.setInt(9, objectToInsertInDB.getInterpreter().getNumInterpreter());
            prStat.setInt(10, objectToInsertInDB.getAddress().getNumAddress());

            int numberOfLines = prStat.executeUpdate();
            if(numberOfLines > 0) {
                isCreated = true;
            }

        } finally {
            if(prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return isCreated;
    }

    @Override
    public boolean update(Beneficiary objectToUpdateInDB) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Beneficiary objectToDeleteFormDB) throws SQLException {
        return false;
    }
}
