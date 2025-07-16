package com.magbel.ia.vao;


public class Charge
{
    
    private String id;
    private String chargeCode;
    private String acPayable;
    private String acReceivable;
    private String staff;
    private String currency;
    private String chargeType;
    private String basis;
    private String percent;
    private String effectiveDate;
    private String status;
    private String overDrawAc;
    private String chargeToZero;
    private String minCharge;
    private String maxCharge;
    private String minMaxBasis;
    private String maxChgYTD;
    private String maxChgLTD;
    private String waiveCharge;
    private String graceDays;
    private String freeDays;
    private String freePeriod;
    private String balance;
    private String balanceThreshold;
    private String balanceType;
    private String crPercent;
    private String threshold;
    private String stateTax;
    private String localTax;
    private String earnings;
    private String createDate;
    private String description;
    private String userId;
    private String chargeEvery;
    private String description2;
    private String triggered;
    private String chargePeriod;

    public Charge()
    {
    }

    public Charge(String id, String chargeCode, String acPayable, String acReceivable, String staff, String currency, String chargeType, 
            String basis, String percent, String effectiveDate, String status, String overDrawAc, String chargeToZero, String minCharge, 
            String maxCharge, String minMaxBasis, String maxChgYTD, String maxChgLTD, String waiveCharge, String graceDays, String freeDays, 
            String freePeriod, String balance, String balanceThreshold, String balanceType, String crPercent, String threshold, String stateTax, 
            String localTax, String earnings, String createDate, String description, String userId, String chargeEvery, String description2, 
            String triggered, String chargePeriod)
    {
        this.id = id;
        this.chargeCode = chargeCode;
        this.acPayable = acPayable;
        this.acReceivable = acReceivable;
        this.staff = staff;
        this.currency = currency;
        this.chargeType = chargeType;
        this.basis = basis;
        this.percent = percent;
        this.effectiveDate = effectiveDate;
        this.status = status;
        this.overDrawAc = overDrawAc;
        this.chargeToZero = chargeToZero;
        this.minCharge = minCharge;
        this.maxCharge = maxCharge;
        this.minMaxBasis = minMaxBasis;
        this.maxChgYTD = maxChgYTD;
        this.maxChgLTD = maxChgLTD;
        this.waiveCharge = waiveCharge;
        this.graceDays = graceDays;
        this.freeDays = freeDays;
        this.freePeriod = freePeriod;
        this.balance = balance;
        this.balanceThreshold = balanceThreshold;
        this.balanceType = balanceType;
        this.crPercent = crPercent;
        this.threshold = threshold;
        this.stateTax = stateTax;
        this.localTax = localTax;
        this.earnings = earnings;
        this.createDate = createDate;
        this.description = description;
        this.userId = userId;
        this.chargeEvery = chargeEvery;
        this.description2 = description2;
        this.triggered = triggered;
        this.chargePeriod = chargePeriod;
    }

    public String getAcPayable()
    {
        return acPayable;
    }

    public void setAcPayable(String acPayable)
    {
        this.acPayable = acPayable;
    }

    public String getAcReceivable()
    {
        return acReceivable;
    }

    public void setAcReceivable(String acReceivable)
    {
        this.acReceivable = acReceivable;
    }

    public String getBalance()
    {
        return balance;
    }

    public void setBalance(String balance)
    {
        this.balance = balance;
    }

    public String getBalanceThreshold()
    {
        return balanceThreshold;
    }

    public void setBalanceThreshold(String balanceThreshold)
    {
        this.balanceThreshold = balanceThreshold;
    }

    public String getBalanceType()
    {
        return balanceType;
    }

    public void setBalanceType(String balanceType)
    {
        this.balanceType = balanceType;
    }

    public String getBasis()
    {
        return basis;
    }

    public void setBasis(String basis)
    {
        this.basis = basis;
    }

    public String getChargeCode()
    {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode)
    {
        this.chargeCode = chargeCode;
    }

    public String getChargeToZero()
    {
        return chargeToZero;
    }

    public void setChargeToZero(String chargeToZero)
    {
        this.chargeToZero = chargeToZero;
    }

    public String getChargeType()
    {
        return chargeType;
    }

    public void setChargeType(String chargeType)
    {
        this.chargeType = chargeType;
    }

    public String getCrPercent()
    {
        return crPercent;
    }

    public void setCrPercent(String crPercent)
    {
        this.crPercent = crPercent;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getEarnings()
    {
        return earnings;
    }

    public void setEarnings(String earnings)
    {
        this.earnings = earnings;
    }

    public String getEffectiveDate()
    {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }

    public String getFreeDays()
    {
        return freeDays;
    }

    public void setFreeDays(String freeDays)
    {
        this.freeDays = freeDays;
    }

    public String getFreePeriod()
    {
        return freePeriod;
    }

    public void setFreePeriod(String freePeriod)
    {
        this.freePeriod = freePeriod;
    }

    public String getGraceDays()
    {
        return graceDays;
    }

    public void setGraceDays(String graceDays)
    {
        this.graceDays = graceDays;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getLocalTax()
    {
        return localTax;
    }

    public void setLocalTax(String localTax)
    {
        this.localTax = localTax;
    }

    public String getMaxCharge()
    {
        return maxCharge;
    }

    public void setMaxCharge(String maxCharge)
    {
        this.maxCharge = maxCharge;
    }

    public String getMaxChgLTD()
    {
        return maxChgLTD;
    }

    public void setMaxChgLTD(String maxChgLTD)
    {
        this.maxChgLTD = maxChgLTD;
    }

    public String getMaxChgYTD()
    {
        return maxChgYTD;
    }

    public void setMaxChgYTD(String maxChgYTD)
    {
        this.maxChgYTD = maxChgYTD;
    }

    public String getMinCharge()
    {
        return minCharge;
    }

    public void setMinCharge(String minCharge)
    {
        this.minCharge = minCharge;
    }

    public String getMinMaxBasis()
    {
        return minMaxBasis;
    }

    public void setMinMaxBasis(String minMaxBasis)
    {
        this.minMaxBasis = minMaxBasis;
    }

    public String getOverDrawAc()
    {
        return overDrawAc;
    }

    public void setOverDrawAc(String overDrawAc)
    {
        this.overDrawAc = overDrawAc;
    }

    public String getPercent()
    {
        return percent;
    }

    public void setPercent(String percent)
    {
        this.percent = percent;
    }

    public String getStaff()
    {
        return staff;
    }

    public void setStaff(String staff)
    {
        this.staff = staff;
    }

    public String getStateTax()
    {
        return stateTax;
    }

    public void setStateTax(String stateTax)
    {
        this.stateTax = stateTax;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getThreshold()
    {
        return threshold;
    }

    public void setThreshold(String threshold)
    {
        this.threshold = threshold;
    }

    public String getWaiveCharge()
    {
        return waiveCharge;
    }

    public void setWaiveCharge(String waiveCharge)
    {
        this.waiveCharge = waiveCharge;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getChargeEvery()
    {
        return chargeEvery;
    }

    public void setChargeEvery(String chargeEvery)
    {
        this.chargeEvery = chargeEvery;
    }

    public String getDescription2()
    {
        return description2;
    }

    public void setDescription2(String description2)
    {
        this.description2 = description2;
    }

    public String getTriggered()
    {
        return triggered;
    }

    public void setTrigerred(String triggered)
    {
        this.triggered = triggered;
    }

    public String getChargePeriod()
    {
        return chargePeriod;
    }

    public void setChargePeriod(String chargePeriod)
    {
        this.chargePeriod = chargePeriod;
    }
}
