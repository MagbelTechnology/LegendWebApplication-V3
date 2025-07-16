package legend.purge.object;

public class PurgeandArchivebean implements java.io.Serializable {
    private String record_Index;
    private String table_Name;
    private String description;
    private int no_of_days = 0;
    private String archive;
    public PurgeandArchivebean(String record_Index, String table_Name,
                               String description, int no_of_days,
                               String archive) {
        this.record_Index = record_Index;
        this.table_Name = table_Name;
        this.description = description;
        this.no_of_days = no_of_days;
        this.archive = archive;
    }

    public void setRecord_Index(String record_Index) {
        this.record_Index = record_Index;
    }

    public void setTable_Name(String table_Name) {
        this.table_Name = table_Name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNo_of_days(int no_of_days) {
        this.no_of_days = no_of_days;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getRecord_Index() {
        return record_Index;
    }

    public String getTable_Name() {
        return table_Name;
    }

    public String getDescription() {
        return description;
    }

    public int getNo_of_days() {
        return no_of_days;
    }

    public String getArchive() {
        return archive;
    }
}
