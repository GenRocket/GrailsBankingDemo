package com.genrocket.bank.testData

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.AccountType
import com.genrocket.bank.testDataLoader.AccountTypeTestDataLoader
import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class AccountTypeTestDataService {
  static transactional = true

  def accountTypeService

  def loadData(Integer loopCount = 1, Map<String, Object> domainMap = null) {
    println "Loading data for AccountType..."

    if (AccountType.count() == 0) {

      def accountTypeList = (LoaderDTO[]) AccountTypeTestDataLoader.load(loopCount)

      accountTypeList.each { node ->
        def accountType = (AccountType) node.object

        accountTypeService.save(accountType)
      }
    }
  }
}
    