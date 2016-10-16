package com.genrocket.bank

class CardPool {
  String cardNumber
  Boolean used = Boolean.FALSE
  Boolean nextAvailable = Boolean.FALSE

  static constraints = {
    cardNumber nullable: false, blank: false, maxSize: 16, unique: true
    used nullable: false
    nextAvailable nullable: false
  }
}
