package se.inera.webcert.service.notification;

import se.inera.webcert.persistence.fragasvar.model.FragaSvar;
import se.inera.webcert.persistence.utkast.model.Utkast;

public interface SendNotificationStrategy {

    Utkast decideNotificationForIntyg(String intygsId);
    
    Utkast decideNotificationForIntyg(Utkast utkast);

    Utkast decideNotificationForFragaSvar(FragaSvar fragaSvar);

}
