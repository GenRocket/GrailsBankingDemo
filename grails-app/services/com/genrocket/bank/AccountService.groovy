package com.genrocket.bank

import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class AccountService {
  def customerService
  def cardService


  def save(User user, Branch branch, CardType cardType, AccountType accountType, CustomerLevel customerLevel) {
    Account account = new Account(
      branch: branch,
      accountType: accountType
    )

    account.save()

    if (!account.hasErrors()) {
      Customer customer = new Customer(
        enabled: false,
        user: user,
        account: account,
        customerLevel: customerLevel
      )

      customerService.save(customer)

      if (!customer.hasErrors()) {
        cardService.save(cardType, customer)
      }
    }
  }

  def update(Account account) {
    account.save()
  }

  def delete(Account account) {
    account.delete()
  }

}
    