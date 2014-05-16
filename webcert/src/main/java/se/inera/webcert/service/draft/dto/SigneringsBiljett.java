package se.inera.webcert.service.draft.dto;

import org.joda.time.LocalDateTime;

public class SigneringsBiljett {

    public enum Status { BEARBETAR, SIGNERAD, OKAND }

    private final String id;
    private final Status status;
    private final String intygsId;
    private final String hash;
    private final LocalDateTime timestamp;

    public SigneringsBiljett(String id, Status status, String intygsId, String hash, LocalDateTime timestamp) {
        this.id = id;
        this.status = status;
        this.intygsId = intygsId;
        this.hash = hash;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public String getIntygsId() {
        return intygsId;
    }

    public String getHash() {
        return hash;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public SigneringsBiljett withStatus(Status status) {
        return new SigneringsBiljett(id, status, intygsId, hash, new LocalDateTime());
    }

    @Override
    public String toString() {
        return "SigneringsBiljett [ id:" + id + " intyg:" + intygsId + " status: " + status + " ]";
    }
}