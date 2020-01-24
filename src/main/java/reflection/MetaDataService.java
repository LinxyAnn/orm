package reflection;

import annotation.*;
import exception.AnnotationException;
import metadata.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class creates metadata using annotated classes
 *
 * @author Anna Severyna
 */

public class MetaDataService {
    private String tableName;
    private IdData idData;
    private List<FieldData> fieldsDataList;
    private List<JoinColumnData> joinColumnDataList;
    private List<ForeignKeyData> foreignKeyDataList;
    private TableData tableData;

    public void createMetaDataSchema(Set<Class<?>> listClassAnnotation) throws AnnotationException {
        for (Class<?> annotatedClass : listClassAnnotation
        ) {
            createTableData(annotatedClass);
            MetaDataSchema.addToClassMap(annotatedClass, tableData);
        }
    }

    /**
     * Method creates the table data
     *
     * @param annotatedClass    annotated class
     * @throws AnnotationException
     */
    private void createTableData(Class<?> annotatedClass) throws AnnotationException {
        setTableName(annotatedClass);
        fieldsDataList = new ArrayList<>();
        joinColumnDataList = new ArrayList<>();
        foreignKeyDataList = new ArrayList<>();
        addFields(annotatedClass);
        tableData = new TableData(tableName, idData, fieldsDataList, joinColumnDataList, foreignKeyDataList);
    }

    /**
     * Method adds the table name
     *
     * @param classAnnotation   annotated class
     */
    private void setTableName(Class<?> classAnnotation) {
        TableSoft tableSoft = classAnnotation.getAnnotation(TableSoft.class);
        if (tableSoft.name().equals("")) {
            tableName = classAnnotation.getSimpleName().toLowerCase();
        } else {
            tableName = tableSoft.name();
        }
    }

    /**
     * Method adds fields data to the table
     *
     * @param   annotatedClass      annotated class
     * @throws  AnnotationException
     */
    private void addFields(Class<?> annotatedClass) throws AnnotationException {
        Field[] fields = annotatedClass.getDeclaredFields();
        for (Field field : fields
        ) {
            if (isIdInBase(field)) {
                addIdField(field);
            } else if (isColumnInBase(field)) {
                addColumnFields(field);
            } else if (isJoinColumn(field)) {
                addJoinColumnFields(field);
            } else if (isForeignKey(field)) {
                addForeignKeyFields(field);
            }
        }
    }


    /**
     * Method adds field for primary key
     *
     * @param field     field name
     */
    private void addIdField(Field field) {
        this.idData = new IdData(field.getName(), field.getType().getSimpleName());
        idData.setNameColumn(setColumnName(field, idData));
        idData.setTypeClass(field.getType());
    }


    /**
     * Method adds fields to the table which corresponding to columns
     *
     * @param field
     */
    private void addColumnFields(Field field) {
        FieldData fieldData = new FieldData(field.getName(), field.getType().getSimpleName());
        fieldData.setNameColumn(setColumnName(field, fieldData));
        fieldData.setTypeClass(field.getType());
        fieldsDataList.add(fieldData);
    }

    /**
     * Method adds related by foreign key fields to the table
     *
     * @param   field       field name
     * @throws  AnnotationException
     */
    private void addJoinColumnFields(Field field) throws AnnotationException {
        Annotation[] annotations = field.getDeclaredAnnotations();
        if (annotations.length <= 1) {
            throw new AnnotationException("Field '" + field.getName() + "' from Class '" + field.getDeclaringClass() + "' does not have relation annotation for JoinColumn");
        }
        String relation = getRelation(field);
        JoinColumnData joinFieldData = new JoinColumnData(field.getName(), field.getType().getSimpleName(), relation);
        String columnName = field.getDeclaredAnnotation(JoinColumnSoft.class).name();
        if (columnName.equals("")) {
            columnName = joinFieldData.getNameField().toLowerCase();
        }
        joinFieldData.setNameColumn(columnName);
        joinFieldData.setTypeClass(field.getType());
        joinColumnDataList.add(joinFieldData);
    }

    /**
     * Method adds foreign key to the table
     *
     * @param   field       field name
     * @throws  AnnotationException
     */
    private void addForeignKeyFields(Field field) throws AnnotationException {
        String relation = getRelation(field);
        ForeignKeyData foreignKeyData = new ForeignKeyData(field.getName(), field.getType().getSimpleName(), relation);
        String columnName = field.getDeclaredAnnotation(OneToManySoft.class).mappedBy();
        if (columnName.equals("")) {
            columnName = foreignKeyData.getNameField().toLowerCase();
        }
        foreignKeyData.setNameColumn(columnName);
        ParameterizedType genericFieldType = (ParameterizedType) field.getGenericType();
        Type[] fieldArgTypes = genericFieldType.getActualTypeArguments();
        for (Type fieldArgType : fieldArgTypes) {
            Class<?> fieldArgClass = (Class<?>) fieldArgType;
            foreignKeyData.setTypeClass(fieldArgClass);
        }
        foreignKeyDataList.add(foreignKeyData);
    }

