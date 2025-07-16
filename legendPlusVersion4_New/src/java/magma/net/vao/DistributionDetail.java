package magma.net.vao;

import java.io.Serializable;

/**
 * <p>Title: DistributionDetail.java</p>
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
public class DistributionDetail implements Serializable {

    private String id;
    private String assetId;
    private String type;
    private String status;
    private String expenseAccount;
    private String accumAccount;
    private double amount;
    private String remainder;

    public DistributionDetail(String id, String assetId, String type,
                              String status,
                              String expenseAccount, String accumAccount,
                              double amount,String remainder) {

        setId(id);
        setAssetId(assetId);
        setType(type);
        setStatus(status);
        setExpenseAccount(expenseAccount);
        setAccumAccount(accumAccount);
        setAmount(amount);
        setRemainder(remainder);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExpenseAccount(String expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public void setAccumAccount(String accumAccount) {
        this.accumAccount = accumAccount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setRemainder(String remainder) {
        this.remainder = remainder;
    }

    public String getId() {
        return this.id;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public String getType() {
        return this.type;
    }

    public String getStatus() {
        return this.status;
    }

    public String getExpenseAccount() {
        return this.expenseAccount;
    }

    public String getAccumAccount() {
        return this.accumAccount;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getRemainder() {
        return remainder;
    }
}
