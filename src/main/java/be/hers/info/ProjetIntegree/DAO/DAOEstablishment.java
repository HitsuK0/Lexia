package be.hers.info.ProjetIntegree.DAO;

/*
@author Rosman Loïs
@reviewer Nicolas Jean-Francois, Halet Louis
 */

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.POJO.Referrer;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.internal.OraclePreparedStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

//Explication pour le relecteur : j'ai décidé de traiter aussi la liste des niveaux d'éducations car c'est une liste d'entier
//et qu'en DB, c'est stocké sous forme de String
public class DAOEstablishment extends DAO<Establishment>{
    @Override
    public Establishment find(int objectToSearchInDB) throws SQLException {
        PreparedStatement prStat = null;
        ResultSet rs = null;
        Establishment establishmentFind = null;
        String query = """
                       SELECT FKAddress, name, phoneNumber, educationLevel FROM Establishment
                       WHERE numEstablishment = ?
                       """;

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();

            if(rs.next()){
                List<String> listStringEducationLevelFind = asList(rs.getString("educationLevel").split(","));
                List<Integer> listIntegerEducationLevelFind = listStringEducationLevelFind.stream()
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());

                establishmentFind = new Establishment(
                        objectToSearchInDB,
                        rs.getString("name"),
                        rs.getString("phoneNumber"),
                        listIntegerEducationLevelFind);
            }
        }
        finally{
            if(rs != null){
                try{
                    rs.close();
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }

            if(prStat != null){
                try{
                    prStat.close();
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }

        return establishmentFind;
    }

    @Override
    public List findAll() throws SQLException {
        List<Establishment> listEstablishmentFind = new ArrayList();
        PreparedStatement prStat = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Establishment";

        try{
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();

            while(rs.next()) {
                List<String> listStringEducationLevelFind = asList(rs.getString("educationLevel").split(","));
                List<Integer> listIntegerEducationLevelFind = listStringEducationLevelFind.stream()
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());

                Establishment establishmentFind = new Establishment(
                        rs.getInt("numEstablishment"),
                        rs.getString("name"),
                        rs.getString("phoneNumber"),
                        listIntegerEducationLevelFind);

                listEstablishmentFind.add(establishmentFind);
            }
        }
        finally{
            if(rs != null){
                try{
                    rs.close();
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }

            if(prStat != null){
                try{
                    prStat.close();
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }

        return listEstablishmentFind;
    }

    @Override
    public boolean create(Establishment objectToInsertInDB) throws SQLException {
        int nbEstablishmentToAdd = 0;
        int nbEstablishmentInsert = 0;
        OraclePreparedStatement prStat = null;
        ResultSet rs = null;

        String query = """
                INSERT INTO Establishment (FKAddress, name, phoneNumber, educationLevel) VALUES (?, ?, ?, ?, ?)
                returning numEstablishment into ?
                """;

        try {
            prStat = (OraclePreparedStatement) connect.prepareStatement(query);

            List<Address> listAddress = objectToInsertInDB.getAddresses();
            List<Integer> listEducationLevel = objectToInsertInDB.getEducationLevel();

            prStat.setString(2, objectToInsertInDB.getNameBuilding());
            prStat.setString(3, objectToInsertInDB.getPhoneNumber());
            prStat.registerReturnParameter(5, OracleTypes.INTEGER);
            for (int addressIndex = 0, educationLevelIndex = 0;
                //Pour les référents, c'est à faire en cascade dans la DB
                 addressIndex < listAddress.size() || educationLevelIndex < listEducationLevel.size(); ) {
                if (addressIndex < listAddress.size() && educationLevelIndex < listEducationLevel.size()) {
                    Address addressToInsert = listAddress.get(addressIndex);
                    prStat.setInt(1, addressToInsert.getNumAddress());
                    prStat.setInt(4, listEducationLevel.get(educationLevelIndex));
                    addressIndex++;
                    educationLevelIndex++;
                } else if (addressIndex < listAddress.size()) {
                    Address addressToInsert = listAddress.get(addressIndex);
                    prStat.setInt(1, addressToInsert.getNumAddress());
                    prStat.setInt(4, listEducationLevel.get(educationLevelIndex - 1));
                    addressIndex++;
                } else if (educationLevelIndex < listEducationLevel.size()) {
                    Address addressToInsert = listAddress.get(addressIndex - 1);
                    prStat.setInt(1, addressToInsert.getNumAddress());
                    prStat.setInt(4, listEducationLevel.get(educationLevelIndex));
                    educationLevelIndex++;
                }

                nbEstablishmentInsert += prStat.executeUpdate();
                nbEstablishmentToAdd++;
            }

            rs = prStat.getReturnResultSet();
            int id = rs.getInt(5);
            objectToInsertInDB.setNumEstablishment(id);
        } finally {
            if(rs != null){
                try{
                    rs.close();
                }
                catch(SQLException ex){
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

        if (nbEstablishmentInsert == nbEstablishmentToAdd)
            return true;
        return false;
    }

    @Override
    public boolean update(Establishment objectToUpdateInDB) throws SQLException {
        List<Integer> listEducationLevel = objectToUpdateInDB.getEducationLevel();
        List<Address> listAddress = objectToUpdateInDB.getAddresses();
        int educationLevelIndex = 0;
        int addressIndex = 0;
        int nbEstablishmentUpdate = 0;

        List<Establishment> listEstablishmentFind = find(objectToUpdateInDB.getNameBuilding(),
                objectToUpdateInDB.getPhoneNumber());

        for(Establishment establishment : listEstablishmentFind){
            Establishment newEstablishment = null;
            List<Integer> newListEducationLevel = new ArrayList<>();
            List<Address> newListAddress = new ArrayList<>();
            if (addressIndex < listAddress.size() && educationLevelIndex < listEducationLevel.size()) {
                newListEducationLevel.add(listEducationLevel.get(educationLevelIndex));
                newListAddress.add(listAddress.get(addressIndex));
                newEstablishment = new Establishment(establishment.getNumEstablishment(),
                        objectToUpdateInDB.getNameBuilding(), objectToUpdateInDB.getPhoneNumber(),
                        newListEducationLevel, new ArrayList<>(), newListAddress);
                update(newEstablishment, 'a');
                educationLevelIndex++;
                addressIndex++;
                nbEstablishmentUpdate++;
            }
            else if(addressIndex < listAddress.size()){
                newListEducationLevel.add(listEducationLevel.get(educationLevelIndex-1));
                newListAddress.add(listAddress.get(addressIndex));
                newEstablishment = new Establishment(establishment.getNumEstablishment(),
                        objectToUpdateInDB.getNameBuilding(), objectToUpdateInDB.getPhoneNumber(),
                        newListEducationLevel, new ArrayList<>(), newListAddress);
                update(newEstablishment, 'a');
                addressIndex++;
                nbEstablishmentUpdate++;
            }
            else if(educationLevelIndex < listEducationLevel.size()){
                newListEducationLevel.add(listEducationLevel.get(educationLevelIndex));
                newListAddress.add(listAddress.get(addressIndex-1));
                newEstablishment = new Establishment(establishment.getNumEstablishment(),
                        objectToUpdateInDB.getNameBuilding(), objectToUpdateInDB.getPhoneNumber(),
                        newListEducationLevel, new ArrayList<>(), newListAddress);
                update(newEstablishment, 'a');
                educationLevelIndex++;
                nbEstablishmentUpdate++;
            }
            else{
                delete(establishment);
            }
        }

        while(addressIndex < listAddress.size() || educationLevelIndex < listEducationLevel.size()){
            List<Integer> newListEducationLevel = new ArrayList<>();
            List<Address> newListAddress = new ArrayList<>();
            if (addressIndex < listAddress.size() && educationLevelIndex < listEducationLevel.size()) {
                newListEducationLevel.add(listEducationLevel.get(educationLevelIndex));
                newListAddress.add(listAddress.get(addressIndex));
                educationLevelIndex++;
                addressIndex++;
            }
            else if(addressIndex < listAddress.size()){
                newListEducationLevel.add(listEducationLevel.get(educationLevelIndex-1));
                newListAddress.add(listAddress.get(addressIndex));
                addressIndex++;
            }
            else if(educationLevelIndex < listEducationLevel.size()){
                newListEducationLevel.add(listEducationLevel.get(educationLevelIndex));
                newListAddress.add(listAddress.get(addressIndex-1));
                educationLevelIndex++;
            }

            Establishment newEstablishment = new Establishment(objectToUpdateInDB.getNameBuilding(),
                    objectToUpdateInDB.getPhoneNumber(), newListEducationLevel, new ArrayList<>(), newListAddress);
            create(newEstablishment);
            nbEstablishmentUpdate++;
        }

        if(nbEstablishmentUpdate == listEstablishmentFind.size())
            return true;
        return false;
    }

    @Override
    public boolean delete(Establishment objectToDeleteFormDB) throws SQLException {
        PreparedStatement prStat = null;
        String query = "DELETE From Establishment WHERE NumEstablishment = ?";

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumEstablishment());
            if(prStat.executeUpdate() > 0)
                return true;
            return false;
        }
        finally{
            if(prStat != null){
                try{
                    prStat.close();
                }
                catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }
    }
}
