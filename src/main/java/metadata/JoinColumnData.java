package metadata;

/**
 * Class stores joined column data
 * 
 * @author Anna Severyna
 */
public class JoinColumnData implements DataHolder {
    private String nameField;
    private String nameColumn;
    private String type;
    private boolean check = false;
    private Class<?> typeClass;
    private String relation;

    public JoinColumnData(String nameField, String type) {
        this.nameField = nameField;
        this.type = type;
    }

    public JoinColumnData(String nameField, String type, String relation) {
        this.nameField = nameField;
        this.nameColumn = nameColumn;
        this.type = type;
        this.relation = relation;
    }

    @Override
    public String getNameField() {
        return nameField;
    }

    @Override
    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    @Override
    public String getNameColumn() {
        return nameColumn;
    }

    @Override
    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
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

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "JoinColumnData{" +
                "nameField='" + nameField + '\'' +
                ", nameColumn='" + nameColumn + '\'' +
                ", type='" + type + '\'' +
                ", check=" + check +
                ", typeClass=" + typeClass +
                ", relation='" + relation + '\'' +
                '}';
    }
}
