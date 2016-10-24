package com.genrocket.bank.util

import com.genrocket.bank.Account

/**
 * Created by htaylor on 10/24/16.
 */
class AccountUtil {

  static Integer generateAccountNumber() {
    Random random = new Random();
    Integer min = 1000010000
    Integer max = 9999999999
    Boolean found = false
    Integer accountNumber = 0

    while (!found) {
      accountNumber = random.nextInt(max - min) + min

      Account account = Account.findByAccountNumber(accountNumber)

      found = account == null
    }

    return accountNumber
  }

}
