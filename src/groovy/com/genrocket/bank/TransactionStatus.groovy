package com.genrocket.bank

/**
 * Created by htaylor on 10/18/16.
 */
enum TransactionStatus {
  UNABLE_TO_CREATE_ACCOUNT('unable.to.create.account'),
  ACCOUNT_NOT_SAVINGS('account.not.savings'),
  ACCOUNT_NOT_ENABLED('account.not.enabled'),
  ACCOUNT_NOT_CHECKING('account.not.checking'),
  INVALID_AMOUNT_VALUE('invalid.amount.value'),
  INVALID_PIN_NUMBER('invalid.pin.number'),
  INVALID_ACCOUNT_NUMBER('invalid.account.number'),
  INVALID_ACCOUNT_TYPE('invalid.account.type'),
  TRANSACTION_COMPLETE('transaction.complete'),
  OVERDRAFT_NOT_ALLOWED('overdraft.not.allowed'),
  MAX_TRANSFERS_EXCEEDED('max.transfers.exceeded'),
  AMOUNT_GT_BALANCE('amount.greater.than.balance'),
  WITHDRAWAL_LIMIT_REACHED('withdrawal.limit.reached'),
  CARD_ALREADY_ACTIVE('card.already.active'),
  CARD_DEACTIVATED('card.deactivated'),
  CARD_NOT_ENABLED('card.not.enabled')

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