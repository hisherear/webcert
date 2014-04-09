package se.inera.webcert.pages

import geb.Page

class UnhandledQAPage extends Page {

    static at = { $("#unhandled-qa").isDisplayed() }

    static content = {
        unitstatUnhandledQuestionsBadgde(required: false) { $("#stat-unitstat-unhandled-question-count") }
        careUnitSelector(required: false) { $("#wc-care-unit-clinic-selector") }
        unhandledQATable(required: false) { $("#qaTable") }

        advancedFilterBtn { $("#show-advanced-filter-btn") }
        advancedFilterForm { $("#advanced-filter-form") }
        advandecFilterFormFragestallare { $("input", name: "frageStallare") }
        advancedFilterSelectDoctor { $("#qp-doctorSelector") }
        advancedFilterVidarebefordrad { $("input", name: "vidarebefordrad") }
        advancedFilterChangeDateFrom { $("#filter-changedate-from") }
        advancedFilterChangeDateTo { $("#filter-changedate-to") }
        advancedFilterStatus { $("#qp-showStatus") }
        advancedFilterSearchBtn { $("#filter-qa-btn") }
        advancedFilterResetBtn { $("#reset-search-form") }

        logoutLink { $("#logoutLink") }
    }

    def visaAllaFragor() {
        $("#select-active-unit-wc-all").click();
    }

    def selectCareUnit(String careUnit) {
        $("#select-active-unit-${careUnit}").click()
    }

    def showQA(String internReferens) {
        $("#showqaBtn-${internReferens}").click()
    }

    def isQAVisible(String internid) {
        $("#showqaBtn-${internid}").isDisplayed()
    }

    def showAdvancedFilter() {
        advancedFilterBtn.click()
    }

    def resetAdvancedFilter() {
        advancedFilterResetBtn.click()
    }

    def logout() {
        logoutLink.click()
    }
}
