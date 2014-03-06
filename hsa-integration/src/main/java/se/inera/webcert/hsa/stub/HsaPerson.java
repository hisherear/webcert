package se.inera.webcert.hsa.stub;

import java.util.ArrayList;
import java.util.List;

import se.inera.webcert.hsa.model.Specialisering;

public class HsaPerson {
    
    private String hsaId;
    
    private String forNamn;
    
    private String efterNamn;
    
    private List<Specialisering> specialiseringar = new ArrayList<Specialisering>();
    
    private List<String> enhetIds = new ArrayList<String>();
    
    public HsaPerson() {
        super();
    }
    
    public HsaPerson(String hsaId, String forNamn, String efterNamn) {
        super();
        this.hsaId = hsaId;
        this.forNamn = forNamn;
        this.efterNamn = efterNamn;
    }

    public String getHsaId() {
        return hsaId;
    }

    public void setHsaId(String hsaId) {
        this.hsaId = hsaId;
    }

    public String getForNamn() {
        return forNamn;
    }

    public void setForNamn(String forNamn) {
        this.forNamn = forNamn;
    }

    public String getEfterNamn() {
        return efterNamn;
    }

    public void setEfterNamn(String efterNamn) {
        this.efterNamn = efterNamn;
    }

    public List<Specialisering> getSpecialiseringar() {
        return specialiseringar;
    }

    public void setSpecialiseringar(List<Specialisering> specialiseringar) {
        this.specialiseringar = specialiseringar;
    }

    public List<String> getEnhetIds() {
        return enhetIds;
    }

    public void setEnhetIds(List<String> enhetIds) {
        this.enhetIds = enhetIds;
    }
    
}