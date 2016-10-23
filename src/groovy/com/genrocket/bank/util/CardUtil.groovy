package com.genrocket.bank.util

import com.genrocket.bank.User

/**
 * Created by htaylor on 10/24/16.
 */
class CardUtil {

  static String parseFullName(User user) {
    String fullName

    if (user.middleInitial)
      fullName = "${user.title} ${user.firstName} ${user.middleInitial}. ${user.lastName} ${user.suffix}"
    else
      fullName = "${user.title} ${user.firstName} ${user.lastName} ${user.suffix}"

    return fullName.trim()
  }

  static Integer createSecurityCode() {
    Random rand = new Random()
    Integer min = 100
    Integer max = 999
    Integer code = rand.nextInt(max - min) + min

    return code
  }
}
