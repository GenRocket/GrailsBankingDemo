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
    Card card = bankingService.selectedCard
    card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
    AccountType accountType = card.customer?.account?.accountType

    if (accountType.name == AccountTypes.CHECKING.getValue()) {
      render(view: 'deposit')
    } else {
      flash.error = message(code: 'deposit.not.allowed.into.saving.account')
      redirect(controller: 'home', action: 'menu')
    }
  }

  def deDeposit(Float amount) {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
    String depositMessage = checkingService.deposit(card.customer.user, card.customer.account, amount)
    if (depositMessage == TransactionStatus.TRANSACTION_COMPLETE.toString()) {
      render(view: "doDeposit", model: [depositAmount: amount, balance: card.customer.account.balance])
    } else {
      render(view: 'deposit', model: [errorMessage: g.message(code: depositMessage.toString())])
    }
  }

  def withdrawal() {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
    AccountType accountType = card.customer?.account?.accountType

    if (accountType.name == AccountTypes.CHECKING.getValue()) {
      render(view: 'withdrawal')
    } else {
      flash.error = message(code: 'withdrawal.not.allowed.from.saving.account')
      redirect(controller: 'home', action: 'menu')
    }
  }

  def doWithdrawal(Float amount) {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
    String withdrawalMessage = checkingService.withdrawal(card.customer.user, card.customer.account, amount)
    if (withdrawalMessage == TransactionStatus.TRANSACTION_COMPLETE.toString()) {
      render(view: "doWithdrawal", model: [withdrawalAmount: amount, balance: card.customer.account.balance])
    } else {
      render(view: 'withdrawal', model: [errorMessage: g.message(code: withdrawalMessage.toString())])
    }
  }

  def changePin() {}

  def transfer() {}

  def transferAmount(TransferCO transferCO) {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)
    transferCO.currentAccountNumber = card.customer.account.accountNumber
    if (transferCO.validate()) {
      render(view: "transferAmount", model: [currentAccountType: card.customer?.account?.accountType,
                                             accountToTransfer : Account.findByAccountNumber(transferCO.accountNumber)])
    } else {
      render(view: 'transfer', model: [transferCO: transferCO])
    }
  }

  def doTransfer(TransferAmountCO transferAmountCO) {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)
    if (transferAmountCO.validate()) {
      Account fromAccount = card.customer.account
      Account toAccount = transferAmountCO.account

      render(view: "doTransfer", model: [])
    } else {
      render(view: 'transferAmount', model: [currentAccountType: card.customer?.account?.accountType, transferAmountCO: transferAmountCO])
    }
  }

  def savePin(ChangePinCO changePinCO) {
    Card card = bankingService.selectedCard
    changePinCO.actualPinNumber = card.pinNumber
      if(changePinCO.validate()) {
        card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
        cardService.changePin(card, changePinCO.newPinNumber)
        bankingService.setSelectedCard(card)
        flash.message = message(code: 'pin.updated')
        redirect(controller: 'home', action: 'menu')
      } else {
        render (view: 'changePin', model: [changePinCO: changePinCO])
      }
  }

  def history() {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)
    [transactions: Transaction.findAllByAccount(card?.customer?.account, [sort:'dateCreated', order: 'desc'])]
  }
}
