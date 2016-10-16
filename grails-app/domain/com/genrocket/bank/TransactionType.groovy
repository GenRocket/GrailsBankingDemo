package com.genrocket.bank

class TransactionType {
  String name

  static constraints = {
    name nullable: false, blank: false, maxSize: 35, unique: true
  }
}
