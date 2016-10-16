package com.genrocket.bank

class CustomerLevel {
  String name
  Boolean overdraftAllowed = Boolean.FALSE
  Integer dailyWithdrawalLimit
  Integer monthlyMaxTransfersAllowed

  static constraints = {
    name nullable: false, blank: false, maxSize: 15, unique: true
    overdraftAllowed nullable: false
    dailyWithdrawalLimit nullable: false
    monthlyMaxTransfersAllowed nullable: false
  }
}
