package be.hers.info.ProjetIntegree.DAO;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author Louis Halet
 * @reviewer Nicolas Jean-François
 */

public class ConnectionPostgres {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPostgres.class);
    private static Connection connectionBD = null;
    private static final Dotenv dotenv = Dotenv.load();

    /**
     * Create a ConnectionPostgres object and establish a connection to the database based on the PostgreSQL driver
     * with the url, username and password read from the .env file.
     */
    private ConnectionPostgres() {
        try {
            String url = dotenv.get("DATABASE_URL");
            String userName = dotenv.get("DATABASE_USER");
            String password = dotenv.get("DATABASE_PASSWORD");

            Class.forName("org.postgresql.Driver");
            connectionBD = DriverManager.getConnection(url, userName, password);
            logger.info("Connexion à la base de données PostgreSQL établie avec succès");
        } catch (ClassNotFoundException e) {
            logger.error("Driver PostgreSQL non trouvé", e);
            throw new RuntimeException("Driver PostgreSQL non trouvé", e);
        } catch (SQLException e) {
            logger.error("Échec de la connexion à la base de données PostgreSQL", e);
            throw new RuntimeException("Échec connexion PostgreSQL", e);
        }
    }

    /**
     *
     * @return The database connection singleton. Create a connection if it does not exist.
     */
    public static Connection getInstance() {
        try {
            if (connectionBD == null || connectionBD.isClosed()) {
                new ConnectionPostgres();
            }
        } catch (SQLException e) {
            new ConnectionPostgres();
        }
        return connectionBD;
    }

}
