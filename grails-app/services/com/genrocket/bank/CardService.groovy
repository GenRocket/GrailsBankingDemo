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

  def save(CardType cardType, Customer customer) {
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

  def update(Card card) {
    card.save()
  }

  def delete(Card card) {
    card.delete()
  }
}
    