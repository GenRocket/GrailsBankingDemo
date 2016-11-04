package com.genrocket.bank.co

import com.genrocket.bank.Card
import grails.validation.Validateable

@Validateable
class LoginCO {
  String cardNumber
  String pinNumber

  static constraints = {
    cardNumber(nullable: false, blank: false, validator: { value, object ->
      Card card = value ? Card.findByCardNumber(value) : null
      if (card) {
        if (!card.enabled) {
          return "card.not.enabled"
        } else if (card.dateExpired < new Date()) {
          return "card.expired"
        } else if (card.dateDeactivated) {
          return "card.deactivated"
        } else if (object.pinNumber && !object.pinNumber) {
          return "invalid.pin.number"
        } else if (object.pinNumber != card.pin) {
          return "invalid.pin.number"
        }
      } else {
        return "invalid.card.number"
      }
    })
    pinNumber(nullable: false, blank: false)
  }
}
