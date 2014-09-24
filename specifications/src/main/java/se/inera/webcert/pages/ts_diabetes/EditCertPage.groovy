package se.inera.webcert.pages.ts_diabetes

import geb.Module
import geb.Page

class EditCertPage extends Page {

    static at = { $("#edit-ts-diabetes").isDisplayed() }

    static content = {

        // Knappar
        sparaKnapp { $("#spara-utkast") }

        // Meddelanden
        intygetSparatMeddelande { $("#intyget-sparat-meddelande") }

        // Formulärfält
        patient { module PatientModule }
        intygetAvser { module IntygetAvserModule }
        identitet { module IdentitetModule }
        allmant { module AllmantModule }
        hypoglykemier { module HypoglykemierModule }
        syn { module SynModule }
        bedomning { module BedomningModule}
        kommentar { $("#kommentar") }
        specialist { $("#specialist") }
        vardenhet { module VardenhetModule }
    }
}

class PatientModule extends Module {
    static base = { $("#patientForm") }
    static content = {
        postadress { $("#patientPostadress") }
        postnummer { $("#patientPostnummer") }
        postort { $("#patientPostort") }
    }
}

class IntygetAvserModule extends Module {
    static base = { $("#intygetAvserForm") }
    static content = {
        am { $("#typcheck0") }
        a1 { $("#typcheck1") }
        a2 { $("#typcheck2") }
        a { $("#typcheck3") }
        b { $("#typcheck4") }
        be { $("#typcheck5") }
        traktor { $("#typcheck6") }
        c1 { $("#typcheck7") }
        c1e { $("#typcheck8") }
        c { $("#typcheck9") }
        ce { $("#typcheck10") }
        d1 { $("#typcheck11") }
        d1e { $("#typcheck12") }
        d { $("#typcheck13") }
        de { $("#typcheck14") }
        taxi { $("#typcheck15") }
    }

    def valjBehorigheter(String valdaBehorigheter) {
        if (valdaBehorigheter != null) {
            am = false
            a1 = false
            a2 = false
            a = false
            b = false
            be = false
            traktor = false
            c1 = false
            c1e = false
            c = false
            ce = false
            d1 = false
            d1e = false
            d = false
            de = false
            taxi = false

            def behorigheter = valdaBehorigheter.split(",");

            if (behorigheter.contains("AM")) am = true
            if (behorigheter.contains("A1")) a1 = true
            if (behorigheter.contains("A2")) a2 = true
            if (behorigheter.contains("A")) a = true
            if (behorigheter.contains("B")) b = true
            if (behorigheter.contains("BE")) be = true
            if (behorigheter.contains("Traktor")) traktor = true
            if (behorigheter.contains("C1")) c1 = true
            if (behorigheter.contains("C1E")) c1e = true
            if (behorigheter.contains("C")) c = true
            if (behorigheter.contains("CE")) ce = true
            if (behorigheter.contains("D1")) d1 = true
            if (behorigheter.contains("D1E")) d1e = true
            if (behorigheter.contains("D")) d = true
            if (behorigheter.contains("DE")) de = true
            if (behorigheter.contains("Taxi")) taxi = true
        }
    }
}

class IdentitetModule extends Module {
    static base = { $("#identitetForm") }
    static content = {
        idkort { $("#identity0") }
        foretagskortTjansterkort { $("#identity1") }
        korkort { $("#identity2") }
        personligKannedom { $("#identity3") }
        forsakran { $("#identity4") }
        pass { $("#identity5") }
    }

    def valjTyp(String identifieringstyp) {
        if (identifieringstyp != null) {
            def validTypes = ["idkort", "foretagskort", "korkort", "kannedom", "forsakran", "pass"]
            assert validTypes.contains(identifieringstyp),
                    "Fältet 'identifieringstyp' kan endast innehålla något av följande värden: ${validTypes}"

            if ("idkort" == identifieringstyp) {
                idkort.click()
            } else if ("foretagskort" == identifieringstyp) {
                foretagskortTjansterkort.click()
            } else if ("korkort" == identifieringstyp) {
                korkort.click()
            } else if ("kannedom" == identifieringstyp) {
                personligKannedom.click()
            } else if ("forsakran" == identifieringstyp) {
                forsakran.click()
            } else if ("pass" == identifieringstyp) {
                pass.click()
            }
        }
    }
}

class AllmantModule extends Module {
    static base = { $("#allmantForm") }
    static content = {
        ar { $("#diabetesyear") }
        diabetestyp { $("input", name: "diabetestyp") }
        behandlingKost { $("#diabetestreat1") }
        behandlingTabletter { $("#diabetestreat2") }
        behandlingInsulin { $("#diabetestreat3") }
        behandlingInsulinPeriod { $("#insulinBehandlingsperiod") }
        behandlingAnnan { $("#annanBehandlingBeskrivning") }
    }

