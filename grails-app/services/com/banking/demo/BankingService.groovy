package com.banking.demo

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder

class BankingService {
    static transactional = false

    static String SELECTED_CARD_SESSION = "selectedCardSession"


    Card getSelectedCard() {
        (Card) getSession().getAttribute(SELECTED_CARD_SESSION)
    }

    void setSelectedCard(Card card) {
        getSession().setAttribute(SELECTED_CARD_SESSION, card)
    }

    private getSession() {
        GrailsWebRequest request = RequestContextHolder.currentRequestAttributes()
        return request.session

    }
}
