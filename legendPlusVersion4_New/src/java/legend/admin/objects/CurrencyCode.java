package legend.admin.objects;

public class CurrencyCode {
    private String currency_id;
    private String iso_code;
    private String currency_symbol;
    private String description;
    private String status;
    private String create_dt;
    private String local_currency;
    private String user_id;
    private String mtid;
    private String countryCode;

    public CurrencyCode() {
    }

    public CurrencyCode(String currency_id, String iso_code,
                        String currency_symbol, String description,
                        String status) {
        this.currency_id = currency_id;
        this.iso_code = iso_code;
        this.currency_symbol = currency_symbol;
        this.description = description;
        this.status = status;

    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public void setIso_code(String iso_code) {
        this.iso_code = iso_code;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreate_dt(String create_dt) {
        this.create_dt = create_dt;
    }

    public void setLocal_currency(String local_currency) {
        this.local_currency = local_currency;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setMtid(String mtid) {
        this.mtid = mtid;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public String getIso_code() {
        return iso_code;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getCreate_dt() {
        return create_dt;
    }

    public String getLocal_currency() {
        return local_currency;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getMtid() {
        return mtid;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}

