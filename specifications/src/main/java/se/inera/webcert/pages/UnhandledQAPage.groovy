package se.inera.webcert.pages

import se.inera.certificate.page.AbstractPage

class UnhandledQAPage extends AbstractPage {

    static at = { doneLoading() && $("#unhandled-qa").isDisplayed() }

    static content = {
        unitstatUnhandledQuestionsBadgde(required: false) { $("#stat-unitstat-unhandled-question-count") }
        careUnitSelector(required: false) { $("#wc-care-unit-clinic-selector") }
        unhandledQATable(required: false) { $("#qaTable") }

        advancedFilterBtn { $("#show-advanced-filter-btn") }
        advancedFilterForm { $("#advanced-filter-form") }
        advandecFilterFormFragestallare { $("input", name: "frageStallare") }
        advancedFilterSelectDoctor { $("#qp-lakareSelector") }
        advancedFilterVidarebefordrad { $("input", name: "vidarebefordrad") }
        advancedFilterChangeDateFrom { $("#filter-changedate-from") }
        advancedFilterChangeDateTo { $("#filter-changedate-to") }
        advancedFilterStatus { $("#qp-showStatus") }
        advancedFilterSearchBtn { $("#filter-qa-btn") }
        advancedFilterResetBtn { $("#reset-search-form") }
        visaAllaFragaBtn(required: false){$("#select-active-unit-wc-all")}
        vcCentrumVastBtn(required: false){$("select-active-unit-centrum-vast")}
        fetchMoreBtn { $("#hamtaFler") }

        logoutLink { $("#logoutLink") }

    }

    def visaAllaFragor() {
        visaAllaFragaBtn.click();
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

    boolean hamtaFler() {
        if (fetchMoreBtn.isDisplayed()) {
            fetchMoreBtn.click()
            sleep(1000)
        }
        return true
    }

    def logout() {
        logoutLink.click()
    }

}
