package mapping;

import crud.CreateData;
import exception.OrmSoftException;
import metadata.*;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class builds instances from database table data
 *
 * @author Anna Severyna
 */
public class InstanceBuilder {
    public static Map<String, List<Integer>> joinGet;
    private Object objectNew;
    private IdData idData;
    private List<FieldData> fieldDataList;
    private List<JoinColumnData> joinColumnDataList;
    private List<ForeignKeyData> foreignKeyDataList;
    private String tempValue;
    private boolean isJoinColumn = false;
    private String id;
    private String idJoin;

    /**
     * Method creates instances of classes from database data
     *
     * @param   objectNew   object
     * @param   resultSet   incoming set of data from database
     * @param   tableData   metadata about database table structure
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void createInstance(Object objectNew, ResultSet resultSet, TableData tableData) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.objectNew = objectNew;
        Class<?> classobj = objectNew.getClass();
        idData = tableData.getIdData();
        String idName = idData.getNameField();
        fieldDataList = tableData.getFieldDataList();
        joinColumnDataList = tableData.getJoinColumnDataList();
        foreignKeyDataList = tableData.getForeignKeyDataList();
        ResultSetMetaData rsMeta = resultSet.getMetaData();
        int countCol = rsMeta.getColumnCount();

        while (resultSet.next()) {
            for (int i = 1; i <= countCol; i++) {
                this.tempValue = resultSet.getString(i);
                idJoin = resultSet.getString(i);
                String columnName = rsMeta.getColumnName(i);
                String fieldName = getFieldName(columnName);
                if (isIdColumn(fieldName)) {
                    this.id = resultSet.getString(i);
                } else if (fieldName == null && joinColumnDataList != null) {
                    for (JoinColumnData joinColumnData : joinColumnDataList
                    ) {
                        if (isJoinColumn(columnName)) {
                            fieldName = joinColumnData.getNameField();
                        }
                    }
                }

                Field field;
                try {
                    field = objectNew.getClass().getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    throw new OrmSoftException("Class '" + classobj + "' does not have field" + fieldName + "'");
                }

                field.setAccessible(true);

                if (isJoinColumn) {
                    JoinColumnData joinColumnData = getJoinColumnData(columnName);
                    Object object = getJoinObject(joinColumnData);
                    field.set(objectNew, object);
                } else {
                    setValue(objectNew, field, resultSet, i);
                }
            }
        }

        Field fieldFK;
        if (foreignKeyDataList != null) {
            List<Object> collectionFK = new ArrayList<>();
            for (ForeignKeyData foreignKeyData : foreignKeyDataList
            ) {
                try {
                    fieldFK = objectNew.getClass().getDeclaredField(foreignKeyData.getNameField());
                } catch (NoSuchFieldException e) {
                    throw new OrmSoftException("Class '" + classobj + "' does not have field" + foreignKeyData.getNameField() + "'");
                }
                collectionFK.add(getForeignObject(foreignKeyData));
                fieldFK.setAccessible(true);
                fieldFK.set(objectNew, collectionFK);

            }
        }
        System.out.println("Object done:");
        System.out.println(objectNew);

    }

    /**
     * Method sets the field value from database data
     *
     * @param objectNew     new object which obtains fields values
     * @param field         field name
     * @param resultSet     incoming set of data from database
     * @param colNumber     column number
     */
    private void setValue(Object objectNew, Field field, ResultSet resultSet, int colNumber) {
        TypeConverter typeConverter = new TypeConverter();
        typeConverter.convertDataSqlToJava(objectNew, field, resultSet, colNumber);
    }

