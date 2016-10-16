package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.CardPoolTestDataLoader
import grails.test.spock.IntegrationSpec

/**
 * Generated By GenRocket 10/16/2016.
 */
class CardPoolServiceIntegrationSpec extends IntegrationSpec {
  def cardPoolTestDataService
  def cardPoolService

  void "create cardPool"() {
    given:


    when:

    def cardPoolList = (List<LoaderDTO>) CardPoolTestDataLoader.load()
    def cardPool = (CardPool) cardPoolList.first().object

    cardPoolService.save(cardPool)

    then:

    cardPool.id
  }

  void "update cardPool"() {
    given:

    cardPoolTestDataService.loadData()
    def cardPool = CardPool.first()
    def id = cardPool.id

    when:

    cardPool.cardNumber = 'TEST'
    cardPoolService.update(cardPool)

    then:

    def temp = CardPool.get(id)
    temp.cardNumber == 'TEST'
  }

  void "delete cardPool"() {
    given:

    cardPoolTestDataService.loadData()
    def cardPool = CardPool.first()
    def id = cardPool.id

    when:

    cardPoolService.delete(cardPool)

    then:

    CardPool.get(id) == null
  }


  void "test cardNumber for Null"() {
    given:

    cardPoolTestDataService.loadData()
    CardPool cardPool = CardPool.first()
    cardPool.cardNumber = null

    when:

    cardPoolService.update(cardPool);

    then:

    cardPool.errors.getFieldError("cardNumber").code == "nullable"

  }

  void "test nextAvailable for Null"() {
    given:

    cardPoolTestDataService.loadData()
    CardPool cardPool = CardPool.first()
    cardPool.nextAvailable = null

    when:

    cardPoolService.update(cardPool);

    then:

    cardPool.errors.getFieldError("nextAvailable").code == "nullable"

  }

  void "test used for Null"() {
    given:

    cardPoolTestDataService.loadData()
    CardPool cardPool = CardPool.first()
    cardPool.used = null

    when:

    cardPoolService.update(cardPool);

    then:

    cardPool.errors.getFieldError("used").code == "nullable"

  }

  void "test cardNumber for unique"() {
    given:


    def cardPoolList = (List<LoaderDTO>) CardPoolTestDataLoader.load(2)
    def cardPool = (CardPool) cardPoolList.first().object

    def temp = cardPool.cardNumber;

    when:

    cardPoolService.save(cardPool);

    def testCardPool = (CardPool) cardPoolList[1].object
    testCardPool.cardNumber = temp;

    cardPoolService.save(testCardPool);

    then:

    testCardPool.errors.getFieldError("cardNumber").code == "unique"
  }
}
    