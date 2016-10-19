package com.genrocket.bank

class Transaction {
  Float amount
  Date dateCreated
  TransactionType transactionType
  User user

  static belongsTo = [
    account: Account
  ]

  static constraints = {
    amount nullable: false
    dateCreated nullable: true
    transactionType nullable: false
    account nullable: false
    user nullable: false
  }
}
