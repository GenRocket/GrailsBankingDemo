package com.genrocket.bank

enum AccountTypes {
  SAVINGS('Savings'),
  CHECKING('Checking')

  AccountTypes(value) {
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
