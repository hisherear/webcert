package se.inera.webcert.service.intyg.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.common.internal.Utlatande;
import se.inera.certificate.schema.util.ClinicalProcessCertificateMetaTypeConverter;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendType;
import se.inera.ifv.insuranceprocess.healthreporting.util.ModelConverter;
import se.inera.webcert.medcertqa.v1.LakarutlatandeEnkelType;
import se.inera.webcert.medcertqa.v1.VardAdresseringsType;
import se.inera.webcert.persistence.utkast.model.Utkast;
import se.inera.webcert.service.exception.WebCertServiceErrorCodeEnum;
import se.inera.webcert.service.exception.WebCertServiceException;
import se.inera.webcert.service.intyg.dto.IntygItem;

@Component
public class IntygServiceConverterImpl implements IntygServiceConverter {

    private static final Logger LOG = LoggerFactory.getLogger(IntygServiceConverterImpl.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<IntygItem> convertToListOfIntygItem(List<CertificateMetaType> source) {
        List<IntygItem> intygItems = new ArrayList<>();
        for (CertificateMetaType certificateMetaType : source) {
            intygItems.add(convertToIntygItem(certificateMetaType));
        }
        return intygItems;
    }

    private IntygItem convertToIntygItem(CertificateMetaType source) {

        IntygItem item = new IntygItem();
        item.setId(source.getCertificateId());
        item.setType(source.getCertificateType());
        item.setFromDate(source.getValidFrom());
        item.setTomDate(source.getValidTo());
        item.setStatuses(ClinicalProcessCertificateMetaTypeConverter.toStatusList(source.getStatus()));
        item.setSignedBy(source.getIssuerName());
        item.setSignedDate(source.getSignDate());

        return item;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * se.inera.webcert.service.intyg.converter.IntygServiceConverter#buildSendTypeFromUtlatande(se.inera.certificate
     * .model.Utlatande)
     */
    @Override
    public SendType buildSendTypeFromUtlatande(Utlatande utlatande) {

        // Lakarutlatande
        LakarutlatandeEnkelType utlatandeType = ModelConverter.toLakarutlatandeEnkelType(utlatande);

        // Vardadress
        VardAdresseringsType vardAdressType = ModelConverter.toVardAdresseringsType(utlatande.getGrundData());

        SendType sendType = new SendType();
        sendType.setLakarutlatande(utlatandeType);
        sendType.setAdressVard(vardAdressType);
        sendType.setVardReferensId(buildVardReferensId(Operation.SEND, utlatande.getId()));
        sendType.setAvsantTidpunkt(LocalDateTime.now());

        return sendType;
    }

    @Override
    public RevokeType buildRevokeTypeFromUtlatande(Utlatande utlatande, String revokeMessage) {

        // Lakarutlatande
        LakarutlatandeEnkelType utlatandeType = ModelConverter.toLakarutlatandeEnkelType(utlatande);

        // Vardadress
        VardAdresseringsType vardAdressType = ModelConverter.toVardAdresseringsType(utlatande.getGrundData());

        RevokeType revokeType = new RevokeType();
        revokeType.setLakarutlatande(utlatandeType);
        revokeType.setAdressVard(vardAdressType);
        revokeType.setVardReferensId(buildVardReferensId(Operation.REVOKE, utlatande.getId()));
        revokeType.setAvsantTidpunkt(LocalDateTime.now());

        if (revokeMessage != null) {
            revokeType.setMeddelande(revokeMessage);
        }

        return revokeType;
    }

    public String concatPatientName(List<String> fNames, List<String> mNames, String lName) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.join(fNames, " "));

        if (!mNames.isEmpty()) {
            sb.append(" ").append(StringUtils.join(mNames, " "));
        }

        sb.append(" ").append(lName);
        return StringUtils.normalizeSpace(sb.toString());
    }

    public String buildVardReferensId(Operation op, String intygId) {
        return buildVardReferensId(op, intygId, LocalDateTime.now());
    }

    public String buildVardReferensId(Operation op, String intygId, LocalDateTime ts) {
        String time = ts.toString(ISODateTimeFormat.basicDateTime());
        return StringUtils.join(new Object[]{op, intygId, time}, "-");
    }

    /**
     * Converts a List of @link{Utkast} into a List of @link{IntygItem} by building a base @link{Utlatande}
     * from the stored model in the utkast and applying the applicable fields
     * onto IntygItem instances.
     *
     * @param drafts
     * @return
     */
    @Override
    public List<IntygItem> convertDraftsToListOfIntygItem(List<Utkast> drafts) {
        List<IntygItem> intygItems = new ArrayList<>();
        for (Utkast utkast : drafts) {
            Utlatande utlatande = buildUtlatandeFromUtkastModel(utkast);
            intygItems.add(draftToIntygItem(utkast, utlatande));
        }
        return intygItems;
    }

    private IntygItem draftToIntygItem(Utkast utkast, Utlatande utlatande) {
        IntygItem intygItem = new IntygItem();

        intygItem.setId(utkast.getIntygsId());
        intygItem.setSignedBy(utkast.getSignatur().getSigneradAv());
        intygItem.setSignedDate(utlatande.getGrundData().getSigneringsdatum());
        intygItem.setType(utkast.getIntygsTyp());
        intygItem.setStatuses(buildStatusesFromUtkast(utkast));
        return intygItem;
    }

    /**
     * Given an Utkast, a List of Statuses is built given:
     *
     * <li>If draft has a skickadTillMottagareDatum, a SENT status is added</li>
     * <li>If draft has a aterkalledDatum, a CANCELLED status is added</li>
     * <li>If there is a signature with a signature date, a RECEIVED status is added.</li>
     * @param draft
     * @return
     */
    @Override
    public List<se.inera.certificate.model.Status> buildStatusesFromUtkast(Utkast draft) {
        List<se.inera.certificate.model.Status> statuses = new ArrayList<>();

        if (draft.getSkickadTillMottagareDatum() != null) {
            se.inera.certificate.model.Status status = new se.inera.certificate.model.Status(CertificateState.SENT,
                    draft.getSkickadTillMottagare(), draft.getSkickadTillMottagareDatum());
            statuses.add(status);
        }
        if (draft.getAterkalladDatum() != null) {
            se.inera.certificate.model.Status status = new se.inera.certificate.model.Status(CertificateState.CANCELLED,
                    null, draft.getAterkalladDatum());
            statuses.add(status);
        }
        if (draft.getSignatur() != null && draft.getSignatur().getSigneringsDatum() != null) {
            se.inera.certificate.model.Status status = new se.inera.certificate.model.Status(CertificateState.RECEIVED,
                    null, draft.getSignatur().getSigneringsDatum());
            statuses.add(status);
        }
        return statuses;
    }

    /**
     * Given the model (e.g. JSON representation of the Intyg stored in the Utkast), build an @{link Utlatande}
     *
     * @param utkast
     * @return
     */
    @Override
    public Utlatande buildUtlatandeFromUtkastModel(Utkast utkast) {
        try {
            return objectMapper.readValue(utkast.getModel(), Utlatande.class);
        } catch (IOException e) {
            LOG.error("Module problems occured when trying to unmarshall Utlatande.", e);
            throw new WebCertServiceException(WebCertServiceErrorCodeEnum.INTERNAL_PROBLEM, e);
        }
    }

    /** Since this class is @Spy injected at unit test level we need to manually set the ObjectMapper */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public enum Operation {
        SEND,
        REVOKE;
    }
}
