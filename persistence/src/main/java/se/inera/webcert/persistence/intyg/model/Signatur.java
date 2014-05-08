package se.inera.webcert.persistence.intyg.model;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "SIGNATUR")
public class Signatur {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SIGNATUR_ID")
    private long signaturId;

    @Column(name = "SIGNERINGS_DATUM")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime signeringsDatum;

    @Column(name = "SIGNERAD_AV")
    private String signeradAv;

    @Column(name = "INTYG_ID")
    private String intygId;

    @Lob
    @Column(name = "INTYG_DATA")
    private String intygData;

    @Lob
    @Column(name = "INTYG_HASH")
    private String intygHash;

    @Lob
    @Column(name = "SIGNATUR_DATA")
    private String signatur;

    public Signatur() {
    }

    public Signatur(LocalDateTime signeringsDatum, String signeradAv, String intygId, String intygData, String intygHash, String signatur) {
        this.signeringsDatum = signeringsDatum;
        this.signeradAv = signeradAv;
        this.intygId = intygId;
        this.intygData = intygData;
        this.intygHash = intygHash;
        this.signatur = signatur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Signatur signatur = (Signatur) o;

        return signatur.signaturId == signaturId;
    }

    @Override
    public int hashCode() {
        return (int) signaturId;
    }

    public long getSignaturId() {
        return signaturId;
    }

    public LocalDateTime getSigneringsDatum() {
        return signeringsDatum;
    }

    public String getSigneradAv() {
        return signeradAv;
    }

    public String getIntygId() {
        return intygId;
    }

    public String getIntygData() {
        return intygData;
    }

    public String getIntygHash() {
        return intygHash;
    }

    public String getSignatur() {
        return signatur;
    }
}
