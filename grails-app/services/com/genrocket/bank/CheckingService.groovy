package com.genrocket.bank

import grails.transaction.Transactional

import java.text.SimpleDateFormat

@Transactional
class CheckingService {
  def accountService
  def savingsService

  TransactionStatus deposit(User user, Account account, Float amount) {

  }

  TransactionStatus withdrawal(User user, Account account, Float amount) {
    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)

    if (account.accountType != accountType) {
      return TransactionStatus.ACCOUNT_NOT_CHECKING
    }

    if (amount > account.balance) {
      if (!accountService.checkOverdraftAllowed(user, account)) {
        return TransactionStatus.OVERDRAFT_NOT_ALLOWED
      }
    }

    if (checkDailyWithdrawalLimit(user, account, amount)) {
      return TransactionStatus.WITHDRAWAL_LIMIT_REACHED
    }

    account.balance -= amount
    account.save()

    return TransactionStatus.TRANSACTION_COMPLETE
  }

  TransactionStatus transfer(User user, Account checking, Account savings, Float amount) {
    withdrawal(user, savings, amount)
    savingsService.deposit(user, checking, amount)
  }

  Boolean checkDailyWithdrawalLimit(User user, Account account, Float amount) {
    if (account.accountType.name != AccountTypes.CHECKING.value) {
      return false
    }

    SimpleDateFormat sdf = new SimpleDateFormat('yyyy/MM/dd')
    Date today = sdf.parse(sdf.format(new Date()))

    Float totalWithdrawal = 0
    Customer customer = Customer.findByUserAndAccount(user, account)
    Integer withdrawalLimit = customer.customerLevel.dailyWithdrawalLimit

    TransactionType transactionType = TransactionType.findByName(TransactionTypes.WITHDRAWAL_CHECKING.value)

    List<Transaction> transactions =
      Transaction.findAllByAccountAndDateCreatedGreaterThanEquals(account, today)

    transactions.each { transaction ->
      if (transaction.transactionType == transactionType) {
        totalWithdrawal += transaction.amount
      }
    }

    return withdrawalLimit < totalWithdrawal + amount
  }
}
