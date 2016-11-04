package com.genrocket.bank

import grails.transaction.Transactional

@Transactional
class BootstrapService {
  def accountService
  def cardService

  void createAccounts() {
    Integer accountNumber = 1000000000
    List<User> users = User.findAll()

    Branch branch = Branch.first()
    CardType cardType = CardType.first()
    AccountType checking = AccountType.findByName('Checking')
    AccountType savings = AccountType.findByName('Savings')
    CustomerLevel[] customerLevels = CustomerLevel.findAll() as CustomerLevel[]
    Integer customerLevelIndex = 0

    if (User.count() == 0) {
      users.each { user ->
        println "Creating accounts for User ${user.id}. ${user.firstName} ${user.lastName}"
        CustomerLevel customerLevel = customerLevels[customerLevelIndex]
        Account account1 = accountService.save(user, branch, cardType, checking, customerLevel)
        Account account2 = accountService.save(user, branch, cardType, savings, customerLevel)

        account1.accountNumber = accountNumber++
        account2.accountNumber = accountNumber++

        account1.save()
        account2.save()

        if (++customerLevelIndex == 3) {
          customerLevelIndex = 0
        }
      }

      User user = User.first()
      List<Customer> customers = Customer.findAllByUser(user) as Customer[]

      customers.each { customer ->
        customer.enabled = true
        customer.save()

        customer.customerLevel.dailyWithdrawalLimit = 500
        customer.customerLevel.monthlyMaxTransfersAllowed = 3
        customer.customerLevel.overdraftAllowed = true
        customer.customerLevel.save()

        Card card = Card.findByCustomer(customer)
        cardService.activateCard(card, 123456)
      }
    }
  }
}
