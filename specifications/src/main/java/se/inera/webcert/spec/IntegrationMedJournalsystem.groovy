package se.inera.webcert.spec

import se.inera.certificate.spec.Browser
import se.inera.webcert.pages.*
import se.inera.webcert.pages.fk7263.EditCertPage
import se.inera.webcert.pages.fk7263.VisaFk7263Page

class IntegrationMedJournalsystem {

    boolean exists(content) {
        content
    }

    def visaIntygViaIntegration(String intygId) {
        Browser.drive {
            go "/visa/intyg/" + intygId + "?alternatePatientSSn="
            waitFor {
                at VisaFk7263Page
            }
        }
    }

    def visaIntygViaIntegrationMedPersonnummer(String intygId, String personnummer) {
        Browser.drive {
            go "/visa/intyg/" + intygId + "?alternatePatientSSn=" + personnummer
            waitFor {
                at VisaFk7263Page
            }
        }
    }

    boolean intygLaddat() {
        boolean result;
        Browser.drive {
            waitFor {
                at VisaPage
            }
            waitFor {
                result = page.intygLaddat.isDisplayed();
            }
        }
        return result;
    }

    boolean intygInteLaddat() {
        Browser.drive {
            waitFor {
                at VisaPage
            }
            intygSaknas.isDisplayed()
        }
    }

    def visaUtkastViaIntegrationMedPersonnummer(String intygId, String personnummer) {
        Browser.drive {
            go "/visa/intyg/" + intygId + "?alternatePatientSSn=" + personnummer
            waitFor {
                at EditCertPage
            }
        }
    }

    def visaUtkastViaIntegrationMedPersonnummerSignerandeLakare(String intygId, String personnummer, String signerandeLakare) {
        Browser.drive {
            go "/visa/intyg/" + intygId + "?alternatePatientSSn=" + personnummer + "&responsibleHospName=" + signerandeLakare
            waitFor {
                at EditCertPage
            }
        }
    }

    boolean utkastVisasViaIntegrationMedBehorighetsfel(String intygId) {
        boolean result
        Browser.drive {
            go "/visa/intyg/" + intygId
            waitFor {
                at EditCertPage
                result = page.errorPanel.isDisplayed()
            }

        }
        result
    }

    boolean utkastVisasViaIntegrationUtanBehorighetsfel(String intygId) {
        boolean result
        Browser.drive {
            go "/visa/intyg/" + intygId
            waitFor {
                at EditCertPage
            }
            result = !page.errorPanel.isDisplayed();
        }
        result
    }

    boolean nyttPersonnummerMeddelandeVisas() {
		def result
        Browser.drive {
            result = page.nyttPersonnummer.isDisplayed()
        }
		return result
    }

    boolean signerandeLakareMeddelandeVisas(expected) {
        def result
        Browser.drive {
            waitFor {
                page.signerandeLakare.isDisplayed()
            }
            result = (expected == page.signerandeLakare.text())
        }
        return result
    }

    boolean signeringKraverLakareMeddelandeVisas() {
        def result
        Browser.drive {
            waitFor {
                result = page.signeringKraverLakare.isDisplayed()
            }
        }
        return result
    }

    String patientensNamn() {
		String namnOchPersonnummer
        Browser.drive {
			waitFor{
				page.namnOchPersonnummer.isDisplayed()
			}
			namnOchPersonnummer = page.namnOchPersonnummer.text()
        }
		
       def (namn, personnummer) = namnOchPersonnummer.split( ' - ' )
	   return namn
    }
	
	String patientensPersonnummer() {
		String namnOchPersonnummer
		Browser.drive {
			waitFor{
				page.namnOchPersonnummer.isDisplayed()
			}
			namnOchPersonnummer = page.namnOchPersonnummer.text()
		}
		
	   def (namn, personnummer) = namnOchPersonnummer.split( ' - ' )
	   return personnummer
	}
	
    boolean kopieraIntyg(String intygId) {
        Browser.drive {
            page.copyButton.isDisplayed()
            page.copy()
        }
    }

    def stangAvKopieraDialogen() {
        Browser.drive {
            page.copyButton.click()
            waitFor {
                doneLoading()
            }
            page.kopieraDialogVisaInteIgen.click()
            page.kopieraDialogKopieraKnapp.click()
            page.kopieraDialogAvbrytKnapp.click()
        }
    }

    def aktiveraKopieraDialogen() {
        Browser.deleteCookie("wc.dontShowCopyDialog");
    }

    boolean kopieraIntygUtanDialog() {
        Browser.drive {
            page.copyButton.isDisplayed()
            page.copyButton.click()
        }
    }

    boolean intygsFelVisas() {
        Browser.drive {
            return page.intygFel.isDisplayed()
        }
    }

