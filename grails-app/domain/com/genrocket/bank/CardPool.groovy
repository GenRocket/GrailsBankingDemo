package com.genrocket.bank

class CardPool {
  String cardNumber
  Boolean used = Boolean.FALSE
  Boolean next_available = Boolean.FALSE

  static constraints = {
    cardNumber nullable: false, blank: false, maxSize: 16, unique: true
    used nullable: false
    next_available nullable: false
  }
}
