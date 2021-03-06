package se.inera.webcert.spec.notification_sender

import se.inera.webcert.spec.util.WebcertRestUtils;
import se.inera.webcert.spec.util.RestClientFixture
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.URLENC

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.core.io.ClassPathResource

import static se.inera.webcert.spec.util.WebcertRestUtils.*

import org.apache.commons.io.IOUtils

class SkapaFragaTillFk {

    String intygId
    String intygTyp
    String internReferens

    def response

    public void execute() {
        WebcertRestUtils.login()
        response = WebcertRestUtils.createQuestionToFk(intygTyp,intygId,makeJson())
        internReferens = response.data.internReferens
    }

    def makeJson() {
        def fraga = new JsonSlurper().parse(new InputStreamReader(new ClassPathResource("fraga_svar_template.json").getInputStream()))
        JsonOutput.toJson(fraga)
    }

    public boolean fragaSkapad() {
        response.success
    }

    public String internReferens() {
        internReferens
    }
}