    /**
     * Method returns field name string from column name
     *
     * @param   nameColumn      column name
     * @return  field name string
     */
    private String getFieldName(String nameColumn) {
        String nameField = null;
        if (checkFieldName(nameColumn, idData)) {
            nameField = idData.getNameField();
        } else if (fieldDataList != null) {
            for (FieldData fieldData : fieldDataList
            ) {
                if (checkFieldName(nameColumn, fieldData)) {
                    nameField = fieldData.getNameField();
                }
            }
        }
        if (joinColumnDataList != null) {
            for (JoinColumnData joinColumnData : joinColumnDataList
            ) {
                if (checkFieldName(nameColumn, joinColumnData) && !joinColumnData.isCheck()) {
                    nameField = joinColumnData.getNameField();
                    isJoinColumn = true;
                }
            }
        }
        return nameField;
    }

    /**
     * Method checks does field have name
     *
     * @param   nameColumn    column name
     * @param   dataHolder    dataholder name
     * @return  true if field has name
     */
    private boolean checkFieldName(String nameColumn, DataHolder dataHolder) {
        return dataHolder.getNameColumn().equals(nameColumn);
    }

    /**
     * Method joins data from different columns
     *
     * @param   nameColumn    column name
     * @return  joined column
     */
    private JoinColumnData getJoinColumnData(String nameColumn) {
        JoinColumnData joinColumnData = null;
        for (JoinColumnData joinColumnDataTemp : joinColumnDataList
        ) {
            if (checkFieldName(nameColumn, joinColumnDataTemp)) {
                joinColumnData = joinColumnDataTemp;
            }
        }
        return joinColumnData;
    }

    /**
     * Method returns data by foreign key 
     * 
     * @param   nameColumn
     * @return  data by foreign key 
     */
    private ForeignKeyData getForeignKeyData(String nameColumn) {
        ForeignKeyData foreignKeyData = null;
        for (ForeignKeyData foreignKeyDataTemp : foreignKeyDataList
        ) {
            if (checkFieldName(nameColumn, foreignKeyDataTemp)) {
                foreignKeyData = foreignKeyDataTemp;
            }
        }
        return foreignKeyData;
    }

    /**
     * Method gets joined object
     * 
     * @param   joinColumnData  data from joined column
     * @return  joined object
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    private Object getJoinObject(JoinColumnData joinColumnData) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class<?> classNew = joinColumnData.getTypeClass();
        String className = classNew.getSimpleName();
        Object object = null;
        CreateData createData = new CreateData();

        object = createData.createRelInstanceById(classNew, Integer.parseInt(idJoin));

        return object;
    }

    /**
     * Method returns object by foreign key
     * 
     * @param   dataHolder  dataholder name
     * @return  object by foreign key
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    private List <?> getForeignObject(DataHolder dataHolder) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class<?> classGeneric = dataHolder.getTypeClass();
        String fieldName = dataHolder.getNameColumn();
        List<JoinColumnData> joinColumnDataList = MetaDataSchema.getTableData(classGeneric.newInstance()).getJoinColumnDataList();
        String columnName = null;
        for (JoinColumnData joinColumnData : joinColumnDataList
        ) {
            if (fieldName.equals(joinColumnData.getNameField())) {
                columnName = joinColumnData.getNameColumn();
            }
        }
        CreateData createData = new CreateData();
        return createData.createFkInstanceByValue(classGeneric, columnName, id);
    }

    /**
     * Method creates related instance
     * 
     * @param   objectNew   created object
     * @param   resultSet   incoming data set from database
     * @param   tableData   meta data describing table structure
     * @throws  SQLException
     * @throws  IllegalAccessException
     */
    public void createInstanceRel(Object objectNew, ResultSet resultSet, TableData tableData) throws SQLException, IllegalAccessException {
        this.objectNew = objectNew;
        Class<?> classobj = objectNew.getClass();
        idData = tableData.getIdData();
        String idName = idData.getNameField();
        fieldDataList = tableData.getFieldDataList();
        foreignKeyDataList = tableData.getForeignKeyDataList();
        ResultSetMetaData rsMeta = resultSet.getMetaData();
        int countCol = rsMeta.getColumnCount();

        while (resultSet.next()) {
            for (int i = 1; i <= countCol; i++) {
                this.tempValue = resultSet.getString(i);
                idJoin = resultSet.getString(i);
                String columnName = rsMeta.getColumnName(i);
                String fieldName = getFieldName(columnName);
                if (idData.getNameField().equals(fieldName)) {
                    this.id = resultSet.getString(i);
                }
                if (fieldName == null && joinColumnDataList != null) {
                    for (JoinColumnData joinColumnData : joinColumnDataList
                    ) {
                        if (joinColumnData.getNameColumn().equals(columnName)) {
                            fieldName = joinColumnData.getNameField();
                        }
                    }
                }

                Field field;
                try {
                    field = objectNew.getClass().getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    throw new OrmSoftException("Class '" + classobj + "' does not have field" + fieldName + "'");
                }

                field.setAccessible(true);

                if (isJoinColumn) {
                    field.set(objectNew, null);
                } else {
                    setValue(objectNew, field, resultSet, i);
                }
            }
        }


        System.out.println("Object done:");
        System.out.println(objectNew);

    }

