package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.AccountTypeTestDataLoader
import grails.test.spock.IntegrationSpec

/**
 * Generated By GenRocket 10/16/2016.
 */
class AccountTypeServiceConstraintSpec extends IntegrationSpec {
  def accountTypeTestDataService
  def accountTypeService

  void "test name for Null"() {
    given:

    accountTypeTestDataService.loadData()
    AccountType accountType = AccountType.first()
    accountType.name = null

    when:

    accountTypeService.update(accountType);

    then:

    accountType.errors.getFieldError("name").code == "nullable"

  }

  void "test name for unique"() {
    given:


    def accountTypeList = (List<LoaderDTO>) AccountTypeTestDataLoader.load(2)
    def accountType = (AccountType) accountTypeList.first().object

    def temp = accountType.name;

    when:

    accountTypeService.save(accountType);

    def testAccountType = (AccountType) accountTypeList[1].object
    testAccountType.name = temp;

    accountTypeService.save(testAccountType);

    then:

    testAccountType.errors.getFieldError("name").code == "unique"
  }
}
    