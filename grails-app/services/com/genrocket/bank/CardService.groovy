package com.genrocket.bank

import com.genrocket.bank.util.CardUtil
import groovy.time.TimeCategory
import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class CardService {
  def cardPoolService

  void enableCard(Card card) {
    card.enabled = true
    card.save()
  }

  void disableCard(Card card) {
    card.enabled = false
    card.save()
  }

  Card changePin(Card card, String newPinNumber) {
    card.pin = newPinNumber
    card.save()
  }

  Card save(CardType cardType, Customer customer) {
    Card activeCard = findActiveCard(customer, cardType)

    if (activeCard) {
      deactivateCard(activeCard)
    }

    use(TimeCategory) {
      Date dateIssued = new Date()
      Date dateExpired = dateIssued + 3.years

      Card card = new Card(
        dateIssued: dateIssued,
        dateExpired: dateExpired,
        enabled: true,
        nameOnCard: CardUtil.parseFullName(customer.user),
        cardNumber: cardPoolService.nextCardNumber(),
        securityCode: CardUtil.createSecurityCode(),
        cardType: cardType,
        customer: customer
      )

      card.save()
    }
  }

  TransactionStatus activateCard(Card card, String pin) {
    if (!pin) {
      return TransactionStatus.INVALID_PIN_NUMBER
    }

    if (pin.size() != 6) {
      return TransactionStatus.INVALID_PIN_NUMBER
    }

    if (card.dateActivated) {
      return TransactionStatus.CARD_ALREADY_ACTIVE
    }

    if (card.dateDeactivated) {
      return TransactionStatus.CARD_DEACTIVATED
    }

    if (!card.enabled) {
      return TransactionStatus.CARD_NOT_ENABLED
    }

    card.pin = pin
    card.dateActivated = new Date()
    card.save(flush: true)

    TransactionStatus.TRANSACTION_COMPLETE
  }

  Card findActiveCard(Customer customer, CardType cardType) {
    Card.where {
      customer == customer && cardType == cardType && dateActivated != null
    }.get()
  }

  void deactivateCard(Card card) {
    card.dateDeactivated = new Date()
    card.save()
  }

  def update(Card card) {
    card.save()
  }

  def delete(Card card) {
    card.delete()
  }
}
    