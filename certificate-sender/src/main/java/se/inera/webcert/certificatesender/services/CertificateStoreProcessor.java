package se.inera.webcert.certificatesender.services;

import javax.xml.ws.WebServiceException;

import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import se.inera.certificate.modules.registry.IntygModuleRegistry;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.exception.ExternalServiceCallException;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.webcert.certificatesender.exception.PermanentException;
import se.inera.webcert.certificatesender.exception.TemporaryException;
import se.inera.webcert.certificatesender.services.validator.CertificateMessageValidator;
import se.inera.webcert.common.Constants;

/**
 * Camel message processor responsible for consuming {@link Constants#STORE_MESSAGE} messages,
 * using the ModuleApi to register certificates in intygstjansten.
 */
public class CertificateStoreProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(CertificateStoreProcessor.class);

    @Autowired
    private IntygModuleRegistry moduleRegistry;

    @Autowired
    @Qualifier("certificateStoreMessageValidator")
    private CertificateMessageValidator certificateStoreMessageValidator;

    public void process(Message message) throws Exception {
        LOG.debug("Receiving message: {}", message.getMessageId());

        certificateStoreMessageValidator.validate(message);

        ModuleApi moduleApi = moduleRegistry.getModuleApi((String) message.getHeader(Constants.INTYGS_TYP));

        try {
            moduleApi.registerCertificate(new InternalModelHolder((String) message.getBody()), (String) message.getHeader(Constants.LOGICAL_ADDRESS));
        } catch (ExternalServiceCallException e) {
            switch (e.getErroIdEnum()) {
                case TECHNICAL_ERROR:
                    // TODO: is this temporary?
                case APPLICATION_ERROR:
                    throw new TemporaryException(e.getMessage());
                default:
                    throw new PermanentException(e.getMessage());
            }
        } catch (ModuleException e) {
            throw new PermanentException(e.getMessage());
        } catch (WebServiceException e) {
            throw new TemporaryException(e.getMessage());
        } catch (Exception e) {
            throw new PermanentException(e.getMessage());
        }
    }
}