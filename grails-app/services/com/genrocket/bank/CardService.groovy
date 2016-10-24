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

  void save(CardType cardType, Customer customer) {
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

  TransactionStatus activateCard(Card card, Integer pinNumber) {
    if (!pinNumber) {
      return TransactionStatus.INVALID_PIN_NUMBER
    }

    if (pinNumber.toString().size() != 6) {
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

    card.pinNumber = pinNumber
    card.dateDeactivated = new Date()
    card.save()

    TransactionStatus.TRANSACTION_COMPLETE
  }

  Card findActiveCard(Customer customer, CardType cardType) {
    Card.findByCustomerAndCardTypeAndDateActivatedIsNotNull(customer, cardType)
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
    