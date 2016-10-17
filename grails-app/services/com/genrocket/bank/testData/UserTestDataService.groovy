package com.genrocket.bank.testData

import com.genRocket.tdl.LoaderDTO
import com.genrocket.bank.User
import com.genrocket.bank.testDataLoader.UserTestDataLoader
import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class UserTestDataService {
  static transactional = true

  def userService

  def loadData(Integer loopCount = 1, Map<String, Object> domainMap = null) {
    println "Loading data for User..."

    if (User.count() == 0) {

      def userList = (LoaderDTO[]) UserTestDataLoader.load(loopCount)

      userList.each { node ->
        def user = (User) node.object

        userService.save(user)
      }
    }
  }
}
    