package se.inera.webcert.spec.certificate_sender

import se.inera.webcert.spec.util.AsyncUtils
import se.inera.webcert.spec.util.AsyncUtilsITStub
import se.inera.webcert.spec.util.WebcertRestUtils

/**
 * Specifically used for asserting that messages sent asynchronously from Webcert has (or has not) been processed
 * by Intygstjansten.
 */
class IntygstjanstAsynk {

    /**
     * Returns true if the Intyg identified by the specified ID exists in Intygstjänsten. Will wait up to ${timeout} ms.
     */
    boolean finnsIntygIIntygstjanstenMedVantetid(String intygsId, long vantetid = 4000L) {
        def result = false
        result = new AsyncUtils().intygFinnsIIntygstjansten(intygsId, vantetid)
    }

    /**
     * Returns true if the Intyg identified by the specified ID exists in Intygstjänsten with state SENT targeting the
     * specified mottagare. Will wait up to ${timeout} ms.
     */
    boolean finnsIntygIIntygstjanstenSkickadTillSkickadTillMedVantetid(String intygsId, String mottagare, long vantetid = 4000) {
        def result = false
        result = new AsyncUtils().intygFinnsMarkeratSomSkickatIIntygstjansten(intygsId, mottagare, vantetid)
    }

    /**
     * Returns true if the Intyg identified by the specified ID exists in Intygstjänsten with makulerat == true.
     * Will wait up to ${timeout} ms.
     */
    boolean finnsMakuleratIntygIIntygstjanstenMedVantetid(String intygsId, long vantetid = 4000L) {
        def result = false
        result = new AsyncUtils().makuleratIntygFinnsIIntygstjansten(intygsId, vantetid)
    }



    boolean finnsIntygIStubMedVantetid(String intygsId, long vantetid = 4000L) {
        def result = false
        result = new AsyncUtilsITStub().intygFinnsIStub(intygsId, vantetid)
    }


    boolean finnsIntygIStubSkickadTillSkickadTillMedVantetid(String intygsId, String mottagare, long vantetid = 4000) {
        def result = false
        result = new AsyncUtilsITStub().intygFinnsMarkeratSomSkickatIStub(intygsId, mottagare, vantetid)
    }


    boolean finnsMakuleratIntygIStubMedVantetid(String intygsId, long vantetid = 4000L) {
        def result = false
        result = new AsyncUtilsITStub().makuleratIntygFinnsIStub(intygsId, vantetid)
    }



    boolean resetIntygtjanstStub() {
        def result = false
        result = WebcertRestUtils.resetIntygtjanstStub()
    }
}
