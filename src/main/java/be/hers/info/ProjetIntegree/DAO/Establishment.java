package be.hers.info.ProjetIntegree.DAO;

/*
@author Rosman Loïs
@reviewer Nicolas Jean-Francois, Halet Louis
 */

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.POJO.Referrer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOEstablishment extends DAO<Establishment>{
    @Override
    public Establishment find(int objectToSearchInDB) throws SQLException {
        Establishment establishmentFind = null;
        String query = """
                       SELECT FKAddress, name, phoneNumber, educationLevel FROM Establishment
                       WHERE numEstablishment = ?
                       """;
        PreparedStatement prStat = null;
        ResultSet rs = null;
        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            rs = prStat.executeQuery();

            List<Integer> listEducationLevel = new ArrayList<>();
            List<Address> listAddress = new ArrayList<>();
            List<Referrer> listReferrer = new ArrayList<>();
            if(rs.next()){
                listEducationLevel.add(rs.getInt("educationLevel"));

                DAOAddress daoAddress = new DAOAddress();
                listAddress.add(daoAddress.find(rs.getInt("FKAddress")));

                DAOReferrer daoReferrer = new DAOReferrer();
                listReferrer = daoReferrer.find(objectToSearchInDB);
                //Retourne la liste des Referrer qui sont liés avec l'Establishment qui possède l'id objectToSearchInDB

                establishmentFind = new Establishment(objectToSearchInDB, rs.getString("name"),
                        rs.getString("phoneNumber"), listEducationLevel, listReferrer, listAddress);
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
        String query = "SELECT * FROM Establishment";
        PreparedStatement prStat = null;
        ResultSet rs = null;

        try{
            prStat = connect.prepareStatement(query);
            rs = prStat.executeQuery();

            while(rs.next()){
                //Vérifie si l'établissement rs.next est déjà présent dans listEstablishmentFind.
                boolean isInEstablishment = false;
                int i = 0;
                while(i < listEstablishmentFind.size() && !isInEstablishment){
                    Establishment establishment = listEstablishmentFind.get(i);
                    if(establishment.getNameBuilding().equals(rs.getString("name"))){
                        isInEstablishment = true;
                    }
                    else{
                        i++;
                    }
                }

                List<Integer> listEducationLevel = new ArrayList<>();
                DAOReferrer daoReferrer = new DAOReferrer();
                List<Referrer> listReferrer = new ArrayList<>();
                DAOAddress daoAddress = new DAOAddress();
                List<Address> listAddress = new ArrayList<>();

                //Si l'établissement est déjà présent,
                //ajoute le niveau d'éducation dans la liste si niveau d'éducation n'est pas encore présent
                if(isInEstablishment){
                    Establishment establishmentFind = listEstablishmentFind.get(i);
                    if(!establishmentFind.getEducationLevel().contains(rs.getInt("educationLevel"))){
                        establishmentFind.getEducationLevel().add(rs.getInt("educationLevel"));
                    }

                    //Si l'établissement est déjà présent,
                    //vérifie si l'adresse est déjà dans la liste des adresses. Si non, elle est ajoutée
                    listAddress = establishmentFind.getAddresses();
                    Address addressFind = daoAddress.find(rs.getInt("FKAddress"));

                    if(!listAddress.contains(addressFind)) {
                        //contains nécessite l'ajout de equals dans Address
                        listAddress.add(addressFind);
                    }
                }
                else{
                    listEducationLevel.add(rs.getInt("educationLevel"));
                    listReferrer = daoReferrer.find(rs.getInt("numEstablishment"));
                    //Retourne la liste des Referrer qui sont liés avec l'Establishment qui possède l'id objectToSearchInDB
                    listAddress.add(daoAddress.find(rs.getInt("FKAddress")));

                    Establishment establishmentFind = new Establishment(rs.getInt("numEstablishment"),
                            rs.getString("name"), rs.getString("phoneNumber"),
                            listEducationLevel, listReferrer, listAddress);
                    listEstablishmentFind.add(establishmentFind);
                }
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
        String query = "INSERT INTO Establishment (NumEstablishment, FKAddress, name, phoneNumber, educationLevel) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement prStat = null;
        try {
            prStat = connect.prepareStatement(query);

            List<Address> listAddress = objectToInsertInDB.getAddresses();
            List<Integer> listEducationLevel = objectToInsertInDB.getEducationLevel();

            prStat.setInt(1, objectToInsertInDB.getNumEstablishment());
            prStat.setString(3, objectToInsertInDB.getNameBuilding());
            prStat.setString(4, objectToInsertInDB.getPhoneNumber());
            for (int addressIndex = 0, educationLevelIndex = 0;
                //Pour les référents, c'est à faire en cascade dans la DB
                 addressIndex < listAddress.size() || educationLevelIndex < listEducationLevel.size();) {
                if (addressIndex < listAddress.size() && educationLevelIndex < listEducationLevel.size()) {
                    Address addressToInsert = listAddress.get(addressIndex);
                    prStat.setInt(2, addressToInsert.getNumAddress());
                    prStat.setInt(5, listEducationLevel.get(educationLevelIndex));
                    addressIndex++;
                    educationLevelIndex++;
                }
                else if(addressIndex < listAddress.size()) {
                    Address addressToInsert = listAddress.get(addressIndex);
                    prStat.setInt(2, addressToInsert.getNumAddress());
                    prStat.setInt(5, listEducationLevel.get(educationLevelIndex-1));
                    addressIndex++;
                }
                else if(educationLevelIndex < listEducationLevel.size()){
                    Address addressToInsert = listAddress.get(addressIndex-1);
                    prStat.setInt(2, addressToInsert.getNumAddress());
                    prStat.setInt(5, listEducationLevel.get(educationLevelIndex));
                    educationLevelIndex++;
                }

                nbEstablishmentInsert += prStat.executeUpdate();
                nbEstablishmentToAdd++;
            }
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

        if(nbEstablishmentInsert == nbEstablishmentToAdd)
            return true;
        return false;
    }

    @Override
    public boolean update(Establishment objectToUpdateInDB) throws SQLException {
        List<Integer> listEducationLevel = objectToUpdateInDB.getEducationLevel();
        List<Address> listAddress = objectToUpdateInDB.getAddresses();

        List<Establishment> listEstablishmentFind = find(objectToUpdateInDB.getNameBuilding(),
                                                             objectToUpdateInDB.getPhoneNumber());

        int educationLevelIndex = 0;
        int addressIndex = 0;
        int nbEstablishmentUpdate = 0;
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
        String query = "DELETE From Establishment WHERE NumEstablishment = ?";

        PreparedStatement prStat = null;
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
