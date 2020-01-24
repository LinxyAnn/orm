package crud;

import connection.DbConnection;
import exception.AnnotationException;
import exception.OrmSoftException;
import metadata.MetaDataSchema;
import metadata.TableData;

import java.sql.*;

import static crud.SQLStatements.*;

/**
 * Class removes the record in database table by ID
 *
 * @author Anna Severyna
 */
public class Remover {

    /**
     * Method removes the record in database table by ID
     *
     * @param   object  object as the annotated class identifier
     * @param   id      record ID
     * @throws  SQLException
     */
    public void deleteById(Object object, Integer id) throws SQLException {
        TableData tableData;
        String tableName;
        try {
            tableData = MetaDataSchema.getTableData(object);
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

        String query = String.format(DELETE_BY_ID, tableName, nameId, id);

        Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (!resultSet.isBeforeFirst()) {
            throw new OrmSoftException("The record with ID '" + id + "' does not exist in table '" + tableName + "'");
        } else {
            System.out.println("The record with ID '" + id + "' was successfully deleted from table '" + tableName + "'");
        }

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
    }
}