    /**
     * Method returns list of related instance data by foreign key 
     * 
     * @param   objectNew   created object
     * @param   resultSet   incoming data set from database
     * @param   tableData   meta data describing table structure
     * @return  list of related instance data by foreign key
     * @throws  SQLException
     * @throws  IllegalAccessException
     * @throws  InstantiationException
     */
    public List<?> createInstanceFK(Object objectNew, ResultSet resultSet, TableData tableData) throws SQLException,  IllegalAccessException, InstantiationException {
        this.objectNew = objectNew;
        List <Object> listFK = new ArrayList<>();
        Class<?> classobj = objectNew.getClass();

        idData = tableData.getIdData();
        fieldDataList = tableData.getFieldDataList();
        joinColumnDataList = tableData.getJoinColumnDataList();
        ResultSetMetaData rsMeta = resultSet.getMetaData();
        int countCol = rsMeta.getColumnCount();

        while (resultSet.next()) {
            objectNew = classobj.newInstance();
            for (int i = 1; i <= countCol; i++) {
                this.tempValue = resultSet.getString(i);
                idJoin = resultSet.getString(i);
                String columnName = rsMeta.getColumnName(i);
                String fieldName = getFieldName(columnName);
                if (idData.getNameField().equals(fieldName)) {
                    this.id = resultSet.getString(i);
                }
                if (fieldName == null && joinColumnDataList != null) {
                    for (JoinColumnData joinColumnData : joinColumnDataList
                    ) {
                        if (joinColumnData.getNameColumn().equals(columnName)) {
                            fieldName = joinColumnData.getNameField();
                        }
                    }
                }

                Field field = null;
                try {
                    if (fieldName != null) {
                        field = objectNew.getClass().getDeclaredField(fieldName);}
                } catch (NoSuchFieldException e) {
                    throw new OrmSoftException("Class '" + classobj + "' does not have field" + fieldName + "'");
                }

                field.setAccessible(true);
                setValue(objectNew, field, resultSet, i);

            }
            listFK.add(objectNew);

        }

        System.out.println("Object done:");
        System.out.println(objectNew);

        return listFK;

    }

    /**
     * Method checks is Id column exit
     * 
     * @param   fieldName   field name
     * @return  true if Id column exits
     */
    private boolean isIdColumn(String fieldName) {
        return idData.getNameField().equals(fieldName);
    }

    /**
     * Method checks is joined column exit
     *
     * @param   columnName   column name
     * @return  true if joined column exits
     */
    private boolean isJoinColumn(String columnName) {
        boolean result = false;
        for (JoinColumnData joinColumnData : joinColumnDataList
        ) {
            if (joinColumnData.getNameColumn().equals(columnName)) {
                result = true;
                isJoinColumn = true;
                break;
            }
        }
        return result;
    }

}
