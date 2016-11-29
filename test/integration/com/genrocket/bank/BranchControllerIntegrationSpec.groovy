package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.BranchTestDataLoader
import grails.test.spock.IntegrationSpec

class BranchControllerIntegrationSpec extends IntegrationSpec {
  def branchTestDataService
  def messageSource

  void "test Branch list"() {
    given:
    branchTestDataService.loadData(20)

    when:
    BranchController controller = new BranchController()
    Map map = controller.list()

    then:
    map.count == Branch.count()
    map.branches == Branch.list([max: 15, sort: 'id', order: 'desc'])
  }

  void "test Branch list with pagination"() {
    given:
    branchTestDataService.loadData(10)

    when:
    BranchController controller = new BranchController()
    controller.params.max = 5
    controller.params.offset = 5
    Map map = controller.list()

    then:
    map.count == Branch.count()
    map.branches == Branch.list([max: 5, offset: 5, sort: 'id', order: 'desc'])

  }

  void "test Branch edit"() {
    given:
    branchTestDataService.loadData()
    Branch branch = Branch.first()

    when:
    BranchController controller = new BranchController()
    Map map = controller.edit(branch.id)

    then:
    map.branch == branch
    map.branch.id
  }

  void "test Branch add"() {
    given:

    when:
    BranchController controller = new BranchController()
    Map map = controller.edit(null)

    then:
    map.branch != null
    !map.branch.id
  }

  void "test Branch save"() {
    given:
    List<LoaderDTO> branchList = (List<LoaderDTO>) BranchTestDataLoader.load()
    Branch branch = (Branch) branchList.first().object

    when:
    BranchController controller = new BranchController()
    controller.save(branch)

    then:
    branch.id
    Branch.count()
    controller.flash.message == messageSource.getMessage("branch.successfully.edited", null, null)
    controller.response.redirectedUrl == '/branch/list'
  }

  void "test validation error on Branch save"() {
    given:
    List<LoaderDTO> branchList = (List<LoaderDTO>) BranchTestDataLoader.load()
    Branch branch = (Branch) branchList.first().object
    branch.name = null

    when:
    BranchController controller = new BranchController()
    controller.save(branch)

    then:
    !branch.id
    !Branch.count()
    branch.errors.getFieldError("name").code == "nullable"
    controller.modelAndView.viewName == '/branch/edit'
  }

}
