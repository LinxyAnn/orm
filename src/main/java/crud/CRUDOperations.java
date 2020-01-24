package crud;

import connection.DbConnection;
import exception.AnnotationException;
import exception.OrmSoftException;
import metadata.MetaDataSchema;
import metadata.TableData;


import java.sql.*;

/**
 * Class for CRUD-methods
 * 
 * @author Anna Severyna
 *
 */
public class CRUDOperations {

    /**
     * Method removes data of specified class object from database table by objects id
     * 
     * @param   classobj    object class
     * @param   id          object id
     * @throws  SQLException
     * @throws  IllegalAccessException
     * @throws  InstantiationException
     */
    public void deleteById(Class<?> classobj, Integer id) throws SQLException, IllegalAccessException, InstantiationException {
        Object object = classobj.newInstance();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        TableData tableData;
        try {
            tableData = MetaDataSchema.getTableData(object);
        } catch (Exception e) {
            throw new AnnotationException("Class '" + object.getClass().getName() + "' does not exist in MetaDataSchema");
        }

        String tableName = tableData.getTableName();

        String query = String.format("DELETE FROM %s WHERE %s = %s", tableName, "id", id);
        try {
            connection = DbConnection.getConnection();
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(!resultSet.isBeforeFirst()){
                throw new OrmSoftException ("The record with ID '" + id + "' does not exist in table '" + tableName + "'");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if(connection != null){
                connection.close();
            }
        }
    }
}
