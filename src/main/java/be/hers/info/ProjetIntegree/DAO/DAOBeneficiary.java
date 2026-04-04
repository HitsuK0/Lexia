package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.Beneficiary;

import java.sql.SQLException;
import java.util.List;

public class DAOBeneficiary extends DAO<Beneficiary> {

    @Override
    public Beneficiary find(String objectToSearchInDB) throws SQLException {
        return null;
    }

    @Override
    public List<Beneficiary> findAll() throws SQLException {
        return List.of();
    }

    @Override
    public boolean create(Beneficiary objectToInsertInDB) throws SQLException {
        return false;
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
