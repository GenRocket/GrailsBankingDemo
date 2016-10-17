package com.genrocket.bank.testData

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.Account
import com.genrocket.bank.Transaction
import com.genrocket.bank.TransactionType
import com.genrocket.bank.testDataLoader.TransactionTestDataLoader
import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class TransactionTestDataService {
  static transactional = true

  def transactionService
  def accountTestDataService
  def transactionTypeTestDataService

  def loadData(Integer loopCount = 1, Map<String, Object> domainMap = null) {
    println "Loading data for Transaction..."

    Account account = null
    TransactionType transactionType = null
    if (domainMap) {
      account = (Account) domainMap['account']
      transactionType = (TransactionType) domainMap['transactionType']
    }

    if (Transaction.count() == 0) {
      if (!account) {
        accountTestDataService.loadData()
        account = Account.first()
      }

      if (!transactionType) {
        transactionTypeTestDataService.loadData()
        transactionType = TransactionType.first()
      }

      def transactionList = (LoaderDTO[]) TransactionTestDataLoader.load(loopCount)

      transactionList.each { node ->
        def transaction = (Transaction) node.object

        transaction.account = account
        transaction.transactionType = transactionType
        transactionService.save(transaction)
      }
    }
  }
}
    