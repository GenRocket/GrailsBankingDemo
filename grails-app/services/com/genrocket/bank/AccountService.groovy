package com.genrocket.bank

import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class AccountService {

  def save(Account account) {
    account.save()
  }

  def update(Account account) {
    account.save()
  }

  def delete(Account account) {
    account.delete()
  }
}
    