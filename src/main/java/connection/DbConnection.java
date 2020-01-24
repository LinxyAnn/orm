package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class establishes connection to database
 *
 * @author Anna Severyna
 */
public class DbConnection {
    static final String JDBC_DRIVER_NOT_FOUND = "JDBC Driver is not found!";
    static final String FAILED_CONNECTION = "Failed connection";
    static final String SUCCESSFUL_CONNECTION = "You're successfully connected to the database now!";

    static final String DB_URL = ConnectionProp.getInstance().getDbUrl();
    static final String LOGIN = ConnectionProp.getInstance().getDbLogin();
    static final String PASSWORD = ConnectionProp.getInstance().getDbPassword();

    private DbConnection() {
        checkDriver();
    }

    /**
     * Method checks database driver
     */
    private static void checkDriver() {
        try {
            Class.forName(ConnectionProp.getInstance().getDbDriver());
        } catch (ClassNotFoundException e) {
            System.out.println(JDBC_DRIVER_NOT_FOUND);
            e.printStackTrace();
        }
    }

    /**
     * Method returns connection to the database
     *
     * @return connection to the database
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            System.out.println(FAILED_CONNECTION);
            e.printStackTrace();
        }
        if (connection != null) {
            System.out.println(SUCCESSFUL_CONNECTION);
        } else {
            System.out.println(FAILED_CONNECTION);
        }
        return connection;
    }
}
