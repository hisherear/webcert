package se.inera.webcert.web.controller.integration;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.webcert.service.intyg.IntygService;
import se.inera.webcert.service.intyg.dto.IntygContentHolder;
import se.inera.webcert.web.service.WebCertUserService;

/**
 * Controller to enable an external user to access certificates directly from a
 * link in an external patient care system.
 *
 * @author nikpet
 */
@Path("/certificate")
public class LegacyIntygIntegrationController {

    private static final String PARAM_CERT_TYPE = "certType";
    private static final String PARAM_CERT_ID = "certId";
    private static final String PARAM_QA_ONLY = "qaOnly";

    private static final Logger LOG = LoggerFactory.getLogger(LegacyIntygIntegrationController.class);

    private String urlBaseTemplate;

    private String urlFragmentTemplate;

    @Autowired
    private IntygService intygService;

    @Autowired
    private WebCertUserService webCertUserService;

    /**
     * Fetches a certificate from IT and then performs a redirect to the view that displays
     * the certificate. Can be used for all types of certificates.
     *
     * @param uriInfo
     * @param intygId
     *            The id of the certificate to view.
     * @return
     */
    @GET
    @Path("/{intygId}/questions")
    public Response redirectToIntyg(@Context UriInfo uriInfo, @PathParam("intygId") String intygId) {

        IntygContentHolder intygData = intygService.fetchExternalIntygData(intygId);

        String intygType = intygData.getMetaData().getType();

        LOG.debug("Redirecting to view intyg {} of type {}", intygId, intygType);

        //webCertUserService.clearEnabledFeaturesOnUser();
        //webCertUserService.enableFeaturesOnUser(WebcertFeature.HANTERA_FRAGOR);
        //webCertUserService.enableModuleFeatureOnUser(intygType, ModuleFeature.HANTERA_FRAGOR);

        return buildRedirectResponse(uriInfo, intygType, intygId);
    }

    private Response buildRedirectResponse(UriInfo uriInfo, String certificateType, String certificateId) {

        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();

        Map<String, Object> urlParams = new HashMap<String, Object>();
        urlParams.put(PARAM_CERT_TYPE, certificateType);
        urlParams.put(PARAM_CERT_ID, certificateId);
        urlParams.put(PARAM_QA_ONLY, true);

        URI location = uriBuilder.replacePath(urlBaseTemplate).fragment(urlFragmentTemplate).buildFromMap(urlParams);

        return Response.status(Status.TEMPORARY_REDIRECT).location(location).build();
    }

    public void setUrlBaseTemplate(String urlBaseTemplate) {
        this.urlBaseTemplate = urlBaseTemplate;
    }

    public void setUrlFragmentTemplate(String urlFragmentTemplate) {
        this.urlFragmentTemplate = urlFragmentTemplate;
    }
}