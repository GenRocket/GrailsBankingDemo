package com.banking.demo

class CustomerLevel {
  String name
  String overdraftAllowed = Boolean.FALSE
  Integer dailyWithdrawalLimit
  Integer monthlyMaxTransfersAllowed

  static constraints = {
    name nullable: false, blank: false, maxSize: 15, unique: true
    overdraftAllowed nullable: false
    dailyWithdrawalLimit nullable: false
    monthlyMaxTransfersAllowed nullable: false
  }
}