package com.genrocket.bank

class Transaction {
  Float amount
  Date dateCreated
  TransactionType transactionType

  static belongsTo = [
    account: Account
  ]

  static constraints = {
    amount nullable: false
    dateCreated nullable: true
    transactionType nullable: false
    account nullable: false
  }
}
