package com.genrocket.bank

/**
 * Created by htaylor on 10/18/16.
 */
enum WithdrawalErrors {
  OVERDRAFT_NOT_ALLOWED('card.not.enabled'),
  WITHDRAWAL_LIMIT_REACHED('card.expired')

  WithdrawalErrors(value) {
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