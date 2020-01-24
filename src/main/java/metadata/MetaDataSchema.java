package metadata;

import exception.AnnotationException;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Class for annotated classes metadata storage
 *
 * @author Anna Severyna
 */

public class MetaDataSchema {
    private static volatile Map<Class<?>, TableData> classMap;
    private String someDetail;

    static {
        classMap = new HashMap<>();
    }

    public static Map<Class<?>, TableData> getClassMap() {
        return classMap;
    }

    private MetaDataSchema() {
    }

    public static void addToClassMap(Class<?> annotatedClass, TableData tableData) {
        classMap.put(annotatedClass, tableData);
    }

    public static TableData getTableData(Object object) {
        return classMap.get(object.getClass());
    }

    /**
     * Getting table name
     */
    public static String getTableName(Object object) {
        return getTableData(object).getTableName();
    }


    /**
     * Data extraction by primary key methods
     */
    public static String getIdName(Object object) {
        return getTableData(object).getIdData().getNameField();
    }

    public static String getIdType(Object object) {
        return getTableData(object).getIdData().getType();
    }


    public static String getColumnName(Object object, Field field) throws AnnotationException {
        String columnName = null;
        String fieldName = field.getName();
        List<FieldData> fieldDataList;
        List<JoinColumnData> joinColumnDataList;
        IdData idData;
        try {
            TableData tableData = classMap.get(object.getClass());
            fieldDataList = tableData.getFieldDataList();
            joinColumnDataList = tableData.getJoinColumnDataList();
            idData = tableData.getIdData();
        } catch (NullPointerException e) {
            throw new AnnotationException("Class '" + object.getClass().getSimpleName() + "' does not exist in MetaDataSchema");
        }

        if (idData != null && fieldName.equals(idData.getNameField())) {
            columnName = idData.getNameColumn();
        } else if (fieldDataList != null) {
            for (DataHolder fieldData : fieldDataList
            ) {
                if (fieldData.getNameField().equals(fieldName)) {
                    columnName = fieldData.getNameColumn();
                }
            }
        } else if (joinColumnDataList != null) {
            for (JoinColumnData joinColumnData : joinColumnDataList
            ) {
                if (joinColumnData.getNameField().equals(fieldName)) {
                    columnName = joinColumnData.getNameColumn();
                }
            }
        }
        if (columnName == null)
            throw new AnnotationException("Field '" + fieldName + "' does not exist in MetaDataSchema");
        return columnName;
    }

    public static void printClassMap(Class<?> annotatedClass) {
        String nameClass = annotatedClass.getSimpleName();
        String nameId = classMap.get(annotatedClass).getIdData().getNameField();
        String nameTable = classMap.get(annotatedClass).getTableName();
        String fields = classMap.get(annotatedClass).getFieldDataList().toString();
        String joinColumn = null;
        try {
            joinColumn = classMap.get(annotatedClass).getJoinColumnDataList().toString();
        } catch (NullPointerException e) {
            joinColumn = null;
        }
        String foreignKey = null;
        try {
            foreignKey = classMap.get(annotatedClass).getForeignKeyDataList().toString();
        } catch (NullPointerException e) {
            foreignKey = null;
        }

        System.out.println("name class: " + nameClass);
        System.out.println("name table: " + nameTable);
        System.out.println("name id: " + nameId);
        System.out.println("Fields: " + fields);
        System.out.println("joinColumn: " + joinColumn);
        System.out.println("foreignKey: " + foreignKey);
    }


}
