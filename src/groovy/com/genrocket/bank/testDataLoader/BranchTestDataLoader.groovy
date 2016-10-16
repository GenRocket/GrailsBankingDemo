package com.genrocket.bank.testDataLoader

import com.genRocket.tdl.LoaderDTO
import com.genRocket.tdl.ScenarioParams
import com.genRocket.tdl.TestDataLoader
import com.genrocket.bank.Branch

/**
 * Generated By GenRocket 10/16/2016.
 */
class BranchTestDataLoader {
  static SCENARIO_PATH = 'GENROCKET_BANK_SCENARIOS'
  static SCENARIO = 'BranchScenario'
  static SCENARIO_DOMAIN = 'Branch'

  static load(Integer loopCount = 1) {
    ScenarioParams scenarioParams = new ScenarioParams(SCENARIO_PATH, SCENARIO, SCENARIO_DOMAIN, loopCount)
    def data = TestDataLoader.runScenario(scenarioParams)
    List<LoaderDTO> testData = []

    data.each { node ->
      LoaderDTO dto = new LoaderDTO()
      dto.object = new Branch(node)
      testData.add(dto)
    }

    return testData
  }
}
    