package com.genrocket.bank

import grails.test.spock.IntegrationSpec

class AccountControllerIntegrationSpec  extends IntegrationSpec {
  def transactionCreatorService

  void "test balance"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Account account = (Account) fromInfo['checkingAccount']
    Card card = (Card) fromInfo['checkingCard']

    account.balance = 500
    account.save()

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    Map map = controller.balance()
    Float balance = (Float) map['balance']
    AccountType accountType = (AccountType) map['accountType']

    then:

    accountType
    balance
  }

  void "test deposit accountType EQ CHECKING"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.deposit()

    then:

    controller.modelAndView.viewName == '/account/deposit'
  }

  void "test deposit accountType NOT EQ CHECKING"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['savingsCard']

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.deposit()

    then:

    controller.flash.error != null
    controller.response.redirectedUrl == '/home/menu'
  }

  void "test deposit TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Account account = (Account) fromInfo['checkingAccount']
    Card card = (Card) fromInfo['checkingCard']

    Float balance = 500.00
    Float depositAmount = 100.00

    account.balance = balance
    account.save()

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doDeposit(depositAmount)

    then:

    controller.modelAndView.viewName == '/account/doDeposit'
    controller.modelAndView.model.get('depositAmount') == depositAmount
    controller.modelAndView.model.get('balance') == balance + depositAmount
  }

  void "test deposit not TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    Float depositAmount = 0.00

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doDeposit(depositAmount)

    then:

    controller.modelAndView.viewName == '/account/deposit'
    controller.modelAndView.model.get('errorMessage') != null
  }

  // ------------------- WITHDRAWAL ----------------------

  void "test withdrawal accountType EQ CHECKING"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.withdrawal()

    then:

    controller.modelAndView.viewName == '/account/withdrawal'
  }

  void "test withdrawal accountType NOT EQ CHECKING"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['savingsCard']

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.withdrawal()

    then:

    controller.flash.error != null
    controller.response.redirectedUrl == '/home/menu'
  }

  void "test withdrawal TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Account account = (Account) fromInfo['checkingAccount']
    Card card = (Card) fromInfo['checkingCard']

    Float balance = 500.00
    Float withdrawalAmount = 100.00

    account.balance = balance
    account.save()

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doWithdrawal(withdrawalAmount)

    then:

    controller.modelAndView.viewName == '/account/doWithdrawal'
    controller.modelAndView.model.get('withdrawalAmount') == withdrawalAmount
    controller.modelAndView.model.get('balance') == balance - withdrawalAmount
  }

  void "test withdrawal not TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    Float withdrawalAmount = 0.00

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doWithdrawal(withdrawalAmount)

    then:

    controller.modelAndView.viewName == '/account/withdrawal'
    controller.modelAndView.model.get('errorMessage') != null
  }
}