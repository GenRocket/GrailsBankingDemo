package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.CustomerTestDataLoader
import grails.test.spock.IntegrationSpec

/**
 * Generated By GenRocket 10/16/2016.
 */
class CustomerServiceIntegrationSpec extends IntegrationSpec {
  def customerService
  def accountTestDataService
  def customerLevelTestDataService
  def userTestDataService

  void "create customer"() {
    given:

    accountTestDataService.loadData()
    def account = Account.first()

    customerLevelTestDataService.loadData()
    def customerLevel = CustomerLevel.first()

    userTestDataService.loadData()
    def user = User.first()

    when:

    def customerList = (List<LoaderDTO>) CustomerTestDataLoader.load()
    def customer = (Customer) customerList.first().object

    customer.account = account

    customer.customerLevel = customerLevel

    customer.user = user

    customerService.save(customer)

    then:

    customer.id
  }

}
    