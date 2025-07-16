package legend.admin.objects;


public class Item_Definition
{

    private String ITEM_ID;
    private String SUB_CATEGORY_ID;
    private String NAME;
    private String DESCRIPTION;
    private String IS_DELETED;
    private String tech_id;
    private String user_id;
    private String create_date;

    public Item_Definition()
    {
    }

    public Item_Definition(String item_id, String sub_category_id, String name, String description, String is_deleted)
    {
        ITEM_ID = item_id;
        SUB_CATEGORY_ID = sub_category_id;
        NAME = name;
        DESCRIPTION = description;
        IS_DELETED = is_deleted;
    }

    public Item_Definition(String item_id, String sub_category_id, String name, String description, String is_deleted, String tech_id, String user_id, 
            String create_date)
    {
        ITEM_ID = item_id;
        SUB_CATEGORY_ID = sub_category_id;
        NAME = name;
        DESCRIPTION = description;
        IS_DELETED = is_deleted;
        this.tech_id = tech_id;
        this.user_id = user_id;
        this.create_date = create_date;
    }

    public String getTech_id()
    {
        return tech_id;
    }

    public void setTech_id(String tech_id)
    {
        this.tech_id = tech_id;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getCreate_date()
    {
        return create_date;
    }

    public void setCreate_date(String create_date)
    {
        this.create_date = create_date;
    }

    public String getITEM_ID()
    {
        return ITEM_ID;
    }

    public void setITEM_ID(String item_id)
    {
        ITEM_ID = item_id;
    }

    public String getSUB_CATEGORY_ID()
    {
        return SUB_CATEGORY_ID;
    }

    public void setSUB_CATEGORY_ID(String sub_category_id)
    {
        SUB_CATEGORY_ID = sub_category_id;
    }

    public String getNAME()
    {
        return NAME;
    }

    public void setNAME(String name)
    {
        NAME = name;
    }

    public String getDESCRIPTION()
    {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String description)
    {
        DESCRIPTION = description;
    }

    public String getIS_DELETED()
    {
        return IS_DELETED;
    }

    public void setIS_DELETED(String is_deleted)
    {
        IS_DELETED = is_deleted;
    }
}
