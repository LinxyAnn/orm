package metadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Class stores all tables meta data: name, fields, relation type
 *
 * @author Anna Severyna
 */

public class TableData {
    private String tableName;
    private IdData idData;
    private List<FieldData> fieldDataList = new ArrayList<>();
    private List<JoinColumnData> joinColumnDataList  = new ArrayList<>();
    private List<ForeignKeyData> foreignKeyDataList  = new ArrayList<>();


    public TableData() {
    }

    public TableData(String tableName) {
        this.tableName = tableName;
    }

    public TableData(String tableName, IdData idData, List<FieldData> fieldDataList, List<JoinColumnData> joinColumnDataList, List<ForeignKeyData> foreignKeyDataList) {
        this.tableName = tableName;
        this.idData = idData;
        this.fieldDataList = fieldDataList;
        this.joinColumnDataList = joinColumnDataList;
        this.foreignKeyDataList = foreignKeyDataList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public IdData getIdData() {
        return idData;
    }

    public void setIdData(IdData idData) {
        this.idData = idData;
    }

    public List<FieldData> getFieldDataList() {
        return fieldDataList;
    }

    public void setFieldDataList(List<FieldData> fieldDataList) {
        this.fieldDataList = fieldDataList;
    }

    public List<JoinColumnData> getJoinColumnDataList() {
        return joinColumnDataList;
    }

    public void setJoinColumnDataList(List<JoinColumnData> joinColumnDataList) {
        this.joinColumnDataList = joinColumnDataList;
    }

    public List<ForeignKeyData> getForeignKeyDataList() {
        return foreignKeyDataList;
    }

    public void setForeignKeyDataList(List<ForeignKeyData> foreignKeyDataList) {
        this.foreignKeyDataList = foreignKeyDataList;
    }

    @Override
    public String toString() {
        return "TableData{" +
                "tableName='" + tableName + '\'' +
                ", idData=" + idData +
                ", fieldDataList=" + fieldDataList +
                ", joinColumnDataList=" + joinColumnDataList +
                ", foreignKeyDataList=" + foreignKeyDataList +
                '}';
    }
}
