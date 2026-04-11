import java.sql.* ;


public class ConnectionOracle {
    private static Connection connectionBD = null;
    private String url = "jdbc:oracle:thin:@labinfo.hers.be:1521:XE";
    private String userName = "BD25Groupe1PI";
    private String password = "PI_Acces01";
    /**
     * Create a ConnectionOracle object and establish a connection to the database based on the jdbc11 driver with its url, username and password.
     */
    private ConnectionOracle(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connectionBD = DriverManager.getConnection(url, userName, password );
            System.out.println("Connecté");
        }catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver Oracle non trouvé", e);
        } catch (SQLException e) {
            throw new RuntimeException("Échec connexion Oracle", e);
        }
    }

    /**
     * 
     * @return The database connection singleton. Create a connection if it does not exist.
     */
    public static Connection getInstance(){
        if (connectionBD == null){
            new ConnectionOracle();
        }
        return connectionBD;
    }

}
