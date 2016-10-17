package com.genrocket.bank.testData

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.*
import com.genrocket.bank.testDataLoader.AccountTestDataLoader
import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class AccountTestDataService {
  static transactional = true

  def accountService
  def accountTypeTestDataService
  def branchTestDataService
  def userTestDataService
  def cardTypeTestDataService
  def cardPoolTestDataService
  def customerLevelTestDataService

  def loadData(Integer loopCount = 1, Map<String, Object> domainMap = null) {
    println "Loading data for Account..."

    AccountType accountType = null
    Branch branch = null
    User user = null
    CardType cardType = null
    CustomerLevel customerLevel = null
    CardPool cardPool = null

    if (domainMap) {
      accountType = (AccountType) domainMap['accountType']
      branch = (Branch) domainMap['branch']
      user = (User) domainMap['user']
      cardType = (CardType) domainMap['cardType']
      customerLevel = (CustomerLevel) domainMap['customerLevel']
    }

    if (Account.count() == 0) {
      if (!cardPool) {
        cardPoolTestDataService.loadData(100)
      }

      if (!accountType) {
        accountTypeTestDataService.loadData()
        accountType = AccountType.first()
      }

      if (!branch) {
        branchTestDataService.loadData()
        branch = Branch.first()
      }

      if (!user) {
        userTestDataService.loadData()
        user = User.first()
      }

      if (!cardType) {
        cardTypeTestDataService.loadData()
        cardType = CardType.first()
      }

      if (!customerLevel) {
        customerLevelTestDataService.loadData()
        customerLevel = CustomerLevel.first()
      }

      def accountList = (LoaderDTO[]) AccountTestDataLoader.load(loopCount)

      accountList.each { node ->
        def account = (Account) node.object

        account.accountType = accountType
        account.branch = branch
        accountService.save(user, branch, cardType, accountType, customerLevel)
      }
    }
  }
}
    