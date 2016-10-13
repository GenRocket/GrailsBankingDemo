package com.banking.demo

class Account {
  Float balance = 0
  AccountType accountType

  static belongsTo = [
    branch : Branch
  ]

  static hasMany = [
    customers : Customer
  ]

  static constraints = {
    balance nullable: false
    accountType nullable: false
    branch nullable: false
  }
}
