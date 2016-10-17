package com.genrocket.bank

import groovy.time.TimeCategory
import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class CardService {
  def cardPoolService

  def parseFullName(User user) {
    String fullName = null

    if (user.middleInitial)
      fullName = "${user.title} ${user.firstName} ${user.middleInitial}. ${user.lastName} ${user.suffix}"
    else
      fullName = "${user.title} ${user.firstName} ${user.lastName} ${user.suffix}"

    return fullName.trim()
  }

  def createSecurityCode() {
    Random rand = new Random()
    Integer max = 999
    Integer code = rand.nextInt(max + 1)

    return (code < 100) ? code + 100 : code
  }

  def save(CardType cardType, Customer customer) {
    use(TimeCategory) {
      Date dateIssued = new Date()
      Date dateExpired = dateIssued + 3.years

      Card card = new Card(
        dateIssued: dateIssued,
        dateExpired: dateExpired,
        enabled: true,
        nameOnCard: parseFullName(customer.user),
        cardNumber: cardPoolService.nextCardNumber(),
        securityCode: createSecurityCode(),
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
    