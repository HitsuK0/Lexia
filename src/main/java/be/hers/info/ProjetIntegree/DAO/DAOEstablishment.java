package be.hers.info.ProjetIntegree.DAO;

/**
 * @author Rosman Loïs
 * @reviewer Nicolas Jean-Francois, Halet Louis
 */

import be.hers.info.ProjetIntegree.DTO.DTOEstablishmentFormAppointment;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.POJO.Referrer;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.internal.OraclePreparedStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DAOEstablishment extends DAO<Establishment>{


    /**
     * This function find all the data of all the Establishment.
     * This function make a List<Address> of size 1.
     * This function find all the referrer who work at the Establishment found.
     * @return a list with all the Establishment with all the field fully initialized.
     * @throws SQLException if the bd request goes wrong
     */
    public List<Establishment> findAllFullEstablishment() throws SQLException {
        List<Establishment> listEstablishmentFind = new ArrayList();
        Establishment establishmentFind = null;

        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "SELECT numEstablishment, name, phoneNumber, FKAddress " +
                        "FROM Establishment";
        try{
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();
            DAOAddress daoAddress = new DAOAddress();
            DAOReferrer daoReferrer = new DAOReferrer();
            while(rs.next()) {
                Address address = daoAddress.find(rs.getInt("FKAddress"));
                List<Referrer> referrers = daoReferrer.findAllByWork(rs.getInt("numEstablishment"));
                List<Integer> educationLevel = findListEducationLevel(rs.getInt("numEstablishment"));
                establishmentFind = new Establishment(
                        rs.getInt("numEstablishment"),
                        rs.getString("name"),
                        rs.getString("phoneNumber"),
                        educationLevel,
                        referrers,
                        List.of(address)
                );
                listEstablishmentFind.add(establishmentFind);
            }
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
        }
        return listEstablishmentFind;
    }
    /**
     * Searches for the establishment whose identifier matches the int passed as a parameter and
     * create this establishment with his numEstablishment, his name and his phoneNumber.
     * @param objectToSearchInDB the identifier of the establishment to search for in the table.
     * @return The establishment whose identifier matches the int passed as a parameter.
     * null if there is no establishment matching the int passed as a parameter.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public Establishment find(int objectToSearchInDB) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet rs = null;
        Establishment establishmentFind = null;
        String query = """
                       SELECT numEstablishment, name, phoneNumber FROM Establishment
                       WHERE numEstablishment = ?
                       """;

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();

            if(rs.next()){
                establishmentFind = new Establishment(
                        rs.getInt("numEstablishment"),
                        rs.getString("name"),
                        rs.getString("phoneNumber")
                );
            }
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
        }
        return establishmentFind;
    }

    /**
     * Create a list of Integers that correspond to the education levels of this institution.
     * @param numEstablishment the establishment number
     * @return A list of integers corresponding to the education levels of this establishment; an empty list is returned if no objects are found.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public List<Integer> findListEducationLevel(int numEstablishment) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet rs = null;
        List<Integer> EductationLevelList = new ArrayList<>();
        String query = " SELECT educationLevel  FROM Establishment " +
                "WHERE numEstablishment = ?";
        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numEstablishment);
            rs = prStat.executeQuery();
            if (rs.next()) {
                EductationLevelList = Arrays.stream(rs.getString("educationLevel").split(","))
                        .map(String::trim)
                        .map(Integer::valueOf)
                        .toList();
            }

        } finally {
            closeStatementAndResultSet(prStat, rs);
        }
        return EductationLevelList;
    }

    /**
     * Create a list containing all the establishments in the table. It initializes each establishment with
     * his numEstablishment, his name and his phoneNumber.
     * @return a list containing all the establishments in the table or an empty list if the table is empty.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public List<Establishment> findAll() throws SQLException {
        List<Establishment> listEstablishmentFind = new ArrayList<>();
        Establishment establishmentFind = null;

        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "SELECT numEstablishment, name, phoneNumber FROM Establishment";

        try{
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();

            while(rs.next()) {
                establishmentFind = new Establishment(
                        rs.getInt("numEstablishment"),
                        rs.getString("name"),
                        rs.getString("phoneNumber")
                );

                listEstablishmentFind.add(establishmentFind);
            }
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
        }
        return listEstablishmentFind;
    }

    /**
     * Create a list containing all the establishments in the table. It initializes each establishment with
     * his numEstablishment and his name
     * @return a list containing all the establishments in the table or an empty list if the table is empty
     * @throws SQLException In case of any SQL problems encountered with this method
     */
    public List<DTOEstablishmentFormAppointment> findAllDTOFormAppointment() throws SQLException {
        List<DTOEstablishmentFormAppointment> listEstablishmentFind = new ArrayList<>();
        DTOEstablishmentFormAppointment establishmentFind = null;

        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "SELECT numEstablishment, name FROM Establishment";

        try{
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();

            while(rs.next()) {
                establishmentFind = new DTOEstablishmentFormAppointment(
                        rs.getInt("numEstablishment"),
                        rs.getString("name")
                );

                listEstablishmentFind.add(establishmentFind);
            }
        }
        finally{
            closeStatementAndResultSet(prStat, rs);
        }
        return listEstablishmentFind;
    }

    /**
     * Precondition: the establishment passed as a parameter cannot be null.
     * Precondition: the educationLevel list in objectToInsertInDB contains only valid integers (between 0 and 4)
     * and doesn't contain duplicates.
     * Adds the establishment passed as a parameter to the table.
     * @param objectToInsertInDB the establishment to be inserted into the table.
     * @return true if the establishment was successfully inserted, false otherwise.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public boolean create(Establishment objectToInsertInDB) throws SQLException {
        boolean isInserted = false;
        List<String> listStrEducationLevel = objectToInsertInDB.getEducationLevel().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        List<Address> addresses = objectToInsertInDB.getAddresses();
        OraclePreparedStatement prStat = null;
        ResultSet rs = null;

        String query = """
                INSERT INTO Establishment (name, phoneNumber, educationLevel, FKAddress) VALUES (?, ?, ?, ?)
                RETURNING numEstablishment INTO ?
                """;

        try {
            prStat = (OraclePreparedStatement) connect.prepareStatement(query);
            String strEducationLevel = String.join(",", listStrEducationLevel);

            int nbLinesInsert = 0;
            for(int indexAddresses = 0; indexAddresses < addresses.size(); indexAddresses++) {
                prStat.setString(1, objectToInsertInDB.getNameBuilding());
                prStat.setString(2, objectToInsertInDB.getPhoneNumber());
                prStat.setString(3, strEducationLevel);
                Address address = addresses.get(indexAddresses);
                prStat.setInt(4, address.getNumAddress());
                prStat.registerReturnParameter(5, OracleTypes.INTEGER);

                nbLinesInsert += prStat.executeUpdate();

                rs = prStat.getReturnResultSet();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    objectToInsertInDB.setNumEstablishment(id);

                    if (!objectToInsertInDB.getReferrers().isEmpty()) {
                        for (Referrer i : objectToInsertInDB.getReferrers()) {
                            if (!addReferrerAtEstablishment(id, i.getNumReferrer()))
                                throw new SQLException("[DAOEstablishment] erreur lors de l'ajout dans la table Work");
                        }
                    }
                }
            }

            if(nbLinesInsert == addresses.size())
                isInserted = true;
        }
        finally {
            closeStatementAndResultSet(prStat, rs);
        }
        return isInserted;
    }

    public boolean addReferrerAtEstablishment(int numEstablishment, int numReferrer) throws SQLException {
        boolean isInserted = false;
        PreparedStatement prStat = null;
        String query = """
                INSERT INTO Work (numEstablishment, numReferer)
                VALUES (?, ?)
                """;

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, numEstablishment);
            prStat.setInt(2, numReferrer);
            int nbLinesInsert = prStat.executeUpdate();
            if(nbLinesInsert > 0)
                isInserted = true;
        }finally {
            closeStatement(prStat);
        }

        return isInserted;
    }

    /**
     * Precondition: the establishment passed as a parameter cannot be null.
     * Updates all establishment fields in the table (except its identifier) that correspond to
     * the establishment identifier passed as a parameter. It updated the name and the phoneNumber of the establishment
     * @param objectToUpdateInDB the establishment containing the identifier and the fields to be updated in the table.
     * @return true if the establishment has been successfully updated, false otherwise.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public boolean update(Establishment objectToUpdateInDB) throws SQLException {
        boolean isUpdated = false;
        PreparedStatement prStat = null;

        String query = """
                       UPDATE Establishment
                       SET name = ?, phoneNumber = ?
                       WHERE numEstablishment = ?
                       """;

        try {
            prStat = connect.prepareStatement(query);
            prStat.setString(1, objectToUpdateInDB.getNameBuilding());
            prStat.setString(2, objectToUpdateInDB.getPhoneNumber());
            prStat.setInt(3, objectToUpdateInDB.getNumEstablishment());

            int nbLinesUpdate = prStat.executeUpdate();
            if(nbLinesUpdate > 0) {
                isUpdated = true;
            }
        } finally {
            closeStatement(prStat);
        }
        return isUpdated;
    }

    /**
     * Precondition: the establishment passed as a parameter cannot be null.
     * Deletes the establishment where its identifier matches the identifier of the establishment passed as a parameter.
     * @param objectToDeleteFormDB the establishment to be deleted from the table.
     * @return true if the establishment was successfully deleted, false otherwise.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    @Override
    public boolean delete(Establishment objectToDeleteFormDB) throws SQLException {
        boolean isDeleted = false;
        PreparedStatement prStat = null;
        String query = "DELETE From Establishment WHERE NumEstablishment = ?";

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumEstablishment());
            if(prStat.executeUpdate() > 0)
                isDeleted = true;
        }
        finally{
            closeStatement(prStat);
        }
        return isDeleted;
    }
}
