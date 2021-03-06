package se.inera.webcert.service.notification;

import se.inera.certificate.modules.support.api.notification.HandelseType;
import se.inera.certificate.modules.support.api.notification.NotificationMessage;
import se.inera.webcert.persistence.fragasvar.model.FragaSvar;
import se.inera.webcert.persistence.utkast.model.Utkast;

public interface NotificationMessageFactory {

    NotificationMessage createNotificationMessage(String intygsId, HandelseType handelse);
    
    NotificationMessage createNotificationMessage(Utkast utkast, HandelseType handelse);

    NotificationMessage createNotificationMessage(FragaSvar fragaSvar, HandelseType handelse);

}
