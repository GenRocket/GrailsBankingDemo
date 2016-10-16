package com.genrocket.bank.testData

import com.genRocket.tdl.LoaderDTO
import org.springframework.transaction.annotation.Transactional

import com.genrocket.bank.testDataLoader.CustomerTestDataLoader
import com.genrocket.bank.Customer
import com.genrocket.bank.Account
import com.genrocket.bank.CustomerLevel
import com.genrocket.bank.User

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class CustomerTestDataService {
  static transactional = true

  def customerService
  def accountTestDataService
  def customerLevelTestDataService
  def userTestDataService

  def loadData(Integer loopCount = 1, Map<String, Object> domainMap = null) {
    println "Loading data for Customer..."

    Account account = null
    CustomerLevel customerLevel = null
    User user = null

    if (domainMap) {
      account = (Account) domainMap['account']
      customerLevel = (CustomerLevel) domainMap['customerLevel']
      user = (User) domainMap['user']
    }

    if (Customer.count() == 0) {
      if (!account) {
        accountTestDataService.loadData()
        account = Account.first()
      }

      if (!customerLevel) {
        customerLevelTestDataService.loadData()
        customerLevel = CustomerLevel.first()
      }

      if (!user) {
        userTestDataService.loadData()
        user = User.first()
      }

      def customerList = (LoaderDTO[]) CustomerTestDataLoader.load(loopCount)

      customerList.each { node ->
        def customer = (Customer) node.object

        customer.account = account
        customer.customerLevel = customerLevel
        customer.user = user
        customerService.save(customer)
      }
    }
  }
}
    