package com.banking.demo

class AccountType {
  String name

  static constraints = {
    name nullable: false, blank: false, maxSize: 25, unique: true
  }
}
