package se.inera.webcert.spec

import static groovyx.net.http.ContentType.JSON

import org.joda.time.LocalDateTime

import se.inera.certificate.integration.json.CustomObjectMapper
import se.inera.log.messages.Enhet
import se.inera.log.messages.IntygCreateMessage
import se.inera.log.messages.IntygDeleteMessage
import se.inera.log.messages.IntygPrintMessage
import se.inera.log.messages.IntygReadMessage
import se.inera.log.messages.IntygRevokeMessage
import se.inera.log.messages.IntygSendMessage
import se.inera.log.messages.IntygSignMessage
import se.inera.log.messages.IntygUpdateMessage
import se.inera.log.messages.Patient
import se.inera.webcert.spec.util.RestClientFixture

/**
 * @author andreaskaltenbach
 */
class ProduceraLoggMeddelande extends RestClientFixture {

    def restClient = createRestClient(logSenderBaseUrl)

    def CustomObjectMapper objectMapper = new CustomObjectMapper()
    
    def aktivitet
    def vardperson
    def personnummer
    def intygId = "intyg-123"
    def enhet = "enEnhet"
    def vardgivare = "enVårdgivare"
    def namn = "namn"
    def systemId = "systemId"
    def systemNamn = "systemNamn"

    def execute() {
        def m
        switch(aktivitet) {
        case "Läsa":
            m = new IntygReadMessage(intygId)
            break
        case "Skapa":
            m = new IntygCreateMessage(intygId)
            break
        case "Spara":
            m = new IntygUpdateMessage(intygId)
            break
        case "Signera":
            m = new IntygSignMessage(intygId)
            break
        case "Radera":
            m = new IntygDeleteMessage(intygId)
            break
        case "Utskrift":
            m = new IntygPrintMessage(intygId, "Intyg")
            break
        case "Återkalla":
            m = new IntygRevokeMessage(intygId)
            break
        case "SkickaTillMottagare":
            m = new IntygSendMessage(intygId, "Mottagare")
            break
        }
        m.systemId = systemId
        m.systemName = systemNamn
        m.timestamp = new LocalDateTime()
        m.userId = vardperson
        m.userCareUnit = new Enhet(enhet, enhet, vardgivare, vardgivare)
        m.resourceOwner = new Enhet(enhet, enhet, vardgivare, vardgivare)
        if (personnummer) {
            m.patient = new Patient(personnummer, namn)
        }
        println objectMapper.writeValueAsString(m)
        restClient.post(
            path: "logMessages/",
            body: objectMapper.writeValueAsString(m),
            requestContentType: JSON
        )
    }

}
