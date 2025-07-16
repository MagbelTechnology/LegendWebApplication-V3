package legend.admin.objects;


public class TableColumn
{  

    private String columId;
    private String tableName;
    private String columnName;

    public TableColumn(String tableName, String columnName)
    {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public TableColumn()
    {
    }

    public String getColumId()
    {
        return columId;
    }

    public void setColumId(String columId)
    {
        this.columId = columId;
    }
    
    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }
}
