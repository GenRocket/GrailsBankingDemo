
package com.genrocket.bank.testDataLoader

import com.genrocket.bank.Transaction
import com.genRocket.tdl.LoaderDTO
import com.genRocket.tdl.TestDataLoader
import com.genRocket.tdl.ScenarioParams
import java.text.SimpleDateFormat

/**
 * Generated By GenRocket 10/18/2016.
 */
class TransactionTestDataLoader {
  static SCENARIO_PATH = 'GENROCKET_BANK_SCENARIOS'
  static SCENARIO = 'TransactionScenario'
  static SCENARIO_DOMAIN = 'Transaction'

  static load(Integer loopCount = 1) {
    ScenarioParams scenarioParams = new ScenarioParams(SCENARIO_PATH, SCENARIO, SCENARIO_DOMAIN, loopCount)
    def data = TestDataLoader.runScenario(scenarioParams)
    List<LoaderDTO> testData = []

    data.each { node ->
      LoaderDTO dto = new LoaderDTO()

      dto.map.put('account', node.account)
      dto.map.put('transactionType', node.transactionType)
      dto.map.put('user', node.user)

      node.remove('account')
      node.remove('transactionType')
      node.remove('user')

      node.dateCreated = new SimpleDateFormat('yyyy-MM-dd').parse(node.dateCreated)


      dto.object = new Transaction(node)
      testData.add(dto)
    }

    return testData
  }
}
    