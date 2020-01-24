package crud;

import connection.DbConnection;
import exception.AnnotationException;
import mapping.InstanceBuilder;
import metadata.MetaDataSchema;
import metadata.TableData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class contains auxiliary methods for the CRUD-operations
 *
 * @author Anna Severyna
 *
 */
public class CreateData {

    /**
     *  Method returns object of specified class from database data
     *
     * @param classobj  object class
     * @param id        object id in database
     * @return          object of specified class from database data
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public Object createInstanceFromRecordById(Class<?> classobj, int id) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object object = classobj.newInstance();
        TableData tableData;
        try {
            tableData = MetaDataSchema.getTableData(classobj.newInstance());
        } catch (Exception e) {
            throw new AnnotationException("Class '" + classobj.getName() + "' does not exist in MetaDataSchema");
        }
        String nameId;
        try {
            nameId = tableData.getIdData().getNameColumn();
        } catch (Exception e) {
            throw new AnnotationException("Class '" + classobj.getName() + "' does not have annotation for id field");
        }

        ResultSet resultSet = selectByValue(classobj, nameId, Integer.toString(id));
        if (resultSet != null) {
            InstanceBuilder instanceBuilder = new InstanceBuilder();
            instanceBuilder.createInstance(object, resultSet, tableData);
        } else {
            object = null;
        }
        return object;
    }

    /**
     *  Method returns object of specified class from database data
     *
     * @param classobj      object class
     * @param columnName    object field column name in database table
     * @param value         object field column name value in database table
     * @return              object of specified class from database data
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public Object createInstanceByValue(Class<?> classobj, String columnName, String value) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object object = classobj.newInstance();
        TableData tableData;
        try {
            tableData = MetaDataSchema.getTableData(classobj.newInstance());
        } catch (Exception e) {
            throw new AnnotationException("Class '" + classobj.getName() + "' does not exist in MetaDataSchema");
        }

        ResultSet resultSet = selectByValue(classobj, columnName, value);
        if (resultSet != null) {
            InstanceBuilder instanceBuilder = new InstanceBuilder();
            instanceBuilder.createInstance(object, resultSet, tableData);
        } else {
            object = null;
        }
        return object;
    }

    /**
     *  Method returns resultset from database data
     *
     * @param classobj      object class
     * @param columnName    object field column name in database table
     * @param value         object field column name value in database table
     * @return              resultset from database data
     * @throws SQLException
     */
    public ResultSet selectByValue(Class<?> classobj, String columnName, String value) throws SQLException {
        TableData tableData;
        String tableName;
        try {
            tableData = MetaDataSchema.getTableData(classobj.newInstance());
            tableName = tableData.getTableName();
        } catch (Exception e) {
            throw new AnnotationException("Class '" + classobj.getName() + "' does not exist in MetaDataSchema");
        }

        String query = String.format("SELECT * from %s WHERE %s = '%s'", tableName, columnName, value);
        Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean isEmpty = !resultSet.next();
        resultSet.previous();
        if (isEmpty) {
            resultSet = null;
        }
        return resultSet;
    }

    /**
     * Method returns the object of specified class from database data
     *
     * @param classobj      object class
     * @param id            object id in database
     * @return              object of specified class from database data
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public Object createRelInstanceById(Class<?> classobj, int id) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object object = classobj.newInstance();
        TableData tableData;
        try {
            tableData = MetaDataSchema.getTableData(classobj.newInstance());
        } catch (Exception e) {
            throw new AnnotationException("Class '" + classobj.getName() + "' does not exist in MetaDataSchema");
        }
        String nameId;
        try {
            nameId = tableData.getIdData().getNameColumn();
        } catch (Exception e) {
            throw new AnnotationException("Class '" + classobj.getName() + "' does not have annotation for id field");
        }

        ResultSet resultSet = selectByValue(classobj, nameId, Integer.toString(id));
        if (resultSet != null) {
            InstanceBuilder instanceBuilder = new InstanceBuilder();
            instanceBuilder.createInstanceRel(object, resultSet, tableData);
        } else {
            object = null;
        }
        return object;
    }

    /**
     *  Method returns list of data obtained by foreign key
     *
     * @param classobj      object class
     * @param columnName    object field column name in database table
     * @param value         object field column name value in database table
     * @return              list of data obtained by foreign key
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public List <?> createFkInstanceByValue(Class<?> classobj, String columnName, String value) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object object = classobj.newInstance();
        List<?> collectionFK = new ArrayList<>();
        TableData tableData;
        try {
            tableData = MetaDataSchema.getTableData(classobj.newInstance());
        } catch (Exception e) {
            throw new AnnotationException("Class '" + classobj.getName() + "' does not exist in MetaDataSchema");
        }

        ResultSet resultSet = selectByValue(classobj, columnName, value);
        if (resultSet != null) {
            InstanceBuilder instanceBuilder = new InstanceBuilder();
            collectionFK = instanceBuilder.createInstanceFK(object, resultSet, tableData);
        } else {
            object = null;
        }
        return collectionFK;
    }

    //public Collection createListFk()

}
