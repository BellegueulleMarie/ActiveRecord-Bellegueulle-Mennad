package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnection {

    // variables de connection
    private  String userName = "root";
    private  String password = "";
    private  String serverName = "127.0.0.1";
    private  String portNumber = "3306";
    private static String dbName = "testpersonne";
    private static Connection instance;


    private DBConnection() throws SQLException{
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        String urlDB = "jdbc:mysql://" + serverName + ":";
        urlDB += portNumber + "/" + dbName;
        instance = DriverManager.getConnection(urlDB, connectionProps);
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (instance == null) {
            new DBConnection();
        }
        return instance;
    }

    public static synchronized void setNomDB(String nomDB) throws SQLException {
        if (nomDB != null && !nomDB.equals(dbName)) {
            dbName = nomDB;
            instance = null;
        }
    }


}
