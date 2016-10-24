
package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import grails.test.spock.IntegrationSpec
import com.genrocket.bank.testDataLoader.TransactionTestDataLoader
import com.genrocket.bank.Account
import com.genrocket.bank.TransactionType
import com.genrocket.bank.User

/**
 * Generated By GenRocket 10/18/2016.
 */
class TransactionServiceIntegrationSpec extends IntegrationSpec {
  def transactionTestDataService
  def transactionService
  def accountTestDataService
  def transactionTypeTestDataService
  def userTestDataService

  void "create transaction"() {
    given:

    accountTestDataService.loadData()
    def account = Account.first()

    transactionTypeTestDataService.loadData()
    def transactionType = TransactionType.first()

    userTestDataService.loadData()
    def user = User.first()

    when:

    def transactionList = (List<LoaderDTO>) TransactionTestDataLoader.load()
    def transaction = (Transaction) transactionList.first().object

    transaction.account = account

    transaction.transactionType = transactionType

    transaction.user = user

    transactionService.save(transaction)

    then:

    transaction.id
  }

  void "update transaction"() {
    given:

    transactionTestDataService.loadData()
    def transaction = Transaction.first()
    def id = transaction.id

    when:

    Integer amount = transaction.amount + 1000
    transaction.amount = amount
    transactionService.update(transaction)

    then:

    def temp = Transaction.get(id)
    temp.amount == amount
  }

  void "delete transaction"() {
    given:

    transactionTestDataService.loadData()
    def transaction = Transaction.first()
    def id = transaction.id

    when:

    transactionService.delete(transaction)

    then:

    Transaction.get(id) == null
  }
}
    