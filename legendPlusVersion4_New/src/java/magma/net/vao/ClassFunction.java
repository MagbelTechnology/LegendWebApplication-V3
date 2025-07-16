package magma.net.vao;

import java.io.Serializable;

/**
 * <p>Title: fileName.java</p>
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
public class ClassFunction implements Serializable {
    private String classCode;
    private String functionCode;
    private boolean editable;
    private boolean addable;

    public ClassFunction(String classCode, String functionCode,
                         boolean editable, boolean addable
            ) {
        setClassCode(classCode);
        setFunctionCode(functionCode);
        setEditable(editable);
        setAddable(addable);
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setAddable(boolean addable) {
        this.addable = addable;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isAddable() {
        return addable;
    }
}
