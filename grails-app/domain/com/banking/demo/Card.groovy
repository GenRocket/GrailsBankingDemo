package com.banking.demo

class Card {
  String nameOnCard
  String cardNumber
  Integer securityCode
  Boolean enabled = Boolean.FALSE
  Date dateIssued
  Date dateExpired
  Date dateActivated
  Date dateDeactivated
  CustomerLevel customerLevel

  static belongsTo = [
    customer: Customer
  ]

  static constraints = {
    nameOnCard nullable: false, blank: false, maxSize: 50
    cardNumber nullable: false, blank: false, maxSize: 16
    securityCode nullable: false
    enabled nullable: false
    dateIssued nullable: false
    dateExpired nullable: false
    dateActivated nullable: true
    dateDeactivated nullable: true
    customerLevel nullable: false
  }
}
