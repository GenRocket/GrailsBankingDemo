package com.genrocket.bank

import grails.test.spock.IntegrationSpec

class AccountControllerIntegrationSpec  extends IntegrationSpec {
  def bankingService
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

  void "test deposit"() {
    when:

    AccountController controller = new AccountController()
    controller.deposit()

    then:

    bankingService.getDeposit()
    controller.modelAndView.viewName == '/account/deposit'
  }

  void "test doDeposit checking TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Account account = (Account) fromInfo['checkingAccount']
    Card card = (Card) fromInfo['checkingCard']

    Float balance = 500.00
    Float depositAmount = 100.00

    account.balance = balance
    account.save()

    bankingService.setDeposit(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doDeposit(depositAmount)

    then:

    !bankingService.getDeposit()
    controller.modelAndView.viewName == '/account/doDeposit'
    controller.modelAndView.model.get('depositAmount') == depositAmount
    controller.modelAndView.model.get('balance') == balance + depositAmount
  }

  void "test doDeposit savings TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Account account = (Account) fromInfo['savingsAccount']
    Card card = (Card) fromInfo['savingsCard']

    Float balance = 500.00
    Float depositAmount = 100.00

    account.balance = balance
    account.save()

    bankingService.setDeposit(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doDeposit(depositAmount)

    then:

    !bankingService.getDeposit()
    controller.modelAndView.viewName == '/account/doDeposit'
    controller.modelAndView.model.get('depositAmount') == depositAmount
    controller.modelAndView.model.get('balance') == balance + depositAmount
  }

  void "test doDeposit checking not TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    Float depositAmount = 0.00

    bankingService.setDeposit(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doDeposit(depositAmount)

    then:

    bankingService.getDeposit()
    controller.modelAndView.viewName == '/account/deposit'
    controller.modelAndView.model.get('errorMessage') != null
  }

  void "test doDeposit savings not TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['savingsCard']

    Float depositAmount = 0.00

    bankingService.setDeposit(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doDeposit(depositAmount)

    then:

    bankingService.getDeposit()
    controller.modelAndView.viewName == '/account/deposit'
    controller.modelAndView.model.get('errorMessage') != null
  }

  // ------------------- WITHDRAWAL ----------------------

  void "test withdrawal"() {
    when:

    AccountController controller = new AccountController()
    controller.withdrawal()

    then:

    bankingService.getWithdrawal()
    controller.modelAndView.viewName == '/account/withdrawal'
  }

  void "test doWithdrawal checking TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Account account = (Account) fromInfo['checkingAccount']
    Card card = (Card) fromInfo['checkingCard']

    Float balance = 500.00
    Float withdrawalAmount = 100.00

    account.balance = balance
    account.save()

    bankingService.setWithdrawal(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doWithdrawal(withdrawalAmount)

    then:

    !bankingService.getWithdrawal()
    controller.modelAndView.viewName == '/account/doWithdrawal'
    controller.modelAndView.model.get('withdrawalAmount') == withdrawalAmount
    controller.modelAndView.model.get('balance') == balance - withdrawalAmount
  }

  void "test doWithdrawal savings TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Account account = (Account) fromInfo['savingsAccount']
    Card card = (Card) fromInfo['savingsCard']

    Float balance = 500.00
    Float withdrawalAmount = 100.00

    account.balance = balance
    account.save()

    bankingService.setWithdrawal(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doWithdrawal(withdrawalAmount)

    then:

    !bankingService.getWithdrawal()
    controller.modelAndView.viewName == '/account/doWithdrawal'
    controller.modelAndView.model.get('withdrawalAmount') == withdrawalAmount
    controller.modelAndView.model.get('balance') == balance - withdrawalAmount
  }

  void "test doWithdrawal checking not TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    Float withdrawalAmount = 0.00

    bankingService.setWithdrawal(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doWithdrawal(withdrawalAmount)

    then:

    bankingService.getWithdrawal()
    controller.modelAndView.viewName == '/account/withdrawal'
    controller.modelAndView.model.get('errorMessage') != null
  }

  void "test doWithdrawal savings not TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['savingsCard']

    Float withdrawalAmount = 0.00

    bankingService.setWithdrawal(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doWithdrawal(withdrawalAmount)

    then:

    bankingService.getWithdrawal()
    controller.modelAndView.viewName == '/account/withdrawal'
    controller.modelAndView.model.get('errorMessage') != null
  }
}
