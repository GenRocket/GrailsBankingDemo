package com.genrocket.bank

import grails.test.spock.IntegrationSpec
import groovy.time.TimeCategory

/**
 * Generated By GenRocket 10/16/2016.
 */
class CardServiceIntegrationSpec extends IntegrationSpec {
  def cardTestDataService
  def cardService
  def cardTypeTestDataService
  def customerTestDataService
  def cardPoolTestDataService

  void "create card"() {
    given:

    cardPoolTestDataService.loadData(100)

    cardTypeTestDataService.loadData()
    def cardType = CardType.first()

    customerTestDataService.loadData()
    def customer = Customer.first()

    when:

    cardService.save(cardType, customer)

    then:

    Card card = Card.findByCardTypeAndCustomer(cardType, customer)
    card.id
    card.cardType == cardType
    card.customer == customer

    card.enabled
    card.cardNumber
    card.nameOnCard
    card.securityCode
    card.dateIssued
    card.dateExpired

    use(TimeCategory) {
      card.dateExpired == card.dateIssued + 3.years
    }

    !card.pinNumber
    !card.dateActivated
    !card.dateDeactivated

  }

  void "update card"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def id = card.id

    when:

    card.nameOnCard = 'TEST'
    cardService.update(card)

    then:

    def temp = Card.get(id)
    temp.nameOnCard == 'TEST'
  }

  void "delete card"() {
    given:

    cardTestDataService.loadData()
    def card = Card.first()
    def id = card.id

    when:

    cardService.delete(card)

    then:

    Card.get(id) == null
  }

  void "test cardActivated INVALID_PIN_NUMBER when null"() {
    given:

    cardTestDataService.loadData()
    Card card = Card.first()

    when:

    TransactionStatus status = cardService.activateCard(card, null)

    then:

    status == TransactionStatus.INVALID_PIN_NUMBER

  }

  void "test cardActivated INVALID_PIN_NUMBER < 6 digits"() {
    given:

    cardTestDataService.loadData()
    Card card = Card.first()

    when:

    TransactionStatus status = cardService.activateCard(card, 12345)

    then:

    status == TransactionStatus.INVALID_PIN_NUMBER

  }

  void "test cardActivated INVALID_PIN_NUMBER > 6 digits"() {
    given:

    cardTestDataService.loadData()
    Card card = Card.first()

    when:

    TransactionStatus status = cardService.activateCard(card, 1234567)

    then:

    status == TransactionStatus.INVALID_PIN_NUMBER

  }

  void "test cardActivated CARD_ALREADY_ACTIVE"() {
    given:

    cardTestDataService.loadData()
    Card card = Card.first()

    card.dateActivated = new Date()
    card.pinNumber = 123456
    card.save()

    when:

    TransactionStatus status = cardService.activateCard(card, 123456)

    then:

    status == TransactionStatus.CARD_ALREADY_ACTIVE

  }

  void "test cardActivated CARD_DEACTIVATED"() {
    given:

    cardTestDataService.loadData()
    Card card = Card.first()

    card.dateDeactivated = new Date()
    card.save()

    when:

    TransactionStatus status = cardService.activateCard(card, 123456)

    then:

    status == TransactionStatus.CARD_DEACTIVATED

  }

  void "test cardActivated CARD_NOT_ENABLED"() {
    given:

    cardTestDataService.loadData()
    Card card = Card.first()

    card.enabled = false
    card.save()

    when:

    TransactionStatus status = cardService.activateCard(card, 123456)

    then:

    status == TransactionStatus.CARD_NOT_ENABLED

  }

  void "test cardActivated TRANSACTION_COMPLETE"() {
    given:

    cardTestDataService.loadData()
    Card card = Card.first()

    when:

    TransactionStatus status = cardService.activateCard(card, 123456)

    then:

    card.dateActivated
    status == TransactionStatus.TRANSACTION_COMPLETE

  }

  void "test deactivateCard"() {
    given:

    cardTestDataService.loadData()
    Card card = Card.first()

    TransactionStatus status = cardService.activateCard(card, 123456)

    assert card.dateActivated
    assert status == TransactionStatus.TRANSACTION_COMPLETE

    when:

    cardService.deactivateCard(card)

    then:

    card.dateDeactivated
  }

  void "test find active card"() {
    given:

    cardTestDataService.loadData()
    Card oldCard = Card.first()
    Customer customer = oldCard.customer
    CardType cardType = oldCard.cardType

    when:

    TransactionStatus status = cardService.activateCard(oldCard, 123456)
    Card activeCard = cardService.findActiveCard(customer, cardType)

    then:

    status == TransactionStatus.TRANSACTION_COMPLETE
    activeCard
  }

  void "test create replace card"() {
    given:

    cardTestDataService.loadData()
    Card oldCard = Card.first()
    Customer customer = oldCard.customer
    CardType cardType = oldCard.cardType

    when:

    TransactionStatus status = cardService.activateCard(oldCard, 123456)

    assert oldCard.dateActivated
    assert status == TransactionStatus.TRANSACTION_COMPLETE

    Card newCard = cardService.save(cardType, customer)

    then:

    oldCard.dateDeactivated
    newCard
  }
}
    