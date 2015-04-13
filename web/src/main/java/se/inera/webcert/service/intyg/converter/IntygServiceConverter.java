package se.inera.webcert.service.intyg.converter;

import java.util.List;

import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;
import se.inera.certificate.model.common.internal.Utlatande;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendType;
import se.inera.webcert.persistence.utkast.model.Utkast;
import se.inera.webcert.service.intyg.dto.IntygItem;

public interface IntygServiceConverter {

    List<IntygItem> convertToListOfIntygItem(List<CertificateMetaType> source);

    List<IntygItem> convertDraftsToListOfIntygItem(List<Utkast> drafts);

    SendType buildSendTypeFromUtlatande(Utlatande utlatande);

    RevokeType buildRevokeTypeFromUtlatande(Utlatande utlatande, String revokeMessage);

    List<se.inera.certificate.model.Status> buildStatusesFromUtkast(Utkast draft);

    Utlatande buildUtlatandeFromUtkastModel(Utkast utkast);
}
