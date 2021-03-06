package com.genrocket.bank

import grails.transaction.Transactional
import org.springframework.transaction.TransactionDefinition

import java.text.SimpleDateFormat

@Transactional
class CheckingService {
  def accountService
  def savingsService
  def transactionService

  TransactionStatus deposit(User user, Account account, Float amount) {
    if (!amount || amount < 0) {
      return TransactionStatus.INVALID_AMOUNT_VALUE
    }

    if (account.accountType.name != AccountTypes.CHECKING.value) {
      return TransactionStatus.ACCOUNT_NOT_CHECKING
    }

    if (!accountService.hasEnabledCustomer(account)) {
      return TransactionStatus.ACCOUNT_NOT_ENABLED
    }

    account.balance += amount
    account.save(flush: true)

    TransactionType transactionType = TransactionType.findByName(TransactionTypes.DEPOSIT_CHECKING.value)

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

  TransactionStatus withdrawal(User user, Account account, Float amount, TransactionTypes defaultTransactionType = TransactionTypes.WITHDRAWAL_CHECKING) {
    if (!amount) {
      return TransactionStatus.INVALID_AMOUNT_VALUE
    }

    AccountType accountType = AccountType.findByName(AccountTypes.CHECKING.value)

    if (account.accountType != accountType) {
      return TransactionStatus.ACCOUNT_NOT_CHECKING
    }

    if (!accountService.hasEnabledCustomer(account)) {
      return TransactionStatus.ACCOUNT_NOT_ENABLED
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
    account.save(flush: true)

    TransactionType transactionType = TransactionType.findByName(defaultTransactionType.value)

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

  TransactionStatus transfer(User user, Account fromChecking, Account toSavings, Float amount) {
    if (!amount) {
      return TransactionStatus.INVALID_AMOUNT_VALUE
    }

    if (fromChecking.balance < amount) {
      return TransactionStatus.AMOUNT_GT_BALANCE
    }

    Account.withTransaction ([propagationBehavior: TransactionDefinition.PROPAGATION_NESTED]){ controlledTransaction ->
      TransactionStatus status = withdrawal(user, fromChecking, amount, TransactionTypes.TRANSFER_CHECKING_SAVINGS)

      if (status == TransactionStatus.TRANSACTION_COMPLETE) {
        status = savingsService.deposit(user, toSavings, amount)
      }

      if (status != TransactionStatus.TRANSACTION_COMPLETE) {
        controlledTransaction.setRollbackOnly()
      }

      return status
    }
  }

  TransactionStatus transferCheckingToChecking(User user, Account fromChecking, Account toChecking, Float amount) {
    if (!amount) {
      return TransactionStatus.INVALID_AMOUNT_VALUE
    }

    if (fromChecking.balance < amount) {
      return TransactionStatus.AMOUNT_GT_BALANCE
    }

    Account.withTransaction ([propagationBehavior: TransactionDefinition.PROPAGATION_NESTED]){ controlledTransaction ->
      TransactionStatus status = withdrawal(user, fromChecking, amount, TransactionTypes.TRANSFER_CHECKING_CHECKING)

      if (status == TransactionStatus.TRANSACTION_COMPLETE) {
        status = deposit(user, toChecking, amount)
      }

      if (status != TransactionStatus.TRANSACTION_COMPLETE) {
        controlledTransaction.setRollbackOnly()
      }

      return status
    }
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
