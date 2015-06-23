package se.inera.webcert.pages

import geb.Browser

class EditeraIntygPage extends AbstractEditCertPage {

    static at = { doneLoading() && $(".edit-form").isDisplayed() }

    static content = {
        tillbakaButton(required: false) { $("#tillbakaButton") }
        radera { $("#ta-bort-utkast") }
        skrivUtBtn { $("#skriv-ut-utkast") }
        konfirmeraRadera { $("#confirm-draft-delete-button") }
        signeraBtn(required: false,wait:true){ displayed($("#signera-utkast-button")) }
        signeraBtnNoWait(required: false) { $("#signera-utkast-button") }
        signRequiresDoctorMessage(required: false) { $("#sign-requires-doctor-message-text") }
        certificateIsSentToITMessage(required: false) { $("#certificate-is-sent-to-it-message-text") }
        enhetsPostadress(required: false) { $("#clinicInfoPostalAddress") }
        enhetsPostnummer(required: false) { $("#clinicInfoPostalCode") }
        enhetsPostort(required: false) { $("#clinicInfoPostalCity") }
        enhetsTelefonnummer(required: false) { $("#clinicInfoPhone") }
        enhetsEpost(required: false) { $("#clinicInfoEmail") }
        intygetSparatOchKomplettMeddelande(wait:20){ displayed($("#intyget-sparat-och-komplett-meddelande")) }
        intygetSparatOchEjKomplettMeddelande(wait:20){ displayed($("#intyget-sparat-och-ej-komplett-meddelande")) }
        errorPanel { $("#error-panel") }
        visaVadSomSaknasKnapp { $("#showCompleteButton") }
        visaVadSomSaknasLista(required: false, wait:true) { displayed($("#visa-vad-som-saknas-lista")) }
        visaVadSomSaknasListaNoWait(required: false) { $("#visa-vad-som-saknas-lista") }
        sekretessmarkering { $("#sekretessmarkering") }
    }

    boolean isSignBtnDisplayed(){
        Browser.drive {
            waitFor {
                return signeraBtn.isDisplayed()
            }
        }
    }

}
