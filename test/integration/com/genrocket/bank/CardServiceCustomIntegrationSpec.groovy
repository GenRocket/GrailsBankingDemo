package com.genrocket.bank

import grails.test.spock.IntegrationSpec

/**
 * Created by htaylor on 10/17/16.
 */
class CardServiceCustomIntegrationSpec extends IntegrationSpec {
  def cardTestDataService
  def cardService

  void "test cardValidation CARD_INVALID"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = '9999999999999999'
    def pinNumber = 123456

    when:

    card.enabled = true
    card.pinNumber = pinNumber
    card.dateExpired = null
    card.dateDeactivated = null

    CardValidationTypes cardValidationType = cardService.cardValidation(cardNumber, pinNumber)

    then:

    cardValidationType == CardValidationTypes.CARD_INVALID
  }

  void "test cardValidation CARD_NOT_ENABLED"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = card.cardNumber
    def pinNumber = 123456

    when:

    card.enabled = false
    card.pinNumber = pinNumber
    card.dateExpired = null
    card.dateDeactivated = null

    CardValidationTypes cardValidationType = cardService.cardValidation(cardNumber, pinNumber)

    then:

    cardValidationType == CardValidationTypes.CARD_NOT_ENABLED
  }

  void "test cardValidation CARD_EXPIRED"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = card.cardNumber
    def pinNumber = 123456

    when:

    card.enabled = true
    card.pinNumber = pinNumber
    card.dateExpired = new Date()
    card.dateDeactivated = null

    CardValidationTypes cardValidationType = cardService.cardValidation(cardNumber, pinNumber)

    then:

    cardValidationType == CardValidationTypes.CARD_EXPIRED
  }

 void "test cardValidation CARD_DEACTIVATED"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = card.cardNumber
    def pinNumber = 123456

    when:

    card.enabled = true
    card.pinNumber = pinNumber
    card.dateExpired = null
    card.dateDeactivated = new Date()

    CardValidationTypes cardValidationType = cardService.cardValidation(cardNumber, pinNumber)

    then:

    cardValidationType == CardValidationTypes.CARD_DEACTIVATED
  }

  void "test cardValidation PIN_INVALID"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = card.cardNumber
    def pinNumber = 123456

    when:

    card.pinNumber = 999999
    card.save()

    card.enabled = true
    card.dateExpired = null
    card.dateDeactivated = null
    CardValidationTypes cardValidationType = cardService.cardValidation(cardNumber, pinNumber)

    then:

    cardValidationType == CardValidationTypes.PIN_INVALID
  }

  void "test cardValidation CARD_VALID"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def cardNumber = card.cardNumber
    def pinNumber = 123456

    when:

    card.pinNumber = pinNumber
    card.save()

    card.enabled = true
    card.dateExpired = null
    card.dateDeactivated = null
    CardValidationTypes cardValidationType = cardService.cardValidation(cardNumber, pinNumber)

    then:

    cardValidationType == CardValidationTypes.CARD_VALIDATED
  }

}
