import connection.DbConnection;
import crud.*;
import exception.OrmSoftException;
import metadata.JoinColumnData;
import metadata.TableData;
import model.Auto;
import model.User;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * Entry point to the application
 *
 * @author Anna Severyna
 */
public class Main {

    public static void main(String[] args) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchFieldException {

        EntityManager entityManager = new EntityManager();
        //TestOrm testOrm = new TestOrm();
        //testOrm.testSaveUser();
        //entityManager.save(auto);
        //entityManager.deleteById(User.class, 56);
        //entityManager.deleteById(User.class, 54);
        //entityManager.loadById(User.class, 30);
        //Auto auto = (Auto) entityManager.loadById(Auto.class, 1);

        //Test create foreign key
        User user1 = (User) entityManager.loadById(User.class,2);
        //Auto auto1 = (Auto) entityManager.loadById(Auto.class,3);


        //Test ResultSet
/*        String query = String.format("SELECT * from %s WHERE %s = '%s'", "users", "id", "3");
        Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = preparedStatement.executeQuery();
        //boolean iff = preparedStatement.execute();
        //System.out.println(iff);
        boolean isEmpty = resultSet.next();
        resultSet.previous();
        System.out.println(isEmpty);

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int countCol = resultSetMetaData.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= countCol; i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                String columnValue = resultSet.getString(i);
                System.out.println("columnName: " + columnName);
                System.out.println("columnValue: " + columnValue);
            }
        }*/

        // Test update foreign key id DONE
  /*      User user1 = new User("Lisa", 28, "Madrid");
        User user2 = new User("Jason", 23, "Berlin");
        entityManager.save(user1);
        entityManager.save(user2);
        System.out.println(user1);
        System.out.println(user2);
        Auto auto1 = new Auto("Jaguar", "violet", true, user1);
        Auto auto2 = new Auto("Mazda", "grey", false, user2);
        entityManager.save(auto1);
        entityManager.save(auto2);
        System.out.println(auto1);
        System.out.println(auto2);*/










    }



}
