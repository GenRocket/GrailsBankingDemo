package com.genrocket.bank

class AccountType {
  String name

  static constraints = {
    name nullable: false, blank: false, maxSize: 25, unique: true
  }
}
