package com.genrocket.bank

import org.springframework.transaction.annotation.Transactional

/**
 * Generated By GenRocket 10/16/2016.
 */
@Transactional
class CustomerService {
  def cardService

  def save(Customer customer) {
    customer.save()
  }

  def enableCustomer(Customer customer) {
    customer.enabled = true
    customer.save()
  }

  def disableCustomer(Customer customer) {
    customer.enabled = false
    customer.save()
  }

  List<Customer> findCustomers(User user, AccountType accountType) {
    List<Customer> list = Customer.findAllByUser(user)
    List<Customer> customers = []

    list.each { node ->
      if (node.account.accountType == accountType) {
        customers.add(node)
      }
    }

    return customers
  }

  Customer createCustomer(User user, Account account, CustomerLevel customerLevel, CardType cardType) {
    Customer customer = new Customer(
        enabled: false,
        user: user,
        account: account,
        customerLevel: customerLevel
    )

    save(customer)

    if (!customer.hasErrors()) {
      cardService.save(cardType, customer)
    }

    return customer
  }
}
    