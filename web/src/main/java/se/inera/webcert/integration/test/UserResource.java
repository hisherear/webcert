package se.inera.webcert.integration.test;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import se.inera.webcert.persistence.privatlakaravtal.repository.AvtalRepository;
import se.inera.webcert.persistence.privatlakaravtal.repository.GodkantAvtalRepository;

@Transactional
public class UserResource {

    @Autowired
    private AvtalRepository avtalRepository;

    @Autowired
    private GodkantAvtalRepository godkantAvtalRepository;

    @PUT
    @Path("/godkannavtal/{hsaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response godkannAvtal(@PathParam("hsaId") String hsaId) {
        int avtalVersion = avtalRepository.getLatestAvtalVersion();
        godkantAvtalRepository.approveAvtal(hsaId, avtalVersion);
        return Response.ok().build();
    }

    @PUT
    @Path("/avgodkannavtal/{hsaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response avgodkannAvtal(@PathParam("hsaId") String hsaId) {
        godkantAvtalRepository.removeAllUserApprovments(hsaId);
        return Response.ok().build();
    }
}
