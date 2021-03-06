package se.inera.webcert.service.log.dto;

import se.inera.certificate.common.util.StringUtil;

public class LogRequest {

    private String intygId;

    private String patientId;

    private String patientName;

    private String intygCareUnitId;
    private String intygCareUnitName;

    private String intygCareGiverId;
    private String intygCareGiverName;

    private String additionalInfo;

    public LogRequest() {
        super();
    }

    public void setPatientName(String fornamn, String mellannamn, String efternamn) {
        setPatientName(StringUtil.join(" ", fornamn, mellannamn, efternamn));
    }

    public String getIntygId() {
        return intygId;
    }

    public void setIntygId(String intygId) {
        this.intygId = intygId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getIntygCareUnitId() {
        return intygCareUnitId;
    }

    public void setIntygCareUnitId(String intygCareUnitId) {
        this.intygCareUnitId = intygCareUnitId;
    }

    public String getIntygCareUnitName() {
        return intygCareUnitName;
    }

    public void setIntygCareUnitName(String intygCareUnitName) {
        this.intygCareUnitName = intygCareUnitName;
    }

    public String getIntygCareGiverId() {
        return intygCareGiverId;
    }

    public void setIntygCareGiverId(String intygCareGiverId) {
        this.intygCareGiverId = intygCareGiverId;
    }

    public String getIntygCareGiverName() {
        return intygCareGiverName;
    }

    public void setIntygCareGiverName(String intygCareGiverName) {
        this.intygCareGiverName = intygCareGiverName;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
