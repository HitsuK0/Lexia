package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Interpreter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return List.of();
    }

    @Override
    public boolean create(Interpreter objectToInsertInDB) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Interpreter objectToUpdateInDB) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Interpreter objectToDeleteFormDB) throws SQLException {
        return false;
    }
}
