package com.genrocket.bank

/**
 * Created by htaylor on 10/18/16.
 */
enum TransactionTypes {
  DEPOSIT_CHECKING('DepositChecking'),
  DEPOSIT_SAVINGS('DepositSavings'),
  WITHDRAWAL_CHECKING('WithdrawalChecking'),
  TRANSFER_CHECKING_SAVINGS('TransferCheckingToSavings'),
  TRANSFER_SAVINGS_CHECKING('TransferSavingToChecking'),
  VIEW_BALANCE_CHECKING('ViewBalanceChecking'),
  VIEW_BALANCE_SAVINGS('ViewBalanceSavings'),
  CHANGE_PIN_NUMBER('ChangePinNumber')

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