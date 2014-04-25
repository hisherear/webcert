package se.inera.webcert.pages

import geb.Page

class OmWebcertIntygPage extends Page {

    static at = { $("#aboutWebcertSupport").isDisplayed() }

    static content = {
        supportLink { $("#about-support") }
        intygLink { $("#about-intyg") }
        faqLink { $("#about-faq") }
        cookiesLink { $("#about-cookies") }
    }
}