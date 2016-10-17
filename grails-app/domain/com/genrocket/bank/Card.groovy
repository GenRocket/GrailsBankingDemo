package com.genrocket.bank

class Card {
  String nameOnCard
  String cardNumber
  Integer securityCode
  Integer pinNumber
  Boolean enabled = Boolean.FALSE
  Boolean activated = Boolean.FALSE
  Date dateIssued
  Date dateExpired
  Date dateActivated
  Date dateDeactivated
  CardType cardType

  static belongsTo = [
    customer: Customer
  ]

  static constraints = {
    nameOnCard nullable: false, blank: false, maxSize: 50
    cardNumber nullable: false, blank: false, maxSize: 16, unique: true
    securityCode nullable: false
    pinNumber nullable: true
    enabled nullable: false
    dateIssued nullable: false
    dateExpired nullable: false
    dateActivated nullable: true
    dateDeactivated nullable: true
  }
}