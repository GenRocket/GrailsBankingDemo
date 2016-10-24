package com.genrocket.bank

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.testDataLoader.UserTestDataLoader
import com.genrocket.bank.util.CardUtil
import spock.lang.Specification

class CardUtilSpec extends Specification {

  def setup() {
  }

  def cleanup() {
  }

  def "test parseFullName"() {
    given:

    List<LoaderDTO> testData = UserTestDataLoader.load(100)

    when:

    List<User> users = []

    testData.each { node ->
      users.add((User) node.object)
    }

    then:

    users.each { user ->
      if (user.firstName && user.middleInitial && user.lastName && user.suffix) {
        assert CardUtil.parseFullName(user) == "${user.firstName} ${user.middleInitial} ${user.lastName} ${user.suffix}".toString()
      }

      else if (user.firstName && user.middleInitial && user.lastName ) {
        assert CardUtil.parseFullName(user) == "${user.firstName} ${user.middleInitial} ${user.lastName}".toString()
      }

      else if (user.firstName && user.lastName && user.suffix) {
        assert CardUtil.parseFullName(user) == "${user.firstName} ${user.lastName} ${user.suffix}".toString()
      }

      else if (user.firstName && user.lastName) {
        assert CardUtil.parseFullName(user) == "${user.firstName} ${user.lastName}".toString()
      }
    }
  }
}
