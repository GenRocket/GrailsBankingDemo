package com.genrocket.bank

/**
 * Created by htaylor on 10/18/16.
 */
enum TransactionStatus {
  ACCOUNT_NOT_SAVINGS('account.not.savings'),
  ACCOUNT_NOT_ENABLED('account.not.enabled'),
  ACCOUNT_NOT_CHECKING('account.not.checking'),
  TRANSACTION_COMPLETE('transaction.complete'),
  OVERDRAFT_NOT_ALLOWED('overdraft.not.allowed'),
  MAX_TRANSFERS_EXCEEDED('max.transfers.exceeded'),
  AMOUNT_GT_BALANCE('amount.greater.than.balance'),
  WITHDRAWAL_LIMIT_REACHED('withdrawal.limit.reached')

  TransactionStatus(value) {
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