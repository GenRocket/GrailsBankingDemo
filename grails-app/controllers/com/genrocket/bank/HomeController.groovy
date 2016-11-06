package com.genrocket.bank

import com.genrocket.bank.co.LoginCO

class HomeController {
  def bankingService

  def index() {
    if (bankingService.getSelectedCard()) {
      redirect(action: 'menu')
    }
  }

  def login(LoginCO loginCO) {
    if (loginCO.validate()) {
      Card card = Card.findByCardNumber(loginCO.cardNumber)
      bankingService.setSelectedCard(card)
      redirect(action: 'menu')
    } else {
      render(view: 'index', model: [loginCO: loginCO])
    }
  }

  def menu() {
    Card card = bankingService.getSelectedCard()
    card = card ? Card.get(card.id) : null   // To fix : could not initialize proxy - no Session
    if (card) {
      [accountType: card.customer.account.accountType]
    } else {
      redirect(action: 'index')
    }
  }

  def exit() {
    session.invalidate()
    redirect(action: 'index')
  }
}
