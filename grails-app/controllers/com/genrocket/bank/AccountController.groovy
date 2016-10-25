package com.genrocket.bank

import com.genrocket.bank.co.ChangePinCO

class AccountController {
  def bankingService
  def checkingService
  def cardService

  def balance() {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
    [balance: card?.customer?.account?.balance, accountType: card.customer?.account?.accountType]
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
}
