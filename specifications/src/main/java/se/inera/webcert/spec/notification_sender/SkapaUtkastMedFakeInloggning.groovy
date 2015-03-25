package se.inera.webcert.spec.notification_sender

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.URLENC
import static se.inera.webcert.spec.util.WebcertRestUtils.*

import org.apache.commons.io.IOUtils

import se.inera.webcert.spec.util.WebcertRestUtils

class SkapaUtkastMedFakeInloggning {

    String intygTyp = "fk7263"

    String patientPersonnummer
    String patientFornamn = "Test"
    String patientEfternamn = "Testsson"
    
    def response

    public void execute() {
        WebcertRestUtils.login()
        response = WebcertRestUtils.createNewUtkast(intygTyp, json())
    }

    boolean utkastCreated() {
        return response.success
    }

    private json() {
        """{ "intygType" : "fk7263", "patientPersonnummer" : "$patientPersonnummer", "patientFornamn" : "$patientFornamn", "patientEfternamn" : "$patientEfternamn", "patientPostadress" : "adres", "patientPostnummer" : "12345", "patientPostort" : "ort" }"""
    }

    // Return a string representation of the response payload, more specifically: utkastId
    String utkastId() {
        IOUtils.toString(response.data)
    }
}
