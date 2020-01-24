package connection;

import java.io.IOException;
import java.util.Properties;

/**
 * Class handles database connection data from .properties-file
 *
 * @author Anna Severyna
 *
 */
public class ConnectionProp {

    private static ConnectionProp connectionProp;


    private String dbDriver;
    private String dbUrl;
    private String dbLogin;
    private String dbPassword;

    private ConnectionProp() {
        Properties prop = new Properties();
        try {
            prop.load(ConnectionProp.class.getClassLoader().getResourceAsStream("connection.properties"));
            this.dbDriver = prop.getProperty("dbDriver");
            this.dbUrl = prop.getProperty("dbUrl");
            this.dbLogin = prop.getProperty("dbLogin");
            this.dbPassword = prop.getProperty("dbPassword");

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Method returns new instance of connection properties set for connection
     *
     * @return  new instance of connection properties set for connection
     */
    public static synchronized ConnectionProp getInstance() {
        if (connectionProp == null) {
            connectionProp = new ConnectionProp();//TODO move to properties
        }
        return connectionProp;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbLogin() {
        return dbLogin;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
