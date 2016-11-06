package com.genrocket.bank

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder

class BankingService {
  static transactional = false

  static String SELECTED_CARD_SESSION = "selectedCardSession"
  static String DEPOSIT_IN_ACTION = "doDeposit"
  static String WITHDRAWAL_IN_ACTION = "doWithdrawal"
  static String TRANSFER_IN_ACTION = "doTransfer"


  Card getSelectedCard() {
    (Card) getSession().getAttribute(SELECTED_CARD_SESSION)
  }

  void setSelectedCard(Card card) {
    getSession().setAttribute(SELECTED_CARD_SESSION, card)
  }

  boolean getDeposit() {
    getSession().getAttribute(DEPOSIT_IN_ACTION)?.toBoolean()
  }

  void setDeposit(boolean value) {
    getSession().setAttribute(DEPOSIT_IN_ACTION, value)
  }

  boolean getWithdrawal() {
    getSession().getAttribute(WITHDRAWAL_IN_ACTION)?.toBoolean()
  }

  void setWithdrawal(boolean value) {
    getSession().setAttribute(WITHDRAWAL_IN_ACTION, value)
  }

  boolean getTransfer() {
    getSession().getAttribute(TRANSFER_IN_ACTION)?.toBoolean()
  }

  void setTransfer(boolean value) {
    getSession().setAttribute(TRANSFER_IN_ACTION, value)
  }

  private getSession() {
    GrailsWebRequest request = RequestContextHolder.currentRequestAttributes()
    return request.session

  }
}
