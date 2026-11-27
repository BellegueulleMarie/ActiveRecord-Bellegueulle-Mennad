package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static DBConnection instance;
    private Connection connect;

    private String userName = "root";
    private String password = "";
    private String serverName = "127.0.0.1";
    private String portNumber = "3306";
    private String dbName = "testpersonne";

    private DBConnection() throws SQLException {
        this.connecter();
    }

    private void connecter() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        String urlDB = "jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName;
        this.connect = DriverManager.getConnection(urlDB, connectionProps);
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        if (instance.connect == null) {
            instance.connecter();
        }
        return instance.connect;
    }

    public static synchronized void setNomDB(String nomDB) throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        instance.dbName = nomDB;
        instance.connect = null;
    }
}
