package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.TransactionTypeTestDataLoader
import grails.test.spock.IntegrationSpec

/**
 * Generated By GenRocket 10/16/2016.
 */
class TransactionTypeServiceConstraintSpec extends IntegrationSpec {
  def transactionTypeTestDataService
  def transactionTypeService

  void "test name for Null"() {
    given:

    transactionTypeTestDataService.loadData()
    TransactionType transactionType = TransactionType.first()
    transactionType.name = null

    when:

    transactionTypeService.update(transactionType);

    then:

    transactionType.errors.getFieldError("name").code == "nullable"

  }

  void "test name for unique"() {
    given:


    def transactionTypeList = (List<LoaderDTO>) TransactionTypeTestDataLoader.load(2)
    def transactionType = (TransactionType) transactionTypeList.first().object

    def temp = transactionType.name;

    when:

    transactionTypeService.save(transactionType);

    def testTransactionType = (TransactionType) transactionTypeList[1].object
    testTransactionType.name = temp;

    transactionTypeService.save(testTransactionType);

    then:

    testTransactionType.errors.getFieldError("name").code == "unique"
  }
}
    