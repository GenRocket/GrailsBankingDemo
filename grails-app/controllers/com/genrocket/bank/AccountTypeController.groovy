package com.genrocket.bank

class AccountTypeController {

  def index() {
    render(view: "list", model: [accountTypes: AccountType.list()])
  }
}
