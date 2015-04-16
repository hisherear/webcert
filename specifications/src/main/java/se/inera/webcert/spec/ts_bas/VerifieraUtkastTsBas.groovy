package se.inera.webcert.spec.ts_bas

import se.inera.webcert.spec.Browser

class VerifieraUtkastTsBas {
    String postadress() {
        def result
        Browser.drive {
            result = page.patient.postadress.value()
        }
        result
    }

    String postnummer() {
        def result
        Browser.drive {
            result = page.patient.postnummer.value()
        }
        result
    }

    String postort() {
        def result
        Browser.drive {
            result = page.patient.postort.value()
        }
        result
    }

    String intygetAvser() {
        def result
        Browser.drive {
            result = page.intygetAvser.hamtaBehorigheter()
        }
        result
    }
}