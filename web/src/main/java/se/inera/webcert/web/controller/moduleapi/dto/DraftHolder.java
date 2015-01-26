package se.inera.webcert.web.controller.moduleapi.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;

import se.inera.webcert.persistence.utkast.model.UtkastStatus;

/**
 * Container for a draft and its current status.
 *
 * @author nikpet
 */
public class DraftHolder {

    private boolean vidarebefordrad;

    private UtkastStatus status;

    @JsonRawValue
    private String content;

    public DraftHolder() {

    }

    public boolean isVidarebefordrad() {
        return vidarebefordrad;
    }

    public void setVidarebefordrad(boolean vidarebefordrad) {
        this.vidarebefordrad = vidarebefordrad;
    }

    public UtkastStatus getStatus() {
        return status;
    }

    public void setStatus(UtkastStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}