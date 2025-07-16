package magma.net.vao;

import java.io.Serializable;

/**
 * <p>Title: Technician.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class Technician implements Serializable {
    private String id;
    private String Name;

    public Technician(String id, String Name) {
        setId(id);
        setName(Name);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return Name;
    }
}
