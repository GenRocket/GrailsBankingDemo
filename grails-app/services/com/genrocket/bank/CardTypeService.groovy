package com.genrocket.bank

import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class CardTypeService {

  def save(CardType cardType) {
    cardType.save()
  }

  def update(CardType cardType) {
    cardType.save()
  }

  def delete(CardType cardType) {
    cardType.delete()
  }
}
    