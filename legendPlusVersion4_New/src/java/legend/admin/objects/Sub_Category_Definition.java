package legend.admin.objects;


public class Sub_Category_Definition
{

    private String SUB_CATEGORY_ID;
    private String CATEGORY_ID;
    private String NAME;
    private String DESCRIPTION;
    private String IS_DELETED;

    public Sub_Category_Definition()
    {
    }

    public Sub_Category_Definition(String sub_category_id, String category_id, String name, String description, String is_deleted)
    {
        SUB_CATEGORY_ID = sub_category_id;
        CATEGORY_ID = category_id;
        NAME = name;
        DESCRIPTION = description;
        IS_DELETED = is_deleted;
    }

    public String getSUB_CATEGORY_ID()
    {
        return SUB_CATEGORY_ID;
    }

    public void setSUB_CATEGORY_ID(String sub_category_id)
    {
        SUB_CATEGORY_ID = sub_category_id;
    }

    public String getCATEGORY_ID()
    {
        return CATEGORY_ID;
    }

    public void setCATEGORY_ID(String category_id)
    {
        CATEGORY_ID = category_id;
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
