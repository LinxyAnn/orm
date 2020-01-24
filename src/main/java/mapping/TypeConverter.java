package mapping;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class converts data types
 *
 * @author Anna Severyna
 */
public class TypeConverter {

    /**
     * Method converts data types from database to java format
     *
     * @param   object      object name
     * @param   field       field name
     * @param   resultSet   incoming data set from database
     * @param   colNumber   column number
     */
    public void convertDataSqlToJava(Object object, Field field, ResultSet resultSet, int colNumber) {
        Class<?>fieldType = field.getType();
        int columnTypeNumber = 0;
        try {
            columnTypeNumber = resultSet.getMetaData().getColumnType(colNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (columnTypeNumber == -7) {
            String colType;
            try {
                colType = resultSet.getMetaData().getColumnTypeName(colNumber);
                if (colType.equals("bool")) {
                    columnTypeNumber = 16;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (DataBaseType dataBaseType : DataBaseType.values()
        ) {
            if (isSameType(columnTypeNumber, dataBaseType, fieldType)) {
                try {
                    dataBaseType.convertData(object, field, resultSet, colNumber);
                } catch (SQLException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method checks do the data type from database matches the data type in specified class
     *
     * @param   colNumber       column number
     * @param   dataBaseType    data type in database
     * @param   fieldType       data type of corresponding field in specified class
     * @return  true if the data type from the database matches the data type in the specified class
     */
    private boolean isSameType(int colNumber, DataBaseType dataBaseType, Class<?> fieldType) {
        return colNumber == dataBaseType.getTypeNumber() && (fieldType.isAssignableFrom(dataBaseType.getClassWrapper())) || fieldType.getName().equals(dataBaseType.getNameJavaTypePrimitive());
    }


}
