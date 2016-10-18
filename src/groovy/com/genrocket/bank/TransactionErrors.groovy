package com.genrocket.bank

/**
 * Created by htaylor on 10/18/16.
 */
enum TransactionErrors {
  OVERDRAFT_NOT_ALLOWED('overdraft.not.allowed'),
  WITHDRAWAL_LIMIT_REACHED('withdrawal.limit.reached'),
  MAX_TRANSFERS_EXCEEDED('max.transfers.exceeded')

  TransactionErrors(value) {
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