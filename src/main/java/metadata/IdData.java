package metadata;

/**
 * Class stores primary key data
 *
 * @author Anna Severyna
 */
public class IdData implements DataHolder {
    private String nameField;
    private String type;
    private Class<?> typeClass;
    private String idGenerate;
    private String nameColumn;

    public IdData() {
    }

    public IdData(String nameField, String type) {
        this.nameField = nameField;
        this.type = type;
    }

    public IdData(String nameField, String type, String idGenerate) {
        this.nameField = nameField;
        this.type = type;
        this.idGenerate = idGenerate;
    }

    public IdData(String nameField, String nameColumn, String type, String idGenerate) {
        this.nameField = nameField;
        this.nameColumn = nameColumn;
        this.type = type;
        this.idGenerate = idGenerate;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Class<?> getTypeClass() {
        return typeClass;
    }

    @Override
    public void setTypeClass(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    public String getIdGenerate() {
        return idGenerate;
    }

    public void setIdGenerate(String idGenerate) {
        this.idGenerate = idGenerate;
    }

    @Override
    public String toString() {
        return "IdData{" +
                "nameField='" + nameField + '\'' +
                ", type='" + type + '\'' +
                ", typeClass=" + typeClass +
                ", idGenerate='" + idGenerate + '\'' +
                ", nameColumn='" + nameColumn + '\'' +
                '}';
    }
}
