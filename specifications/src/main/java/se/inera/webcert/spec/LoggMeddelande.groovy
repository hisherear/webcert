package se.inera.webcert.spec

import se.inera.certificate.integration.json.CustomObjectMapper
import se.inera.log.messages.AbstractLogMessage
import se.inera.webcert.spec.util.RestClientFixture

/**
 * @author andreaskaltenbach
 */
class LoggMeddelande extends RestClientFixture {

    def restClient = createRestClient("${baseUrl}services/")

    
    private def logMessage
    private int count
    
    def rensaLoggMeddelanden() {
        restClient.delete(path: "logMessages/")
    }
    
    def hamtaLoggMeddelande() {
        logMessage = restClient.get(path: "logMessages/").data
    }
    
    String aktivitet() {
        logMessage.activityType
    }

    int antalLoggMeddelanden() {
        restClient.get(path: "logMessages/count").data
    }
    

}
