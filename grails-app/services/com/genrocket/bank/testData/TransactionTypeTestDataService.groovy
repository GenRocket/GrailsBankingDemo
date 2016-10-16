package com.genrocket.bank.testData

import com.genRocket.tdl.LoaderDTO
import org.springframework.transaction.annotation.Transactional

import com.genrocket.bank.testDataLoader.TransactionTypeTestDataLoader
import com.genrocket.bank.TransactionType

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class TransactionTypeTestDataService {
  static transactional = true

  def transactionTypeService

  def loadData(Integer loopCount = 1, Map<String, Object> domainMap = null) {
    println "Loading data for TransactionType..."

    if (domainMap) {
    }


    if (TransactionType.count() == 0) {

      def transactionTypeList = (LoaderDTO[]) TransactionTypeTestDataLoader.load(loopCount)

      transactionTypeList.each { node ->
        def transactionType = (TransactionType) node.object

        transactionTypeService.save(transactionType)
      }
    }
  }
}
    