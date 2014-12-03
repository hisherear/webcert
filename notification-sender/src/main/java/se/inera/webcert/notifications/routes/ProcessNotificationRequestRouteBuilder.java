package se.inera.webcert.notifications.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.webcert.notifications.process.EnrichWithIntygDataStrategy;
import se.inera.webcert.notifications.process.EnrichWithIntygModelDataStrategy;
import se.inera.webcert.notifications.process.FragaSvarEnricher;
import se.inera.webcert.notifications.process.SetIntygStatusProcessor;
import se.inera.webcert.persistence.intyg.model.IntygsStatus;

public class ProcessNotificationRequestRouteBuilder extends RouteBuilder {
        
    @Autowired
    private EnrichWithIntygModelDataStrategy intygModelEnricher;
    
    @Autowired
    private EnrichWithIntygDataStrategy intygPropertiesEnricher;
    
    @Autowired
    private FragaSvarEnricher fragaSvarEnricher;
    
    @Autowired
    private SetIntygStatusProcessor setIntygsStatusProcessor;
    
    @Override
    public void configure() throws Exception {
        from("ref:processNotificationRequestEndpoint").routeId("processNotificationRequest")
        .unmarshal("notificationRequestJaxb")
        .process(setIntygsStatusProcessor)
        .processRef("createAndInitCertificateStatusRequestProcessor")
        //Do not enrich for deleted drafts
        .choice()
            .when(header(RouteHeaders.RADERAT))
                .to("ref:sendCertificateStatusUpdateEndpoint")
            .otherwise()
                .enrich("getIntygFromWebcertRepositoryServiceEndpoint", AggregationStrategies.bean(intygPropertiesEnricher, "enrichWithIntygProperties"))
                .enrich("getIntygModelFromWebcertRepositoryServiceEndpoint", AggregationStrategies.bean(intygModelEnricher, "enrichWithArbetsformagorAndDiagnos"))
                //Check if intyg is signed, in that case enrich with fråga & svar
                .choice()
                    .when(header(RouteHeaders.INTYGS_STATUS).isEqualTo(IntygsStatus.SIGNED))
                        .enrich("getNbrOfQuestionsEndpoint", AggregationStrategies.bean(fragaSvarEnricher, "enrichWithNbrOfQuestionsForIntyg"))
                        .enrich("getNbrOfAnsweredQuestionsEndpoint", AggregationStrategies.bean(fragaSvarEnricher, "enrichWithNbrOfAnsweredQuestionsForIntyg"))
                        .enrich("getNbrOfHandledQuestionsEndpoint", AggregationStrategies.bean(fragaSvarEnricher, "enrichWithNbrOfHandledQuestionsForIntyg"))
                        .enrich("getNbrOfHandledAndAnsweredQuestionsEndpoint", AggregationStrategies.bean(fragaSvarEnricher, "enrichWithNbrOfHandledAndAnsweredQuestionsForIntyg"))
                        .to("ref:sendCertificateStatusUpdateEndpoint")
                    .otherwise()
                        .to("ref:sendCertificateStatusUpdateEndpoint");


    }
}