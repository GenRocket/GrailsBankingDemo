package com.genrocket.bank

import com.genrocket.bank.co.ChangePinCO
import com.genrocket.bank.co.TransferAmountCO
import com.genrocket.bank.co.TransferCO
import grails.test.spock.IntegrationSpec

class AccountControllerIntegrationSpec extends IntegrationSpec {
  def bankingService
  def transactionCreatorService

  // ------------------- BALANCE ----------------------

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

  // ------------------- DEPOSIT ----------------------

  void "test deposit"() {
    when:

    AccountController controller = new AccountController()
    controller.deposit()

    then:

    bankingService.getDeposit()
    controller.modelAndView.viewName == '/account/deposit'
  }

  void "test doDeposit not getDeposit()"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    bankingService.setDeposit(false)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)
    controller.doDeposit((float) 100.00)

    then:

    controller.response.redirectedUrl == '/home/menu'
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

  void "test doWithdrawal not getWithdrawal()"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    bankingService.setWithdrawal(false)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)
    controller.doDeposit((float) 100.00)

    then:

    controller.response.redirectedUrl == '/home/menu'
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

  // ------------------- TRANSFER CO ----------------------

  void "test TransferCO invalid account number"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']
    Account account = (Account) fromInfo['checkingAccount']

    account.accountNumber = 9999999999
    account.save()

    TransferCO transferCO = new TransferCO()
    transferCO.accountNumber = 12345

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)
    controller.transferAmount(transferCO)

    then:

    controller.modelAndView.viewName == '/account/transfer'
    controller.modelAndView.model.get("transferCO") == transferCO
    controller.modelAndView.model.get("transferCO").errors.getFieldError("accountNumber").code == "invalid.account.number"
  }

  void "test TransferCO same account number"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']
    Account account = (Account) fromInfo['checkingAccount']

    account.accountNumber = 9999999999
    account.save(flush: true)

    TransferCO transferCO = new TransferCO()

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)
    transferCO.accountNumber = account.accountNumber
    controller.transferAmount(transferCO)

    then:

    controller.modelAndView.viewName == '/account/transfer'
    controller.modelAndView.model.get("transferCO") == transferCO
    controller.modelAndView.model.get("transferCO").errors.getFieldError("accountNumber").code == "same.account.number"
  }

  // ------------------- TRANSFER AMOUNT -------------------

  void "test transferAmount invalid account number"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']
    Account account = (Account) fromInfo['checkingAccount']

    account.accountNumber = 9999999999
    account.save()

    TransferCO transferCO = new TransferCO()
    transferCO.accountNumber = 12345

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)
    controller.transferAmount(transferCO)

    then:

    controller.modelAndView.viewName == '/account/transfer'
    controller.modelAndView.model.get("transferCO") == transferCO
    controller.modelAndView.model.get("transferCO").errors.getFieldError("accountNumber").code == "invalid.account.number"
  }

  // ------------------- DO TRANSFER -------------------

  void "test doTransfer not getTransfer()"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(1)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']
    TransferAmountCO transferAmountCO = new TransferAmountCO()

    bankingService.setTransfer(false)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)
    controller.doTransfer(transferAmountCO)

    then:

    controller.response.redirectedUrl == '/home/menu'
  }

  void "test doTransfer transferAmountCO invalid account id"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']
    AccountType accountType = (AccountType) fromInfo['checkingType']

    TransferAmountCO transferAmountCO = new TransferAmountCO()
    transferAmountCO.amount = 1.00
    transferAmountCO.accountIdTo = 9999999

    bankingService.setTransfer(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)
    controller.doTransfer(transferAmountCO)

    then:

    bankingService.getTransfer()
    controller.modelAndView.viewName == '/account/transferAmount'
    controller.modelAndView.model.get('currentAccountType') == accountType
    controller.modelAndView.model.get("transferAmountCO").errors.getFieldError("accountIdTo").code == "invalid.account.id"
  }

  void "test doTransfer transferAmountCO invalid amount"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']
    AccountType accountType = (AccountType) fromInfo['checkingType']

    Account toAccount = (Account) toInfo['checkingAccount']

    TransferAmountCO transferAmountCO = new TransferAmountCO()
    transferAmountCO.amount = null
    transferAmountCO.accountIdTo = toAccount?.id

    bankingService.setTransfer(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)
    controller.doTransfer(transferAmountCO)

    then:

    bankingService.getTransfer()
    controller.modelAndView.viewName == '/account/transferAmount'
    controller.modelAndView.model.get('currentAccountType') == accountType
    controller.modelAndView.model.get("transferAmountCO").errors.getFieldError("amount").code == "nullable"
  }

  void "test doTransfer NOT TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    CustomerLevel customerLevel = (CustomerLevel) fromInfo['checkingCustomerLevel']
    customerLevel.dailyWithdrawalLimit = 500
    customerLevel.save()

    Account account = (Account) fromInfo['checkingAccount']
    account.balance = 5000
    account.save()

    AccountType checkingType = (AccountType) fromInfo['checkingType']
    Account toAccount = (Account) toInfo['checkingAccount']

    TransferAmountCO transferAmountCO = new TransferAmountCO()
    transferAmountCO.amount = 1000
    transferAmountCO.accountIdTo = toAccount?.id

    bankingService.setTransfer(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doTransfer(transferAmountCO)

    then:

    bankingService.getTransfer()
    controller.modelAndView.viewName == '/account/transferAmount'
    controller.modelAndView.model.get('currentAccountType') == checkingType
    controller.modelAndView.model.get('errorMessage')
  }

  void "test doTransfer CHECKING to CHECKING TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    CustomerLevel customerLevel = (CustomerLevel) fromInfo['checkingCustomerLevel']
    customerLevel.dailyWithdrawalLimit = 2000
    customerLevel.save()

    Account account = (Account) fromInfo['checkingAccount']
    account.balance = 5000
    account.save()

    Account toAccount = (Account) toInfo['checkingAccount']

    TransferAmountCO transferAmountCO = new TransferAmountCO()
    transferAmountCO.amount = 1000
    transferAmountCO.accountIdTo = toAccount?.id

    bankingService.setTransfer(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doTransfer(transferAmountCO)

    then:

    !bankingService.getTransfer()
    controller.modelAndView.viewName == '/account/doTransfer'
    controller.modelAndView.model.get('fromAccount') == account
    controller.modelAndView.model.get('toAccount') == toAccount
    controller.modelAndView.model.get('amount') == transferAmountCO.amount
  }

  void "test doTransfer CHECKING to SAVINGS TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['checkingCard']

    CustomerLevel customerLevel = (CustomerLevel) fromInfo['checkingCustomerLevel']
    customerLevel.dailyWithdrawalLimit = 2000
    customerLevel.save()

    Account account = (Account) fromInfo['checkingAccount']
    account.balance = 5000
    account.save()

    Account toAccount = (Account) toInfo['savingsAccount']

    TransferAmountCO transferAmountCO = new TransferAmountCO()
    transferAmountCO.amount = 1000
    transferAmountCO.accountIdTo = toAccount?.id

    bankingService.setTransfer(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doTransfer(transferAmountCO)

    then:

    !bankingService.getTransfer()
    controller.modelAndView.viewName == '/account/doTransfer'
    controller.modelAndView.model.get('fromAccount') == account
    controller.modelAndView.model.get('toAccount') == toAccount
    controller.modelAndView.model.get('amount') == transferAmountCO.amount
  }

  void "test doTransfer SAVINGS to CHECKING TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['savingsCard']

    CustomerLevel customerLevel = (CustomerLevel) fromInfo['savingsCustomerLevel']
    customerLevel.dailyWithdrawalLimit = 2000
    customerLevel.save()

    Account account = (Account) fromInfo['savingsAccount']
    account.balance = 5000
    account.save()

    Account toAccount = (Account) toInfo['checkingAccount']

    TransferAmountCO transferAmountCO = new TransferAmountCO()
    transferAmountCO.amount = 1000
    transferAmountCO.accountIdTo = toAccount?.id

    bankingService.setTransfer(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doTransfer(transferAmountCO)

    then:

    !bankingService.getTransfer()
    controller.modelAndView.viewName == '/account/doTransfer'
    controller.modelAndView.model.get('fromAccount') == account
    controller.modelAndView.model.get('toAccount') == toAccount
    controller.modelAndView.model.get('amount') == transferAmountCO.amount
  }

  void "test doTransfer SAVINGS to SAVINGS TRANSACTION_COMPLETE"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)
    Map toInfo = transactionCreatorService.getUserAccountInformation(1)

    Card card = (Card) fromInfo['savingsCard']

    CustomerLevel customerLevel = (CustomerLevel) fromInfo['savingsCustomerLevel']
    customerLevel.dailyWithdrawalLimit = 2000
    customerLevel.save()

    Account account = (Account) fromInfo['savingsAccount']
    account.balance = 5000
    account.save()

    Account toAccount = (Account) toInfo['savingsAccount']

    TransferAmountCO transferAmountCO = new TransferAmountCO()
    transferAmountCO.amount = 1000
    transferAmountCO.accountIdTo = toAccount?.id

    bankingService.setTransfer(true)

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.doTransfer(transferAmountCO)

    then:

    !bankingService.getTransfer()
    controller.modelAndView.viewName == '/account/doTransfer'
    controller.modelAndView.model.get('fromAccount') == account
    controller.modelAndView.model.get('toAccount') == toAccount
    controller.modelAndView.model.get('amount') == transferAmountCO.amount
  }

  // ------------------- CHANGE PIN CO -------------------

  void "test incorrect pin"() {
    given:

    ChangePinCO changePinCO = new ChangePinCO(
      oldPinNumber: '123456',
      actualPinNumber: '1234567',
      newPinNumber: '654321',
      confirmPinNumber: '654321'
    )

    when:

    Boolean match = changePinCO.validate()

    then:

    !match
    changePinCO.errors.getFieldError('oldPinNumber').code == 'incorrect.pin'
  }

  void "test old pin not match"() {
    given:

    ChangePinCO changePinCO = new ChangePinCO(
      oldPinNumber: '123456',
      actualPinNumber: '123456',
      newPinNumber: '654321',
      confirmPinNumber: '654322'
    )

    when:

    Boolean match = changePinCO.validate()

    then:

    !match
    changePinCO.errors.getFieldError('confirmPinNumber').code == 'old.pin.not.match'
  }

  void "test newPinNumber minSize 6"() {
    given:

    ChangePinCO changePinCO = new ChangePinCO(
      oldPinNumber: '123456',
      actualPinNumber: '123456',
      newPinNumber: '12345',
      confirmPinNumber: '12345'
    )

    when:

    Boolean match = changePinCO.validate()

    then:

    !match
    changePinCO.errors.getFieldError('newPinNumber').code == 'minSize.notmet'
  }

  void "test newPinNumber maxSize 25"() {
    given:

    ChangePinCO changePinCO = new ChangePinCO(
      oldPinNumber: '123456',
      actualPinNumber: '123456',
      newPinNumber: '123451234512345123451234512345',
      confirmPinNumber: '123451234512345123451234512345'
    )

    when:

    Boolean match = changePinCO.validate()

    then:

    !match
    changePinCO.errors.getFieldError('newPinNumber').code == 'maxSize.exceeded'
  }

  // -------------- CHANGE PIN -----------------

  void "test savePin valid"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)

    Card card = (Card) fromInfo['checkingCard']
    card.pin = '123456'
    card.save()

    ChangePinCO changePinCO = new ChangePinCO(
      oldPinNumber: '123456',
      actualPinNumber: '123456',
      newPinNumber: '654321',
      confirmPinNumber: '654321'
    )

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.changePin(changePinCO)
    Card changedCard = bankingService.selectedCard
    String message = controller.flash.message

    then:

    changedCard.pin == changePinCO.newPinNumber
    message == 'Your pin has been updated.'
    controller.response.redirectedUrl == '/home/menu'
  }

  void "test savePin not valid"() {
    given:

    transactionCreatorService.createCheckingAndSavingsAccounts(2)
    Map fromInfo = transactionCreatorService.getUserAccountInformation(0)

    Card card = (Card) fromInfo['checkingCard']
    card.pin = '123456'
    card.save()

    ChangePinCO changePinCO = new ChangePinCO(
      oldPinNumber: '123456',
      actualPinNumber: '123456',
      newPinNumber: '65432!',
      confirmPinNumber: '654321'
    )

    when:

    AccountController controller = new AccountController()
    controller.session.setAttribute(BankingService.SELECTED_CARD_SESSION, card)

    controller.changePin(changePinCO)

    then:

    controller.modelAndView.viewName == '/account/changePin'
    controller.modelAndView.model.get('changePinCO') == changePinCO
  }

}
