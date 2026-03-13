import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class DAO<T> {
    /**
     * the connection to the database.
     */

    public Connection connect = ConnectionOracle.getInstance();
    /**
     * Searches for the object whose identifier matches the String passed as a parameter.
     * @param objectToSearchInDB the identifier of the object to search for in the table.
     * @return The object whose identifier matches the String passed as a parameter. null if there is no object matching the String passed as a parameter.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public abstract T find(String objectToSearchInDB) throws SQLException;
    /**
     * Create a list containing all the objects in the table.
     * @return a list containing all the objects in the table or an empty list if the table is empty.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public abstract List<T> findAll() throws SQLException;
    /**
     * Precondition: the object passed as a parameter cannot be null.
     * Adds the object passed as a parameter to the table.
     * @param objectToInsertInDB the object to be inserted into the table.
     * @return true if the object was successfully inserted, false otherwise.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public abstract boolean create(T objectToInsertInDB) throws SQLException;
    /**
     * Precondition: the object passed as a parameter cannot be null.
     * Updates all object fields in the table (except its identifier) ​​that correspond to the object identifier passed as a parameter.
     * @param objectToUpdateInDB the object containing the identifier and the fields to be updated in the table.
     * @return true if the object has been successfully updated, false otherwise.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public abstract boolean update(T objectToUpdateInDB) throws SQLException;
    /**
     * Precondition: the object passed as a parameter cannot be null.
     * Deletes the object where its identifier matches the identifier of the object passed as a parameter.
     * @param objectToDeleteFormDB the object to be deleted from the table.
     * @return true if the object was successfully deleted, false otherwise.
     * @throws SQLException In case of any SQL problems encountered with this method.
     */
    public abstract boolean delete(T objectToDeleteFormDB) throws SQLException;
}
