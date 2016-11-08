package com.genrocket.bank

import com.genrocket.bank.co.ChangePinCO
import com.genrocket.bank.co.TransferAmountCO
import com.genrocket.bank.co.TransferCO

class AccountController {
  def bankingService
  def checkingService
  def savingsService
  def cardService

  def balance() {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
    [balance: card?.customer?.account?.balance, accountType: card.customer?.account?.accountType]
  }

  def deposit() {
    bankingService.setDeposit(true)
    render(view: 'deposit')
  }

  def doDeposit(Float amount) {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
    String depositMessage

    if(bankingService.getDeposit()) {
      AccountType accountType = card.customer?.account?.accountType

      if (accountType.name == AccountTypes.CHECKING.getValue()) {
        depositMessage = checkingService.deposit(card.customer.user, card.customer.account, amount)
      } else {
        depositMessage = savingsService.deposit(card.customer.user, card.customer.account, amount)
      }

      if (depositMessage == TransactionStatus.TRANSACTION_COMPLETE.toString()) {
        bankingService.setDeposit(false)
        render(view: "doDeposit", model: [depositAmount: amount, balance: card.customer.account.balance])
      } else {
        render(view: 'deposit', model: [errorMessage: g.message(code: depositMessage.toString())])
      }
    } else {
      redirect(controller: 'home', action: 'menu')
    }
  }

  def withdrawal() {
    bankingService.setWithdrawal(true)
    render(view: 'withdrawal')
  }

  def doWithdrawal(Float amount) {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
    String withdrawalMessage

    if(bankingService.getWithdrawal()) {
      AccountType accountType = card.customer?.account?.accountType

      if (accountType.name == AccountTypes.CHECKING.getValue()) {
        withdrawalMessage = checkingService.withdrawal(card.customer.user, card.customer.account, amount)
      } else {
        withdrawalMessage = savingsService.withdrawal(card.customer.user, card.customer.account, amount)
      }

      if (withdrawalMessage == TransactionStatus.TRANSACTION_COMPLETE.toString()) {
        bankingService.setWithdrawal(false)
        render(view: "doWithdrawal", model: [withdrawalAmount: amount, balance: card.customer.account.balance])
      } else {
        render(view: 'withdrawal', model: [errorMessage: g.message(code: withdrawalMessage.toString())])
      }
    } else {
      redirect(controller: 'home', action: 'menu')
    }
  }

  def changePin() {}

  def transfer() {}

  def transferAmount(TransferCO transferCO) {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)
    transferCO.currentAccountNumber = card.customer.account.accountNumber

    if (transferCO.validate()) {
      bankingService.setTransfer(true)
      render(view: "transferAmount", model: [currentAccountType: card.customer?.account?.accountType,
                                             accountToTransfer : Account.findByAccountNumber(transferCO.accountNumber)])
    } else {
      render(view: 'transfer', model: [transferCO: transferCO])
    }
  }

  def doTransfer(TransferAmountCO transferAmountCO) {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)

    String CHECKING = AccountTypes.CHECKING.value
    String SAVINGS = AccountTypes.SAVINGS.value

    if(bankingService.getTransfer()) {
      if (transferAmountCO.validate()) {
        TransactionStatus transactionStatus = null

        Account fromAccount = card.customer.account
        Account toAccount = transferAmountCO.account

        String fromAccountType = fromAccount.accountType.name
        String toAccountType = toAccount.accountType.name

        if (fromAccountType == CHECKING && toAccountType == CHECKING) {
          transactionStatus = checkingService.transferCheckingToChecking(card.customer.user, fromAccount, toAccount, transferAmountCO.amount)
        } else if (fromAccountType == CHECKING && toAccountType == SAVINGS) {
          transactionStatus = checkingService.transfer(card.customer.user, fromAccount, toAccount, transferAmountCO.amount)
        } else if (fromAccountType == SAVINGS && toAccountType == CHECKING) {
          transactionStatus = savingsService.transfer(card.customer.user, fromAccount, toAccount, transferAmountCO.amount)
        } else if (fromAccountType == SAVINGS && toAccountType == SAVINGS) {
          transactionStatus = savingsService.transferSavingsToSavings(card.customer.user, fromAccount, toAccount, transferAmountCO.amount)
        }

        if (transactionStatus == TransactionStatus.TRANSACTION_COMPLETE) {
          bankingService.setTransfer(false)
          render(view: "doTransfer", model: [fromAccount: fromAccount, toAccount: toAccount, amount: transferAmountCO.amount])
        } else {
          render(view: 'transferAmount', model: [currentAccountType: card.customer?.account?.accountType, errorMessage: g.message(code: transactionStatus.toString()),
                                                 transferAmountCO: transferAmountCO])
        }
      } else {
        render(view: 'transferAmount', model: [currentAccountType: card.customer?.account?.accountType, transferAmountCO: transferAmountCO])
      }
    } else {
      redirect(controller: 'home', action: 'menu')
    }
  }

  def savePin(ChangePinCO changePinCO) {
    Card card = bankingService.selectedCard
    changePinCO.actualPinNumber = card.pin

    if (changePinCO.validate()) {
      card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
      cardService.changePin(card, changePinCO.newPinNumber)
      bankingService.setSelectedCard(card)
      flash.message = message(code: 'pin.updated')
      redirect(controller: 'home', action: 'menu')
    } else {
      render(view: 'changePin', model: [changePinCO: changePinCO])
    }
  }

  def history() {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)
    [transactions: Transaction.findAllByAccount(card?.customer?.account, [sort: 'dateCreated', order: 'desc'])]
  }
}
