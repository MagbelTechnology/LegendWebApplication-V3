package magma.net.vao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Title: ProcesingInfo.java</p>
 *
 * <p>Description: An object representation<br>
 * of the processing information retrived from<br>
 * the company setup</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Jejelowo.B.Festus
 * @version 1.0
 */
public class ProcesingInfo implements Serializable {

    private int frequency;
    private double residual;
    private Date processingDate;
    private Date nextProcessingDate;
    private String financialStartDate;
    private String financialEndDate;
    private int noOfMonths;

    /**
     * ProcesingInfo
     *
     * @param frequency int
     * @param residual double
     * @param processingDate Date
     * @param nextProcessingDate Date
     */
    public ProcesingInfo(int frequency, double residual, Date processingDate,
                         Date nextProcessingDate, String financialStartDate,
                         String financialEndDate, int noOfMonths) {

        setFrequency(frequency);
        setResidual(residual);
        setProcessingDate(processingDate);
        setNextProcessingDate(nextProcessingDate);
        setFinancialStartDate(financialStartDate);
        setFinancialEndDate(financialEndDate);
        setNoOfMonths(noOfMonths);
    }

    /**
     * setFrequency
     *
     * @param frequency int
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * setResidual
     *
     * @param residual double
     */
    public void setResidual(double residual) {
        this.residual = residual;
    }

    /**
     * setProcessingDate
     *
     * @param processingDate Date
     */
    public void setProcessingDate(Date processingDate) {
        this.processingDate = processingDate;
    }

    /**
     * setNextProcessingDate
     *
     * @param nextProcessingDate Date
     */
    public void setNextProcessingDate(Date nextProcessingDate) {
        this.nextProcessingDate = nextProcessingDate;
    }

    public void setFinancialStartDate(String financialStartDate) {
        this.financialStartDate = financialStartDate;
    }

    public void setFinancialEndDate(String financialEndDate) {
        this.financialEndDate = financialEndDate;
    }

    public void setNoOfMonths(int noOfMonths) {
        this.noOfMonths = noOfMonths;
    }

    /**
     * getFrequency
     *
     * @return int
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * getResidual
     *
     * @return double
     */
    public double getResidual() {
        return residual;
    }

    /**
     * getProcessingDate
     *
     * @return Date
     */
    public Date getProcessingDate() {
        return processingDate;
    }

    /**
     * getNextProcessingDate
     *
     * @return Date
     */
    public Date getNextProcessingDate() {
        return nextProcessingDate;
    }

    public String getFinancialStartDate() {
        return financialStartDate;
    }

    public String getFinancialEndDate() {
        return financialEndDate;
    }

    public int getNoOfMonths() {
        return noOfMonths;
    }

    /**
     * dateToString
     *
     * @param date Date
     * @return String
     */
    public String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }
}
