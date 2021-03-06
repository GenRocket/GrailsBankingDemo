package com.genrocket.bank

class Card {
  String nameOnCard
  String cardNumber
  Integer securityCode
  String pin
  Boolean enabled = Boolean.TRUE
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
    pin nullable: true, blank: false, maxSize: 255
    enabled nullable: false
    dateIssued nullable: false
    dateExpired nullable: false
    dateActivated nullable: true
    dateDeactivated nullable: true
  }

  def beforeInsert() {
    encodePin()
  }

  def beforeUpdate() {
    if (isDirty('pin')) {
      encodePin()
    }
  }

  protected void encodePin() {
    pin = pin ? Encrypt.generateStrongPasswordHash(pin) : null
  }
}