    def valjTyp(String valdDiabetestyp) {
        if (valdDiabetestyp != null) {
            def validTypes = ["typ1", "typ2"]
            assert validTypes.contains(valdDiabetestyp),
                    "Fältet 'diabetestyp' kan endast innehålla något av följande värden: ${validTypes}"

            if ("typ1" == valdDiabetestyp) {
                diabetestyp = "DIABETES_TYP_1"
            } else if ("typ2" == valdDiabetestyp) {
                diabetestyp = "DIABETES_TYP_2"
            }
        }
    }
}

class HypoglykemierModule extends Module {
    static base = { $("#hypoglykemierForm") }
    static content = {
        fragaA { $("input", name: "hypoa") }
        fragaB { $("input", name: "hypob") }
        fragaC { $("input", name: "hypoc") }
        fragaD { $("input", name: "hypod") }
        allvarligForekomstEpisoder { $("#allvarligForekomstBeskrivning") }
        fragaE { $("input", name: "hypoe") }
        allvarligForekomstTrafikEpisoder { $("#allvarligForekomstTrafikBeskrivning") }
        fragaF { $("input", name: "hypof") }
        fragaG { $("input", name: "hypog") }
        allvarligForekomstVakenTid { $("#allvarligForekomstVakenTidObservationstid") }
    }
}

class SynModule extends Module {
    static base = { $("#synForm") }
    static content = {
        fragaA { $("input", name: "syna") }
        fragaB { $("input", name: "synb") }
        hogerOgaUtanKorrektion { $("#synHogerOgaUtanKorrektion") }
        hogerOgaMedKorrektion { $("#synHogerOgaMedKorrektion") }
        vansterOgaUtanKorrektion { $("#synVansterOgaUtanKorrektion") }
        vansterOgaMedKorrektion { $("#synVansterOgaMedKorrektion") }
        binokulartUtanKorrektion { $("#synBinokulartUtanKorrektion") }
        binokulartMedKorrektion { $("#synBinokulartMedKorrektion") }
        fragaD { $("input", name: "synd") }
    }
}

class BedomningModule extends Module {
    static base = { $("#bedomningForm") }
    static content = {
        behorighet { $("input", name: "behorighet") }
        am { $("#korkortstyp0") }
        a1 { $("#korkortstyp1") }
        a2 { $("#korkortstyp2") }
        a { $("#korkortstyp3") }
        b { $("#korkortstyp4") }
        be { $("#korkortstyp5") }
        traktor { $("#korkortstyp6") }
        c1 { $("#korkortstyp7") }
        c1e { $("#korkortstyp8") }
        c { $("#korkortstyp9") }
        ce { $("#korkortstyp10") }
        d1 { $("#korkortstyp11") }
        d1e { $("#korkortstyp12") }
        d { $("#korkortstyp13") }
        de { $("#korkortstyp14") }
        taxi { $("#korkortstyp15") }
        bedomning { $("input", name:  "bedomning") }
    }

    def valjBehorigheter(String valdaBehorigheter) {
        if (valdaBehorigheter != null) {
            am = false
            a1 = false
            a2 = false
            a = false
            b = false
            be = false
            traktor = false
            c1 = false
            c1e = false
            c = false
            ce = false
            d1 = false
            d1e = false
            d = false
            de = false
            taxi = false

            def behorigheter = valdaBehorigheter.split(",");

            if (behorigheter.contains("AM")) am = true
            if (behorigheter.contains("A1")) a1 = true
            if (behorigheter.contains("A2")) a2 = true
            if (behorigheter.contains("A")) a = true
            if (behorigheter.contains("B")) b = true
            if (behorigheter.contains("BE")) be = true
            if (behorigheter.contains("Traktor")) traktor = true
            if (behorigheter.contains("C1")) c1 = true
            if (behorigheter.contains("C1E")) c1e = true
            if (behorigheter.contains("C")) c = true
            if (behorigheter.contains("CE")) ce = true
            if (behorigheter.contains("D1")) d1 = true
            if (behorigheter.contains("D1E")) d1e = true
            if (behorigheter.contains("D")) d = true
            if (behorigheter.contains("DE")) de = true
            if (behorigheter.contains("Taxi")) taxi = true
        }
    }
}

class VardenhetModule extends Module {
    static base = { $("#vardenhetForm") }
    static content = {
        postadress { $("#clinicInfoPostalAddress") }
        postnummer { $("#clinicInfoPostalCode") }
        postort { $("#clinicInfoPostalCity") }
        telefonnummer { $("#clinicInfoPhone") }
    }
}