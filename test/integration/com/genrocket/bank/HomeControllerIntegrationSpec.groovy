package com.genrocket.bank

import com.genrocket.bank.co.LoginCO
import grails.test.spock.IntegrationSpec

class HomeControllerIntegrationSpec extends IntegrationSpec {
  def cardTestDataService

  void "test index"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()

    when:

    HomeController controller = new HomeController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)
    controller.index()

    then:

    controller.response.redirectedUrl == '/home/menu'
  }

  void "test exit"() {
    given:

    when:

    HomeController controller = new HomeController()
    controller.session.setAttribute('test','test')
    controller.exit()

    then:

    try {
      controller.session.getAttribute('test')
      assert false
    } catch (IllegalStateException e) {
      assert true
    }

    controller.response.redirectedUrl == '/'
  }

  void "test cardValidation CARD_INVALID"() {
    given:

    cardTestDataService.loadData()

    Card card = Card.first()
    String cardNumber = '9999999999999999'
    Integer pinNumber = 123456

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
    controller.modelAndView.viewName == '/home/index'
    controller.modelAndView.model.get('loginCO') == loginCO
  }

  void "test cardValidation CARD_NOT_ENABLED"() {
    given:

    cardTestDataService.loadData()

    Card card = Card.first()
    String cardNumber = card.cardNumber
    Integer pinNumber = 123456

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
    controller.modelAndView.viewName == '/home/index'
    controller.modelAndView.model.get('loginCO') == loginCO
  }

  void "test cardValidation CARD_EXPIRED"() {
    given:

    cardTestDataService.loadData()

    Card card = Card.first()
    String cardNumber = card.cardNumber
    Integer pinNumber = 123456

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
    controller.modelAndView.viewName == '/home/index'
    controller.modelAndView.model.get('loginCO') == loginCO
  }

  void "test cardValidation CARD_DEACTIVATED"() {
    given:

    cardTestDataService.loadData()

    Card card = Card.first()
    String cardNumber = card.cardNumber
    Integer pinNumber = 123456

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
    controller.modelAndView.viewName == '/home/index'
    controller.modelAndView.model.get('loginCO') == loginCO
  }

  void "test cardValidation PIN_INVALID"() {
    given:

    cardTestDataService.loadData()

    Card card = Card.first()
    String cardNumber = card.cardNumber
    Integer pinNumber = 123456

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
    controller.modelAndView.viewName == '/home/index'
    controller.modelAndView.model.get('loginCO') == loginCO
  }

  void "test cardValidation CARD_VALID"() {
    given:

    cardTestDataService.loadData()

    Card card = Card.first()
    String cardNumber = card.cardNumber
    Integer pinNumber = 123456

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
    controller.response.redirectedUrl == '/home/menu'
  }
}
