package com.genrocket.bank

import grails.test.mixin.TestFor
import grails.test.spock.IntegrationSpec
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
class CardControllerIntegrationSpec extends IntegrationSpec {
  def cardTestDataService

  void "test Card list for null customerId"() {
    given:

    when:
    CardController controller = new CardController()
    Map map = controller.list(null)

    then:
    map.cards == []
  }

  void "test Card list"() {
    given:
    cardTestDataService.loadData(1)
    Customer customer = Customer.first()

    when:
    CardController controller = new CardController()
    Map map = controller.list(customer.id)

    then:
    map.cards == Card.findAllByCustomer(customer)
  }

  def "test card enable"() {
    given:
    cardTestDataService.loadData(1)
    Card card = Card.first()
    card.enabled = false
    card.save(flush: true)

    when:
    CardController controller = new CardController()
    controller.enable(card.id)

    then:
    card.enabled
    controller.response.redirectedUrl == '/card/list?customerId=' + card.customer.id
  }

  def "test card enable for no id passed"() {
    when:
    CardController controller = new CardController()
    controller.enable(null)

    then:
    controller.response.redirectedUrl == '/card/list'
  }

  def "test card disable"() {
    given:
    cardTestDataService.loadData(1)
    Card card = Card.first()
    card.enabled = true
    card.save(flush: true)

    when:
    CardController controller = new CardController()
    controller.disable(card.id)

    then:
    !card.enabled
    controller.response.redirectedUrl == '/card/list?customerId=' + card.customer.id
  }

  def "test card disable with null id"() {
    when:
    CardController controller = new CardController()
    controller.disable(null)

    then:
    controller.response.redirectedUrl == '/card/list'
  }

  def "test card deactivare with null id"() {
    when:
    CardController controller = new CardController()
    controller.deactivate(null)

    then:
    controller.response.redirectedUrl == '/card/list'
  }

  def "test card deactivated"() {
    given:
    cardTestDataService.loadData(1)
    Card card = Card.first()
    card.dateDeactivated = null
    card.save(flush: true)

    when:
    CardController controller = new CardController()
    controller.deactivate(card.id)

    then:
    card.dateDeactivated
    controller.response.redirectedUrl == '/card/list?customerId=' + card.customer.id
  }
}
