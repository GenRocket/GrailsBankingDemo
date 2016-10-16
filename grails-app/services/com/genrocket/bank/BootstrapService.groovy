package com.genrocket.bank

import grails.transaction.Transactional

@Transactional
class BootstrapService {

  void createAccountType() {
    new AccountType(name: "Checking").save(flush: true)
    new AccountType(name: "Savings").save(flush: true)
  }

  CustomerLevel createCustomerLevel() {
    CustomerLevel customerLevel = new CustomerLevel()
    customerLevel.with {
      name = "Gold"
      overdraftAllowed = Boolean.FALSE
      dailyWithdrawalLimit = 5000
      monthlyMaxTransfersAllowed = 100000
    }
    customerLevel.save(flush: true)
  }

  User createUser() {
    User user = new User()
    user.with {
      lastName = "Canas"
      firstName = "Stefanie"
      username = "testAcme"
      emailAddress = "test@gmail.com"
      phoneNumber = "+1 626-124-3425"
    }
    user.save(flush: true)
  }

  void createBranch() {
    if (!Branch.count()) {
      Branch branch = new Branch()
      branch.with {
        externalId = 164623
        name = "Bank of America Financial Center"
        address = "Plaza West Covina, 150 S"
        city = "California Ave"
        state = "West Covina"
        zipCode = "91790"
        country = "California"
        phoneNumber = "+1 626-854-8050"
      }
      branch.addToAccounts(createAccount())
      branch.save(flush: true)
    }
  }

  Account createAccount() {
    createAccountType()
    Account account = new Account()
    account.with {
      balance = 1000000
      accountType = AccountType.first()
    }
    account.addToCustomers(createCustomer())
    account
  }

  Customer createCustomer() {
    Customer customer = new Customer()
    customer.with {
      enabled = true
      user = createUser()
      customerLevel = createCustomerLevel()
    }
    customer
  }

  void createCardType() {
    new CardType(name: "MasterCard").save(flush: true)
    new CardType(name: "Visa").save(flush: true)
  }

  void createCard() {
    if (!Card.count()) {
      createCardType()
      Card card = new Card()
      card.with {
        nameOnCard = "Stefanie"
        cardNumber = "4232867112630887"
        securityCode = 1234
        pinNumber = 1234
        enabled = true
        activated = true
        dateIssued = new Date()
        dateExpired = new Date() + 5000
        dateActivated = new Date()
        cardType = CardType.first()
        customer = Customer.first()
      }
      card.save()
    }
  }
}
