
    package com.genrocket.bank.testDataLoader

    import com.genrocket.bank.User
    import com.genRocket.tdl.LoaderDTO
    import com.genRocket.tdl.TestDataLoader
    import com.genRocket.tdl.ScenarioParams
    

    /**
     * Generated By GenRocket 10/16/2016.
     */
    class UserTestDataLoader {
      static SCENARIO_PATH = 'GENROCKET_BANK_SCENARIOS'
      static SCENARIO = 'UserScenario'
      static SCENARIO_DOMAIN = 'User'

      static load(Integer loopCount = 1) {
        ScenarioParams scenarioParams = new ScenarioParams(SCENARIO_PATH, SCENARIO, SCENARIO_DOMAIN, loopCount)
        def data = TestDataLoader.runScenario(scenarioParams)
        List<LoaderDTO> testData = []

        data.each { node ->
          LoaderDTO dto = new LoaderDTO()
          dto.object = new User(node)
          testData.add(dto)
        }

        return testData
      }
    }
    