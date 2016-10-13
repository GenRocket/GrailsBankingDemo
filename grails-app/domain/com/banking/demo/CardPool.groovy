package com.banking.demo

class CardPool {
  String cardNumber
  Boolean used = Boolean.FALSE
  Boolean next_available = Boolean.FALSE

  static constraints = {
    cardNumber nullable: false, blank: false, maxSize: 16
    used nullable: false
    next_available nullable: false
  }
}
