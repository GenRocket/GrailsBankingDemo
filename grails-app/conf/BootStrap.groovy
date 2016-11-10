import com.genrocket.bank.MetaClassHelper

class BootStrap {
  def bootstrapService
  def userTestDataService
  def branchTestDataService
  def cardPoolTestDataService
  def cardTypeTestDataService
  def accountTypeTestDataService
  def customerLevelTestDataService
  def transactionTypeTestDataService
  def transactionManager

  def init = { servletContext ->
    MetaClassHelper.initialize()
    transactionManager.setNestedTransactionAllowed(true)

    environments {
      development {
        accountTypeTestDataService.loadData()
        transactionTypeTestDataService.loadData()
        customerLevelTestDataService.loadData()
        cardTypeTestDataService.loadData()
        cardPoolTestDataService.loadData(1000)
        branchTestDataService.loadData(10)
        userTestDataService.loadData(100)
        bootstrapService.createAccounts()
      }
      production {
        accountTypeTestDataService.loadData()
        transactionTypeTestDataService.loadData()
        customerLevelTestDataService.loadData()
        cardTypeTestDataService.loadData()
        cardPoolTestDataService.loadData(1000)
        branchTestDataService.loadData(10)
        userTestDataService.loadData(100)
        bootstrapService.createAccounts()
      }
    }
  }

  def destroy = {
  }
}
