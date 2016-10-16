package com.banking.demo

class Customer {
  Boolean enabled = Boolean.FALSE
  User user
  Account account
  CustomerLevel customerLevel

  static belongsTo = [
    account: Account
  ]

  static constraints = {
    enabled nullable: false
    user nullable: false
    account nullable: false
    customerLevel nullable: false
  }
}
