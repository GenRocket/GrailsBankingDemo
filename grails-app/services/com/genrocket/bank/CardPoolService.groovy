package com.genrocket.bank

import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class CardPoolService {

  def save(CardPool cardPool) {
    cardPool.save()
  }

  def synchronized nextCardNumber() {
    CardPool cardPool = CardPool.findByNextAvailable(true)

    if (!cardPool) {
      cardPool = CardPool.first()

      if (cardPool?.used) {
        return null
      }
    }

    cardPool.used = true
    cardPool.nextAvailable = false
    cardPool.save(flush: true)

    CardPool nextCardPool = CardPool.findById(cardPool.id + 1)

    if (nextCardPool) {
      nextCardPool.nextAvailable = true
      nextCardPool.save(flush: true)
    }

    return cardPool.cardNumber
  }
}
    