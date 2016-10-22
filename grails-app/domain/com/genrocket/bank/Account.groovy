package com.genrocket.bank

class Account {
  Float balance = 0.0
  Long accountNumber
  AccountType accountType

  static belongsTo = [
    branch : Branch
  ]

  static hasMany = [
    customers : Customer
  ]

  static constraints = {
    balance nullable: false
    accountNumber nullable: false, unique: true
    accountType nullable: false
    branch nullable: false
  }
}
