package com.genrocket.bank

import grails.converters.JSON

class RestController {
  def branchService
  def cardTypeService
  def cardPoolService
  def accountTypeService
  def customerLevelService
  def transactionTypeService

  def createAccountType() {
    Map accountTypeMap = request.JSON as Map
    AccountType accountType = new AccountType()
    accountType.properties = accountTypeMap.accountType

    accountTypeService.save(accountType)

    render ([success: true] as JSON)
  }

  def createCardPool() {
    Map cardPoolMap = request.JSON as Map
    CardPool cardPool = new CardPool()
    cardPool.properties = cardPoolMap.cardPool

    cardPoolService.save(cardPool)

    render ([success: true] as JSON)
  }

  def createCardType() {
    Map cardTypeMap = request.JSON as Map
    CardType cardType = new CardType()
    cardType.properties = cardTypeMap.cardType

    cardTypeService.save(cardType)

    render ([success: true] as JSON)
  }

  def createCustomerLevel() {
    Map customerLevelMap = request.JSON as Map
    CustomerLevel customerLevel = new CustomerLevel()
    customerLevel.properties = customerLevelMap.customerLevel

    customerLevelService.save(customerLevel)

    render ([success: true] as JSON)
  }

  def createTransactionType() {
    Map transactionTypeMap = request.JSON as Map
    TransactionType transactionType = new TransactionType()
    transactionType.properties = transactionTypeMap.transactionType

    transactionTypeService.save(transactionType)

    render ([success: true] as JSON)
  }

  def createBranch() {
    Map branchMap = request.JSON as Map
    Branch branch = new Branch()
    branch.properties = branchMap.branch

    branchService.save(branch)

    render ([success: true] as JSON)
  }

  def openAccountWithDeposit() {


    render ([success: true] as JSON)
  }
}
