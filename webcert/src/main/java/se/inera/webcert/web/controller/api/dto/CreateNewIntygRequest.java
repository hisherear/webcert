package se.inera.webcert.web.controller.api.dto;

import org.apache.commons.lang.StringUtils;

public class CreateNewIntygRequest {

    private String intygType;

    private String patientPersonnummer;

    private String patientFornamn;

    private String patientEfternamn;

    private String vardEnhetHsdId;

    private String vardEnhetNamn;

    private String vardGivareHsdId;

    private String vardGivareNamn;

    public CreateNewIntygRequest() {

    }

    public boolean isValid() {

        if (StringUtils.isBlank(intygType)) {
            return false;
        }

        if (StringUtils.isBlank(patientPersonnummer)) {
            return false;
        }

        if (StringUtils.isBlank(patientFornamn) || StringUtils.isBlank(patientEfternamn)) {
            return false;
        }

        return true;
    }

    public String getIntygType() {
        return intygType;
    }

    public void setIntygType(String intygType) {
        this.intygType = intygType;
    }

    public String getPatientPersonnummer() {
        return patientPersonnummer;
    }

    public void setPatientPersonnummer(String patientPersonnummer) {
        this.patientPersonnummer = patientPersonnummer;
    }

    public String getPatientFornamn() {
        return patientFornamn;
    }

    public void setPatientFornamn(String patientFornamn) {
        this.patientFornamn = patientFornamn;
    }

    public String getPatientEfternamn() {
        return patientEfternamn;
    }

    public void setPatientEfternamn(String patientEfternamn) {
        this.patientEfternamn = patientEfternamn;
    }

    public String getVardEnhetHsdId() {
        return vardEnhetHsdId;
    }

    public void setVardEnhetHsdId(String vardEnhetHsdId) {
        this.vardEnhetHsdId = vardEnhetHsdId;
    }

    public String getVardEnhetNamn() {
        return vardEnhetNamn;
    }

    public void setVardEnhetNamn(String vardEnhetNamn) {
        this.vardEnhetNamn = vardEnhetNamn;
    }

    public String getVardGivareHsdId() {
        return vardGivareHsdId;
    }

    public void setVardGivareHsdId(String vardGivareHsdId) {
        this.vardGivareHsdId = vardGivareHsdId;
    }

    public String getVardGivareNamn() {
        return vardGivareNamn;
    }

    public void setVardGivareNamn(String vardGivareNamn) {
        this.vardGivareNamn = vardGivareNamn;
    }

}
