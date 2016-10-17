package com.genrocket.bank

import grails.transaction.Transactional

@Transactional
class BootstrapService {
  def accountService

  void createAccounts() {
    List<User> users = User.findAll()

    Branch branch = Branch.first()
    CardType cardType = CardType.first()
    AccountType checking = AccountType.findByName('Checking')
    AccountType savings = AccountType.findByName('Savings')
    CustomerLevel[] customerLevels = CustomerLevel.findAll() as CustomerLevel[]
    Integer customerLevelIndex = 0

    users.each { user ->
      println "Creating accounts for User ${user.id}. ${user.firstName} ${user.lastName}"
      CustomerLevel customerLevel = customerLevels[customerLevelIndex]
      accountService.save(user, branch, cardType, checking, customerLevel)
      accountService.save(user, branch, cardType, savings, customerLevel)

      if (++customerLevelIndex == 3) {
        customerLevelIndex = 0
      }
    }

    User user = User.first()
    List<Customer> customers = Customer.findAllByUser(user) as Customer[]

    customers.each { customer ->
      Card card = Card.findByCustomer(customer)
      card.dateActivated = new Date()
      card.pinNumber = 123456
      card.save()
    }
  }
}
