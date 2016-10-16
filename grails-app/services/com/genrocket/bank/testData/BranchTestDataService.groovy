package com.genrocket.bank.testData

import com.genRocket.tdl.LoaderDTO
import org.springframework.transaction.annotation.Transactional

import com.genrocket.bank.testDataLoader.BranchTestDataLoader
import com.genrocket.bank.Branch

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class BranchTestDataService {
  static transactional = true

  def branchService

  def loadData(Integer loopCount = 1, Map<String, Object> domainMap = null) {
    println "Loading data for Branch..."

    if (Branch.count() == 0) {

      def branchList = (LoaderDTO[]) BranchTestDataLoader.load(loopCount)

      branchList.each { node ->
        def branch = (Branch) node.object

        branchService.save(branch)
      }
    }
  }
}
    