package com.magbel.ia.vao;

public class GLAccount {

    private String ledgerNo;
    private String glAccountNo;
    private String glAccountDesc;
    public GLAccount() {}
    
    public GLAccount(String ledgerNo,String glAccountNo,String glAccountDesc) {
    setLedgerNo(ledgerNo);
    setGlAccountNo(glAccountNo);
    setGlAccountDesc(glAccountDesc);
    
    }


    public void setLedgerNo(String ledgerNo) {
        this.ledgerNo = ledgerNo;
    }

    public String getLedgerNo() {
        return ledgerNo;
    }

    public void setGlAccountNo(String glAccountNo) {
        this.glAccountNo = glAccountNo;
    }

    public String getGlAccountNo() {
        return glAccountNo;
    }
    
    public void setGlAccountDesc(String glAccountDesc) {
        this.glAccountDesc = glAccountDesc;
    }

    public String getGlAccountDesc() {
        return glAccountDesc;
    }
}
