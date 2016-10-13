package com.banking.demo

class Customer {
  Integer pinNumber
  Boolean enabled = Boolean.FALSE
  User user
  Account account
  CustomerLevel customerLevel

  static belongsTo = [
    account: Account
  ]

  static constraints = {
    pinNumber nullable: false
    enabled nullable: false
    user nullable: false
    account nullable: false
    customerLevel nullable: false
  }
}
