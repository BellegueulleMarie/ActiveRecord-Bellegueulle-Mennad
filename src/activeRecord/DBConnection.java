package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestionnaire de connexion a la base de donnees implementant les patrons Singleton et Factory.
 * Permet de limiter la connexion a une instance unique tout en encapsulant les parametres de connexion.
 */
public class DBConnection {

    // Instance unique pour le patron Singleton
    private static DBConnection instance;

    // Instance unique de connexion SQL
    private Connection connect;

    // Parametres de connexion encapsules dans des attributs
    private String userName = "root";
    private String password = "";
    private String serverName = "127.0.0.1";
    private String portNumber = "3306";
    private String dbName = "testpersonne";

    /**
     * Constructeur prive pour empecher l'instanciation directe (Singleton).
     */
    private DBConnection() throws SQLException {
        creerConnexion();
    }

    /**
     * Etablit une nouvelle connexion a la base de donnees via JDBC.
     */
    private void creerConnexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL com.mysql.cj.jdbc.Driver introuvable dans le classpath.", e);
        }

        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        String urlDB = "jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName;
        this.connect = DriverManager.getConnection(urlDB, connectionProps);
    }

    /**
     * Retourne l'instance unique du Singleton DBConnection.
     */
    public static synchronized DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /**
     * Retourne la connexion active.
     * Si aucune connexion n'existe ou si elle est fermee, elle est creee (Factory).
     *
     * @return java.sql.Connection
     * @throws SQLException en cas d'erreur de connexion
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        if (instance.connect == null || instance.connect.isClosed()) {
            instance.creerConnexion();
        }
        return instance.connect;
    }

    /**
     * Modifie le nom de la base de donnees demandee.
     * Apres chaque appel de setNomDB, une nouvelle connexion devra etre creee a l'appel de getConnection.
     *
     * @param nomDB le nouveau nom de la base de donnees
     */
    public static synchronized void setNomDB(String nomDB) throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        if (nomDB != null && !nomDB.equals(instance.dbName)) {
            instance.dbName = nomDB;
            // Fermeture de l'ancienne connexion si active
            if (instance.connect != null && !instance.connect.isClosed()) {
                instance.connect.close();
            }
            // Reinitialisation a null pour qu'une nouvelle connexion soit generee au prochain getConnection()
            instance.connect = null;
        }
    }

    /**
     * Retourne le nom actuel de la base de donnees.
     */
    public static synchronized String getNomDB() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance.dbName;
    }

    // Getters / Setters pour les parametres de connexion si besoin de reconfigurer l'hote ou le port
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }
}
