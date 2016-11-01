package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.BranchTestDataLoader
import grails.test.spock.IntegrationSpec

/**
 * Generated By GenRocket 10/16/2016.
 */
class BranchServiceConstraintSpec extends IntegrationSpec {
  def branchTestDataService
  def branchService

  void "test externalId for Null"() {
    given:

    branchTestDataService.loadData()
    Branch branch = Branch.first()
    branch.branchCode = null

    when:

    branchService.update(branch);

    then:

    branch.errors.getFieldError("branchCode").code == "nullable"

  }

  void "test name for Null"() {
    given:

    branchTestDataService.loadData()
    Branch branch = Branch.first()
    branch.name = null

    when:

    branchService.update(branch);

    then:

    branch.errors.getFieldError("name").code == "nullable"

  }

  void "test address for Null"() {
    given:

    branchTestDataService.loadData()
    Branch branch = Branch.first()
    branch.address = null

    when:

    branchService.update(branch);

    then:

    branch.errors.getFieldError("address").code == "nullable"

  }

  void "test city for Null"() {
    given:

    branchTestDataService.loadData()
    Branch branch = Branch.first()
    branch.city = null

    when:

    branchService.update(branch);

    then:

    branch.errors.getFieldError("city").code == "nullable"

  }

  void "test state for Null"() {
    given:

    branchTestDataService.loadData()
    Branch branch = Branch.first()
    branch.state = null

    when:

    branchService.update(branch);

    then:

    branch.errors.getFieldError("state").code == "nullable"

  }

  void "test zipCode for Null"() {
    given:

    branchTestDataService.loadData()
    Branch branch = Branch.first()
    branch.zipCode = null

    when:

    branchService.update(branch);

    then:

    branch.errors.getFieldError("zipCode").code == "nullable"

  }

  void "test country for Null"() {
    given:

    branchTestDataService.loadData()
    Branch branch = Branch.first()
    branch.country = null

    when:

    branchService.update(branch);

    then:

    branch.errors.getFieldError("country").code == "nullable"

  }

  void "test phoneNumber for Null"() {
    given:

    branchTestDataService.loadData()
    Branch branch = Branch.first()
    branch.phoneNumber = null

    when:

    branchService.update(branch);

    then:

    branch.errors.getFieldError("phoneNumber").code == "nullable"

  }

  void "test branchCode for unique"() {
    given:


    def branchList = (List<LoaderDTO>) BranchTestDataLoader.load(2)
    def branch = (Branch) branchList.first().object

    def temp = branch.branchCode;

    when:

    branchService.save(branch);

    def testBranch = (Branch) branchList[1].object
    testBranch.branchCode = temp;

    branchService.save(testBranch);

    then:

    testBranch.errors.getFieldError("branchCode").code == "unique"
  }
}
    