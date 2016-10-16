package com.genrocket.bank.testDataLoader

import com.genRocket.tdl.LoaderDTO
import com.genRocket.tdl.ScenarioParams
import com.genRocket.tdl.TestDataLoader
import com.genrocket.bank.Customer

/**
 * Generated By GenRocket 10/16/2016.
 */
class CustomerTestDataLoader {
  static SCENARIO_PATH = 'GENROCKET_BANK_SCENARIOS'
  static SCENARIO = 'CustomerScenario'
  static SCENARIO_DOMAIN = 'Customer'

  static load(Integer loopCount = 1) {
    ScenarioParams scenarioParams = new ScenarioParams(SCENARIO_PATH, SCENARIO, SCENARIO_DOMAIN, loopCount)
    def data = TestDataLoader.runScenario(scenarioParams)
    List<LoaderDTO> testData = []

    data.each { node ->
      LoaderDTO dto = new LoaderDTO()

      dto.map.put('account', node.account)
      dto.map.put('customerLevel', node.customerLevel)
      dto.map.put('user', node.user)

      node.remove('account')
      node.remove('customerLevel')
      node.remove('user')

      dto.object = new Customer(node)
      testData.add(dto)
    }

    return testData
  }
}
    