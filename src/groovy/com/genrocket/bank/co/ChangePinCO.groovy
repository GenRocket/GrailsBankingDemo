package com.genrocket.bank.co

import grails.validation.Validateable

@Validateable
class ChangePinCO {

  String oldPinNumber
  String newPinNumber
  String confirmPinNumber
  String actualPinNumber

  static constraints = {
    oldPinNumber(nullable: false, blank: false, validator: { value, object ->
      if (value && value != object.actualPinNumber) {
        return "incorrect.pin"
      }
    })
    newPinNumber(nullable: false, blank: false, max: 6, min: 6)
    confirmPinNumber(nullable: false, blank: false, validator: { value, object ->
      if (value && value != object.newPinNumber) {
        return "old.pin.not.match"
      }
    })
  }
}
