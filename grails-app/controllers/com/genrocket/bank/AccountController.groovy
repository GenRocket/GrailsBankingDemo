package com.genrocket.bank

class AccountController {
  def bankingService
  def checkingService

  def balance() {
    Card card = bankingService.selectedCard
    card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
    [balance: card?.customer?.account?.balance]
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
}
