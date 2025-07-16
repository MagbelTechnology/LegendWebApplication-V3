package com.magbel.ia.vao;

public class WareHouse {

    private String id;
    private String code;
    private String name;
    private String userId;
    private String address;
    private String status;
    private String branchCode;

    
    public WareHouse(String id,String code, String name,String userId,String address,String branchCode,String status) {
    
    setId(id);
    setCode(code);
    setName(name);
    setUserId(userId);
    setAddress(address);
    setStatus(status);
    setBranchCode(branchCode);
    
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }
}
