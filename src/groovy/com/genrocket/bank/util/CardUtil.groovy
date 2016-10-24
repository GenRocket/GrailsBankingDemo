package com.genrocket.bank.util

import com.genrocket.bank.User

/**
 * Created by htaylor on 10/24/16.
 */
class CardUtil {

  static String parseFullName(User user) {
    StringBuilder fullName = new StringBuilder()

    if (user.middleInitial)
      fullName.append("${user.firstName} ${user.middleInitial} ${user.lastName}")
    else
      fullName.append("${user.firstName} ${user.lastName}")

    if (user.suffix) {
      fullName.append(" ${user.suffix}")
    }

    return fullName.toString().trim()
  }

  static Integer createSecurityCode() {
    Random rand = new Random()
    Integer min = 100
    Integer max = 999
    Integer code = rand.nextInt(max - min) + min

    return code
  }
}
