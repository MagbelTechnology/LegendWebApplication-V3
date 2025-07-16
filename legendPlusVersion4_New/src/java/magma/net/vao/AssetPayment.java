package magma.net.vao;

public class AssetPayment {
	private String payId;
	private String assetId;
	private double payment;
	private double vatableCost;
	private double vatAmount;
	private double whtAmount;
	private String payDate;
	private String raised;
	private String payDesc;
        private int tranId;
	/**
	 * @param assetId
	 * @param payment
	 * @param payDate
	 * @param raised
	 * @param payDesc
	 */
	public AssetPayment(String assetId, double payment, String payDate, String raised, String payDesc) {
		this.assetId = assetId;
		this.payment = payment;
		this.payDate = payDate;
		this.raised = raised;
		this.payDesc = payDesc;
	}

    public AssetPayment(String assetId, double payment, String payDate, String raised, String payDesc, int tranId) {
        this.assetId = assetId;
        this.payment = payment;
        this.payDate = payDate;
        this.raised = raised;
        this.payDesc = payDesc;
        this.tranId = tranId;
    }
    public AssetPayment(String assetId, double payment, double vatableCost, double vatAmount, double whtAmount, String payDate, String raised, String payDesc, int tranId) {
        this.assetId = assetId;
        this.payment = payment;
        this.vatableCost = vatableCost;
        this.vatAmount = vatAmount;
        this.whtAmount = whtAmount;
        this.payDate = payDate;
        this.raised = raised;
        this.payDesc = payDesc;
        this.tranId = tranId;
    }

	/**
	 * 
	 */
	public AssetPayment() {
		
	}
	/**
	 * @return the assetId
	 */
	public String getAssetId() {
		return assetId;
	}
	/**
	 * @param assetId the assetId to set
	 */
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	/**
	 * @return the payDate
	 */
	public String getPayDate() {
		return payDate;
	}
	/**
	 * @param payDate the payDate to set
	 */
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	/**
	 * @return the payDesc
	 */
	public String getPayDesc() {
		return payDesc;
	}
	/**
	 * @param payDesc the payDesc to set
	 */
	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}
	/**
	 * @return the payment
	 */
	public double getPayment() {
		return payment;
	}
	/**
	 * @param payment the payment to set
	 */
	public void setPayment(double payment) {
		this.payment = payment;
	}
	/**
	 * @return the vatableCost
	 */
	public double getVatableCost() {
		return vatableCost;
	}
	/**
	 * @param vatableCost the vatableCost to set
	 */
	public void setVatableCost(double vatableCost) {
		this.vatableCost = vatableCost;
	}
	/**
	 * @return the whtAmount
	 */
	public double getWhtAmount() {
		return whtAmount;
	}
	/**
	 * @param whtAmount the whtAmount to set
	 */
	public void setWhtAmount(double whtAmount) {
		this.whtAmount = whtAmount;
	}
	/**
	 * @return the vatAmount
	 */
	public double getVatAmount() {
		return vatAmount;
	}
	/**
	 * @param vatAmount the vatAmount to set
	 */
	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}	
	/**
	 * @return the raised
	 */
	public String getRaised() {
		return raised;
	}
	/**
	 * @param raised the raised to set
	 */
	public void setRaised(String raised) {
		this.raised = raised;
	}
	/**
	 * @return the payId
	 */
	public String getPayId() {
		return payId;
	}
	/**
	 * @param payId the payId to set
	 */
	public void setPayId(String payId) {
		this.payId = payId;
	}

    /**
     * @return the tranId
     */
    public int getTranId() {
        return tranId;
    }

    /**
     * @param tranId the tranId to set
     */
    public void setTranId(int tranId) {
        this.tranId = tranId;
    }
	
	

}
