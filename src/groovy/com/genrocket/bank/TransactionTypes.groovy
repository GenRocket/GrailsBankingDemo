package com.genrocket.bank

/**
 * Created by htaylor on 10/18/16.
 */
enum TransactionTypes {
  DEPOSIT_CHECKING('DEPOSIT_CHECKING'),
  DEPOSIT_SAVINGS('DEPOSIT_SAVINGS'),
  WITHDRAWAL_CHECKING('WITHDRAWAL_CHECKING'),
  WITHDRAWAL_SAVINGS('WITHDRAWAL_SAVINGS'),
  TRANSFER_CHECKING_SAVINGS('TRANSFER_CHECKING_SAVINGS'),
  TRANSFER_SAVINGS_CHECKING('TRANSFER_SAVINGS_CHECKING'),
  VIEW_BALANCE_CHECKING('VIEW_BALANCE_CHECKING'),
  VIEW_BALANCE_SAVINGS('VIEW_BALANCE_SAVINGS'),
  CHANGE_PIN_NUMBER('CHANGE_PIN_NUMBER')

  TransactionTypes(value) {
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