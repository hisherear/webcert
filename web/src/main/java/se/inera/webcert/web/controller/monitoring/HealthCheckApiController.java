package se.inera.webcert.web.controller.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.webcert.service.monitoring.HealthCheckService;
import se.inera.webcert.service.monitoring.dto.HealthStatus;
import se.inera.webcert.web.controller.AbstractApiController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * RESTinterface for checking the general health status of the application.
 *
 * @author nikpet
 *
 */
public class HealthCheckApiController extends AbstractApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckApiController.class);

    @Autowired
    private HealthCheckService healthCheck;

    @GET
    @Path("/ping")
    @Produces(MediaType.APPLICATION_XML)
    public Response checkPing() {
        String xmlResponse = buildXMLResponse(true, 0);
        return Response.ok(xmlResponse).build();
    }

    @GET
    @Path("/db")
    @Produces(MediaType.APPLICATION_XML)
    public Response checkDB() {
        HealthStatus status = healthCheck.checkDB();
        String xmlResponse = buildXMLResponse(status);
        return Response.ok(xmlResponse).build();
    }

    @GET
    @Path("/jms")
    @Produces(MediaType.APPLICATION_XML)
    public Response checkJMS() {
        HealthStatus status = healthCheck.checkJMS();
        String xmlResponse = buildXMLResponse(status);
        return Response.ok(xmlResponse).build();
    }

    @GET
    @Path("/hsa")
    @Produces(MediaType.APPLICATION_XML)
    public Response checkHSA() {
        HealthStatus status = healthCheck.checkHSA();
        String xmlResponse = buildXMLResponse(status);
        return Response.ok(xmlResponse).build();
    }

    @GET
    @Path("/signature-queue")
    @Produces(MediaType.APPLICATION_XML)
    public Response checkSignatureQueue() {
        HealthStatus status = healthCheck.checkSignatureQueue();
        String xmlResponse = buildXMLResponse(status);
        return Response.ok(xmlResponse).build();
    }

    @GET
    @Path("/intygstjanst")
    @Produces(MediaType.APPLICATION_XML)
    public Response checkIntygstjanst() {
        HealthStatus status = healthCheck.checkIntygstjanst();
        String xmlResponse = buildXMLResponse(status);
        return Response.ok(xmlResponse).build();
    }

    @GET
    @Path("/uptime")
    @Produces(MediaType.APPLICATION_XML)
    public Response checkUptime() {
        HealthStatus status = healthCheck.checkUptime();
        String xmlResponse = buildXMLResponse(status);
        return Response.ok(xmlResponse).build();
    }

    private String buildXMLResponse(HealthStatus status) {
        return buildXMLResponse(status.isOk(), status.getMeasurement());
    }

    private String buildXMLResponse(boolean ok, long time) {
        StringBuilder sb = new StringBuilder();
        sb.append("<pingdom_http_custom_check>");
        sb.append("<status>" + (ok ? "OK" : "FAIL") + "</status>");
        sb.append("<response_time>" + time + "</response_time>");
        sb.append("</pingdom_http_custom_check>");
        return sb.toString();
    }
}
