package pages

import geb.Page

class LoginPage extends Page {

  static url = "home/index"

  static at = {
    title == /Welcome to GenRocket Bank ATM/
  }

  static content = {
    cardNumber { $("input[name='cardNumber']") }
    pin { $("input[name='pin']") }
    error { $(".alert-danger") }
    submitButton { $("#submit") }
  }

}