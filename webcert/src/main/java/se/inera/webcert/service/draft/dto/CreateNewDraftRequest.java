package se.inera.webcert.service.draft.dto;


import se.inera.webcert.service.dto.HoSPerson;
import se.inera.webcert.service.dto.Patient;
import se.inera.webcert.service.dto.Vardenhet;

public class CreateNewDraftRequest {

    private String intygId;
    
    private String intygType;
    
    private Patient patient;
    
    private HoSPerson hosPerson;
    
    private Vardenhet vardenhet;
    
    public CreateNewDraftRequest() {
    
    }

    public String getIntygId() {
        return intygId;
    }

    public void setIntygId(String intygId) {
        this.intygId = intygId;
    }

    public String getIntygType() {
        return intygType;
    }

    public void setIntygType(String intygType) {
        this.intygType = intygType;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public HoSPerson getHosPerson() {
        return hosPerson;
    }

    public void setHosPerson(HoSPerson hosPerson) {
        this.hosPerson = hosPerson;
    }

    public Vardenhet getVardenhet() {
        return vardenhet;
    }

    public void setVardenhet(Vardenhet vardenhet) {
        this.vardenhet = vardenhet;
    }
}