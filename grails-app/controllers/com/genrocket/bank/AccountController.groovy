package com.genrocket.bank

class AccountController {
  def bankingService

  def withdrawal() {
    Card card = bankingService.selectedCard
    if (!card) {
      redirect(controller: 'home', action: 'index')
    } else {
      card = Card.get(card.id)    // To fix : could not initialize proxy - no Session
      AccountType accountType = card.customer?.account?.accountType
      if (accountType.name == AccountTypes.CHECKING.getValue()) {
        render(view: 'withdrawal')
      } else {
        flash.error = message(code: 'withdrawal.not.allowed.from.saving.account')
        redirect(controller: 'home', action: 'menu')
      }
    }
  }

  def doWithdrawal() {

  }
}