    def visaUtkastViaIntegration(String intygId) {
        Browser.drive {
            go "/visa/intyg/" + intygId
            waitFor {
                at EditCertPage
            }
        }
        true
    }


    boolean skickaKnappVisas() {
        Browser.drive {
            return page.skickaKnapp.isDisplayed()
        }
    }

    boolean skickaKnappVisasEj() {
        Browser.drive {
            return !page.skickaKnappNoWait.isDisplayed()
        }
    }

    boolean skrivUtKnappVisas() {
        Browser.drive {
            return (page.skrivUtKnapp?.present && page.skrivUtKnapp.isDisplayed()) || (page.skrivUtKnappEmployer?.present && page.skrivUtKnappEmployer.isDisplayed())
        }
    }

    boolean skrivUtKnappVisasEj() {
        Browser.drive {
            return !page.skrivUtKnappNoWait.isDisplayed()
        }
    }

    boolean kopieraKnappVisas() {
        boolean result;
        Browser.drive {
            waitFor {
                result = page.kopieraKnapp.isDisplayed()
            }
        }
        return result;
    }

    boolean kopieraKnappVisasEj() {
        Browser.drive {
            return !page.kopieraKnappNoWait.isDisplayed()
        }
    }

    boolean makuleraKnappVisas() {
        Browser.drive {
            return page.makuleraKnapp.isDisplayed()
        }
    }

    boolean makuleraKnappVisasEj() {
        Browser.drive {
            return !page.makuleraKnappNoWait.isDisplayed()
        }
    }

    boolean verifieraTillbakaknappBorta() {
        Browser.drive {
            waitFor() {
                !exists(page.tillbakaButton)
            }
        }
        true
    }

    boolean raderaUtkastFelmeddelandeVisas() {
        Browser.drive {
            waitFor {
                at EditCertPage
            }
            return page.errorPanel.isDisplayed()

        }
    }

    boolean integrationBorttagetMeddelandeVisas() {
        boolean result;
        Browser.drive {
            waitFor {
                at EditCertPage
            }
            waitFor{
                result = page.integrationBorttaget.isDisplayed();
            }
            return result;
        }
    }

    boolean vidarebefordraEjHanteradFragaEjSynlig() {
        Browser.drive {
            return !page.vidarebefordraEjHanterad.present
        }
    }

    boolean webcertLogoEjKlickbar() {
        Browser.drive {
            return !page.webcertLogoLink.present
        }
    }

    boolean huvudmenyEjSynlig() {
        Browser.drive {
            return !page.huvudmeny.present
        }
    }

    boolean bytVardenhetEjSynlig() {
        Browser.drive {
            return !page.bytVardenhetLink.present
        }
    }

    boolean loggaUtEjSynlig() {
        Browser.drive {
            return !page.loggaUtLink
        }
    }


    boolean omWebcertKanOppnasViaLank() {
        Browser.drive {
            page.omWebcertLink.click()
            return page.omWebcertDialog.isDisplayed()
        }
    }

    boolean inteFranJournalSystemTextVisas() {
        Browser.drive {
            return page.kopieraDialogMsgInteFranJournalSystem.isDisplayed()
        }
    }

    boolean inteFranJournalSystemTextInteVisas() {
        Browser.drive {
            return !page.kopieraDialogMsgInteFranJournalSystemNoWait.isDisplayed()
        }
    }

	boolean nyttPersonNummerTextVisas() {
		boolean result
		Browser.drive {
			result = page.kopieraDialogMsgNyttPersonId.isDisplayed()
		}
		return result
	}
	
     boolean nyttPersonNummerTextInteVisas() {
		boolean result
        Browser.drive {
            result = !page.kopieraDialogMsgNyttPersonIdNoWait.isDisplayed();
        }
		return result
     }


    def toggleKopieraDialogen(boolean val) {
        if (val) {
            Browser.drive {
                page.openCopyDialog();
                page.copyDialog.isDisplayed();
            }
        } else {
            Browser.drive {
                page.closeCopyDialog();
                !page.copyDialog.isDisplayed();
            }
        }
    }

    boolean kopieraDialogenVisas() {
        Browser.drive {
            return page.copyDialog.isDisplayed();
        }
    }

    boolean forlangningSjukskrivningVisas() {
        def result
        Browser.drive {
            result =  page.kopieraDialogMsgForlangningSjukskrivning();
        }
        return result
    }

    def sleepForNSeconds(String time) {
        def n = time as int;
        def originalMilliseconds = System.currentTimeMillis()
        Browser.drive {
            waitFor(n + 1, 0.5) {
                (System.currentTimeMillis() - originalMilliseconds) > (n * 1000)
            }
        }
    }


}
