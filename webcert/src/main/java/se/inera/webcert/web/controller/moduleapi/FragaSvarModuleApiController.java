package se.inera.webcert.web.controller.moduleapi;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.webcert.persistence.fragasvar.model.FragaSvar;
import se.inera.webcert.service.FragaSvarService;
import se.inera.webcert.web.controller.moduleapi.dto.CreateQuestionParameter;
import se.inera.webcert.web.service.WebCertUserService;



public class FragaSvarModuleApiController {

    private static final Logger LOG = LoggerFactory.getLogger(FragaSvarModuleApiController.class);

    @Autowired
    private FragaSvarService fragaSvarService;
    

    /**
     * Helper service to get current user.
     */
    @Autowired
    private WebCertUserService webCertUserService;
    
    @GET
    @Path("/{intygId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<FragaSvar> fragaSvarForIntyg(@PathParam("intygId") String intygId) {
        return fragaSvarService.getFragaSvar(intygId);
    }
    @PUT
    @Path("/{fragasvarId}/answer" )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response answer(@PathParam( "fragasvarId" ) final Long frageSvarId, String svarsText) {
        LOG.debug("answer" + frageSvarId + ", text:" + svarsText);
        FragaSvar fragaSvarResponse =  fragaSvarService.saveSvar(frageSvarId, svarsText);
        return Response.ok(fragaSvarResponse).build();
    }
    
    @POST
    @Path("/{intygId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response createQuestion(@PathParam( "intygId" ) final String intygId, CreateQuestionParameter parameter) {
        LOG.debug("New question for cert:" + intygId + ", ämne:" + parameter.getAmne());
        
        //Amne amnet = Amne.valueOf(parameter.getAmne());
        
        FragaSvar fragaSvarResponse =  fragaSvarService.saveNewQuestion(intygId, parameter.getAmne(), parameter.getFrageText());
        return Response.ok(fragaSvarResponse).build();
    }
}
