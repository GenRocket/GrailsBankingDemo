package com.genrocket.bank

import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class TransactionTypeService {

  def save(TransactionType transactionType) {
    transactionType.save()
  }

  def update(TransactionType transactionType) {
    transactionType.save()
  }

  def delete(TransactionType transactionType) {
    transactionType.delete()
  }
}
    