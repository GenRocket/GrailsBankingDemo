package com.genrocket.bank

import grails.transaction.Transactional

@Transactional
class SavingsService {
  def accountService
  def checkingService
  def transactionService

  TransactionStatus deposit(User user, Account account, Float amount) {
    if (!amount) {
      return TransactionStatus.INVALID_AMOUNT_VALUE
    }

    if (account.accountType.name != AccountTypes.SAVINGS.value) {
      return TransactionStatus.ACCOUNT_NOT_SAVINGS
    }

    Customer customer = Customer.findByUser(user)

    if (!customer.enabled) {
      return TransactionStatus.ACCOUNT_NOT_ENABLED
    }

    account.balance += amount
    account.save()

    TransactionType transactionType = TransactionType.findByName(TransactionTypes.DEPOSIT_SAVINGS.value)

    Transaction transaction = new Transaction(
      user: user,
      amount: amount,
      account: account,
      dateCreated: new Date(),
      transactionType: transactionType
    )

    transactionService.save(transaction)

    return TransactionStatus.TRANSACTION_COMPLETE
  }

  TransactionStatus withdrawal(User user, Account account, Float amount) {
    if (!amount) {
      return TransactionStatus.INVALID_AMOUNT_VALUE
    }

    AccountType accountType = AccountType.findByName(AccountTypes.SAVINGS.value)

    if (account.accountType != accountType) {
      return TransactionStatus.ACCOUNT_NOT_SAVINGS
    }

    Customer customer = Customer.findByUser(user)

    if (!customer.enabled) {
      return TransactionStatus.ACCOUNT_NOT_ENABLED
    }

    if (amount > account.balance) {
      if (!accountService.checkOverdraftAllowed(user, account)) {
        return TransactionStatus.OVERDRAFT_NOT_ALLOWED
      }
    }

    account.balance -= amount
    account.save()

    TransactionType transactionType = TransactionType.findByName(TransactionTypes.WITHDRAWAL_CHECKING.value)

    Transaction transaction = new Transaction(
      user: user,
      amount: amount,
      account: account,
      dateCreated: new Date(),
      transactionType: transactionType
    )

    transactionService.save(transaction)

    return TransactionStatus.TRANSACTION_COMPLETE
  }

  TransactionStatus transfer(User user, Account savings, Account checking, Float amount) {
    Account.withTransaction { controlledTransaction ->
      if (!amount) {
        return TransactionStatus.INVALID_AMOUNT_VALUE
      }

      if (checkMonthlyMaxTransfersExceeded(user, savings)) {
        return TransactionStatus.MAX_TRANSFERS_EXCEEDED
      }

      TransactionStatus status = withdrawal(user, savings, amount)

      if (status == TransactionStatus.TRANSACTION_COMPLETE) {
        status = checkingService.deposit(user, checking, amount)
      }

      if (status != TransactionStatus.TRANSACTION_COMPLETE) {
        controlledTransaction.setRollbackOnly()
      }

      return status
    }
  }

  Boolean checkMonthlyMaxTransfersExceeded(User user, Account account) {
    Customer customer = Customer.findByUserAndAccount(user, account)

    if (!customer) {
      return true
    }

    Integer monthlyMaxTransfersAllowed = customer.customerLevel.monthlyMaxTransfersAllowed

    Date firstDayOfMonth = new Date() // todo
    Date lastDayOfMonth = new Date()  // toto

    Integer count = 0                 // todo select count(*)

    return count < monthlyMaxTransfersAllowed
  }
}
