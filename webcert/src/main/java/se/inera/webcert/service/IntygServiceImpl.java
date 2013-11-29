package se.inera.webcert.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.inera.certificate.clinicalprocess.healthcond.certificate.getcertificateforcare.v1.GetCertificateForCareRequestType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.getcertificateforcare.v1.GetCertificateForCareResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.getcertificateforcare.v1.GetCertificateForCareResponseType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.ObjectFactory;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.UtlatandeType;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.integration.rest.ModuleRestApi;
import se.inera.certificate.integration.rest.ModuleRestApiFactory;
import se.inera.certificate.integration.rest.dto.CertificateContentHolder;
import se.inera.certificate.integration.rest.dto.CertificateContentMeta;
import se.inera.certificate.integration.rest.dto.CertificateStatus;
import se.inera.certificate.model.Utlatande;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.webcert.service.dto.UtlatandeCommonModelHolder;
import se.inera.webcert.service.exception.WebCertServiceErrorCodeEnum;
import se.inera.webcert.service.exception.WebCertServiceException;
import se.inera.webcert.web.service.WebCertUserService;

import com.google.common.base.Throwables;

/**
 * @author andreaskaltenbach
 */
@Service
public class IntygServiceImpl implements IntygService {

    private static Marshaller marshaller;
    private static final Logger LOG = LoggerFactory.getLogger(IntygServiceImpl.class);

    static {
        try {
            JAXBContext context = JAXBContext.newInstance(UtlatandeType.class);
            marshaller = context.createMarshaller();
        } catch (JAXBException e) {
            LOG.error("Failed to initialize marshaller for GetCertificate interaction", e);
            Throwables.propagate(e);
        }
    }

    @Autowired
    LogService logService;

    @Autowired
    private GetCertificateForCareResponderInterface certificateService;

    @Autowired
    private ModuleRestApiFactory moduleApiFactory;

    @Autowired
    private WebCertUserService webCertUserService;

    @Override
    public CertificateContentHolder fetchIntygData(String intygId) {
        CertificateContentHolder external = fetchExternalIntygData(intygId);
        return convertToInternalJson(external);
    }

    @Override
    public CertificateContentHolder fetchExternalIntygData(String intygId) {

        GetCertificateForCareResponseType intyg = fetchIntygFromIntygstjanst(intygId);

        verifyEnhetsAuth(intyg.getCertificate().getSkapadAv().getEnhet().getEnhetsId().getExtension());

        CertificateContentMeta metaData = convert(intyg.getMeta());

        ModuleRestApi moduleRestApi = moduleApiFactory.getModuleRestService(intyg.getMeta().getCertificateType());

        String externalJson = convertToExternalJson(moduleRestApi, intyg);

        CertificateContentHolder holder = new CertificateContentHolder();
        holder.setCertificateContent(externalJson);
        holder.setCertificateContentMeta(metaData);

        logService.logReadOfIntyg(intyg);

        return holder;
    }

    @Override
    public UtlatandeCommonModelHolder fetchIntygCommonModel(String intygId) {
        CertificateContentHolder external = fetchExternalIntygData(intygId);

        // Map it to our common model
        CustomObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande utlatande;
        try {
            utlatande = objectMapper.readValue(external.getCertificateContent(), Utlatande.class);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return new UtlatandeCommonModelHolder(utlatande, external.getCertificateContentMeta());
    }

    private CertificateContentMeta convert(CertificateMetaType source) {

        CertificateContentMeta metaData = new CertificateContentMeta();
        metaData.setId(source.getCertificateId());
        metaData.setType(source.getCertificateType());
        metaData.setStatuses(convertStatus(source.getStatus()));
        return metaData;
    }

    private List<CertificateStatus> convertStatus(List<CertificateStatusType> source) {
        List<CertificateStatus> status = new ArrayList<>();
        for (CertificateStatusType certificateStatusType : source) {
            status.add(convert(certificateStatusType));
        }
        return status;
    }

    private CertificateStatus convert(CertificateStatusType source) {
        return new CertificateStatus(source.getType().value(), source.getTarget(), source.getTimestamp());
    }

    private CertificateContentHolder convertToInternalJson(CertificateContentHolder external) {

        ModuleRestApi moduleRestApi = moduleApiFactory.getModuleRestService(external.getCertificateContentMeta()
                .getType());

        Response response = moduleRestApi.convertExternalToInternal(external);

        switch (response.getStatus()) {
        case 200:
            CertificateContentHolder responseHolder = new CertificateContentHolder();
            responseHolder.setCertificateContentMeta(external.getCertificateContentMeta());
            responseHolder.setCertificateContent(response.readEntity(String.class));
            return responseHolder;
        default:
            throw new WebCertServiceException(WebCertServiceErrorCodeEnum.INTERNAL_PROBLEM,
                    "Failed to convert intyg to internal JSON.");
        }
    }

    private String marshal(GetCertificateForCareResponseType intyg) {
        StringWriter writer = new StringWriter();
        try {
            JAXBElement<UtlatandeType> jaxbElement = new ObjectFactory().createUtlatande(intyg.getCertificate());
            marshaller.marshal(jaxbElement, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new WebCertServiceException(WebCertServiceErrorCodeEnum.INTERNAL_PROBLEM,
                    "Failed to marshall intyg coming from intygstjanst");
        }
    }

    private String convertToExternalJson(ModuleRestApi moduleRestApi, GetCertificateForCareResponseType intyg) {

        String xml = marshal(intyg);
        Response response = moduleRestApi.unmarshall(xml);

        switch (response.getStatus()) {
        case 200:
            return response.readEntity(String.class);
        default:
            String message = "Failed to convert intyg to external JSON: " + response.readEntity(String.class);
            LOG.error(message);
            throw new WebCertServiceException(WebCertServiceErrorCodeEnum.INTERNAL_PROBLEM, message);
        }
    }

    private GetCertificateForCareResponseType fetchIntygFromIntygstjanst(String intygsId) {
        GetCertificateForCareRequestType request = new GetCertificateForCareRequestType();
        request.setCertificateId(intygsId);

        GetCertificateForCareResponseType response = certificateService.getCertificateForCare("", request);

        switch (response.getResult().getResultCode()) {
        case OK:
            return response;
        case VALIDATION_ERROR:
            throw new WebCertServiceException(WebCertServiceErrorCodeEnum.DATA_NOT_FOUND,
                    "getCertificateForCare WS call:  VALIDATION_ERROR :" + response.getResult().getResultText());
        case REVOKED:
            throw new WebCertServiceException(WebCertServiceErrorCodeEnum.CERTIFICATE_REVOKED,
                    "getCertificateForCare WS call: REVOKED :" + response.getResult().getResultText());
        default:
            throw new WebCertServiceException(WebCertServiceErrorCodeEnum.EXTERNAL_SYSTEM_PROBLEM,
                    "getCertificateForCare WS call: ERROR :" + response.getResult().getResultText());
        }
    }

    protected void verifyEnhetsAuth(String enhetsId) {
        if (!webCertUserService.isAuthorizedForUnit(enhetsId)) {
            throw new WebCertServiceException(WebCertServiceErrorCodeEnum.AUTHORIZATION_PROBLEM,
                    "User not authorized for for enhet " + enhetsId);
        }

    }
}
