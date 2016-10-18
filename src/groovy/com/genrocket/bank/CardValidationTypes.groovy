package com.genrocket.bank

enum CardValidationTypes {
  CARD_NOT_ENABLED('card.not.enabled'),
  CARD_EXPIRED('card.expired'),
  CARD_INVALID('invalid.card.number'),
  CARD_DEACTIVATED('card.deactivated'),
  CARD_VALIDATED('card.validated'),
  PIN_INVALID('invalid.pin.number')

  CardValidationTypes(value) {
    this.value = value
  }

  private final String value

  String getKey() {
    name()
  }

  String getValue() {
    value
  }

  String toString() {
    value
  }
}