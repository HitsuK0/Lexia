package be.hers.info.ProjetIntegree.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author Louis Halet
 * @reviewer Nicolas Jean-François
 */

public class ConnectionOracle {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionOracle.class);
    private static Connection connectionBD = null;
    private String url = "jdbc:oracle:thin:@labinfo.hers.be:1521:XE";
    private String userName = "BD25Groupe1PI";
    private String password = "PI_Acces01";

    /**
     * Create a ConnectionOracle object and establish a connection to the database based on the jdbc11 driver with its url, username and password.
     */
    private ConnectionOracle() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connectionBD = DriverManager.getConnection(url, userName, password);
            logger.info("Connexion à la base de données Oracle établie avec succès");
        } catch (ClassNotFoundException e) {
            logger.error("Driver Oracle non trouvé", e);
            throw new RuntimeException("Driver Oracle non trouvé", e);
        } catch (SQLException e) {
            logger.error("Échec de la connexion à la base de données Oracle", e);
            throw new RuntimeException("Échec connexion Oracle", e);
        }
    }

    /**
     *
     * @return The database connection singleton. Create a connection if it does not exist.
     */
    public static Connection getInstance() {
        try {
            if (connectionBD == null || connectionBD.isClosed()) {
                new ConnectionOracle();
            }
        } catch (SQLException e) {
            new ConnectionOracle();
        }
        return connectionBD;
    }

}
