package legend.admin.objects;


public class Rules
{

    private String Id;
    private String name;
    private String desc;
    private String userId;

    public Rules(String id, String name, String desc, String userId)
    {
        Id = id;
        this.name = name;
        this.desc = desc;
        this.userId = userId;
    }

    public Rules()
    {
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}
