package com.genrocket.bank

class TransactionType {
  String name

  static constraints = {
    name nullable: false, blank: false, maxSize: 25, unique: true
  }
}
