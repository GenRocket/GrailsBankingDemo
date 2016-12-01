package com.genrocket.bank

class CardController {
  def cardService

  def list(Long customerId) {
    Customer customer = customerId ? Customer.get(customerId) : null
    [cards: customer ? Card.findAllByCustomer(customer) : []]
  }

  def enable(Long id) {
    Card card = id ? Card.get(id) : null
    if (card) {
      cardService.enableCard(card)
      redirect(action: 'list', params: [customerId: card.customer.id])
    } else {
      redirect(action: 'list')
    }
  }

  def disable(Long id) {
    Card card = id ? Card.get(id) : null
    if (card) {
      cardService.disableCard(card)
      redirect(action: 'list', params: [customerId: card.customer.id])
    } else {
      redirect(action: 'list')
    }
  }

  def deactivate(Long id) {
    Card card = id ? Card.get(id) : null
    if (card) {
      cardService.deactivateCard(card)
      redirect(action: 'list', params: [customerId: card.customer.id])
    } else {
      redirect(action: 'list')
    }
  }
}
