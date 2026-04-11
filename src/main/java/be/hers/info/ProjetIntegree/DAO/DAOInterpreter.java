package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Interpreter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOInterpreter extends DAO<Interpreter> {

    @Override
    public Interpreter find(int idToSearchInDB) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet rs = null;
        Interpreter interpreterFind = null;

        String query = "SELECT * FROM Interpreter WHERE numInterpreter = ?";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, idToSearchInDB);
            rs = prStat.executeQuery();

            if(rs.next()) {
                DAOAddress daoAddress = new DAOAddress();
                Address address = daoAddress.find(rs.getInt"numAdress"));

                interpreterFind = new Interpreter(
                        rs.getInt("numInterpreter"),
                        rs.getString("lastName"),
                        rs.getString("firstName"),
                        rs.getString("emailAddress"),
                        rs.getString("phoneNumber"),
                        rs.getInt("weeklyWorkHours"),
                        address
                );
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return interpreterFind;
    }

    @Override
    public List<Interpreter> findAll() throws SQLException {
        List<Interpreter> interpreterList = new ArrayList<>();
        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Interpreter";

        try {
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();

            while(rs.next()) {
                DAOAddress daoAddress = new DAOAddress();
                Address address = daoAddress.find(rs.getInt("numAdress"));

                Interpreter interpreterFind = new Interpreter(
                        rs.getInt("numInterpreter"),
                        rs.getString("lastName"),
                        rs.getString("firstName"),
                        rs.getString("emailAddress"),
                        rs.getString("phoneNumber"),
                        rs.getInt("weeklyWorkHours"),
                        address
                );
                interpreterList.add(interpreterFind);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return interpreterList;
    }

    @Override
    public boolean create(Interpreter objectToInsertInDB) throws SQLException {
        boolean isInserted = false;
        PreparedStatement prStat = null;

        String query = "INSERT INTO Interpreter (lastName, firstName, emailAddress, " +
                "phoneNumber, weeklyWorkHours, numAdress) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            prStat = connect.prepareStatement(query);
            prStat.setString(1, objectToInsertInDB.getLastName());
            prStat.setString(2, objectToInsertInDB.getFirstName());
            prStat.setString(3, objectToInsertInDB.getEmail());
            prStat.setString(4, objectToInsertInDB.getPhoneNumber());
            prStat.setInt(5, objectToInsertInDB.getWeeklyWorkHours());
            prStat.setInt(6, objectToInsertInDB.getAddress().getNumAddress());

            int nbLinesInsert = prStat.executeUpdate();
            if(nbLinesInsert > 0) {
                isInserted = true;
            }
        } finally {
            if (prStat != null) {
                try {
                    prStat.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return isInserted;
    }

    @Override
    public boolean update(Interpreter objectToUpdateInDB) throws SQLException {
        
    }

    @Override
    public boolean delete(Interpreter objectToDeleteFormDB) throws SQLException {
        return false;
    }
}
