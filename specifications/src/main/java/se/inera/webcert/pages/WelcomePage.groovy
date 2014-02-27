package se.inera.webcert.pages

import geb.Page

class WelcomePage extends Page {

    static at = { $("#loginForm").isDisplayed() }

    static content = {
        userSelect { $("#jsonSelect") }
        loginBtn { $("#loginBtn") }
    }

    def startLogin() {
        loginBtn.click()
    }
}