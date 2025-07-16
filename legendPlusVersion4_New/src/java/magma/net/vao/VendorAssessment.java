package magma.net.vao;

import java.io.Serializable;

public class VendorAssessment implements Serializable {
    private String id;
    private String criteria;
    private String category;
    private String grade;
    private String percentage;
    private String contractorDetail;
    private String serviceType;
    private String suggestImprove;
    private String overalRating;
    private String criteriaA;
    private String criteriaB;
    private String criteriaC;
    private String criteriaD;
    private String criteriaE;
    
    public VendorAssessment(){};
    
    public VendorAssessment(String id, String criteria, String category) {
        setId(id);
        setCriteria(criteria);
        setCategory(category);
        
    }

    public VendorAssessment(String id, String criteria, String grade, String percentage,
                    String contractorDetail, String serviceType, String suggestImprove, String overalRating
            ) {
        setId(id);
        setCriteria(criteria);
        setGrade(grade);
        setPercentage(percentage);
        setContractorDetail(contractorDetail);
        setServiceType(serviceType);
        setSuggestImprove(suggestImprove);
        setOveralRating(overalRating);
        
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public void setContractorDetail(String contractorDetail) {
        this.contractorDetail = contractorDetail;
    }

    public String getId() {
        return id;
    }

    public String getCriteria() {
        return criteria;
    }

    public String getGrade() {
        return grade;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getContractorDetail() {
        return contractorDetail;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSuggestImprove() {
        return suggestImprove;
    }

    public void setSuggestImprove(String suggestImprove) {
        this.suggestImprove = suggestImprove;
    }

    public String getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(String overalRating) {
        this.overalRating = overalRating;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCriteriaA(String criteriaA) {
        this.criteriaA = criteriaA;
    }

    public String getCriteriaA() {
        return criteriaA;
    }

    public void setCriteriaB(String criteriaB) {
        this.criteriaB = criteriaB;
    }

    public String getCriteriaB() {
        return criteriaB;
    }

    public void setCriteriaC(String criteriaC) {
        this.criteriaC = criteriaC;
    }

    public String getCriteriaC() {
        return criteriaC;
    }

    public void setCriteriaD(String criteriaD) {
        this.criteriaD = criteriaD;
    }

    public String getCriteriaD() {
        return criteriaD;
    }

    public void setCriteriaE(String criteriaE) {
        this.criteriaE = criteriaE;
    }

    public String getCriteriaE() {
        return criteriaE;
    }
    
}
