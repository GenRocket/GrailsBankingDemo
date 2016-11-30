package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.UserTestDataLoader
import grails.test.spock.IntegrationSpec

class UserControllerIntegrationSpec extends IntegrationSpec {

  def userTestDataService
  def customerTestDataService
  def messageSource

  void "test User list"() {
    given:
    userTestDataService.loadData(20)

    when:
    UserController controller = new UserController()
    Map map = controller.list()

    then:
    map.count == User.count()
    map.users == User.list([max: 15, sort: 'id', order: 'desc'])
  }

  void "test User list with pagination"() {
    given:
    userTestDataService.loadData(10)

    when:
    UserController controller = new UserController()
    controller.params.max = 5
    controller.params.offset = 5
    Map map = controller.list()

    then:
    map.count == User.count()
    map.users == User.list([max: 5, offset: 5, sort: 'id', order: 'desc'])

  }

  void "test User edit"() {
    given:
    userTestDataService.loadData()
    User user = User.first()

    when:
    UserController controller = new UserController()
    Map map = controller.edit(user.id)

    then:
    map.user == user
    map.user.id
  }

  void "test User add"() {
    given:

    when:
    UserController controller = new UserController()
    Map map = controller.edit(null)

    then:
    map.user != null
    !map.user.id
  }

  void "test User save for newly added"() {
    given:
    List<LoaderDTO> userList = (List<LoaderDTO>) UserTestDataLoader.load()
    User user = (User) userList.first().object

    when:
    UserController controller = new UserController()
    controller.save(user)

    then:
    user.id
    User.count()
    controller.flash.message == messageSource.getMessage("user.successfully.added", null, null)
    controller.response.redirectedUrl == '/user/list'
  }

  void "test User save for edited"() {
    given:
    List<LoaderDTO> userList = (List<LoaderDTO>) UserTestDataLoader.load()
    User user = (User) userList.first().object
    user.save(flush: true)

    when:
    UserController controller = new UserController()
    controller.save(user)

    then:
    user.id
    User.count()
    controller.flash.message == messageSource.getMessage("user.successfully.edited", null, null)
    controller.response.redirectedUrl == '/user/list'
  }

  void "test validation error on User save"() {
    given:
    List<LoaderDTO> userList = (List<LoaderDTO>) UserTestDataLoader.load()
    User user = (User) userList.first().object
    user.firstName = null

    when:
    UserController controller = new UserController()
    controller.save(user)

    then:
    !user.id
    !User.count()
    user.errors.getFieldError("firstName").code == "nullable"
    controller.modelAndView.viewName == '/user/edit'
  }

  void "test User accounts for null Id"() {
    given:

    when:
    UserController controller = new UserController()
    Map map = controller.accounts(null)

    then:
    map.user == null
    map.customers == []
  }

  void "test User accounts"() {
    given:
    customerTestDataService.loadData(1)
    User user = User.first()

    when:
    UserController controller = new UserController()
    Map map = controller.accounts(user.id)

    then:
    map.user == user
    map.customers == Customer.findAllByUser(user)
  }

  def "test user enable"() {
    given:
    customerTestDataService.loadData(1)
    Customer customer = Customer.first()
    customer.enabled = false
    customer.save(flush: true)

    when:
    UserController controller = new UserController()
    controller.enable(customer.id)

    then:
    customer.enabled
    controller.response.redirectedUrl == '/user/accounts/' + customer.user.id
  }

  def "test user enable for no id passed"() {
    when:
    UserController controller = new UserController()
    controller.enable(null)

    then:
    controller.response.redirectedUrl == '/user/list'
  }

  def "test user disable"() {
    given:
    customerTestDataService.loadData(1)
    Customer customer = Customer.first()
    customer.enabled = true
    customer.save(flush: true)

    when:
    UserController controller = new UserController()
    controller.disable(customer.id)

    then:
    !customer.enabled
    controller.response.redirectedUrl == '/user/accounts/' + customer.user.id
  }

  def "test user disable with null id"() {
    when:
    UserController controller = new UserController()
    controller.disable(null)

    then:
    controller.response.redirectedUrl == '/user/list'
  }
}
