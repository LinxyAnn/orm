package metadata;

/**
 * Class stores fields data obtained by reflection
 *
 * @author Anna Severyna
 */
public class FieldData implements DataHolder{
    private String nameField;
    private String type;
    private Class<?> typeClass;
    private String annotation;
    private String nameColumn;


    public FieldData(String nameField, String type) {
        this.nameField = nameField;
        this.type = type;
    }

    public FieldData(String nameField, String type, String annotation) {
        this.nameField = nameField;
        this.type = type;
        this.annotation = annotation;
    }


    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    @Override
    public String toString() {
        return "FieldData{" +
                "nameField='" + nameField + '\'' +
                ", type='" + type + '\'' +
                ", typeClass=" + typeClass +
                //", annotation='" + annotation + '\'' +
                ", nameColumn='" + nameColumn + '\'' +
                '}';
    }
}
