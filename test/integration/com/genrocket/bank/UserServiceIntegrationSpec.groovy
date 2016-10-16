package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.UserTestDataLoader
import grails.test.spock.IntegrationSpec

/**
 * Generated By GenRocket 10/16/2016.
 */
class UserServiceIntegrationSpec extends IntegrationSpec {
  def userTestDataService
  def userService

  void "create user"() {
    given:


    when:

    def userList = (List<LoaderDTO>) UserTestDataLoader.load()
    def user = (User) userList.first().object

    userService.save(user)

    then:

    user.id
  }

  void "update user"() {
    given:

    userTestDataService.loadData()
    def user = User.first()
    def id = user.id

    when:

    user.firstName = 'TEST'
    userService.update(user)

    then:

    def temp = User.get(id)
    temp.firstName == 'TEST'
  }

  void "delete user"() {
    given:

    userTestDataService.loadData()
    def user = User.first()
    def id = user.id

    when:

    userService.delete(user)

    then:

    User.get(id) == null
  }


  void "test emailAddress for Null"() {
    given:

    userTestDataService.loadData()
    User user = User.first()
    user.emailAddress = null

    when:

    userService.update(user);

    then:

    user.errors.getFieldError("emailAddress").code == "nullable"

  }

  void "test title for Null"() {
    given:

    userTestDataService.loadData()
    User user = User.first()
    user.title = null

    when:

    userService.update(user);

    then:

    user.errors.getFieldError("title").code == "nullable"

  }

  void "test firstName for Null"() {
    given:

    userTestDataService.loadData()
    User user = User.first()
    user.firstName = null

    when:

    userService.update(user);

    then:

    user.errors.getFieldError("firstName").code == "nullable"

  }

  void "test lastName for Null"() {
    given:

    userTestDataService.loadData()
    User user = User.first()
    user.lastName = null

    when:

    userService.update(user);

    then:

    user.errors.getFieldError("lastName").code == "nullable"

  }

  void "test phoneNumber for Null"() {
    given:

    userTestDataService.loadData()
    User user = User.first()
    user.phoneNumber = null

    when:

    userService.update(user);

    then:

    user.errors.getFieldError("phoneNumber").code == "nullable"

  }

  void "test username for Null"() {
    given:

    userTestDataService.loadData()
    User user = User.first()
    user.username = null

    when:

    userService.update(user);

    then:

    user.errors.getFieldError("username").code == "nullable"

  }

  void "test emailAddress for unique"() {
    given:


    def userList = (List<LoaderDTO>) UserTestDataLoader.load(2)
    def user = (User) userList.first().object

    def temp = user.emailAddress;

    when:

    userService.save(user);

    def testUser = (User) userList[1].object
    testUser.emailAddress = temp;

    userService.save(testUser);

    then:

    testUser.errors.getFieldError("emailAddress").code == "unique"
  }

  void "test username for unique"() {
    given:


    def userList = (List<LoaderDTO>) UserTestDataLoader.load(2)
    def user = (User) userList.first().object

    def temp = user.username;

    when:

    userService.save(user);

    def testUser = (User) userList[1].object
    testUser.username = temp;

    userService.save(testUser);

    then:

    testUser.errors.getFieldError("username").code == "unique"
  }
}
    