    /**
     * Method returns the relation type
     *
     * @param       field       field name
     * @return                  relation type string
     * @throws      AnnotationException
     */

    private String getRelation(Field field) throws AnnotationException {

        String relation = null;
        if (isOneToOneRel(field)) {
            relation = "OneToOne";
        } else if (isOneToManyRel(field)) {
            relation = "OneToMany";
        } else if (isManyToOneRel(field)) {
            relation = "ManyToOne";
        } else if (isManyToManyRel(field)) {
            relation = "ManyToMany";
        }
        if (relation == null) {
            throw new AnnotationException("Field '" + field.getName() + "' from Class '" + field.getDeclaringClass() + "' does not have relation annotation");
        }
        return relation;
    }

    /**
     * Method adds column name for the field
     *
     * @param   field       field name
     * @param   dataHolder  dataholder name
     * @return              string with column name
     */
    private String setColumnName(Field field, DataHolder dataHolder) {
        String columnName;
        try {
            columnName = field.getDeclaredAnnotation(ColumnSoft.class).name();
        } catch (NullPointerException e) {
            columnName = field.getName().toLowerCase();
        }
        if (columnName.equals("")) {
            columnName = dataHolder.getNameField().toLowerCase();
        }
        return columnName;
    }


    /**
     * Method checks if @IdSoft annotation exists
     * @IdSoft - the annotation for the fields which are the primary key
     *
     * @param   field   field name
     * @return  true    if @IdSoft annotation exists
     * @see     annotation.IdSoft
     */
    private boolean isIdInBase(Field field) {
        return field.getDeclaredAnnotation(IdSoft.class) != null;
    }


    /**
     * Method checks if @ColumnSoft annotation exists
     * @ColumnSoft - the annotation for fields which corresponding to the columns in database
     *
     * @param   field   field name
     * @return  true    if @ColumnSoft annotation exists
     * @see     annotation.ColumnSoft
     */
    private boolean isColumnInBase(Field field) {
        return field.getDeclaredAnnotation(ColumnSoft.class) != null;
    }

    /**
     * Method checks if @ForeignKeySoft annotation exists
     * @ForeignKeySoft is the annotation for foreign keys
     *
     * @param   field   field name
     * @return  true    if @ForeignKeySoft annotation exists
     * @see annotation.ForeignKeySoft
     */
    private boolean isForeignKey(Field field) {
        return field.getDeclaredAnnotation(ForeignKeySoft.class) != null;
    }

    /**
     * Method checks if @JoinColumnSoft annotation exists
     * @JoinColumnSoft - annotation for table related by foreign key
     *
     * @param   field   field name
     * @return  true    if @JoinColumnSoft annotation exists
     * @see annotation.JoinColumnSoft
     */
    private boolean isJoinColumn(Field field) {
        return field.getDeclaredAnnotation(JoinColumnSoft.class) != null;
    }

    /**
     * Method checks if @OneToOneSoft annotation exists
     *
     * @param   field   field name
     * @return  true if @OneToOneSoft annotation exists
     * @see annotation.OneToOneSoft
     */
    private boolean isOneToOneRel(Field field) {
        return field.getDeclaredAnnotation(OneToOneSoft.class) != null;
    }

    /**
     * Method checks if @OneToManySoft annotation exists
     *
     * @param   field   field name
     * @return  true if @OneToManySoft annotation exists
     * @see annotation.OneToManySoft
     */
    private boolean isOneToManyRel(Field field) {
        return field.getDeclaredAnnotation(OneToManySoft.class) != null;
    }

    /**
     * Method checks if @ManyToOneSoft annotation exists
     *
     * @param   field   field name
     * @return  true if @ManyToOneSoft annotation exists
     * @see annotation.ManyToOneSoft
     */
    private boolean isManyToOneRel(Field field) {
        return field.getDeclaredAnnotation(ManyToOneSoft.class) != null;
    }

    /**
     * Method checks if @ManyToManySoft annotation exists
     *
     * @param   field   field name
     * @return  true if @ManyToManySoft annotation exists
     * @see annotation.ManyToManySoft
     */
    private boolean isManyToManyRel(Field field) {
        return field.getDeclaredAnnotation(ManyToManySoft.class) != null;
    }


}




