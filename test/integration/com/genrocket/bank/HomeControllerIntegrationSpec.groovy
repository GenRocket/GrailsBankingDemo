package com.genrocket.bank

import com.genrocket.bank.co.LoginCO
import grails.test.spock.IntegrationSpec

class HomeControllerIntegrationSpec extends IntegrationSpec {
  def cardTestDataService

  void "test cardValidation CARD_INVALID"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = '9999999999999999'
    def pinNumber = 123456

    LoginCO loginCO = new LoginCO(
      cardNumber: cardNumber,
      pinNumber: pinNumber
    )

    when:

    card.enabled = true
    card.pinNumber = pinNumber
    card.dateDeactivated = null

    HomeController controller = new HomeController()
    controller.login(loginCO)

    then:

    loginCO.errors.getFieldError("cardNumber").code == "invalid.card.number"
  }

  void "test cardValidation CARD_NOT_ENABLED"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = card.cardNumber
    def pinNumber = 123456

    LoginCO loginCO = new LoginCO(
      cardNumber: cardNumber,
      pinNumber: pinNumber
    )

    when:

    card.enabled = false
    card.pinNumber = pinNumber
    card.dateDeactivated = null

    HomeController controller = new HomeController()
    controller.login(loginCO)

    then:

    loginCO.errors.getFieldError("cardNumber").code == "card.not.enabled"
  }

  void "test cardValidation CARD_EXPIRED"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = card.cardNumber
    def pinNumber = 123456

    LoginCO loginCO = new LoginCO(
      cardNumber: cardNumber,
      pinNumber: pinNumber
    )

    when:

    card.enabled = true
    card.pinNumber = pinNumber
    card.dateExpired = new Date() - 10
    card.dateDeactivated = null

    HomeController controller = new HomeController()
    controller.login(loginCO)

    then:

    loginCO.errors.getFieldError("cardNumber").code == "card.expired"
  }

  void "test cardValidation CARD_DEACTIVATED"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = card.cardNumber
    def pinNumber = 123456

    LoginCO loginCO = new LoginCO(
      cardNumber: cardNumber,
      pinNumber: pinNumber
    )

    when:

    card.enabled = true
    card.pinNumber = pinNumber
    card.dateDeactivated = new Date()

    HomeController controller = new HomeController()
    controller.login(loginCO)

    then:

    loginCO.errors.getFieldError("cardNumber").code == "card.deactivated"
  }

  void "test cardValidation PIN_INVALID"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = card.cardNumber
    def pinNumber = 123456

    LoginCO loginCO = new LoginCO(
      cardNumber: cardNumber,
      pinNumber: pinNumber
    )

    when:

    card.pinNumber = 999999
    card.save()

    card.enabled = true
    card.dateDeactivated = null

    HomeController controller = new HomeController()
    controller.login(loginCO)

    then:

    loginCO.errors.getFieldError("cardNumber").code == "invalid.pin.number"
  }

  void "test cardValidation CARD_VALID"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = card.cardNumber
    def pinNumber = 123456

    LoginCO loginCO = new LoginCO(
      cardNumber: cardNumber,
      pinNumber: pinNumber
    )

    when:

    card.pinNumber = pinNumber
    card.save()

    card.enabled = true
    card.dateDeactivated = null

    HomeController controller = new HomeController()
    controller.login(loginCO)
    Card temp = (Card) controller.session.getAttribute(BankingService.SELECTED_CARD_SESSION)

    then:

    !loginCO.hasErrors()
    temp.cardNumber == card.cardNumber
    controller.response.redirectedUrl
  }

}
