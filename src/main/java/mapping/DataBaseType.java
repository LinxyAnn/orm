package mapping;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;

/**
 * Database type enumeration for converter to JavaType
 *
 * @author Anna Severyna
 */
public enum DataBaseType {
    STRING_FROM_CHAR(1, String.class, null) {
        @Override
        public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
            field.set(objectNew, resultSet.getString(colNumber));
        }
    },
    STRING(12, String.class, null){
        @Override
        public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
            field.set(objectNew, resultSet.getString(colNumber));
        }
    },
    INTEGER(4, Integer.class, "int"){
        @Override
        public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
            field.set(objectNew, resultSet.getInt(colNumber));
        }
    },
    BYTE(-7, null, "byte"){
        @Override
        public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
            field.set(objectNew, resultSet.getByte(colNumber));
        }
    },

    BOOLEAN(16, Boolean.class, "boolean"){
        @Override
        public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
            field.set(objectNew, resultSet.getBoolean(colNumber));
        }
    },
    SHORT(5, Short.class, "short"){
        @Override
        public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
            field.set(objectNew, resultSet.getShort(colNumber));
        }
    },
    LONG(-5, Long.class, "long"){
        @Override
        public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
            field.set(objectNew, resultSet.getLong(colNumber));
        }
    },
    DOUBLE(8, Double.class, "double"){
        @Override
        public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
            field.set(objectNew, resultSet.getDouble(colNumber));
        }
    },
    BIG_DECIMAL(3, BigDecimal.class, null){
        @Override
        public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
            field.set(objectNew, resultSet.getBigDecimal(colNumber));
        }
    },
/*    NUMERIC(2, Numer){

    },*/
   DATE(91, Date.class, null){
    @Override
    public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
        field.set(objectNew, resultSet.getDate(colNumber));
    }
    },
     TIME(92, Time.class, null){
         @Override
         public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
             field.set(objectNew, resultSet.getTime(colNumber));
         }
    },
   TIMESTAMP(93, Timestamp.class, null){
       @Override
       public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
           field.set(objectNew, resultSet.getTimestamp(colNumber));
       }
    },

     OTHER(1111, Object.class, null){
         @Override
         public void convertData(Object objectNew, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException {
             field.set(objectNew, resultSet.getString(colNumber));
         }
    },
/*    ARRAY(2003){

    },
    NULL(0){

    },*/
    ;

    private int typeNumber;
    private Class<?> classWrapper;
    private String nameJavaTypePrimitive;

    DataBaseType(int typeNumber, Class<?> classWrapper, String nameJavaTypePrimitive) {
        this.typeNumber = typeNumber;
        this.classWrapper = classWrapper;
        this.nameJavaTypePrimitive = nameJavaTypePrimitive;
    }

    public int getTypeNumber() {
        return typeNumber;
    }

    public Class<?> getClassWrapper() {
        return classWrapper;
    }

    public String getNameJavaTypePrimitive() {
        return nameJavaTypePrimitive;
    }

    /**
     *  Abstract method for data conversion
     * 
     * @param object        object
     * @param field         field name
     * @param resultSet     incoming data resultset 
     * @param colNumber     column number in database table
     * @throws SQLException
     * @throws IllegalAccessException
     */
    public abstract void convertData(Object object, Field field, ResultSet resultSet, int colNumber) throws SQLException, IllegalAccessException;

}
