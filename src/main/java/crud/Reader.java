package crud;

import connection.DbConnection;
import exception.AnnotationException;
import exception.OrmSoftException;
import mapping.InstanceBuilder;
import metadata.MetaDataSchema;
import metadata.TableData;

import static crud.SQLStatements.*;

import java.sql.*;

/**
 * Class reads the record from the database table by ID and returns the object with fields values
 * which match the values in database record
 *
 * @author Anna Severyna
 */
public class Reader {

    /**
     * Method reads the record from the database table by ID and returns the object with fields values
     * which match the values in database record
     *
     * @param       object      object
     * @param       id          database record ID
     * @return      the object with fields values which match the values in database record
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public Object createInstanceFromRecordById(Object object, int id) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object resultObject = object.getClass().newInstance();
        TableData tableData;
        String tableName;
        try {
            tableData = MetaDataSchema.getTableData(object.getClass().newInstance());
            tableName = tableData.getTableName();
        } catch (Exception e) {
            throw new AnnotationException("Class '" + object.getClass().getName() + "' does not exist in MetaDataSchema");
        }

        String nameId;
        try {
            nameId = tableData.getIdData().getNameColumn();
        } catch (Exception e) {
            throw new AnnotationException("Class '" + object.getClass().getName() + "' does not have annotation for id field");
        }

        String query = String.format(SELECT_BY_ID, tableName, nameId, id);

        Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.isBeforeFirst()) {
            throw new OrmSoftException("The record with ID '" + id + "' does not exist in table '" + tableName + "'");
        }

        InstanceBuilder instanceBuilder = new InstanceBuilder();
        instanceBuilder.createInstance(resultObject, resultSet, tableData);


        try {
            preparedStatement.close();
            connection.close();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return resultObject;
    }
}
