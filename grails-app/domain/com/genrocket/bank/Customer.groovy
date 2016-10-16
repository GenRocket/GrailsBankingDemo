package com.genrocket.bank

class Customer {
  Boolean enabled = Boolean.FALSE
  User user
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
