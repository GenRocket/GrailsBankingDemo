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

  def update(CardPool cardPool) {
    cardPool.save()
  }

  def delete(CardPool cardPool) {
    cardPool.delete()
  }
}
    